(ns wireworld.grid)

(defn repeatv
  "utility for repeating v n times into an
   an associative structure"
  [n v]
  (into [] (repeat n v)))

(defn make-grid
  "create a width x height grid of :empty cells"
  [width height]
  (repeatv width (repeatv height :empty)))

(defn get-neighbours
  "returns the vector of neighbours around grid[x][y]"
  [grid x y]
  [(get-in grid [(- x 1) (- y 1)])
   (get-in grid [(- x 1) (- y 0)])
   (get-in grid [(- x 1) (+ y 1)])
   (get-in grid [(- x 0) (- y 1)])
   (get-in grid [(- x 0) (+ y 1)])
   (get-in grid [(+ x 1) (- y 1)])
   (get-in grid [(+ x 1) (- y 0)])
   (get-in grid [(+ x 1) (+ y 1)])])

(defn update-inc
  "utility for inc'ing the value at k in m"
  [m k]
  (update m k inc))

(defn count-neighbours
  "returns a map of type -> count for neighbours
   at grid[x][y]"
  [grid x y]
  (->> (get-neighbours grid x y)
       (reduce update-inc {})))

(defn update-cell
  "returns new state of cell after applying wireworld
   rules to cell and corresponding map from count-neighbours"
  [cell neighbours]
  (condp = cell
    :empty :empty
    :head  :tail
    :tail  :wire
    :wire  (let [heads (:head neighbours)]
             (if (or (= heads 1) (= heads 2))
               :head
               :wire))))

(defn update-grid
  "returns new grid state after applying wireworld rules
   to each cell in grid"
  [grid]
  (into [] (map-indexed
    (fn [x column]
      (into [] (map-indexed
        (fn [y cell]
          (update-cell
            cell
            (count-neighbours grid x y)))
        column)))
    grid)))

