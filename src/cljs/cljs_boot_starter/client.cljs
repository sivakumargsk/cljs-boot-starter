(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent :refer [atom render]]
            [cljs-pikaday.reagent :as pikaday]))

(enable-console-print!)


(defonce the-date (atom "07/07/1992"))

(defn home-page []
  [:div [:h2 "Select a date"]
   [pikaday/date-selector {:date-atom the-date}]
   [:p (str @the-date)]])

(defn home-render [this id class data]
  [:input
   {:id id
    :class class
    :type "text"
    :value (@data id)
    :on-change #(swap! data assoc id (.val (js/$ (reagent/dom-node this))))
    :on-blur #(swap! data assoc id (.val (js/$ (reagent/dom-node this))))
    :on-focus #(.datepicker
                (js/$ (reagent/dom-node this))
                "update"
                (.val (js/$ (reagent/dom-node this))))
    :placeholder "click to show datepicker"}])

(defn home-did-mount [this id data]
  (do (.val (js/$ (reagent/dom-node this)) (@data id))
      (.datepicker (js/$ (reagent/dom-node this))
                   (clj->js {:format "dd-M-yyyy"
                             :autoclose true}))
      (.on (.datepicker (js/$ (reagent/dom-node this)))
           "changeDate"
           #(swap! data assoc id (.val (js/$ (reagent/dom-node this)))))
      (js/console.log "first")))

(defn home [id data]
  (reagent/create-class
   {:render #(home-render % id "" data)
    :component-did-mount #(home-did-mount % id data)}))

(defn my-home []
  (let [data (atom {})]
    (fn []
      [:div
       [:div [home :date data]] [:span (str @data)]
       [:button {:on-click #(swap! data assoc :date "07-Jan-2015")}
        "Change"]])))

(defn init []
  (render [my-home] (.getElementById js/document "my-app-area")))

(init)
