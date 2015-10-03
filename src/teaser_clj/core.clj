(ns teaser-clj.core
  (:require
   [teaser-clj.html :refer [process-html]]
   [teaser-clj.parsing :as parsing]
   [teaser-clj.scoring :refer [score-sentences top-x normalize]]
   [teaser-clj.stopwords :refer [filter-stopwords-string
                                 filter-stopwords-wordmap
                                 filter-symbols]]
   [clojure.string :as string]))

(defn get-from-indices
  "Returns the values from a data structure corresponding to passed indices."
  [sentences indices]
  (for [i indices]
    (nth sentences i)))

(defn summarize
  [title sentences]
  (let [words        (filter-symbols (mapcat parsing/tokenize sentences))
        lowercase    (map string/lower-case sentences)
        startmap     (frequencies (map string/lower-case words))
        wordcount    (count startmap)
        keyword-map  (->> (filter-stopwords-wordmap startmap)
                          (top-x 10)
                          (normalize wordcount))]
    (->> (filter-stopwords-string title)
         (score-sentences lowercase keyword-map wordcount)
         (top-x 5)
         (keys)
         (get-from-indices sentences)
         (string/join "  "))))

(defn summarize-url
  "Returns a five-sentence (max) summary of the given url."
  [url]
  (let [{:keys [title text-blob]}  (process-html url)
        sentences (parsing/get-sentences text-blob)]
    (summarize title sentences)))

(defn summarize-text
  "Returns a five-sentence (max) summary of the given story and title."
  [title story]
  (let [sentences (parsing/get-sentences story)]
    (summarize title sentences)))
