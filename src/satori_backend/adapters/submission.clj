(ns satori-backend.adapters.submission
  (:require
   [clj-time.format :as time.format]
   [satori-backend.models.insurance.submission :as models.insurance.submission]
   [satori-backend.schemata.out.submission :as schemata.out.submission]
   [schema.core :as s])
  (:import
   (org.joda.time DateTime)))

(s/defn ^:private date-time->dd-mm-yyyy :- s/Str
  [date-time :- DateTime]
  (let [formatter (time.format/formatter "dd/MM/yyyy")]
    (time.format/unparse formatter date-time)))

(s/defn ->response :- schemata.out.submission/SubmissionResponse
  [submission :- models.insurance.submission/Submission]
  (-> submission
      (dissoc :id)
      (dissoc :inserted-at)
      (update :effective-date date-time->dd-mm-yyyy)
      (update :expiration-date date-time->dd-mm-yyyy)))

(s/defn submissions->response :- [schemata.out.submission/SubmissionResponse]
  [submissions :- [models.insurance.submission/Submission]]
  (map ->response submissions))
