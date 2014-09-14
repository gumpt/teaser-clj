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
         0 ""
         0.25  "this sentence is a test"
         1 "this sentence is a test sentence with twenty words so that its score will be exactly one and not more")))

(deftest get-keyword-score-test
  (testing "#'teaser-clj.scoring/get-keyword-score"
    (are [x y z] (= x (get-keyword-score y z))
         0 {"test" 0}    "test"
         0 {"notest" 12} "test"
         5 {"test" 5}    "test")))

(deftest positionscore-test
  (testing "#'teaser-clj.scoring/positionscore"
    (are [x y z] (= x (positionscore y z))
         0    0 10
         0.17 1 10
         0.23 2 10
         0.14 3 10
         0.08 4 10
         0.05 5 10
         0.04 6 10
         0.06 7 10
         0.04 8 10
         0.04 9 10
         0.15 10 10)))

(deftest sbs-sub-test
  (testing "#'teaser-clj.scoring/sbs-sub"
    (are [x y z] (= x (sbs-sub y z))
         [0 0]  ["not" "in"]    {"keyword" 0, "map" 0}
         [5 10] ["these" "are"] {"these" 5, "are" 10}
         [0 7 0 0 5 17] ["this" "test" "is" "much" "more" "thorough"]
                        {"test" 7, "thorough" 17, "more" 5})))

(deftest sbs-test
  (testing "#'teaser-clj.scoring/sbs"
    (are [x y z] (== x (sbs y z))
         0     "not in"    {"keyword" 0, "map" 0}
         0.75  "these are" {"these" 5, "are" 10}
         29/60 "this test is much more thorough" {"test" 7, "thorough" 17, "more" 5})))

(deftest dbs-test
  (testing "#'teaser-clj.scoring/dbs"
    (are [x y z] (== x (dbs y z))
         0    "not in"           {"keyword" 0, "map" 0}
         25/6 "these are"        {"these" 5, "are" 10}
         30   "these are better" {"these" 5, "are" 10, "better" 55})))

(deftest final-score-test
  (testing "#'teaser-clj.scoring/final-score"
    (are [x t f l p] (== x (final-score t f l p))
         0     0 0 0 0
         0.375 1 0 0 0
         0.5   0 1 0 0
         0.25  0 0 1 0
         0.25  0 0 0 1)))
