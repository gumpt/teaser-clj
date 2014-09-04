(ns teaser-clj.core
  (:require [teaser-clj.html :refer :all]
            [teaser-clj.scoring :refer [score-sentences]]
            [teaser-clj.stopwords :refer [filter-stopwords-wordmap filter-stopwords-string]]
            [clojure.data.priority-map :refer [priority-map]]
            [clojure.math.numeric-tower :refer [expt]]
            [clojure.java.io :refer [as-url]]
            [clojure.set :refer [union intersection]]
            [clojure.string :refer [join split lower-case]]))

(defn get-words
  "Takes a string and returns a coll of the words."
  [s]
  (split s #"\s+"))

(defn get-sentences
  [sentences indices]
  (for [i indices]
    (nth sentences i)))

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
        lowercase                        (map lower-case sentences)
        startmap                         (frequencies words)
        wordcount                        (count startmap)
        wordmap                          (filter-stopwords-wordmap startmap)
        keyword-map                      (top-x 10 wordmap)]
    (->> (filter-stopwords-string title)
         (score-sentences lowercase keyword-map wordcount)
         (top-x 5)
         (keys)
         (get-sentences sentences)
         (join "  "))))
