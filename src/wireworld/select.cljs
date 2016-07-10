(ns wireworld.select)

(defn make-selection
  "turns a start and end coord into a vector
   which contains a vector of all x coords
   and a vector of all y coords"
  [[sx sy] [dx dy]]
  (let [x1 (min sx dx)
        x2 (+ 1 (max sx dx))
        y1 (min sy dy)
        y2 (+ 1 (max sy dy))]
    [[x1 y1] [x2 y2]]))

(defn extract-selection
  "given a vector of x coords and a range of
   y coords returns a 2d list of the cells
   at these positions within this grid"
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

