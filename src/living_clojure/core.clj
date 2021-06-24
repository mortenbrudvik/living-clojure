(ns living-clojure.core)
(require 'clojure.set)

; ####################################
; Chapter 1 - The Structure of Clojure
; ####################################

42
(/ 2 3) ; ratio 2/3
(/ 2 3.0) ; division by decimal
"jam"
:jam ; keyword
\j ; letter
true
nil ; represent absence of a value
(+ 1 1) ; operation - adding two numbers 1+1
(+ 1 (+ 8 3))
1

; #############################################
; Collections (Immutable)
; #############################################
; Lists
'(1 2 'fruit' :sun)
'(1, 2, 'fruit', :sun) ; Commas is ignored
(first '(1 2 'fruit' :sun))
(rest '(1 2 'fruit' :sun))
(first (rest '(1 2 'fruit' :sun)))
(cons 5 '()) ; Add to a list
(cons 5 '(1 2 'fruit' :sun)) ; Add to a list - at beginning
(conj '(1 2 'fruit' :sun) 5 3) ; Add multiple - at beginning

; Vectors - Works similar to list but with index access
[1 2 :bag 'sun']
(nth [1 2 :bag 'sun'] 2) ; Get item at an index
(last [1 2 :bag 'sun']) ; Better performance than list
(count [1 2 :bag 'sun'])
(conj '(1 2 'fruit' :sun) 5 3) ; Add multiple - at end of list

; Maps - key-value pairs
{:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'}
(get {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car2) ;  Get an item from map
(get {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car4 'Ford') ;  Default value
(:car2 {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'}) ;  Use the key only to get value
(keys {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'})
(vals {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'})
(assoc {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car2 'Audi') ; update a value
(dissoc {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'} :car2) ; Remove key-value pair
(merge {:car1 'Toyota', :car2 'Fiat', :car3 'Volvo'}
       {:car4 'Audi', :car5 'Ford'}
       {:car6 'Saab'})

; Sets - Unique collection of data
#{:red :blue :white :pink}
(clojure.set/union #{:fiat :volvo :ford} #{:toyota :ford :audi}) ; Union
(clojure.set/difference #{:fiat :volvo :ford} #{:toyota :ford :audi}) ; Difference - Removes from first set the matching values
(clojure.set/intersection #{:fiat :volvo :ford} #{:toyota :ford :audi}) ; Intersection - Matching values between sets
(set [1 2 3]) ; Convert vector to set
(get #{:door :wall :floor :ceiling} :wall)
(:wall #{:door :wall :floor :ceiling}) ; get
(#{:door :wall :floor :ceiling} :wall) ; get
(contains? #{:red :blue :white :pink} :blue)
(conj #{:red :blue :white :pink} :black) ; add
(disj #{:red :blue :white :pink} :pink) ; remove

; Clojure is all about lists -> Clojure code is made of lists of data -> Code is data!

; ##############################
; Symbols and the art of binding
; ##############################

; def allows you to give something a name
(def planet "Mars")
planet
living-clojure.core/planet

(let [planet "Venus"] ; let - binds symbols that are only available within the context of let
  planet)
(let [planet "Jupiter" moon "Io"]
  [planet moon])
; NOTE: What happens in a let, stays in a let.

; def - global  vars
; let - temporary bindings


; ##########################
; Creating our own functions
; ##########################

(defn take-the-red-pill [] "Clojure here we go!")
(take-the-red-pill)

(defn get-full-name [first-name last-name]
  {:data-type "Full name"
   :first-name first-name
   :last-name last-name})
(get-full-name "Morten" "Brudvik")

(fn [] (str "lets do it" "!")) ; anonymous functions
((fn [] (str "lets do it" "!"))) ; extra paranteces to invoke


(def lets-do-it (fn [] (str "lets do it" "!"))) ; defn is the same as def
(lets-do-it)

(#(str "lets do it" "!")) ; # short hand form of anonymous functions
(#(str "lets do it" "!" " - " %) "again") ; % to represent a value
(#(str "lets do it" "!" " - " %1 %2) "again" "?") ; multiple values


; #########################################
; Keep your symbols organized in Namespaces
; #########################################

(ns brudvik.morten) ; specify the namespace
(require '[brudvik.morten :as mb]) ; load a given namespace and set an alias for it
mb/planet-name
*ns* ;  show current namespace

(def planet-name "Jupiter")
planet-name
brudvik.morten/planet-name

(ns brudvik.morten ; Typical setup of namespace and require to load other namespaces
  "My app example"
  (:require
   [clojure.set :as set]
   [clojure.string :as str]))

; More about namespaces: https://clojure.org/guides/learn/namespaces



; ###############################################
; Chapter 2 - Flow and Functional Transformations
; ###############################################

; Expression is code that can be evaluated for a result.
(first [1 2 3 4])
; Form is a valid expression that can be evaluated.
(first [:a :b :c])

; #########################
; Controlling flow an logic
; #########################

(true? true)
(true? false)

(nil? nil) ; true
(nil? 1) ; false 

(not true) ; false
(not nil) ; true
(not "hi") ; false

(= :planet :planet)
(= :planet 1)
(= '(:planet "fish")  [:planet "fish"]) ; true
(not=  :planet 4) ; true - shortcut for  (not (= :planet 4))

; ##########################
; Logic Tests on Collections
; ##########################

; Test if a collection is empty
(empty? [:planet])
(empty? [])
(empty? '())

; IPersistentCollection - Shared interface between all collections (count, conj and seq)
; seq - turns a collection into a sequence
; sequence is a walkable list structure (first, rest and cons)
(seq [1 2 3])  ; is treated in tests as logically true
(seq []) ; nil - logically false, same as (not (empty? []))

(class [1 2 3])
(class (seq [1 2 3]))

(every? odd? [1 3 5]) ; check every collection item
(every? odd? [1 2 3 4 5])

; NOTE: it'd idiomatic in Clojure to use question mark if it returns a boolean
(defn planet? [x]
  (= x :planet))
(planet? :planet)
(planet? :moon)
(every? planet? [:planet :planet])
(every? planet? [:planet :moon])
(every? (fn [x] (= x :planet)) [:planet :planet]) ; anonymous function
(every? (fn [x] (= x :planet)) [:planet :moon])
(every? #(= % :planet) [:planet :planet]) ; shorthand 
(not-any? #(= % :moon) [:planet]) ; true if does not contain

; some pred col - returns the first logical true value of predicate, nil otherwise.
(some #(> % 3) [1 2 3 4 5]) ; true if there is numbers greater than 3
(#{1 2 3 4 5} 3) ; set is a functions of its members. non-nil is considered logical true
(some #{3} [1 2 3 4 5]) ; first matching element
(some #{nil} [nil nil]) ; be careful with logical false values
(some #{false} [false false])

; ####################################
; Harnessing the Power of Flow Control
; ####################################






































