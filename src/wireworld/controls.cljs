(ns wireworld.controls
  (:require [wireworld.actions :as act]
            [wireworld.settings :as s]))

(def play-icon "\u25BA")
(def pause-icon "\u23F8")

(defn play-pause
  [state]
  [:div
   (if (:paused @state)
     [:span.icon
      {:on-click #(swap! state act/play)}
      play-icon]

     [:span.icon
      {:on-click #(swap! state act/pause)}
      pause-icon])])

(defn tool
  [state k]
  [:span.swatch
    {:on-click #(swap! state act/select-tool k)
     :data-selected (= (get @state :tool) k)
     :style {:background (get s/colors k)}}])

(defn select-tool
  [state]
  [:div
   [tool state :empty]
   [tool state :wire]
   [tool state :head]
   [tool state :tail]])

(defn toolbar
  [app-state]
  [:div.toolbar
   [:div.fixed.top.left
    [play-pause app-state]]
   [:div.fixed.top.right
    [select-tool app-state]]])

