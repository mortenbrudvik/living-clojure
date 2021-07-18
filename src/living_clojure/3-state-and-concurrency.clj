(ns living-clojure.3-state-and-concurrency)

; #################################
; Chapter 3 - State and Concurrency
; #################################

(def who-atom (atom :Earth))
who-atom
@who-atom ; value of atom

(reset! who-atom (atom :Mars)) ; Change value

(def who-atom (atom :Earth))
(defn change [state]
  (case state
    :Earth :Mars
    :Mars  :Jupiter
    :Jupiter))
(swap! who-atom change) ; swap value 
@who-atom
(swap! who-atom change)

(def counter (atom 0))
(dotimes [_ 4] (swap! counter inc))
@counter

(def counter (atom 0))
(let [n 5]
  (future (dotimes [_ n] (swap! counter inc))) 
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc))))
@counter


(defn inc-print [val]
  (println val)
  (inc val))
(swap! counter inc-print)

(def counter (atom 0))
(let [n 2]
  (future (dotimes [_ n] (swap! counter inc-print)))
  (future (dotimes [_ n] (swap! counter inc-print)))
  (future (dotimes [_ n] (swap! counter inc-print))))
@counter


; ##################################
; Using Refs for Coordinated Changes
; ##################################
