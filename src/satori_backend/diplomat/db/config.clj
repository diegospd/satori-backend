(ns satori-backend.diplomat.db.config)

(def db-spec-local
  {:dbtype   "postgresql"
   :dbname   "satori"
   :user     "diego"
   ;:password "mypassword"
   :host     "localhost"
   :port     5432})

(def spec db-spec-local)


