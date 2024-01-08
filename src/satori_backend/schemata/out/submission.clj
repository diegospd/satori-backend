(ns satori-backend.schemata.out.submission
  (:require
   [schema.core :as s]))

(s/defschema SubmissionResponse
  {:account-name      s/Str
   :lines-of-coverage [s/Str]
   :uw-name           s/Str
   :premium-usd       BigDecimal
   :state             s/Str
   :effective-date    s/Str
   :expiration-date   s/Str
   :sic               s/Str
   :status            s/Str})

