(ns karotz.view
  (:use [hiccup.core :only [html]]))

(def index
  (html
   [:form {:method "get" :action "rabbit"}
    [:legend "Log-in to your rabbit"]
    [:label "Installation id"
      [:input {:name "id" :type "text"}]]
    [:input {:value "GO" :type "submit"}]]))

(defn rabbit-actions [id]
  (html
   [:form {:method "get" :action (str "/rabbit/" id "/ears")}
    [:legend "Move ears"]
    [:label "Left"
      [:input {:name "left" :type "text"}]]
    [:label "Right"
      [:input {:name "right" :type "text"}]]
    [:input {:value "Move" :type "submit"}]]
   [:form {:method "get" :action (str "/rabbit/" id "/speech")}
    [:legend "Say"]
    [:textarea {:name "text"}]
    [:input {:value "Say" :type "submit"}]]))