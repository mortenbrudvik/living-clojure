(ns living-clojure.3-state-and-concurrency)

; #################################
; Chapter 3 - State and Concurrency
; #################################

; * Refs   - synchronous  | coordinated
; * Atoms  - synchronous  | uncoordinated
; * Agents - asynchronous | uncoordinated
; https://stackoverflow.com/questions/9132346/clojure-differences-between-ref-var-agent-atom-with-examples

; Using Atoms for Independent Items

(def who-atom (atom :Earth))
who-atom
@who-atom ; @ derefernce to get value of atom

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
  (future (dotimes [_ n] (swap! counter inc))) ; future - creates a new  thread
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



; Using Refs for Coordinated Changes

(def put_apple_in_basket (ref 10))
@put_apple_in_basket
(def basket (ref 0))
@basket

(defn pick_apple []
  (when (pos? @put_apple_in_basket)
    (alter put_apple_in_basket dec) ; alter - set the transaction value (must be side effect free)
    (alter basket inc)))
(dosync (pick_apple)) ; dosync - run in transaction
@put_apple_in_basket
@basket

(defn pick_apple2 []
  (dosync (when (pos? @put_apple_in_basket)
    (alter put_apple_in_basket dec)
    (alter basket inc))))
(let [n 2]
  (future (dotimes [_ n] (pick_apple2)))
  (future (dotimes [_ n] (pick_apple2)))
  (future (dotimes [_ n] (pick_apple2))))
@put_apple_in_basket
@basket

(defn pick_apple3 []
  (dosync (when (pos? @put_apple_in_basket)
            (commute put_apple_in_basket dec) ; commute - will not retry
            (commute basket inc))))
(let [n 2]
  (future (dotimes [_ n] (pick_apple3)))
  (future (dotimes [_ n] (pick_apple3)))
  (future (dotimes [_ n] (pick_apple3))))
@put_apple_in_basket
@basket

(def x (ref 1))
(def y (ref 1))
(defn new-values []
  (dosync 
   (alter x inc)
   (ref-set y (+ 2 @x)))) ; ref-set - directly set the value
(let [n 2]
  (future (dotimes [_ n] (new-values)))
  (future (dotimes [_ n] (new-values))))
@x
@y


; Using Agents to Manage Changes on Their Own
(def who-agent (agent :moon))
@who-agent
(defn change [state]
  (case state 
    :moon :planet
    :planet :gas-giant
    :gas-giant :star
    :star :black-hole
    :black-hole :white-hole))
(send who-agent change) ; send - change state (CPU bond operations)
(send-off who-agent change) ; send-off -  (IO bond operations)

(defn change-error [state]
  (throw (Exception. "Boom!"))) ; 
(send who-agent change-error) ; Will return failed state, but last state will be kept
(send-off who-agent change) ; will see catched error thrown
(agent-errors who-agent) ; inspect errors
(restart-agent who-agent :moon) ; restart agent
(send who-agent change)

(set-error-mode! who-agent :continue) ; Control how agent respond to error :continue|:fail
(defn err-handler-fn [a ex]
  (println "error " ex " value is " @a))
(set-error-handler! who-agent err-handler-fn) ; set error handler for agent
(send who-agent change-error)


