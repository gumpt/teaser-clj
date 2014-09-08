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

(deftest is-between?-test
  (testing "#'teaser-clj.scoring/is-between?"
    (are [x a b c] (= x (is-between? a b c))
         false 0 0 10
         true  5 0 10      
         true 10 0 10)))

(deftest get-keyword-score-test
  (testing "#'teaser-clj.scoring/get-keyword-score"
    (are [x y z] (= x (get-keyword-score y z))
         0 {"test" 0}    "test"
         0 {"notest" 12} "test"
         5 {"test" 5}    "test")))

(deftest positionscore-test
  (testing "#'teaser-clj.scoring/positionscore"
    (are [x y z] (= x (positionscore y z))
         0.17 0 10
         0.23 1 10
         0.14 2 10
         0.08 3 10
         0.05 4 10
         0.04 5 10
         0.06 6 10
         0.04 7 10
         0.04 8 10
         0.15 9 10
         0   10 10)))