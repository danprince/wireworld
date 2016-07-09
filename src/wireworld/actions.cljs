(ns wireworld.actions
  (:require [wireworld.grid :as grid]))

(defn play
  [state]
  (assoc state :paused false))

(defn pause
  [state]
  (assoc state :paused true))

(defn toggle-pause
  [state]
  (update state :paused not))

(defn select-tool
  [state tool]
  (assoc state :tool tool))

(defn move-cursor
  [state movement]
  (update state :cursor (partial map +) movement))

(defn teleport-cursor
  [state coords]
  (assoc state :cursor coords))

(defn paint
  [state]
  (assoc-in
    state
    (cons :grid (:cursor state))
    (:tool state)))

(defn swap-grid
  [state grid]
  (assoc
    state
    :grid
    grid))

(defn tick
  [state]
  (update state :grid grid/update-grid))

