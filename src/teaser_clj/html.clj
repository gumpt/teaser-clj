(ns teaser-clj.html
  (:require
   [boilerpipe-clj.core :as boilerpipe]
   [clojure.java.io :refer [as-url]]
   [clojure.string :refer [lower-case]]
   [net.cgrand.enlive-html :as e]))

(defn title-from-html
  "Returns the title from enlive html."
  [html]
  (let [page (-> html
                 java.io.StringReader.
                 e/html-resource)]
    (first (map lower-case (e/select page [:title e/text-node])))))

(defn process-html
  "Returns a map with the title, words, and sentences
  from a given url."
  [url]
  (let [page  (slurp url)
        title (title-from-html page)]
    {:title title
     :sentences (boilerpipe/get-text
                 page
                 boilerpipe-clj.extractors/article-sentence-extractor)}))
