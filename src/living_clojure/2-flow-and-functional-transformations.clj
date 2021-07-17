(ns living-clojure.2-flow-and-functional-transformations)

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

(let [[planet size] ["Earth" 1]] ; destructering - the vector symbols is bound the values
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

(take 3 (rest (cycle ["Earth" "Mars"]))) ; rest 


; #########
; Recursion
; #########

(def planets ["Earth", "Mars", "Jupiter", "Uranus"])

(defn planet-is [in out] ; Recursive
  (if (empty? in)
    out
    (planet-is
     (rest in)
     (conj out
           (str "Planet is " (first in))))))
(planet-is planets [])

(defn planet-is [input]
  (loop [in input ; using a loop instead
         out []]
    (if (empty? in)
      out
      (recur (rest in)
             (conj out
                   (str "Planet is " (first in)))))))
(planet-is planets)

(defn countdown [n]
  (if (= n 0)
    n
    (countdown (- n 1))))
(countdown 1000000) ; will result in stackoverflow

(defn countdown [n]
  (if (= n 0)
    n
    (recur (- n 1)))) ; recur will not add to stack
(countdown 1000000) ; this will work fine

; ############################################
; The Functional Shape of Data Transformations
; ############################################

; Map the Ultimate

(def planets [:Earth :Mars :Jupiter])
(#(str %) :Earth)

(map #(str %) planets) ; map

(class (map #(str %) planets)) ; map returns a lazy seq

(take 10 (map #(str %) (range)))

(println "Look at Mars!")

(def planet-print (map #(println %) planets))
planet-print
(def planet-print (doall (map #(println %) planets)))
planet-print

(def planets ["Earth" "Mars" "Jupiter"])
(def moons ["Moon",  "Phobos", "Europa"])
(defn gen-planet-string [planet moon]
  (str planet "-" moon))
(map gen-planet-string planets moons)
; note: if one list is shorter than the other it will terminate at the shortest one

(map gen-planet-string planets (cycle ["a-moon", "another-moon"]))


; Reduce the Ultimate
(reduce + [1 2 3 4 5])
(reduce (fn [r x] (+ r (* x x))) [1 2 3]) ; 1 + (2*2) + (3*3)
(reduce (fn [r x] (if (nil? x) r (conj r x))) ; remove nil
        []
        [:Earh nil :Mars])


; Misc data shapting functions
(filter (complement nil?) [:Earth nil :Mars nil]) ; complement - return the oposite truth value

(filter keyword? [:Earth nil :Mars nil]) ; return only keyword values

(remove nil? [:Earth nil :Mars nil]) ; remove nil

(for [planet [:Earth :Mars]]
  (str (name planet)))

(for [planet [:Earth :Mars] ; nested iteration
      color [:blue :red]]
  (str (name color) (name planet)))

(for [planet [:Earth :Mars]
      color [:blue :red]
      :let [planet-str (str "planet-" (name planet))
            color-str (str "color-" (name color))
            display-str (str planet-str "-" color-str)]]
  display-str)

(for [planet [:Earth :Mars]
      color [:blue :red]
      :let [planet-str (str "planet-" (name planet))
            color-str (str "color-" (name color))
            display-str (str planet-str "-" color-str)]
      :when (= color :blue)]
  display-str)

(flatten [[:Earth :Mars]]) ; flattens the structure

(vec '(1 2 3))
(into [] '(1 2 3))

(sorted-map :b 2 :a 1 :c 4)
(into (sorted-map) {:b 2 :a 1 :c 7})

(into {} [[:b1 2] [:a 1] [:c 7]])
(into [] {:b1 2,  :a 1, :c 7})

(partition 3 [1 2 3 4 5 6 7 8 9]) ; into lists of 3 elements
(partition 3 [1 2 3 4 5 6 7 8 9 10])
(partition-all 3 [1 2 3 4 5 6 7 8 9 10])
(partition-by #(= 6 %) [1 2 3 4 5 6 7 8 9 10])






