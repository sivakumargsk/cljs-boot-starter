(ns cljs-boot-starter.client
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [cljs-boot-starter.handlers]
            [cljs-boot-starter.subs]
            [cljs-boot-starter.routes :as routes]
            [cljs-boot-starter.views :as views]))

(enable-console-print!)

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "my-app-area")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))

(init)
