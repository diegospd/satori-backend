(ns satori-backend.adapters.submission-test
  (:require
   [clojure.test :refer :all]
   [satori-backend.adapters.sql]
   [satori-backend.adapters.submission :as adapters.submission]
   [schema.test :as s]))

(def submission-1
  {:lines-of-coverage ["Coverage1" "Coverage2"]
   :premium-usd       100.0M
   :state             "Active"
   :account-name      "Account1"
   :status            "Approved"
   :id                #uuid "123e4567-e89b-12d3-a456-426614174001"
   :effective-date    #clj-time/date-time "2023-01-01T06:00:00.000Z"
   :sic               "SIC123"
   :expiration-date   #clj-time/date-time "2023-12-31T06:00:00.000Z"
   :uw-name           "UW1"
   :inserted-at       #clj-time/date-time "2023-12-31T06:00:00.000Z"})

(def submission-2
  {:lines-of-coverage ["Coverage1" "Coverage3"]
   :premium-usd       100.0M
   :state             "Active"
   :account-name      "Account2"
   :status            "Approved"
   :id                #uuid "123e4567-e89b-12d3-a456-426614174002"
   :effective-date    #clj-time/date-time "2023-01-01T06:00:00.000Z"
   :sic               "SIC123"
   :expiration-date   #clj-time/date-time "2023-12-31T06:00:00.000Z"
   :uw-name           "UW1"
   :inserted-at       #clj-time/date-time "2023-12-31T06:00:00.000Z"})

(s/deftest ->response
  (testing (str "internal model is transformed to response output schema"
                "id is not exposed & and dates are properly formatted")
    (is (= {:lines-of-coverage ["Coverage1" "Coverage2"]
            :premium-usd       100.0M
            :state             "Active"
            :account-name      "Account1"
            :status            "Approved"
            :effective-date    "01/01/2023"
            :sic               "SIC123"
            :expiration-date   "31/12/2023"
            :uw-name           "UW1"}
           (adapters.submission/->response submission-1)))))

(s/deftest submissions->response
  (testing "vector of internal model is transformed to vector of output response schemas"
    (is (= [{:lines-of-coverage ["Coverage1" "Coverage2"]
             :premium-usd 100.0M
             :state "Active"
             :account-name "Account1"
             :status "Approved"
             :effective-date "01/01/2023"
             :sic "SIC123"
             :expiration-date "31/12/2023"
             :uw-name "UW1"}

            {:lines-of-coverage ["Coverage1" "Coverage3"]
             :premium-usd 100.0M
             :state "Active"
             :account-name "Account2"
             :status "Approved"
             :effective-date "01/01/2023"
             :sic "SIC123"
             :expiration-date "31/12/2023"
             :uw-name "UW1"}]
           (adapters.submission/submissions->response
            [submission-1 submission-2])))))
