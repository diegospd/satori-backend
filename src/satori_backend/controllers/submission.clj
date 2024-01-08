(ns satori-backend.controllers.submission
  (:require
   [clojure.core.async :as async]
   [clojure.core.async.impl.protocols :as chan]
   [satori-backend.adapters.json :as adapters.json]
   [satori-backend.adapters.submission :as adapters.submission]
   [satori-backend.controllers.mock-data :as controllers.mock-data]
   [satori-backend.diplomat.db.submission :as db.submission]
   [satori-backend.logic.submission :as logic.submission]
   [satori-backend.models.insurance.submission :as models.insurance.submission]
   [schema.core :as s]))

(def ^:private channels-to-stream-live-submissions
  (atom []))

(defn subscribe-to-new-submissions!
  [new-channel _context]
  (swap! channels-to-stream-live-submissions #(cons new-channel %))
  (async/>!! new-channel {:name "subscribe-to-new-submissions!" :data "success"}))

(s/defn stream-submission!
  [submission :- models.insurance.submission/Submission]
  (swap! channels-to-stream-live-submissions #(remove chan/closed? %))
  (let [payload (-> submission
                    adapters.submission/->response
                    adapters.json/->pretty-json)]
    (doseq [channel @channels-to-stream-live-submissions]
      (async/>!! channel {:name "new-submission"
                          :data payload}))))

(def ^:private default-params-for-fetch-submissions
  {:offset         0
   :limit          10
   :sort-field     "account_name"
   :sort-direction "asc"})

(defn fetch-submissions!
  [{:keys [query-params]}]
  (let [{:keys [offset limit
                sort-field
                sort-direction]} (merge default-params-for-fetch-submissions query-params)
        max-limit                200
        parsed-offset            (logic.submission/maybe-parse-integer offset)
        parsed-limit-capped      (logic.submission/parse-&-cap-limit limit max-limit)]
    (if (logic.submission/limit-or-offset-is-invalid? parsed-limit-capped parsed-offset)
      {:status 400
       :body   (logic.submission/error-string-for-limit-or-offset parsed-limit-capped)}
      {:status 200
       :body   (-> (db.submission/paginated-fetch! parsed-offset parsed-limit-capped sort-field sort-direction)
                   adapters.submission/submissions->response
                   adapters.json/->pretty-json)})))

(s/defn ^:private insert!
  [new-submission :- models.insurance.submission/Submission]
  (db.submission/insert! new-submission)
  (stream-submission! new-submission))

(defn insert-random!
  [_request]
  (let [random-submission (controllers.mock-data/random-submission!)]
    (insert! random-submission)
    {:status 200
     :body (-> random-submission
               adapters.submission/->response
               adapters.json/->pretty-json)}))
