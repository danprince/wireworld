(ns wireworld.core
  (:require [reagent.core :as reagent]
            [goog.events :refer [listen]]
            [wireworld.settings :as settings]
            [wireworld.actions :as actions]
            [wireworld.grid :as grid]
            [wireworld.controls :as controls]
            [wireworld.render :as render]))

(enable-console-print!)

;; use the window size to calculate grid size
(def window-width  (.-innerWidth js/window))
(def window-height (.-innerHeight js/window))
(def screen-width  (/ window-width settings/size))
(def screen-height (/ window-height settings/size))
(def width  (.ceil js/Math screen-width))
(def height (.ceil js/Math screen-height))

;; resize canvas to match grid size
(def canvas (.getElementById js/document "game"))
(def ctx (.getContext canvas "2d"))
(set! (.-width canvas) (* width settings/size))
(set! (.-height canvas) (* height settings/size))

;; used to initialize and later reset the grid
(defonce empty-grid (grid/make-grid width height))

;; app state will be persisted between reloads
(defonce app-state
  (reagent/atom
    {:grid (grid/make-grid width height)
     :width width
     :height height
     :cursor [-1 -1]
     :mousedown false
     :paused true
     :tool :wire}))

(defn scale-coord
  "turn screen coord into grid coord"
  [n] (.floor js/Math (/ n settings/size)))

(defn handle-key
  [event]
  (case (.-keyCode event)
    ;; play/pause
    13 (swap! app-state actions/toggle-pause)
    ;; clear grid
    27 (swap! app-state actions/swap-grid empty-grid)
    ;; draw with tool
    32 (swap! app-state actions/paint)
    ;; changing tool
    49 (swap! app-state actions/select-tool :empty)
    50 (swap! app-state actions/select-tool :wire)
    51 (swap! app-state actions/select-tool :head)
    52 (swap! app-state actions/select-tool :tail)
    ;; moving cursor
    37 (swap! app-state actions/move-cursor [-1 0])
    38 (swap! app-state actions/move-cursor [0 -1])
    39 (swap! app-state actions/move-cursor [1 0])
    40 (swap! app-state actions/move-cursor [0 1])
    ;; move cursor (vim)
    72 (swap! app-state actions/move-cursor [-1 0])
    74 (swap! app-state actions/move-cursor [0 1])
    75 (swap! app-state actions/move-cursor [0 -1])
    76 (swap! app-state actions/move-cursor [1 0])
    :no-else))

(defn handle-mousemove
  [event]
  (let [x (scale-coord (.-clientX event))
        y (scale-coord (.-clientY event))]
    ;; constantly move cursor to mouse coords
    (swap! app-state actions/teleport-cursor [x y])
    ;; if mouse is currently down, paint here
    (when (:mousedown @app-state)
      (swap! app-state actions/paint))))

(defn handle-mousedown
  [event]
  (swap! app-state assoc :mousedown true))

(defn handle-mouseup
  [event]
  (swap! app-state assoc :mousedown false))

(defn handle-click
  [event]
  (swap! app-state actions/paint))

(defn reset-cursor!
  [event]
  (swap! app-state actions/teleport-cursor [-1 -1]))

(defn interaction-loop! []
  (render/render! ctx @app-state)
  (js/setTimeout interaction-loop! 50))

(defn update-loop! []
  (when-not (:paused @app-state)
    (swap! app-state actions/tick))
  (js/setTimeout update-loop! 200))

;; define event handlers once to make sure they
;; aren't duplicated each time figwheel reloads
(defonce events
  (do
    (update-loop!)
    (interaction-loop!)
    (listen js/window "keydown" handle-key)
    (listen canvas "mousedown"  handle-mousedown)
    (listen canvas "mouseup"    handle-mouseup)
    (listen canvas "mousedown"  handle-click)
    (listen canvas "mouseenter" handle-mousemove)
    (listen canvas "mousemove"  handle-mousemove)
    (listen canvas "touchmove"  handle-mousemove)
    (listen canvas "touchstart" handle-mousedown)
    (listen canvas "touchend"   (juxt handle-mouseup reset-cursor!))))

;; render controls into the dom
(reagent/render-component
  [controls/toolbar app-state]
  (.getElementById js/document "app"))

(defn on-js-reload [])

