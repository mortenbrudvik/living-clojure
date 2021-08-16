(ns living-clojure.planet-talk-test
  (:require [clojure.test :refer :all]
            [living-clojure.5-how-to-use-Clojure-project-and-libraries :refer :all]))

(deftest test-5-Clojure-project-and-libraries
  (testing "string should be converted to snake_case"
    (is (= "hello_earth!" 
           (planet-talk "Earth")))))
