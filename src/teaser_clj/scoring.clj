(ns teaser-clj.scoring
  (:require [teaser-clj.stopwords :refer [get-words filter-stopwords-string]]
            [clojure.data.priority-map :refer [priority-map]]
            [clojure.math.numeric-tower :refer [expt]]
            [clojure.set :refer [intersection]]))

(defn common-elements [& colls]
  (let [freqs (map frequencies colls)]
    (mapcat (fn [e] (repeat (apply min (map #(% e) freqs)) e))
            (apply intersection (map (comp set keys) freqs)))))

(defn top-x
  [x m]
  (into {} (take x (rseq (into (priority-map) m)))))

(defn titlescore
  [s t]
  (let [words (filter-stopwords-string s)]
    (/ (count (common-elements words t)) (count t))))

(defn lengthscore
  [w]
  (- 1 (/ (Math/abs (- 20 (count w))) 20)))

(defn is-between?
  [x a b]
  (and (<= x b) (> x a)))

(defn get-keyword-score
  [keyword-map x]
  (if-let [score (get keyword-map x)]
    score
    0))

(defn positionscore
  [idx length]
  (let [s (/ (inc idx) length)]
    (cond
     (is-between? s 0 0.1) 0.17
     (is-between? s 0.1 0.2) 0.23
     (is-between? s 0.2 0.3) 0.14
     (is-between? s 0.3 0.4) 0.08
     (is-between? s 0.4 0.5) 0.05
     (is-between? s 0.5 0.6) 0.04
     (is-between? s 0.6 0.7) 0.06
     (is-between? s 0.7 0.8) 0.04
     (is-between? s 0.8 0.9) 0.04
     (is-between? s 0.9 1) 0.15)))

(defn sbs
  [s keyword-map]
  (/ (* (/ 1 (count s)) (count (common-elements s keyword-map))) 10))

(def f (atom []))
(def t (atom []))
(def r (atom 0))

(defn dbs
  [sentence keyword-map]
  (reset! r 0)
  (if-let [sa (not-empty (get-words sentence))]
    (do
      (doseq [s sa
              i (range (count sa))
              :let [score (get-keyword-score keyword-map s)]]
        (if (= 0 i)
          (reset! f (vector i score))
          (do
            (reset! t @f)
            (reset! f [i score])
            (let [d (- (first @t) (first @f))]
              (swap! r + (-> (second @t)
                             (* (second @f))
                             (/ (expt d 2))))
              ))))
      (let [k (+ 1 (count (common-elements (keys keyword-map) sa)))]
        (* (/ 1 (* k (+ k 1))) @r)))
    @r))

(defn final-score
  [ts fq ls ps]
  (/ (->> (* 1.5 ts)
          (+ (* fq 2))
          (+ ls)
          (+ ps))
     4))

(defn score
  [s idx keyword-map wordcount titlewords length]
  (let [w  (get-words s)
        ts (titlescore s titlewords)
        ls (lengthscore s)
        ps (positionscore idx length)
        sb (sbs s keyword-map)
        db (dbs s keyword-map)]
    (let [fq (-> (+ sb db)
                 (/ 2)
                 (* 10))
          fs (final-score ts fq ls ps)]
      [idx fs])))

(defn score-sentences
  [sentences keyword-map wordcount titlewords]
  (let [length (count sentences)]
    (into [] (map-indexed
              (fn [idx itm] (score itm idx keyword-map wordcount titlewords length))
              sentences))))
