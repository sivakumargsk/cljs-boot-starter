(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent :refer [atom render]]
            [cljs-pikaday.reagent :as pikaday]))

(enable-console-print!)


(defonce the-date (atom (js/Date.)))

(defn cljs-pikaday-datepicker []
  [:div [:h2 "Select a date"]
   [pikaday/date-selector {:date-atom the-date}]
   [:p (str @the-date)]])


;; -----------------------------------------------------
;; bootstrap date picker
;; -----------------------------------------------------

(defn datepicker-render [this id key data]
  [:input.form-control
   {:id id
    :type "text"
    :placeholder "click to show datepicker"
    :value (@data key "")
    :on-change #(swap! data assoc key (.val (js/$ (reagent/dom-node this))))
    :on-blur #(swap! data assoc key (.val (js/$ (reagent/dom-node this))))
    }])

(defn datepicker-did-mount [this key data]
  (let [dp-node (js/$ (reagent/dom-node this))]
    (do (.val dp-node (@data key))
        (swap! data assoc key (.val dp-node))
        (.datepicker
         dp-node
         (clj->js {:format "dd-M-yyyy"
                   :autoclose true
                   :todayBtn  true
                   :todayHighlight true}))
        (.on (.datepicker dp-node)
             "changeDate"
             #(swap! data assoc key (.val dp-node))))))

(defn datepicker-did-update [this key data]
  (let [dp-node (js/$ (reagent/dom-node this))]
    (.datepicker dp-node "update")))

(defn datepicker [id key data]
  (reagent/create-class
   {:render #(datepicker-render % id key data)
    :component-did-mount #(datepicker-did-mount % key data)
    :component-did-update #(datepicker-did-update % key data)
    }))

(defn div-datepicker-render [id key data]
  [:div.input-group.date
   [datepicker id key data]
   [:div.input-group-addon
    {:style {:cursor "pointer"}
     :on-click #(.datepicker  (js/$ (str "#" id)) "show")}
    [:span.glyphicon.glyphicon-calendar]]])

;; ------------ end of date picker --------------------------------

(defn my-home []
  (let [data (atom {})]
    (fn []
      [:div.container
       [:div.row
        [:div.col-md-4 [cljs-pikaday-datepicker]]]
       [:div.row
        [:div.col-md-4
         [div-datepicker-render "mydatepicker" :date data]]
        [:div.col-md-4
         [:span (str @data)]]
        [:div.col-md-4
         [:button
          {:on-click #(swap! data assoc :date "07-Jan-2015")} "Change"]]]])))

(defn init []
  (render [my-home] (.getElementById js/document "my-app-area")))

(init)
