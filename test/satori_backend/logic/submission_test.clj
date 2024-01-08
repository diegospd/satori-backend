(ns satori-backend.logic.submission-test
  (:require
   [clojure.test :refer :all]
   [satori-backend.logic.submission :as logic.submission]
   [schema.test :as s]))

(s/deftest maybe-parse-integer
  (are [unparsed expected] (= expected (logic.submission/maybe-parse-integer unparsed))
    0 0
    "0" 0
    -1 -1
    "-1" -1
    "five" nil
    "3.4" nil))

(def max-limit 3)
(s/deftest parse-&-cap-limit
  (are [unparsed expected] (= expected (logic.submission/parse-&-cap-limit unparsed max-limit))
    0 0
    "0" 0
    -1 -1
    "-1" -1
    "five" nil
    "3.4" nil
    4 max-limit
    "4" max-limit))

(s/deftest limit-or-offset-is-invalid?
  (are [limit offset expected] (= expected (logic.submission/limit-or-offset-is-invalid? limit offset))
    nil nil true
    nil 0 true
    1 0 false
    0 0 false
    -1 2 true))

(s/deftest error-string-for-limit-or-offset
  (are [limit expected] (= expected (logic.submission/error-string-for-limit-or-offset limit))
    nil "Pagination limit must be a non-negative integer"
    -1 "Pagination limit must be a non-negative integer"
    0 "Offset must be a non-negative integer"
    1 "Offset must be a non-negative integer"))

(s/deftest sql-query-select-with-pagination-and-order
  (are [sort-field sort-direction expected]
       (= expected (logic.submission/sql-query-select-with-pagination-and-order sort-field sort-direction))
    "account_name" "asc" "SELECT * FROM submission ORDER BY account_name ASC OFFSET ? LIMIT ?"
    "uw_name" "wat" "SELECT * FROM submission ORDER BY uw_name ASC OFFSET ? LIMIT ?"
    "premium_usd" "DESC" "SELECT * FROM submission ORDER BY premium_usd DESC OFFSET ? LIMIT ?"
    "effective_date" "desc" "SELECT * FROM submission ORDER BY effective_date DESC OFFSET ? LIMIT ?"
    "expiration_date" "dESc" "SELECT * FROM submission ORDER BY expiration_date DESC OFFSET ? LIMIT ?"
    "nonsense?!" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"
    "id" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"
    "lines_of_coverage" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"
    "state" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"
    "sic" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"
    "status" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"
    "inserted_at" "dESc" "SELECT * FROM submission ORDER BY account_name DESC OFFSET ? LIMIT ?"))
