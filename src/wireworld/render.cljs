(ns wireworld.render
  (:require [wireworld.settings :refer [colors size]]
            [wireworld.select :as select]))

(def px size)

(defn render-cell!
  [ctx x y cell]
    (set! (.-fillStyle ctx) (get colors cell))
    (.fillRect ctx (* x px) (* y px) px px))

(defn clear-grid!
  [ctx grid]
  (let [width (.-width (.-canvas ctx))
        height (.-height (.-canvas ctx))]
    (.clearRect ctx 0 0 width height)))

(defn render-line!
  [ctx x1 y1 x2 y2]
  (.beginPath ctx)
  (.moveTo ctx x1 y1)
  (.lineTo ctx x2 y2)
  (.stroke ctx))

(defn render-gridlines!
  [ctx width height]
  (set! (.-strokeStyle ctx) "#444")
  (set! (.-lineWidth ctx) .2)
  (doseq [x (range width)]
    (render-line! ctx (* x px) 0 (* x px) (* height px)))
  (doseq [y (range height)]
    (render-line! ctx 0 (* y px) (* width px) (* y px))))

(defn render-grid!
  [ctx state]
  (let [grid (:grid state)
        xs (range (:width state))
        ys (range (:height state))]
    (doseq [x xs]
      (doseq [y ys]
        (let [cell (get-in grid [x y])]
          (when (not= cell :empty)
            (render-cell! ctx x y cell)))))))

(defn render-selection!
  [ctx start end]
  (let [[[x1 y1] [x2 y2]] (select/make-selection start end)
        xs (range x1 x2)
        ys (range y1 y2)]
    (doseq [x xs
            y ys]
        (render-cell! ctx x y :selection))))

(defn render-cursor!
  [ctx [x y] tool]
  (render-cell! ctx x y tool))

(defn render!
  [ctx state]
  (clear-grid! ctx (:grid state))
  (render-gridlines! ctx (:width state) (:height state))
  (render-grid! ctx state)
  (if (:selector-enabled? state)
    (render-selection! ctx (:select-from state) (:cursor state))
    (render-cursor! ctx (:cursor state) (:tool state))))

