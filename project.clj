(defproject satori-backend "0.1.0-SNAPSHOT"
  :description "Service responsible of providing insurance submissions to the frontend"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.logging "1.2.4"]
                 [prismatic/schema "1.4.1"]
                 [clj-time "0.15.2"]
                 [io.pedestal/pedestal.service "0.6.2"]
                 [io.pedestal/pedestal.route "0.6.2"]
                 [io.pedestal/pedestal.jetty "0.6.2"]
                 [org.slf4j/slf4j-simple "2.0.9"]
                 [environ/environ "1.2.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "42.2.5"]
                 [clojure.java-time "1.4.2"]
                 [cheshire "5.12.0"]]
  :main ^:skip-aot satori-backend.start-here
  :target-path "target/%s"
  :profiles {:uberjar {:aot      :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :aliases {"format"       ["clojure-lsp" "format" "--dry"]
            "clean-ns-fix" ["clojure-lsp" "clean-ns"]
            "format-fix"   ["clojure-lsp" "format"]
            "lint-fix"     ["do" ["clean-ns-fix"] "format-fix"]}
  :plugins [[com.github.clojure-lsp/lein-clojure-lsp "1.2.2"]
            [lein-ancient "0.7.0-SNAPSHOT"]])
