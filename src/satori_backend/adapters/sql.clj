(ns satori-backend.adapters.sql
  (:require
   [clj-time.jdbc])
  (:import
   (clojure.lang IPersistentVector)
   [java.sql Array  PreparedStatement]))

;; Automatic conversions to and from database types
(defmethod print-method org.joda.time.DateTime
  [dt out]
  (.write out (str "#joda/inst \"" (.toString dt) "\"")))

(defmethod print-dup org.joda.time.DateTime
  [dt out]
  (.write out (str "#joda/inst \"" (.toString dt) "\"")))

(defn parse-joda-inst [time-str]
  (org.joda.time.DateTime/parse time-str))

(extend-protocol clojure.java.jdbc/ISQLParameter
  IPersistentVector
  (set-parameter [v ^PreparedStatement stmt ^long i]
    (let [conn (.getConnection stmt)
          meta (.getParameterMetaData stmt)
          type-name (.getParameterTypeName meta i)]
      (if-let [elem-type (when (= (first type-name) \_) (apply str (rest type-name)))]
        (.setObject stmt i (.createArrayOf conn elem-type (to-array v)))
        (.setObject stmt i v)))))

(extend-protocol clojure.java.jdbc/IResultSetReadColumn
  Array
  (result-set-read-column [val _ _]
    (into [] (.getArray val))))
