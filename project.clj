(defproject lt.inventi/rabbitz "1.0.0"
  :description "Provides RESfull API for karotz"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.1.1"]
                 [compojure "1.1.5"]
                 [http-kit "2.1.2"]
                 [hiccup "1.0.3"]
                 [com.googlecode.soundlibs/jlayer "1.0.1-1"]
                 [com.googlecode.soundlibs/tritonus-share "0.3.7-1"]
                 [com.googlecode.soundlibs/mp3spi "1.9.5-1"]]
  :jvm-opts ["-Dfile.encoding=UTF-8" "-Dconsole.encoding=utf-8"]
  :main karotz.server
  :aot [karotz.server]
  :uberjar-name "rabbitz-standalone.jar"
  :deploy-repositories [["releases" "http://nexus.inventi.corp/content/repositories/releases/"]
                        ["snapshots" "http://nexus.inventi.corp/content/repositories/snapshots/"]])
