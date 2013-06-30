(ns karotz.rest
  (:use karotz.view
        compojure.core
        ring.middleware.params
        ring.middleware.keyword-params)

  (:require
   [compojure.handler]
   [clojure.java.io :as io]
   [compojure.route :as route]
   [karotz.api :as rabbit]
   [karotz.tts :as tts]))

(def tokens (ref {}))

(defn load-props [file]
  (let [props (java.util.Properties. )]
    (with-open [reader (io/reader (io/resource file))]
    (.load props reader)
    (into {} (for [[k v] props] [(keyword k) v])))))


(def props (load-props "api.properties"))


(defn store [id token]
  (dosync
   (alter tokens #(assoc % id token))))


(defn receive-token [id]
  (let [old-token ((deref tokens) id)
        token (rabbit/sign-in [id old-token] props)]
    (store id token)
    token))


(defn sign-in [id]
  (do
    (receive-token id)
    (rabbit-actions id)))

(defn move-ears [id left right]
  (let [token (receive-token id)]
    (rabbit/move-ears token left right)
    (rabbit-actions id)))


(defn speech-url [id text]
  (let [file (str id ".wav")
        local-file (str (:file-root props) "/" file)]
    (tts/to-file "en" text (io/file local-file))
    (str (:file-url props) "/" file)))


(defn speek [id text]
  (let [token (receive-token id)
        speech (speech-url id text)]
    (rabbit/play-media token speech)
    (rabbit-actions id)))


(defroutes api-routes
  (GET "/" [] index)
  (GET "/rabbit" [id] (sign-in id))
  (context "/rabbit/:id" [id]
           (GET "/" [] (sign-in id))
           (GET "/ears" [left right] (move-ears id left right))
           (GET "/speech" [text] (speek id text))))


(def api (compojure.handler/api #'api-routes))