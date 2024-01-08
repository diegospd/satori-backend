(ns satori-backend.adapters.json
  (:require
   [cheshire.core :as json]
   [clojure.string :as str]))

(defn kebab-case [s]
  (str/replace s #"_" "-"))

(defn snake-case [s]
  (str/replace s #"-" "_"))

(defn ->pretty-json [x]
  (json/generate-string x {:pretty true
                           :key-fn #(snake-case (name %))}))
