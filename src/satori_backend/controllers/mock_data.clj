(ns satori-backend.controllers.mock-data
  (:require
   [clj-time.core :as time]
   [satori-backend.models.insurance.submission :as models.insurance.submission]
   [schema.core :as s])
  (:import
   (java.util UUID)))

(defn- random-lines-of-coverage! []
  (for [_ (range (rand-int 5))]
    (str "Coverage" (inc (rand-int 5)))))

(def ^:private us-states
  ["AL" "AK" "AZ" "AR" "CA" "CO" "CT" "DE" "DC" "FL"
   "GA" "HI" "ID" "IL" "IN" "IA" "KS" "KY" "LA" "ME"
   "MD" "MA" "MI" "MN" "MS" "MO" "MT" "NE" "NV" "NH"
   "NJ" "NM" "NY" "NC" "ND" "OH" "OK" "OR" "PA" "RI"
   "SC" "SD" "TN" "TX" "UT" "VT" "VA" "WA" "WV" "WI" "WY"])

(s/defn random-submission! :- models.insurance.submission/Submission
  []
  {:id                (UUID/randomUUID)
   :account-name      (str "Account" (inc (rand-int 100000000)))
   :lines-of-coverage (vec (random-lines-of-coverage!))
   :uw-name           (str "UW" (inc (rand-int 5)))
   :premium-usd       (rand-nth (range 50.0 3500.0))
   :state             (rand-nth us-states)
   :effective-date    (time/plus (time/now) (time/days (rand-int 365)))
   :expiration-date   (time/plus (time/now) (time/days (+ 366 (rand-int 365))))
   :sic               (str "SIC" (inc (rand-int 900)))
   :status            (rand-nth ["New" "In Progress" "Done"])
   :inserted-at       (time/now)})
