(ns ^{:skip-wiki true} labs.incanter-charts
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "In this lab, you will learn how to interact with Incanter's " (c charts) " namespace, which allows you to build a variety of charts based on the " [:a {:href "http://www.jfree.org/jfreechart/"} "JFreeChart library"] "."
       " We will be using datasets from the web."
       " The " (c charts) " namespace is expansive, and this tutorial is by no means exhaustive, so please check the API for additional features."]])

(defn bar_charts
  []
  [[:h3 "Popular Programming Language Paradigms"]
   [:ol
    [:li [:a {:href "http://www.freebase.com/"} "Freebase"] " is a large knowledge base which aggregates data from a multitude of sources."
          " Users can then edit the entries to add meaningful information, or correct information which Freebase may have extracted incorrectly."
          " Freebase provides " [:a {:href "http://download.freebase.com/datadumps/"} "data dumps"] " of its facts and assertations as of a specific date."
          " We'll be using the data regarding programming languages to illustrate how to build a bar chart."
          " Our goal is to display programming language paradigms by how frequently they're associated with a programming language."]
    [:li "We'll be using the " (c core) ", " (c charts) ", and " (c io) " libraries to load our data:"
         (code "(use '[incanter io core charts])
(def pl_data_dump (read-dataset \"http://bit.ly/cR4sNd\" :header true :delim \\tab))")
         "The " (c :header) " option tells " (c read-dataset) " that the first line is header information, and " (c :delim) " indicates what kind of delimiter is used in the data."]
    [:li "Using " (c "(col-names pl_data_dump)") ", we can see that there is a " (c :language_paradigms) " column, which is the one we want to be focusing on."
         " For those languages that have multiple paradigms, paradigms are comma separated in the column."
         " An example is Clojure, which is listed as \"Functional programming,Multi-paradigm programming language,Concurrent computing\"."
         " Because Freebase is imperfect in how it extracts information, many languages do not have a paradigm, so we want to make sure to remove these:"
         (code "(def paradigms_grouped (sel pl_data_dump :cols :language_paradigms
                                         :filter #(not (= \"\" (first %)))))")]
    [:li "We now have our paradigms, but they're still grouped for each language, like we saw with Clojure above."
         " We can use " (c map) " and " (c reduce) " to turn these into a dataset of individual paradigms:"
         (code "(def paradigms (to-dataset (reduce concat
                             (map #(vec (.split % \",\")) paradigms_grouped))))")]
    [:li "Our new dataset can take advantage of the " (c $rollup) " and " (c $order) " commands to count the number of occurrences of each paradigm in the list and order them in descending order."
         " Note that we remove the \"Multi-paradigm programming language\" paradigm, since it doesn't say much about the style of a language, only the number of paradigms it has:"
         (code "(def ordered_count (->> paradigms ($rollup :count :count :col-0)
                                  ($order :count :desc)
                                  ($where {:col-0 {:$nin
                                    #{\"Multi-paradigm programming language\"}}})))")]
    [:li "To view the five most-referenced paradigms in a bar chart:"
         (code "(def pl_chart (bar-chart :col-0 :count
                 :data (sel ordered_count :rows (range 5))
                 :x-label \"Paradigms\"))
(view pl_chart)")
         "The resulting chart indicates that OOP is the clear leader amongst paradigms in this dataset, followed by functional and imperative programming, each with about two-thirds as many languages as OOP."
         " Procedural and structued programming are fourth and fifth."
         " We can adjust " (c range) " in the above function to see the paradigms that ranked lower."]
    [:li "If desired, an option is provided to set the theme of plot to black and white using " (c set-theme-bw) ":"
         (code "(set-theme-bw pl_chart)
(view pl_chart)")]
    ;[:li "If we wanted to provide the specific values for each column, we can use " (c add-text) ":"
    ;     (code )]
    ]])

(defn b_and_w_plots
  []
  [[:h3 "State Population Box and Whisker Plots"]
   [:ol
    [:li "The " [:a {:href "http://www.census.gov/"} "U.S. Census Bureau"] " posts " [:a {:href "http://www.census.gov/popest/datasets.html"} "population datasets"] " online that are freely available for download."
         " The dataset we'll be using contains population information from 2000 through 2009, including population change, births, and deaths."
         " We'll be looking at the population data across regions of the US (Northeast, Midwest, South, and West)."
         " Let's go ahead and import the data:"
         (code "(def pop_data_dump (read-dataset \"http://bit.ly/cqSGMz\" :header true :delim \\,))")]
    [:li (c "(:col-names pop_data_dump)") " shows that there are many columns."
         " We're only interested in \"REGION\", \"DIVISION\", \"NAME\", and all the ones prefixed with \"POPESTIMATE\":"
         (code "(def pop_data (sel pop_data_dump :cols [:REGION :DIVISION :NAME
                                        :POPESTIMATE2000 :POPESTIMATE2001
                                        :POPESTIMATE2002 :POPESTIMATE2003
                                        :POPESTIMATE2004 :POPESTIMATE2005
                                        :POPESTIMATE2006 :POPESTIMATE2007
                                        :POPESTIMATE2008 :POPESTIMATE2009]))")]
    [:li "The \"REGION\" column indicates which of the four US regions the state is in and \"DIVISION\" further breaks down the regions."
         " Division values are unique overall and mapped to a specific region."
         " We'll use a box plot to display the population in each region:"
         (code "(with-data (sel pop_data :except-rows [0 1 2 3 4 56])
           (def pop_plot (box-plot :POPESTIMATE2009
                                   :group-by :REGION
                                   :title \"United States Population by Region\"
                                   :legend true))
           (view pop_plot))")
         "To see which number refers to which region, refer back to pop_data_dump - there are " (c :NAME) " values for the four regions which shows what region number they correspond to."
         " You can change the value for which population year to plot, or you can try grouping by division instead of region."]
    [:li "Finally, we can save the plot out using the " (c pdf) " library:"
         (code "(use 'incanter.pdf)
(save-pdf pop_plot \"./pop_plot.pdf\")")]
    ;[:li "Going back to the plot of the population by region, because of the numeric codes that indicate region, it isn't immediately obvious which region is which box."
    ;     " To remedy this, we can add "]
    ]])

;(defn ???
;  []
;  [[:h3 ""]
;   [:ol
;    [:li ""]
;    [:li ""]
;    [:li ""]]])

(defn bonus
  []
  [[:h3 "Bonus"]
   [:ol
    [:li "What other kinds of charts/graphs/plots can you find in the API?"]]])

(defn instructions
  []
  (concat
   (overview)
   (bar_charts)
   (b_and_w_plots)
   (bonus)))
