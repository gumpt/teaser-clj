(ns teaser-clj.stopwords
  (:require [clojure.edn :as edn]
            [clojure.java.io :refer [resource]]
            [clojure.string :refer [split]]))

(declare stopwords)

(defn split-sentence
  "Takes a string and returns a coll of the words."
  [s]
  (split s #"\s+"))

(defn filter-stopwords-wordmap
  [wordmap]
  (apply dissoc wordmap stopwords))

(defn filter-stopwords-seq
  [s]
  (remove stopwords s))

(defn filter-stopwords-string
  [string]
  (->> (split-sentence string)
       (remove stopwords)))

(def stopwords
  (edn/read-string (slurp (resource "stopwords.edn"))))
