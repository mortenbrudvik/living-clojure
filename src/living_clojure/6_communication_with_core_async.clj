(ns living-clojure.6-communication-with-core-async
  (:require [clojure.core.async :as async]))

; #############################
; Communication with core.async
; #############################

(def planet-channel (async/chan 10)) ; create buffered channel 
(async/>!! planet-channel :Mars) ; blocking put >!!
(async/<!! planet-channel) ; blocking take <!!
(async/>!! planet-channel :Jupiter)
(async/>!! planet-channel :Saturn)
(async/close! planet-channel)
(async/<!! planet-channel) ; can remove after it is closed

(let [planet-channel (async/chan)]
  (async/go (async/>! planet-channel :Mars)) ; >! async
  (async/go (println "Earth is calling " (async/<! planet-channel))))

(def planet-channel (async/chan 10))
(async/go-loop [] ; Will wait for messagse
  (println "Earth is callin " (async/<! planet-channel))
  (recur))
(async/>!! planet-channel :Jupiter)
(async/>!! planet-channel :Mars)

(def mars-channel (async/chan 10)) ; multiple channels
(def earth-channel (async/chan 10))
(def jupiter-channel (async/chan 10))
(async/go-loop []
               (let[[v ch] (async/alts! [mars-channel
                                         earth-channel
                                         jupiter-channel])]
                (println "Got " v " from " ch)
                 (recur)))
(async/>!! jupiter-channel :jupiter)
(async/>!! earth-channel :earth)
(async/>!! mars-channel :mars)


