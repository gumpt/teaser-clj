(ns teaser-clj.html
  (:require [net.cgrand.enlive-html :as html]
            [clojure.java.io :refer [as-url]]
            [clojure.string :refer [lower-case split]]))

(defn title-from-html
  "Returns the title from enlive html."
  [html]
  (first (map lower-case (html/select html [:title html/text-node]))))

(defn fetch-url
  "Grabs a given URI and returns enlive html of the page."
  [url]
  (html/html-resource (as-url url)))

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
           (mapcat #(split % #"(?<=[.!?])\s+(?=\p{Lt})"))))
    (catch IllegalArgumentException e)))

(defn process-html
  "Returns a map with the title, words, and sentences
  from a given url."
  [url]
  (let [content (fetch-url url)]
    {:title (title-from-html content)
     :words (words-from-html content)
     :sentences (sentences-from-html content)}))
