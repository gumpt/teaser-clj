(ns teaser-clj.parsing
  (:require [opennlp.nlp :refer [make-sentence-detector]]
            [clojure.java.io :refer [resource]]))

(def get-sentences
  (make-sentence-detector (resource "models/en-sent.bin")))