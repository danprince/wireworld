(ns wireworld.actions
  (:require [wireworld.grid :as grid]
            [wireworld.select :as select]))

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

(defn toggle-menu
  [state]
  (update state :showing-menu? not))

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

(defn enable-selector
  [state]
  (-> state
    (assoc :selector-enabled? true)
    (assoc :select-from (:cursor state))))

(defn disable-selector
  [state]
  (assoc state :selector-enabled? false))

(defn selection->clipboard
  [state]
  (let [origin (:select-from state)
        cursor (:cursor state)
        grid (:grid state)
        selection (select/make-selection origin cursor)
        cells (select/extract-selection selection grid)]
    (assoc state :clipboard cells)))

(defn clipboard->grid
  [state]
  (let [cells (:clipboard state)
        grid (:grid state)
        [x y] (:cursor state)]
    (if cells
      (assoc state :grid (select/patch-cells grid cells x y))
      state)))

