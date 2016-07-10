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

(defn clear-selection
  [state]
  (let [select-from (:select-from state)
        cursor (:cursor state)
        [[x1 y1] [x2 y2]] (select/make-selection select-from cursor)
        width (- x2 x1)
        height (- y2 y1)
        grid (:grid state)
        cells (grid/make-grid width height :empty)]
    (assoc state :grid (select/patch-cells grid cells x1 y1))))

(defn clear-grid
  [state]
  (let [width (:width state)
        height (:height state)
        cells (grid/make-grid width height :empty)]
    (swap-grid state cells)))

(defn delete
  [state]
  (if (:selector-enabled? state)
    (clear-selection state)
    (clear-grid state)))

(defn tick
  [state]
  (update state :grid grid/update-grid))

(defn start-selection
  [state]
  (-> state
    (assoc :selector-enabled? true)
    (assoc :select-from (:cursor state))))

(defn end-selection
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

