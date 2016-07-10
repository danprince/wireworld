(ns wireworld.core
  (:require [reagent.core :as reagent]
            [goog.events :refer [listen]]
            [wireworld.settings :as settings]
            [wireworld.actions :as actions]
            [wireworld.events :as events]
            [wireworld.grid :as grid]
            [wireworld.controls :as controls]
            [wireworld.render :as render]))

(enable-console-print!)

;; DOM Events -> wireworld.events -> wireworld.actions
;;                                          v
;;  wireworld.render <- update-loop!  <- app-state

;; use the window size to calculate grid size
(def window-width  (.-innerWidth js/window))
(def window-height (.-innerHeight js/window))
(def screen-width  (/ window-width settings/size))
(def screen-height (/ window-height settings/size))
(def width  (.ceil js/Math screen-width))
(def height (.ceil js/Math screen-height))

;; resize canvas to match grid size
(def canvas (.getElementById js/document "game"))
(set! (.-width canvas) (* width settings/size))
(set! (.-height canvas) (* height settings/size))
(def ctx (.getContext canvas "2d"))

;; used to initialize and later reset the grid
(defonce empty-grid (grid/make-grid width height))

;; app state will be persisted between reloads
(defonce app-state
  (reagent/atom
    {:grid empty-grid
     :width width
     :height height
     :cursor [-1 -1]
     :paused false
     :tool :wire}))

(defn ui-loop! []
  (render/render! ctx @app-state)
  (js/setTimeout ui-loop! 50))

(defn update-loop! []
  (when-not (:paused @app-state)
    (swap! app-state actions/tick))
  (js/setTimeout update-loop! 100))

(defn handle-keys [event]
  (swap! app-state events/handle-keys event))

(defn handle-keydown [event]
  (swap! app-state events/handle-keydown event))

(defn handle-keyup [event]
  (swap! app-state events/handle-keyup event))

(defn handle-click [event]
  (swap! app-state actions/paint))

(defn handle-mousedown [event]
  (swap! app-state assoc :mousedown? true))

(defn handle-mouseup [event]
  (swap! app-state assoc :mousedown? false))

(defn handle-mousemove [event]
  (swap!
    app-state
    (fn [state]
      (-> state
        (events/handle-mousemove event)
        (events/handle-drag event)))))

(defn reset-mouse!
  "reset the mouse down and selecting states"
  [event]
  (swap! app-state merge {:mousedown? false
                          :selecting? false}))

(defn reset-cursor!
  "move the cursor off of the screen"
  [event]
  (swap! app-state actions/teleport-cursor [-1 -1]))

;; uses defonce to make sure event handlers
;; aren't redefined each time figwheel reloads
(defonce events
  (do
    (update-loop!)
    (ui-loop!)
    (listen js/window "keydown" handle-keys)
    (listen js/window "keydown" handle-keydown)
    (listen js/window "keyup" handle-keyup)
    (listen canvas "mousedown" handle-mousedown)
    (listen canvas "mouseup" handle-mouseup)
    (listen canvas "mousedown" handle-click)
    (listen canvas "mouseenter" handle-mousemove)
    (listen canvas "mousemove" handle-mousemove)
    (listen canvas "touchmove" handle-mousemove)
    (listen canvas "touchstart" handle-mousedown)
    ;; reset mouse if it leaves the canvas
    (listen canvas "mouseout" reset-mouse!)
    ;; reset cursor after touch to prevent it hanging around onscreen
    (listen canvas "touchend" (juxt handle-mouseup reset-cursor!))))

;; render reagent components into the dom
(reagent/render-component
  [controls/toolbar app-state]
  (.getElementById js/document "app"))

(defn on-js-reload [])

