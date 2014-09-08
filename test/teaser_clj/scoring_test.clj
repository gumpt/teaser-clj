(ns teaser-clj.scoring-test
  (:require [clojure.test :refer :all]
            [teaser-clj.scoring :refer :all]))

(deftest common-elements-test
  (testing "#'teaser-clj.scoring/common-elements"
    (are [x y z] (= x (common-elements y z))
     ["this" "and"] ["this", "and", "not", "that"] ["this", "and"])))

(deftest top-x-test
  (testing "#'teaser-clj.scoring/top-x"
    (are [x y z] (= x (top-x y z))
         {\space 4, \t 4, \s 3} 3 (frequencies "this isn't a good test"))))

(deftest titlescore-test
  (testing "#'teaser-clj.scoring/titlescore"
    (are [x y z] (= x (titlescore y z))
         1/4 "title" ["this" "is" "a" "title"]
         1/3 "test"  ["this" "test" "bad"])))

(deftest lengthscore-test
  (testing "#'teaser-clj.scoring/lengthscore"
    (are [x y] (== x (lengthscore y))
         0.85 "this is a test sentence"
         0.4  "this sentence is a test sentence"
         0.35 "this sentence is a test sentence.")))

(defn is-between?-test
  (testing "#'teaser-clj.scoring/is-between?"
    (are [x a b c] (== x (is-between? a b c))
         )))
