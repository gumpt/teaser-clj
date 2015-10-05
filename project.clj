(defproject teaser-clj "0.5.4"
  :description "Summarize urls."
  :url "https://clojars.org/teaser-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["resources"]
  :dependencies [[clojure-opennlp "0.3.3"]
                 [enlive "1.1.6"]
                 [io.curtis/boilerpipe-clj "0.3.0"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [org.clojure/math.numeric-tower "0.0.4"]])
