(ns living-clojure.5-how-to-use-Clojure-project-and-libraries
  (:require [camel-snake-kebab.core :as csk]))


(defn planet-talk [input]
  (csk/->snake_case
   (str "Hello " input "!")))

(defn -main [& args]
  (println  (planet-talk (first args))))



(planet-talk "Earth")

(csk/->snake_case "Hello Earth!")

