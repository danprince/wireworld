(ns wireworld.render
  (:require [wireworld.settings :as settings]))

(def px settings/size)

(defn render-cell!
  [ctx x y cell]
    (set! (.-fillStyle ctx) (get settings/colors cell))
    (.fillRect ctx (* x px) (* y px) px px))

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
                (render-cell! ctx x y cell)))
            column)))
      grid)))

(defn render-cursor!
  [ctx [x y] tool]
  (render-cell! ctx x y tool))

(defn render-note!
  [ctx x y text]
  (let [sx (* x px)
        sy (* y px)
        len (* (count text) 9)]
    (set! (.-fillStyle ctx) "#fff")
    (.fillText
      ctx
      text
      (+ sx (/ px 4))
      (+ sy (/ px 2)))))

(defn render-notes!
  [ctx notes]
  (set! (.-font ctx) "bold 10pt monospace")
  (set! (.-textBaseline ctx) "middle")
  (doall (for [note notes]
    (render-note!
      ctx
      (:x note)
      (:y note)
      (:text note)))))

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
  (doall (for [x (range width)]
    (render-line! ctx (* x px) 0 (* x px) (* height px))))
  (doall (for [y (range height)]
    (render-line! ctx 0 (* y px) (* width px) (* y px)))))

(defn render!
  [ctx state]
  (clear-grid! ctx (:grid state))
  (render-gridlines! ctx (:width state) (:height state))
  (render-grid! ctx (:grid state))
  (render-cursor! ctx (:cursor state) (:tool state))
  (render-notes! ctx (:notes state)))

