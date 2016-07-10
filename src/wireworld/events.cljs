(ns wireworld.events
  "Collection of functions which describe event-based updates
   to state, designed to be used with swap!"
  (:require [wireworld.actions :as actions]
            [wireworld.util :refer [scale-coord]]))

(defn handle-keys
  [state event]
  (case (.-keyCode event)
    ;; play/pause
    13 (actions/toggle-pause state)
    ;; delete from grid or selection
    88 (actions/delete state)
    ;; draw with tool
    32 (actions/paint state)
    ;; changing tool
    49 (actions/select-tool state :empty)
    50 (actions/select-tool state :wire)
    51 (actions/select-tool state :head)
    52 (actions/select-tool state :tail)
    ;; moving cursor
    37 (actions/move-cursor state [-1 0])
    38 (actions/move-cursor state [0 -1])
    39 (actions/move-cursor state [1 0])
    40 (actions/move-cursor state [0 1])
    ;; move cursor (vim)
    72 (actions/move-cursor state [-1 0])
    74 (actions/move-cursor state [0 1])
    75 (actions/move-cursor state [0 -1])
    76 (actions/move-cursor state [1 0])
    ;; step by one
    78 (actions/tick state)
    89 (actions/selection->clipboard state)
    80 (actions/clipboard->grid state)
    ;; encode and update url
    ;;83 (actions/encode-to-url state)
    state))

(defn handle-keydown
  [state event]
  (case (.-keyCode event)
    17 (actions/start-selection state)
    state))

(defn handle-keyup
  [state event]
  (case (.-keyCode event)
    17 (actions/end-selection state)
    state))

(defn handle-mousemove
  [state event]
  (let [x (scale-coord (.-clientX event))
        y (scale-coord (.-clientY event))]
    ;; constantly move cursor to mouse coords
    (actions/teleport-cursor state [x y])))

(defn handle-drag
  [state event]
  (let [mousedown? (:mousedown? state)
        selecting? (:selecting? state)]
    (if (and mousedown? (not selecting?))
      (actions/paint state)
      state)))

