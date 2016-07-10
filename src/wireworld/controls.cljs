(ns wireworld.controls
  (:require [wireworld.actions :as act]
            [wireworld.settings :as s]
            [wireworld.encode :as encode]))

(def sprite-size 24)

(defn sprite
  [x y]
  [:div.sprite
   {:style
    {:background-image "url(img/sprites.png)"
     :background-position-x (str (* x -1 sprite-size) "px")
     :background-position-y (str (* y -1 sprite-size) "px")
     :height sprite-size
     :width sprite-size
     :cursor :pointer
     :display :inline-block}}])

(def play-icon [sprite 0 0])
(def pause-icon [sprite 1 0])
(def step-icon [sprite 2 0])
(def trash-icon [sprite 3 0])
(def download-icon [sprite 4 0])
(def info-icon [sprite 5 0])

(defn player
  [state]
  [:div
   (if (:paused @state)
     [:span
       [:span.icon
        {:title "Enter"
         :on-click #(swap! state act/play)}
        play-icon]
       [:span.icon
        {:title "n"
         :on-click #(swap! state act/tick)}
        step-icon]]
     [:span.icon
      {:on-click #(swap! state act/pause)}
      pause-icon])])

(defn tool
  [state k title]
  [:span.swatch
    {:title title
     :on-click #(swap! state act/select-tool k)
     :data-selected (= (get @state :tool) k)
     :style {:background (get s/colors k)}}])

(defn select-tool
  [state]
  [:div
   [tool state :empty 1]
   [tool state :wire 2]
   [tool state :head 3]
   [tool state :tail 4]])

(defn dialog
  [child]
  [:div.overlay
    [:div.dialog child]])

(defn menu
  [state]
  [dialog
   [:div
    [:h2 "Wireworld"]
    [:p
     "Cellular automata for modelling electrons"]
    ]])

(defn toolbar
  [app-state]
  [:div.toolbar
   (when (:showing-menu? @app-state)
     [menu app-state])
   [:div.fixed.top.left
    [player app-state]]
   [:div.fixed.top.right
    [select-tool app-state]]])

