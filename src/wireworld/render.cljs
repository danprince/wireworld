(ns wireworld.render
  (:require [wireworld.settings :as settings]))

(defn render-cell!
  [ctx size x y cell]
    (set! (.-fillStyle ctx) (get settings/colors cell))
    (.fillRect ctx (* x size) (* y size) size size))

(defn clear-grid!
  [ctx grid]
  (let [width (.-width (.-canvas ctx))
        height (.-height (.-canvas ctx))]
    (.clearRect ctx 0 0 width height)))

(defn render-grid!
  [ctx grid]
  (doall
    (map-indexed
      (fn [x column]
        (doall
          (map-indexed
            (fn [y cell]
              (when (not= cell :empty)
                (render-cell! ctx settings/size x y cell)))
            column)))
      grid)))

(defn render-cursor!
  [ctx [x y] tool]
  (render-cell! ctx settings/size x y tool))

(defn render!
  [ctx state]
  (clear-grid! ctx (:grid state))
  (render-grid! ctx (:grid state))
  (render-cursor! ctx (:cursor state) (:tool state)))

