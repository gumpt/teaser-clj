(ns teaser-clj.scoring
  (:require
   [teaser-clj.stopwords :refer [split-sentence filter-stopwords-string]]
   [clojure.data.priority-map :refer [priority-map]]
   [clojure.math.numeric-tower :refer [expt abs]]
   [clojure.set :refer [intersection]]
   [clojure.string :refer [blank?]]))

(defn common-elements [& colls]
  (let [freqs (map frequencies colls)]
    (mapcat (fn [e] (repeat (apply min (map #(% e) freqs)) e))
            (apply intersection (map (comp set keys) freqs)))))

(defn top-x
  [x m]
  (into {} (take x (rseq (into (priority-map) m)))))

(defn normalizer
  [c x]
  (->> (/ x c)
       (* 1.5)
       (+ 1)))

(defn normalize
  [c keywords]
  (->> (map #(normalizer c %) (vals keywords))
       (zipmap (keys keywords))))

(defn titlescore
  [s t]
  (let [words (filter-stopwords-string s)]
    (/ (count (common-elements words t)) (count t))))

(defn lengthscore
  [w]
  (if (blank? w)
    0
    (- 1 (/ (abs (- 20 (count (split-sentence w)))) 20))))

(defn get-keyword-score
  [keyword-map x]
  (if-let [score (get keyword-map x)]
    score
    0))

(defn positionscore
  [idx length]
  (let [s (/ idx length)]
    (cond
     (> s 1.0) 0
     (> s 0.9) 0.15
     (> s 0.8) 0.04
     (> s 0.7) 0.04
     (> s 0.6) 0.06
     (> s 0.5) 0.04
     (> s 0.4) 0.05
     (> s 0.3) 0.08
     (> s 0.2) 0.14
     (> s 0.1) 0.23
     (> s 0.0) 0.17
     :else 0)))

(defn sbs-sub
  [words keyword-map]
  (for  [w words]
    (get-keyword-score keyword-map w)))

(defn sbs
  [s keyword-map]
  (if (zero? (count (split-sentence s)))
    0
    (let [subscore (apply + (sbs-sub (split-sentence s) keyword-map))]
      (/ (* (/ 1 (count (split-sentence s))) subscore) 10))))

(defn dbs
  [sentence keyword-map]
  (let [f (atom [])
        t (atom [])
        r (atom 0)]
    (if-let [sa (not-empty (split-sentence sentence))]
      (do
        (doseq [i (range (count sa))
                :let [s (nth sa i)
                      score (get-keyword-score keyword-map s)]]
          (if (zero? i)
            (reset! f (vector i score))
            (do
              (reset! t @f)
              (reset! f [i score])
              (let [d (- (first @t) (first @f))]
                (swap! r + (-> (second @t)
                               (* (second @f))
                               (/ (expt d 2))))))))
        (let [k (inc (count (common-elements (keys keyword-map) sa)))]
          (* (/ 1 (* k (inc k))) @r)))
      @r)))

(defn final-score
  [ts fq ls ps]
  (/ (->> (* 1.5 ts)
          (+ (* fq 2))
          (+ ls)
          (+ ps))
     4))

(defn score
  [s idx keyword-map wordcount titlewords length]
  (let [w  (split-sentence s)
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
    (vec
     (map-indexed
      (fn [idx itm]
        (score itm idx keyword-map wordcount titlewords length))
      sentences))))
