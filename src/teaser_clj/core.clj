(ns teaser-clj.core
  (:require [teaser-clj.html :refer [process-html]]
            [teaser-clj.scoring :refer [score-sentences top-x]]
            [teaser-clj.stopwords :refer [filter-stopwords-wordmap filter-stopwords-string]]
            [clojure.string :refer [join lower-case]]))

(defn get-sentences
  [sentences indices]
  (for [i indices]
    (nth sentences i)))

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
