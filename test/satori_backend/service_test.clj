(ns satori-backend.service-test
  (:require
   [cheshire.core :as json]
   [clj-time.format :as time.format]
   [clojure.test :refer :all]
   [io.pedestal.test :as http]
   [satori-backend.service :as service]))

(def service
  (-> service/service-map
      io.pedestal.http/create-servlet
      :io.pedestal.http/service-fn))

(defn parse-date [date-string]
  (let [formatter   (time.format/formatter "dd/MM/yyyy")
        parsed-date (time.format/parse formatter date-string)]
    parsed-date))

(defn response-status-&-body-get
  [path]
  (-> service
      (http/response-for :get path)
      (select-keys [:status :body])))

(defn response-body-get
  [path]
  (-> service (http/response-for :get path)
      :body
      json/decode))

(deftest get-submissions-with-pagination
  (testing "without query params"
    (let [response-no-query-params (response-body-get "/api/submissions")]
      (testing "query params: None"
        (testing "10 elements are returned by default"
          (is (= 10
                 (count response-no-query-params))))
        (testing "Submissions are sorted by account-name in ascending order"
          (is (= (sort (map #(get % "account-name") response-no-query-params))
                 (map #(get % "account-name") response-no-query-params))))
        (testing "content"
          (is (= [{"effective_date"    "01/01/2023"
                   "uw_name"           "UW1"
                   "expiration_date"   "31/12/2023"
                   "status"            "Approved"
                   "premium_usd"       100.0
                   "account_name"      "Account1"
                   "lines_of_coverage" ["Coverage1" "Coverage2"]
                   "state"             "Active"
                   "sic"               "SIC123"}]
                 (take 1 response-no-query-params))))))

    (testing "with valid query-params"
      (let [response-no-query-params (response-body-get "/api/submissions")
            response-descending (response-body-get "/api/submissions?sort-direction=desc")
            response-limit-5 (response-body-get "/api/submissions?limit=5")
            response-limit-5-offset-5 (response-body-get "/api/submissions?limit=5&offset=5")
            response-sorting-uw-name (response-body-get "/api/submissions?sort-field=uw_name")
            response-sorting-premium-usd (response-body-get "/api/submissions?sort-field=premium_usd")
            response-sorting-effective-date (response-body-get "/api/submissions?sort-field=effective_date")
            response-sorting-expiration-date (response-body-get "/api/submissions?sort-field=expiration_date")]

        (testing "query-params: sort-direction=desc"
          (testing "compared to the result without query params it has the same amount of elements"
            (is (= (count response-descending)
                   (count response-no-query-params))))
          (testing "result is not the same as the result without query params"
            (is (not= response-descending
                      response-no-query-params))))

        (testing "query-params: limit=5"
          (testing "the same as the first 5 elements when no query-params are present"
            (is (= response-limit-5
                   (take 5 response-no-query-params)))))

        (testing "query-params limit=5 offset=5"
          (testing "the same as the last 5 elements when no query-params are present"
            (is (= response-limit-5-offset-5
                   (drop 5 response-no-query-params)))))

        (testing "query-params: sort-field=uw_name"
          (testing "Submissions are sorted by uw_name in ascending order"
            (let [response (map #(get % "uw_name") response-sorting-uw-name)
                  response-sorted (sort response)]
              (is (= response
                     response-sorted))
              (is (= 10
                     (->> response (remove nil?) count))))))

        (testing "query-params: sort-field=premium_usd"
          (testing "Submissions are sorted by premium_usd in ascending order"
            (let [response (map #(get % "premium_usd") response-sorting-premium-usd)
                  response-sorted (sort response)]
              (is (= response
                     response-sorted))
              (is (= 10
                     (->> response (remove nil?) count))))))

        (testing "query-params: sort-field=effective-date"
          (testing "Submissions are sorted by effective_date in ascending order"
            (let [response (map #(parse-date (get % "effective_date")) response-sorting-effective-date)
                  response-sorted (sort response)]
              (is (= response
                     response-sorted))
              (is (= 10
                     (->> response (remove nil?) count))))))

        (testing "query-params: sort-field=expiration_date"
          (testing "Submissions are sorted by expiration_date in ascending order"
            (let [response (map #(parse-date (get % "expiration_date")) response-sorting-expiration-date)
                  response-sorted (sort response)]
              (is (= response
                     response-sorted))
              (is (= 10
                     (->> response (remove nil?) count))))))))

    (testing "invalid query-params are ignored and default-params are used instead"
      (let [response-no-query-params (response-body-get "/api/submissions")
            response-descending (response-body-get "/api/submissions?sort-direction=desc")
            response-direction-nonsense (response-body-get "/api/submissions?sort-direction=nonsense")
            response-limit-too-big (response-body-get "/api/submissions?limit=201")
            response-sorting-id (response-body-get "/api/submissions?sort-field=id")
            response-sorting-lines-of-coverage (response-body-get "/api/submissions?sort-field=lines_of_coverage")
            response-sorting-state (response-body-get "/api/submissions?sort-field=state")
            response-sorting-sic (response-body-get "/api/submissions?sort-field=sic")
            response-sorting-status (response-body-get "/api/submissions?sort-field=status")
            response-sorting-nonsense (response-body-get "/api/submissions?sort-field=nonsense")]

        (testing "query-params: sort-direction=nonsense"
          (testing "if sorting-direction is not 'desc' it defaults to ascending order"
            (is (= response-direction-nonsense
                   response-no-query-params)))
          (testing "result is not the same as the result without query params"
            (is (not= response-descending
                      response-no-query-params))))

        (testing "query-params: limit larger than maximum limit"
          (testing "only 200 submissions are returned"
            (is (= 200
                   (count response-limit-too-big)))))

        (testing "query-params: sort-field is set to a non sortable field"
          (testing "non-sortable fields are not accepted and sort-order defaults to account_name"
            (is (= response-sorting-id
                   response-no-query-params))
            (is (= response-sorting-lines-of-coverage
                   response-no-query-params))
            (is (= response-sorting-state
                   response-no-query-params))
            (is (= response-sorting-sic
                   response-no-query-params))
            (is (= response-sorting-status
                   response-no-query-params))
            (is (= response-sorting-nonsense
                   response-no-query-params))))))

    (testing "Illegal query-params return an http error without exposing internal exceptions"
      (let [response-limit-negative (response-status-&-body-get "/api/submissions?limit=-5")
            response-limit-not-numeric (response-status-&-body-get "/api/submissions?limit=five")
            response-offset-not-numeric (response-status-&-body-get "/api/submissions?offset=five")
            response-limit-floating (response-status-&-body-get "/api/submissions?limit=5.3")
            response-offset-floating (response-status-&-body-get "/api/submissions?offset=5.3")
            response-offset-negative (response-status-&-body-get "/api/submissions?offset=-5")]
        (testing "query-params: limit is negative. Not accepted"
          (testing "returns a 400 status code"
            (is (= {:status 400
                    :body "Pagination limit must be a non-negative integer"}
                   response-limit-negative))))

        (testing "query-params: offset is negative. Not accepted"
          (testing "returns a 400 status code"
            (is (= {:status 400
                    :body "Offset must be a non-negative integer"}
                   response-offset-negative))))

        (testing "query-params: limit is not a number. Not accepted"
          (testing "returns a 400 status code"
            (is (= {:status 400
                    :body "Pagination limit must be a non-negative integer"}
                   response-limit-not-numeric))))

        (testing "query-params: offset is not a number. Not accepted"
          (testing "returns a 400 status code"
            (is (= {:status 400
                    :body "Offset must be a non-negative integer"}
                   response-offset-not-numeric))))

        (testing "query-params: limit is a floating point number. Not accepted"
          (testing "returns a 400 status code"
            (is (= {:status 400
                    :body "Pagination limit must be a non-negative integer"}
                   response-limit-floating))))

        (testing "query-params: offset is a floating point number. Not accepted"
          (testing "returns a 400 status code"
            (is (= {:status 400
                    :body "Offset must be a non-negative integer"}
                   response-offset-floating))))))))
