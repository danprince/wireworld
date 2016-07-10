(ns wireworld.util
  (:require [wireworld.settings :as settings]))

(defn scale-coord
  "turn screen coord into grid coord"
  [n] (.floor js/Math (/ n settings/size)))

(defn repeatv
  "utility for repeating v n times into an
   an associative structure"
  [n v]
  (into [] (repeat n v)))
