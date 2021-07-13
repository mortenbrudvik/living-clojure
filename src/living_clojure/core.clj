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

(if true "it is true" "it is false")
(if false "it is true" "it is false")
(if nil "it is true" "it is false") ; false
(if (= :planet :planet)
  "it is a planet"
  "it is not a planet")

(let [is-larger-than-earth (> 1.1 1)]
  (if is-larger-than-earth
    "Larger"
    "Smaller"))
(if-let [is-larger-than-earth (> 1.1 1)] ; if-let combines let and if
  "Larger"
  "Smaller")

(defn is-large-planet [is-larger-than-earth]
  (when is-larger-than-earth "Larger")) ; when will ignore false

(is-large-planet (> 1.1 1.0))

(when-let [is-larger-than-earth true] ; combine let and when
  "larger")

(let [planet "Earth"] ; NB! will return nil if there is no match
  (cond ; Similar to if else.
    (= planet "Earth") "Home Sweet Home"
    (= planet "Mars") "The Rusty Planet"
    (= planet "Jupiter") "King of the Solar System"
    :else "Unknown")) ; default clause. Note: There is nothing special about :else, we could have used :default

(let [planet "Mars"] ; NB! will return an error if no match
  (case planet ; Shorthand for cond
    "Earth" "Home Sweet Home"
    "Mars" "The Rusty Planet"
    "Jupiter" "King of the Solar System"
    "Unknown")) ; default

; #######################################################
; Functions Creating Functions and Other Neat Expressions
; #######################################################

(defn turn [car-brand direction]
  (if (= direction :left)
    (str car-brand " is turning left")
    (str car-brand " is turning right")))

(turn "Volvo" :left)

(partial turn "Volvo") ; partial - creating a new function with some of the parameters partially applied
((partial turn "Volco") :left)

(defn multiply-with-2 [number]
  (* number 2))
(defn multiply-with-3 [number]
  (* number 3))
(multiply-with-2 2)
(multiply-with-3 2)

(defn multiply-with-6 [number] ; Method composition
  (multiply-with-3
   (multiply-with-2 number)))
(multiply-with-6 5)

(defn multiply-with-12 [number]
  ((comp multiply-with-6 multiply-with-2) number)) ; comp - more elegant
(multiply-with-12 10)

(defn adder [x y]
  (+ x y))
(adder 1 2)

(def adder-5 (partial adder 5))
(adder-5 10)


; #############
; Destructering
; #############

(let [[planet size] ["Earth" 1]] ; destructering - the vector sybols is bound the values
  (str planet " is " size " Earth sizes"))

(let [x ["Earth" 1]
      planet (first x) ; same as above without destructering.
      size (last x)]
  (str planet " is " size " Earth masses"))

(let [[planet [size]] ["Earth" [1]]]
  (str planet " is " size " Earth masses"))

(let [[planet [size] :as original] ["Earth" [1]]] ; :as to keep the original data structure
  {:planet planet :size size :original original})

(let [{planet1 :planet1 planet2 :planet2} ; destructering maps
      {:planet1 "Earth" :planet2 "Mars"}]
  (str planet1 " and " planet2))

(let [{planet1 :planet1 planet2 :planet2 :or {planet2 "Jupiter"}} ; default value if value is not in the map
      {:planet1 "Earth"}]
  (str planet1 " and " planet2))

(let [{planet1 :planet1 :as all-planets} ; keep the original as data structure
      {:planet1 "Earth"}]
  [planet1 all-planets])

(let [{:keys [planet1 planet2]} ; :keys - same name of the binding as the key
  {:planet1 "Earth" :planet2 "Mars"}]
 (str planet1 " and " planet2))

(defn planet-names [planets] ; Destructering parameters to a function
  (str "The planets are "
       (:planet1 planets)
       " and "
       (:planet2 planets)))
(planet-names {:planet1 "Earth" :planet2 "Mars"})

(defn planet-names [{:keys [planet1 planet2]}] ; even clearer
  (str planet1 " and " planet2))
(planet-names {:planet1 "Earth" :planet2 "Mars"})


; #####################
; The Power of Laziness
; #####################

(take 5 (range)) ; range - first five numbers - Lazy sequence

(count (take 1000 (range)))

(repeat 3 "Earth") ;  ; repeat 3 times

(take 5 (repeat "Earth")) 

(rand-int 10) ; random 0-10

(repeat 5 (rand-int 10)) ; 

#(rand-int 10)

(take 10 (repeatedly #(rand-int 10))) ; repeatedly - same a repeat but takes a method

(take 6 (cycle ["Earth" "Mars"])) ; Cycle between two values

(take 3 (rest (cycle ["Earth" "Mars"]))) ; rest - returns a seq


; #########
; Recursion
; #########


















































