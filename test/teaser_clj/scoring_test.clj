(ns teaser-clj.scoring-test
  (:require [clojure.test :refer :all]
            [teaser-clj.scoring :refer :all]))

(deftest common-elements-test
  (testing "#'teaser-clj.html/common-elements"
    (are [x y z] (= x (common-elements y z))
     ["this" "and"] ["this", "and", "not", "that"] ["this", "and"])))

(deftest top-x-test
  (testing "#'teaser-clj.html/top-x"
    (are [x y z] (= x (top-x y z))
         {\space 4, \t 4, \s 3} 3 (frequencies "this isn't a good test"))))

(deftest titlescore-test
  (testing "#'teaser-clj.html/titlescore"
    (are [x y z] (= x (titlescore y z))
         0.25 "this is a title" "title")))
