(ns satori-backend.start-here
  (:require
   [satori-backend.service :as service])
  (:gen-class))

(defn -main [& _args]
  (service/start!))
