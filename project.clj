(defproject teaser-clj "0.4.0"
  :description "Summarize urls."
  :url "https://clojars.org/teaser-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["resources"]
  :dependencies [[enlive "1.1.5"]
                 [clojure-opennlp "0.3.3"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [org.clojure/math.numeric-tower "0.0.4"]])
