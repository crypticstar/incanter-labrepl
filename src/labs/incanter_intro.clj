(ns ^{:skip-wiki true} labs.incanter-intro
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "In this lab you will learn about the documentation, namespaces and datasets provided with Incanter."]
   [:p "You need to have both Clojure and Incanter installed to perform the steps in this lab.
        For information on how to install these, please look " [:a {:href "http://wiki.github.com/liebke/incanter/#getstarted"} "here"] "."]])

(defn namespaces
  []
  [[:h3 "Namespaces"]
   [:ol
    [:li "Incanter namespaces are prefixed by " (c incanter) ":"
     (code (require 'incanter.core))]
    [:li "There are several namespaces available for Incater. Below are a few examples:"
     (code bayes)
     (code charts)
     (code core)
     (code stats)
     "A full list (including descriptions) can be found " [:a {:href "http://liebke.github.com/incanter/"} "here"] "."]]])

(defn documentation
  []
  [[:h3 "Documentation"]
   [:ol
    [:li "The API for all documented Incanter functions and variables can be found " [:a {:href "http://liebke.github.com/incanter/api-index.html"} "here"] "."]
    [:li "Like in the Clojure REPL, all of the Clojure documentation features are available for Incanter."
         " Load the " (c datasets) " namespace and try to find the sample data sets that come with Incanter:"
         (showme "(use 'incanter.datasets)
(find-doc \"sample data sets\")")
         "Many of these datasets come from R, and are provided for your use."]
    [:li "A " [:a {:href "http://incanter.org/docs/incanter-cheat-sheet.pdf"} "cheat sheet"] " is available which provides a quick overview of Incanter."]]])

(defn bonus
  []
  [[:h3 "Bonus"]
   [:ol
    [:li "What other namespaces are available in Incanter?"]]])

(defn instructions
  []
  (concat
   (overview)
   (namespaces)
   (documentation)
   (bonus)))
