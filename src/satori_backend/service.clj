(ns satori-backend.service
  (:require
   [clojure.tools.logging :as log]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params :as http.body-params]
   [io.pedestal.http.route :as http.route]
   [io.pedestal.http.sse :as server-sent-event]
   [satori-backend.adapters.sql]
   [satori-backend.controllers.submission :as controllers.submission]))

(def routes
  (http.route/expand-routes
   #{["/"
      :get [(fn [_] {:status 200 :body "Hello, world! :)"})]
      :route-name ::hello-world!]

     ["/api/submissions"
      :get [(http.body-params/body-params)
            controllers.submission/fetch-submissions!]
      :route-name ::fetch-submissions!]

     ["/api/submissions/random"
      :post [(http.body-params/body-params)
             controllers.submission/insert-random!]
      :route-name ::insert-random!]

     ["/api/submissions/live"
      :get (server-sent-event/start-event-stream controllers.submission/subscribe-to-new-submissions!)
      :route-name ::subscribe-to-new-submissions!]}))

(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8080})

(defn start!
  []
  (log/info (str "Starting satori-backend server on port "
                 (::http/port service-map)))
  (-> service-map
      http/create-server
      http/start))
