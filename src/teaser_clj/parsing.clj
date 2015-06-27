(ns teaser-clj.parsing
  (:require
   [opennlp.nlp :refer [make-sentence-detector make-tokenizer]]
   [clojure.java.io :refer [resource]]))

(def get-sentences
  (make-sentence-detector (resource "models/en-sent.bin")))

(def tokenize
  (make-tokenizer (resource "models/en-token.bin")))
