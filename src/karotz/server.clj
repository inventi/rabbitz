(ns karotz.server
  (:use [org.httpkit.server :only [run-server]])
  (:require karotz.rest)
  (:gen-class))

(defn -main [port]
  (do
    (run-server karotz.rest/api {:port (Integer/valueOf port)})
    (println "Rabbitz server started")))
