(ns living-clojure.4-java-interop-and-polymorphism)

; #########################################
; Chapter 4 - Java Interop and Polymorphism
; #########################################

(class "planet")
(. "planet" toUpperCase) ; . to invoke method on object
(.toUpperCase "planet") ; altermative to above
(.indexOf "planet" "net") ; with argument
(new String "Skål!") ; create new object
(String. "Skål!") ; alternative to above

(.getHostName (java.net.InetAddress/getByName "localhost")) ; from specfic namespace

(def sb (doto (StringBuffer. "Who") ; doto macro
          (.append " are")
          (.append " you?")))
(.toString sb)

(import 'java.util.UUID)
(UUID/randomUUID)


; Practical Polymorphism
(defn who-are-you [input] ; cond
  (cond
    (= java.lang.String (class input)) "String - Who are you?"
    (= clojure.lang.Keyword (class input)) "Keyword - Who are you?"
    (= java.lang.Long (class input)) "Number - Who are you?"))
(who-are-you :alice)
(who-are-you "alice")
(who-are-you 123)
(who-are-you true)

(defmulti who-are-you class) ; multimethods - example 1 (one function)
(defmethod who-are-you java.lang.String [input] 
  (str "String - who are you? " input))
(defmethod who-are-you clojure.lang.Keyword [input]
  (str "Keyword - who are you? " input))
(defmethod who-are-you java.lang.Long [input]
  (str "Number - who are you? " input))
(defmethod who-are-you :default [input]
  (str "I don't know - who are you? " input))
(who-are-you :alice)
(who-are-you "alice")
(who-are-you 123)
(who-are-you true)

(defn calculate-bmi [height weight] ; multimethods -example 2
  (float (* (/ (/ weight height) height) 10000)))
(calculate-bmi 177 80)
(defn is-bmi-normal? [bmi]
  (and (< bmi 25.0) (> bmi 19.9999)))
(is-bmi-normal? 20.0)
(defn check-bmi [height weight]
                      (let [bmi (calculate-bmi height weight)]
                        (if (and (< bmi 25.0) (> bmi 19.9999))
                          :normal
                          :not-normal)))
(defmethod check-bmi :normal [_]
  "Your weight is normal")
(defmethod check-bmi :not-normal [_]
  "Your weight is not normal")
(check-bmi 177 70)

(defprotocol BigMushroom 
  (eat-mushroom [this])) ; defprotocol (multi functions)
(extend-protocol BigMushroom
  java.lang.String
  (eat-mushroom [this]
    (str (.toUpperCase this) " mmm tasty"))
  clojure.lang.Keyword
  (eat-mushroom [this]
    (case this
      :grow "Eat the right side!"
      :shrink "Eat the left side"))
  java.lang.Long 
  (eat-mushroom[this]
    (if (< this 3)
      "Eat the right side to grow"
      "Eat the left side to shrink")))
(eat-mushroom "Big Mushroom")
(eat-mushroom :grow)
(eat-mushroom 1)

(defprotocol Habitable
  (is-habitable-for-humans [this]))
(defrecord Planet [ratio-to-earth-diameter nickname]
  Habitable 
  (is-habitable-for-humans [this]
                           (str "no")))
(defrecord HabitablePlanet [ratio-to-earth-diameter nickname]
  Habitable
  (is-habitable-for-humans [this]
   (str "yes")))
(def Mercury (Planet. 0.383 "The swift planet"))
(def Venus (Planet. 0.949 "Morning star"))
(def Earth (HabitablePlanet. 1.0 "Blue planet"))
(def Mars (Planet. 0.532 "Red planet"))
(def Jupiter (Planet. 11.21 "Giant planet"))
(def Saturn (Planet. 9.49 "Ringed planet"))
(def Uranus (Planet. 4.01 "Ice giant"))
(def Neptune (Planet. 3.88 "Big blue planet"))
(class Mars)
(.-nickname Saturn)
(.-ratio-to-earth-diameter Neptune)
(is-habitable-for-humans Mercury)
(is-habitable-for-humans Earth)

(deftype Planet [] ; deftype - drop parameters
  Habitable
  (is-habitable-for-humans [this]
                           (str "no")))
(deftype HabitablePlanet []
  Habitable
  (is-habitable-for-humans [this]
                           (str "yes")))
(def Mars (Planet.))
(def Earth (HabitablePlanet.))
(is-habitable-for-humans Mars)
(is-habitable-for-humans Earth)

(defn is-habitable-for-humans [planet] ; using map
  (if (= (:type planet) "Planet")
    "no"
    "yes"))
(is-habitable-for-humans {:type "Planet"})
(is-habitable-for-humans {:type "HabitablePlanet"})








                                                          
