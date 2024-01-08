(ns satori-backend.models.insurance.submission
  (:require
   [schema.core :as s])
  (:import
   [org.joda.time DateTime]))

(s/defschema Submission
  {:id                s/Uuid
   :account-name      s/Str
   :lines-of-coverage [s/Str]
   :uw-name           s/Str
   :premium-usd       BigDecimal
   :state             s/Str
   :effective-date    DateTime
   :expiration-date   DateTime
   :sic               s/Str
   :status            s/Str
   :inserted-at       DateTime})
