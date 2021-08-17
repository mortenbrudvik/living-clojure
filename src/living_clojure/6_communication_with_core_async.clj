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

; Random selection of a channel
(def spacex-planet-service-chan (async/chan 10))
(def blue-origin-planet-service-chan (async/chan 10))
(defn random-add []
  (reduce + (conj [] (repeat 1 (rand-int 10000)))))
(defn request-spacex-planet-service []
  (async/go
   (random-add)
   (async/>! spacex-planet-service-chan
             "Space-X wins the NASA contract")))
(defn request-blue-moon-planet-service []
  (async/go
    (random-add)
    (async/>! blue-origin-planet-service-chan
              "Blue Mooon wins the NASA contract")))
(defn request-nasa-contract []
  (request-spacex-planet-service)
  (request-blue-moon-planet-service)
  (async/go (let [[v] (async/alts!
                       [spacex-planet-service-chan
                        blue-origin-planet-service-chan])]
              (println v))))
(request-nasa-contract)





