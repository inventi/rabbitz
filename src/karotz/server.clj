(ns karotz.server
  (:use
   compojure.core
   ring.middleware.params
   ring.middleware.keyword-params
   [org.httpkit.server :only [run-server]])
  (:require [karotz.rabbit :as rabbit]
            [karotz.view :as view]
            [compojure.route :as route])
  (:gen-class))


(defroutes api-routes
  (GET "/" [] view/index)
  (route/files "/")
  (GET "/rabbit" [id] (do
                        (rabbit/receive-token id)
                        (view/actions id)))
  (context "/rabbit/:id" [id]
           (GET "/" [] (do
                         (rabbit/receive-token id)
                         (view/actions id)))
           (GET "/ears" [left right] (do
                                       (rabbit/move-ears id left right)
                                       (view/actions id)))
           (GET "/speech" [text] (do
                                   (rabbit/speek id text)
                                   (view/actions id)))))

(def api (compojure.handler/api #'api-routes))

(defn -main [port]
  (do
    (run-server api {:port (Integer/valueOf port)})
    (println "Rabbitz server started")))
