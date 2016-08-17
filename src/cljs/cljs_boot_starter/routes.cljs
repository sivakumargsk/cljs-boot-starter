(ns cljs-boot-starter.routes
  ;; (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   ;; [secretary.core :as secretary]
   [clojure.set :refer [rename-keys]]
   [domkm.silk :as silk]
   [pushy.core :as pushy]
   [goog.events :as events]
   [goog.history.EventType :as EventType]
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



(def routes (silk/routes [[:home [[]]]
                          [:about [["about"]]]]))

(defn- parse-url [url]
  (silk/arrive routes url))

(defn- sanitize-silk-keywords [matched-route]
  (rename-keys matched-route {:domkm.silk/name    :name
                              :domkm.silk/pattern :pattern
                              :domkm.silk/routes  :routes
                              :domkm.silk/url     :url}))

(defn- dispatch-route [matched-route]
  (let [matched-route (sanitize-silk-keywords matched-route)
        panel-name (keyword (str (name (:name matched-route)) "-panel"))]
    (re-frame/dispatch [:set-active-panel panel-name])))

(defn app-routes []
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (partial silk/depart routes))
