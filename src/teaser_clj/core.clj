(ns teaser-clj.core
  (:require [teaser-clj.html :refer [process-html]]
            [teaser-clj.parsing :as parsing]
            [teaser-clj.scoring :refer [score-sentences top-x normalize]]
            [teaser-clj.stopwords :refer [filter-stopwords-wordmap filter-stopwords-string]]
            [clojure.string :as string]))

(defn get-from-indices
  "Returns the values from a data structure corresponding to passed indices."
  [sentences indices]
  (for [i indices]
    (nth sentences i)))

(defn get-words
  "Returns a coll of all words given in the coll."
  [coll]
  (->> coll
       (mapcat (partial re-seq #"\w+"))
       (remove (partial re-matches #"\d+"))
       (map string/lower-case)))

(defn get-sentences
  "Returns a coll of all sentences given in the coll."
  [text]
  (string/split (string/replace text #"([.?!;])\s{1}" "$1|||") #"\|\|\|"))

(defn summarize
  [title sentences]
  (prn sentences)
  (let [words        (get-words sentences)
        lowercase    (map string/lower-case sentences)
        startmap     (frequencies words)
        wordcount    (count startmap)
        keyword-map  (->> (filter-stopwords-wordmap startmap)
                          (top-x 10)
                          (normalize wordcount))]
    (prn keyword-map)
    (->> (filter-stopwords-string title)
         (score-sentences lowercase keyword-map wordcount)
         (top-x 5)
         (keys)
         (get-from-indices sentences)
         (string/join "  "))))

(defn summarize-url
  "Returns a five-sentence (max) summary of the given url."
  [url]
  (let [{:keys [title sentences]}  (process-html url)]
    (summarize title sentences)))

(defn summarize-text
  "Returns a five-sentence (max) summary of the given story and title."
  [title story]
  (let [sentences (parsing/get-sentences story)]
    (summarize title sentences)))
