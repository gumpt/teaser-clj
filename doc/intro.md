# Introduction to summarizer

`core.clj` contains the core function `summarize-url`.  This should be the
first stop for summarizing links.  The more general `summarize` function for
summarizing text with a passed title is in development.

`html.clj` contains useful functions for parsing the HTML calls.  The
`IllegalArgumentExceptions` occur when they are called without the proper
arguments; the exceptions are a note to the user that their data is not as
Enlive expects it to be more than a warning regarding the execution of the
function.

`scoring.clj` handles the scoring of sentences and words.  It is messy and
not incredibly Clojure-idiomatic, a result of its author learning through
development.

`stopwords.clj` handles basic filtering of the stopwords contained in
`resouces/stopwords.edn` out of multiple types of data structures.
