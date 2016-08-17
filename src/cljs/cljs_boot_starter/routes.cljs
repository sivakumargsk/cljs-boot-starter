(ns cljs-boot-starter.routes
  ;; (:require-macros [secretary.core :refer [defroute]])
  ;; (:import goog.History)
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            ;; [goog.events :as events]
            ;; [goog.history.EventType :as EventType]
            [re-frame.core :as re-frame]))

;; (defn hook-browser-navigation! []
;;   (doto (History.)
;;     (events/listen
;;      EventType/NAVIGATE
;;      (fn [event]
;;        (secretary/dispatch! (.-token event))))
;;     (.setEnabled true)))

;; (defn app-routes []
;;   (secretary/set-config! :prefix "#")
;;   ;; --------------------
;;   ;; define routes here
;;   (defroute "/" []
;;     (re-frame/dispatch [:set-active-panel :home-panel]))

;;   (defroute "/about" []
;;     (re-frame/dispatch [:set-active-panel :about-panel]))


;;   ;; --------------------
;;   (hook-browser-navigation!))



(def routes ["/" {""      :home
                  "about" :about}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route)) "-panel"))]
    (re-frame/dispatch [:set-active-panel panel-name])))

(defn app-routes []
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (partial bidi/path-for routes))
