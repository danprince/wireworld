(ns wireworld.encode
  (:require [clojure.string :refer [join split trim]]))

(def chunk-size 3)
(def cell-size 2)

(def encodings
  [\a \b \c \d \e \f \g \h \i \j \k \l \m
   \n \o \p \q \r \s \t \u \v \w \x \y \z
   \0 \1 \2 \3 \4 \6])

(defn encode-cell
  [cell]
  (case cell
    :empty 0
    :wire 1
    :head 2
    :tail 3
    0))

(defn decode-cell
  [n]
  (case n
    0 :empty
    1 :wire
    2 :head
    3 :tail))

(defn shift-encode
  [bits n]
  (-> bits
    (bit-shift-left cell-size)
    (bit-or n)))

(defn encode-chunk
  [cells]
  (reduce shift-encode 0 cells))

(defn encode-grid
  "gonna need some serious docs"
  [grid]
  (let [flat (flatten grid)
        nums (map encode-cell flat)
        chunks (partition chunk-size nums)]
    (->> chunks
         (map encode-chunk)
         (map #(get encodings %))
         (join ""))))

(defn decode-shift
  [bits]
  (let [mask 2r00000011]
    [(bit-and mask bits)
     (bit-and mask (bit-shift-right bits 2))
     (bit-and mask (bit-shift-right bits 4))
     (bit-and mask (bit-shift-right bits 6))]))

(defn decode-chunk
  [c]
  (let [n (.charCodeAt c 0)]
    (decode-shift n)))

(defn decode-grid
  [data]
  (let [[w b64] (split data "|")
        width (js/parseInt w)
        chrs (split (js/atob b64) "")
        chunks (map decode-chunk chrs)
        cells (flatten chunks)]
    (->> cells
         (map decode-cell)
         (partition width)
         (mapv vec))))

(defn build-hash
  [width data]
  (str "w=" width "&data=" data))

(defn parse-hash
  [string]
  )

