(ns karotz.server
  (:use [org.httpkit.server :only [run-server]])
  (:require karotz.rest)
  (:gen-class))

(defn -main [& args]
  (do
    (run-server karotz.rest/api {:port 8080})
    (println "Rabbitz server started")))