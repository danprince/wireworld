(ns wireworld.select
  "Utilities for working with visual selections")

(defn make-selection
  [[sx sy] [dx dy]]
  (let [x1 (min sx dx)
        x2 (+ 1 (max sx dx))
        y1 (min sy dy)
        y2 (+ 1 (max sy dy))]
    [[x1 y1] [x2 y2]]))

(defn extract-selection
  [[[x1 y1] [x2 y2]] grid]
  (let [xs (range x1 x2)
        ys (range y1 y2)]
    (for [x xs]
      (for [y ys]
        (get-in grid [x y])))))

(defn patch-cells
  "pastes cells into grid at tx ty"
  [grid cells tx ty]
  (let [width (count cells)
        height (count (first cells))]
    (into [] (map-indexed
      (fn [x column]
        (into [] (map-indexed
          (fn [y cell]
            (if (and (>= x tx)
                     (>= y ty)
                     (< x (+ tx width))
                     (< y (+ ty height)))
              (nth (nth cells (- x tx)) (- y ty))
              cell))
          column)))
    grid))))

