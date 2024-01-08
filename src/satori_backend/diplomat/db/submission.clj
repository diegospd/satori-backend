(ns satori-backend.diplomat.db.submission
  (:require
   [clojure.java.jdbc :as jdbc]
   [satori-backend.adapters.json :as adapters.json]
   [satori-backend.diplomat.db.config :as db.config]
   [satori-backend.logic.submission :as logic.submission]
   [satori-backend.models.insurance.submission :as models.insurance.submission]
   [schema.core :as s]))

(s/defn paginated-fetch! :- [models.insurance.submission/Submission]
  [offset         :- s/Int
   limit          :- s/Int
   sort-field     :- s/Str
   sort-direction :- s/Str]
  (jdbc/query db.config/spec
              [(logic.submission/sql-query-select-with-pagination-and-order sort-field sort-direction)
               (bigint offset) (bigint limit)]
              {:identifiers adapters.json/kebab-case}))

(s/defn insert! :- models.insurance.submission/Submission
  [new-submission :- models.insurance.submission/Submission]
  (let [key->snake-case (fn [k] (-> k name adapters.json/snake-case keyword))
        submission-db   (update-keys new-submission key->snake-case)]
    (jdbc/insert! db.config/spec :submission submission-db)
    new-submission))
