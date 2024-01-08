(ns satori-backend.logic.submission
  (:require
   [clojure.string :as str]
   [schema.core :as s]))

(s/defn maybe-parse-integer :- (s/maybe s/Num)
  [num]
  (if (number? num)
    num
    (try (Integer/parseInt num)
         (catch NumberFormatException _e nil))))

(s/defn parse-&-cap-limit :- (s/maybe s/Num)
  [limit-param max-limit]
  (when-let [limit (maybe-parse-integer limit-param)]
    (min limit max-limit)))

(s/defn ^:private limit-is-invalid? :- s/Bool
  [limit :- (s/maybe s/Num)]
  (if (nil? limit)
    true
    (neg? limit)))

(s/defn limit-or-offset-is-invalid? :- s/Bool
  [limit  :- (s/maybe s/Num)
   offset :- (s/maybe s/Num)]
  (if (or (nil? limit)
          (nil? offset))
    true
    (or (neg? limit)
        (neg? offset))))

(s/defn error-string-for-limit-or-offset
  [limit :- (s/maybe s/Num)]
  (if (limit-is-invalid? limit)
    "Pagination limit must be a non-negative integer"
    "Offset must be a non-negative integer"))

(s/defn ^:private sorting-field :- s/Str
  "PLEASE ensure a database index is created for any new sortable field."
  [field :- s/Str]
  (let [default-field        "account_name"
        field-is-sortable? #{"account_name"
                             "uw_name"
                             "premium_usd"
                             "effective_date"
                             "expiration_date"}]
    (or (field-is-sortable? field)
        default-field)))

(s/defn ^:private sorting-direction :- s/Str
  [direction :- s/Str]
  (let [default-direction "ASC"]
    (if (= "DESC"
           (str/upper-case direction))
      "DESC"
      default-direction)))

(s/defn sql-query-select-with-pagination-and-order :- s/Str
  [sort-field     :- s/Str
   sort-direction :- s/Str]
  (str "SELECT * FROM submission ORDER BY "
       (sorting-field sort-field)
       " "
       (sorting-direction sort-direction)
       " OFFSET ? LIMIT ?"))
