(ns satori-backend.diplomat.db.submission-test
  (:require
   [clojure.test :refer :all]
   [satori-backend.adapters.sql]
   [satori-backend.diplomat.db.submission :as db.submission]
   [schema.test :as s]))

(s/deftest paginated-fetch!
  (testing (str "output structure & schema validation"
                "Note that this test depends on the initial seeding of the database")
    (is (= [{:lines-of-coverage ["Coverage1" "Coverage2"]
             :premium-usd       100.0M
             :state             "Active"
             :account-name      "Account1"
             :status            "Approved"
             :id                #uuid "123e4567-e89b-12d3-a456-426614174001"
             :inserted-at       #joda/inst "2024-01-06T20:52:02.803Z"
             :effective-date    #joda/inst "2023-01-01T06:00:00.000Z"
             :sic               "SIC123"
             :expiration-date   #joda/inst "2023-12-31T06:00:00.000Z"
             :uw-name           "UW1"}]
           (take 1 (db.submission/paginated-fetch! 0 1 "account_name" "asc"))))))
