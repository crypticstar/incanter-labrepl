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
    [:li "Click " [:a {:href "http://liebke.github.com/incanter/api-index.html"} "here"] " to find the documentation for Incanter."]
    [:li " Load the " (c datasets) " namespace and find the sample data sets that come with Incanter:"
         (code "(use 'incanter.datasets)
(find-doc \"sample data sets\")")
         "Many of these datasets come from R, and are provided for your use."]
    [:li "Look at this " [:a {:href "http://incanter.org/docs/incanter-cheat-sheet.pdf"} "cheat sheet"] " for a quick overview of Incanter."]]])

(defn preview
  []
  [[:h3 "Preview"]
   [:ol
    [:li "Run the following code to see a preview of what Incanter can do:"
         (code "(use '(incanter charts core datasets)) 
(def data (get-dataset :airline-passengers))
(view (line-chart (sel data :cols 2)
                  (sel data :cols 1)
                  :group-by (sel data :cols 0)
                  :title \"Airline Travel in 1949-1960\"
                  :legend true
                  :y-label \"Passengers\"
                  :x-label \"Month\"))")]]])

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
   (preview)
   (bonus)))
