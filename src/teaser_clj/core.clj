(ns teaser-clj.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.priority-map :refer [priority-map]]
            [clojure.math.numeric-tower :refer [expt]]
            [clojure.java.io :refer [as-url]]
            [clojure.set :refer [union intersection]]
            [clojure.string :refer [join split lower-case]]))

; HTML processing
(defn title-from-html
  "Returns the title from enlive html."
  [html]
  (try
    (first (map lower-case (html/select html [:title html/text-node])))
    (catch IllegalArgumentException e)))

(defn fetch-url
  "Grabs a given URI and returns enlive html of the page."
  [url]
  (html/html-resource (as-url url)))

(defn get-words
  "Takes a string and returns a coll of the words."
  [s]
  (split s #"\s+"))

(defn words-from-html
  "Returns the words from enlive html."
  [html]
  (try
    (let [chunks (-> html
                     (html/at [:script] nil)
                     (html/at [:style] nil)
                     (html/select [:body html/text-node]))]
      (->> chunks
           (mapcat (partial re-seq #"\w+"))
           (remove (partial re-matches #"\d+"))
           (map lower-case)))
    (catch IllegalArgumentException e)))

(defn sentences-from-html
  "Returns the sentences from enlive html."
  [html]
  (try
    (let [chunks (-> html
                     (html/at [:script] nil)
                     (html/at [:style] nil)
                     (html/select [:body html/text-node]))]
      (->> chunks
           (map lower-case)
           (mapcat #(split % #"(?<=[.!?])\s+(?=\p{Lt})"))))
    (catch IllegalArgumentException e)))

; Stopwords
(declare stopwords)

(defn filter-stopwords-wordmap
  [wordmap]
  (apply dissoc wordmap stopwords))

(defn filter-stopwords-seq
  [s]
  (remove stopwords s))

(defn filter-stopwords-string
  [string]
  (->> (get-words string)
       (remove stopwords)))

; Scoring/summarizing
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
  ; (prn "LENGTH - HERE")
  (/ (- 1 (Math/abs (- 20 (count w)))) 20))

(defn is-between
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
     (is-between s 0 0.1) 0.17
     (is-between s 0.1 0.2) 0.23
     (is-between s 0.2 0.3) 0.14
     (is-between s 0.3 0.4) 0.08
     (is-between s 0.4 0.5) 0.05
     (is-between s 0.5 0.6) 0.04
     (is-between s 0.6 0.7) 0.06
     (is-between s 0.7 0.8) 0.04
     (is-between s 0.8 0.9) 0.04
     (is-between s 0.9 1) 0.15)))

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
      [s fs])))

(defn score-sentences
  [sentences keyword-map wordcount titlewords]
  (let [length (count sentences)]
    (into [] (map-indexed
              (fn [idx itm] (score itm idx keyword-map wordcount titlewords length))
              sentences))))

(defn process-html
  "Returns a map with the title, words, and sentences
  from a given url."
  [url]
  (let [content (fetch-url url)]
    {:title (title-from-html content)
     :words (words-from-html content)
     :sentences (sentences-from-html content)}))

(defn summarize-url
  "Returns a five-sentence (max) summary of the given url."
  [url]
  (let [{:keys [title words sentences]}  (process-html url)
        startmap                         (frequencies words)
        wordcount                        (count startmap)
        wordmap                          (filter-stopwords-wordmap startmap)
        keyword-map                      (top-x 10 wordmap)]
    (->> (filter-stopwords-string title)
         (score-sentences sentences keyword-map wordcount)
         (top-x 5)
         (keys)
         (join \newline))))

(def stopwords
  #{"-" " " "|" "," "." "a" "e" "i" "o" "u" "t" "about" "above"
    "across" "after" "afterwards" "again" "against" "all"
    "almost" "alone" "along" "already" "also" "although" "always"
    "am" "among" "amongst" "amoungst" "amount" "an" "and"
    "another" "any" "anyhow" "anyone" "anything" "anyway"
    "anywhere" "are" "around" "as" "at" "back" "be" "became"
    "because" "become" "becomes" "becoming" "been" "before"
    "beforehand" "behind" "being" "below" "beside" "besides"
    "between" "beyond" "both" "bottom" "but" "by" "call" "can"
    "cannot" "can't" "co" "con" "could" "couldn't" "de"
    "describe" "detail" "did" "do" "done" "down" "due" "during"
    "each" "eg" "eight" "either" "eleven" "else" "elsewhere"
    "empty" "enough" "etc" "even" "ever" "every" "everyone"
    "everything" "everywhere" "except" "few" "fifteen" "fifty"
    "fill" "find" "fire" "first" "five" "for" "former"
    "formerly" "forty" "found" "four" "from" "front" "full"
    "further" "get" "give" "go" "got" "had" "has" "hasnt"
    "have" "he" "hence" "her" "here" "hereafter" "hereby"
    "herein" "hereupon" "hers" "herself" "him" "himself" "his"
    "how" "however" "hundred" "ie" "if" "in" "inc" "indeed"
    "into" "is" "it" "its" "it's" "itself" "just" "keep" "last"
    "latter" "latterly" "least" "less" "like" "ltd" "made" "make"
    "many" "may" "me" "meanwhile" "might" "mill" "mine" "more"
    "moreover" "most" "mostly" "move" "much" "must" "my" "myself"
    "name" "namely" "neither" "never" "nevertheless" "new" "next"
    "nine" "no" "nobody" "none" "noone" "nor" "not" "nothing"
    "now" "nowhere" "of" "off" "often" "on" "once" "one" "only"
    "onto" "or" "other" "others" "otherwise" "our" "ours"
    "ourselves" "out" "over" "own" "part" "people" "per"
    "perhaps" "please" "put" "rather" "re" "said" "same" "see"
    "seem" "seemed" "seeming" "seems" "several" "she" "should"
    "show" "side" "since" "sincere" "six" "sixty" "so" "some"
    "somehow" "someone" "something" "sometime" "sometimes"
    "somewhere" "still" "such" "take" "ten" "than" "that" "the"
    "their" "them" "themselves" "then" "thence" "there"
    "thereafter" "thereby" "therefore" "therein" "thereupon"
    "these" "they" "thickv" "thin" "third" "this" "those"
    "though" "three" "through" "throughout" "thru" "thus" "to"
    "together" "too" "top" "toward" "towards" "twelve" "twenty"
    "two" "un" "under" "until" "up" "upon" "us" "use" "very"
    "via" "want" "was" "we" "well" "were" "what" "whatever"
    "when" "whence" "whenever" "where" "whereafter" "whereas"
    "whereby" "wherein" "whereupon" "wherever" "whether" "which"
    "while" "whither" "who" "whoever" "whole" "whom" "whose"
    "why" "will" "with" "within" "without" "would" "yet" "you"
    "your" "yours" "yourself" "yourselves" "reuters" "news"
    "monday" "tuesday" "wednesday" "thursday" "friday" "saturday"
    "sunday" "mon" "tue" "wed" "thu" "fri" "sat" "sun"
    "rappler" "rapplercom" "inquirer" "yahoo" "home"
    "1" "10" "2012" "sa" "says" "tweet" "pm" "homepage"
    "sports" "section" "newsinfo" "stories" "story" "photo"
    "2013" "na" "ng" "ang" "year" "years" "percent" "ko" "ako"
    "yung" "yun" "2" "3" "4" "5" "6" "7" "8" "9" "0" "time"
    "january" "february" "march" "april" "june" "july"
    "august" "september" "october" "november" "december"})
