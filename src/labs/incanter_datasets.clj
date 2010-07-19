(ns ^{:skip-wiki true} labs.incanter-datasets
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "In this lab, you will learn to create and manipulate datasets."
       " In particular, we'll work with a set of data which categorizes people by hair color, eye color, and gender."
       " With it, we'll see which hair color/eye color combinations are the most prevalent, regardless of gender."]])

(defn creating
  []
  [[:h3 "Creating Datasets"]
   [:ol
    [:li "Import the " (c core) " namespace for dataset functionality."
     (code (use 'incanter.core))]
    [:li "Make a dataset using two sets of sequences:"
		     (code "(dataset [\"a\" \"b\" \"c\"]
         [[1 2 3]
          [4 5 6]
          [7 8 9]])")
     "A dataset is defined as a mapping between a set of column names and values."
     " This will return a map of type " (c dataset) "."]
    [:li "Now, use " (c to-dataset) " by giving it a sequence of hash maps, where the keys in each hash map correspond to a column:"
          (code "(to-dataset [{:a 1 :b 2 :c 3}
             {:a 4 :b 5 :c 6}
             {:a 7 :b 8 :c 9}])")
          "What makes " (c to-dataset) " different from " (c dataset) " is that " (c dataset) " takes 2 arguments: one where the column names are specified, followed by a list of rows."
          " " (c to-dataset) " arguments are already other types of sequences (such as maps) that need to be converted into a dataset of column names and rows."
          " How this conversion happens depends on the type of sequence."
          " For example, in maps, the keys become column names, and the values become part of a row under that column."
          " Try out some of the following to see the differences:"
          (code "(dataset (range 10))
(to-dataset (range 10))

(dataset [[1 2] [3 4] [5 6]])
(to-dataset [[1 2] [3 4] [5 6]])

(dataset [{:a 1 :b 2} {:a 1 :b 2}])
(to-dataset [{:a 1 :b 2} {:a 1 :b 2}])")]
    [:li "Conjugate together sequences that represent columns or rows to form new datasets:"
          (code "(conj-cols [1 4 7]
           [2 5 8]
           [3 6 9])")
          (code "(conj-rows [1 2 3]
           [4 5 6]
           [7 8 9])")]
    [:li "Load the following dataset:"
         (code (use 'incanter.datasets)(def data (get-dataset :hair-eye-color)))
         "As we saw in the previous labrepl, the statistical computing program R provides several sample datasets with it."
         " Incanter provides a number of these datasets (such as the one we just loaded) in the " (c datasets) " namespace."]
    [:li "Use " (c view) " to read the dataset in a GUI table:"
         (code (view data))]]])

(defn rolling_up_sorting
  []
  [[:h3 "Rolling Up & Sorting"]
   [:ol
    [:li "The following will show how many samples (of either gender) have a specific hair/eye color combination:"
         (code (def data-summary ($rollup :sum :count [:hair :eye] data)))
         "The " (c $rollup) " function allows for data to be summarized according to some function over some columns."
         " We can use " (c (doc $rollup)) " to find other ways to summarize data."
         " The " (c $) " syntax comes from R's operator for accessing columns from a data frame and is used for functions that operate on datasets and can be used with the " (c with-data) " macro."]
    [:li "To sort the resulting data by " (c :count) ", we can use the " (c $order) " function:"
         (code ($order :count :desc data-summary))]]])

;(defn sorting
;  []
;  [[:h3 "Sorting"]
;   [:ol
;    [:li "Incater has the ability to sort datasets with the function " (c $order) ":"
;         (code ($order :count :asc data)
;(with-data data ($order :count :asc)))
;         (c :asc) " sorts in ascending order, and " (c :desc) " sorts in descening order."]
;    [:li "Sorting may be done over one column or a sequence of columns:"
;         (code ($order [:hair :eye] :asc data))
;         "Note how the eye colors are sorted within the hair colors."]]])

(defn selection
  []
  [[:h3 "Selection"]
    [:ol
     [:li "You can access datasets as maps:"
          (code (:rows data-summary))
          "The datasets can be accessed using all the Clojure functions you would usually use with maps."]
     [:li "The following will access the first row of " (c data-summary) ":"
          (code (sel data-summary :rows 0))
          "Datasets also have functions that are unique to their type (such as " (c sel) ") to make it easier to access a specific element."]
     [:li "To find tuples in the dataset that meet a certain criteria, use " (c $where) ":"
          (code ($where {:count {:$gt 20}} data-summary))
          " This will find all the rows where the value in the " (c :count) " column is greater than 20."
          " Because " (c $where) " is an alias to " (c query-dataset) ", you can find information on specific options by looking at the " (c query-dataset) " doc."]
     [:li "How would you select the hair and eye color combinations in " (c data-summary) " which are equal to 14?"
          (showme "(sel ($where {:count {:$eq 14}} data-summary) :cols [:hair :eye])")]]])

(defn dynamic_datasets
  []
  [[:h3 "Dynamic Datasets"]
   [:ol
    [:li "Datasets can be dynamically generated and displayed from a base dataset by loading the " (c charts) " namespace:"
         (code (use 'incanter.charts) (let [data data-summary
table (data-table data)]
(view table)
(sliders [hair ["black" "red" "brown" "blond"]]
(set-data table ($order :count :desc ($where {:hair hair} data))))))
         "Note that sliders are available for both numeric and non-numeric values."]
    [:li "Scrolling the data provides some interesting insights."
         " For example, this dataset shows that the prevalance of a particular eye color is ordered the same for everyone except blondes, where it is much different."]]])

(defn bonus
  []
  [[:h3 "Bonus"]
   [:ol
    [:li "When creating a dataset with " (c to-dataset) ", what is the behavior when a hash map is passed in, but the key values in the two maps aren't in the same order?"
         " What about when there aren't the same number of keys in each hash map?"
         " What about when there are different keys in each hash map?"]
    [:li "How would you find all the values of " (c :count) " in " (c data) " which are even using " (c $where) "?"]]])

(defn instructions
  []
  (concat
   (overview)
   (creating)
   (rolling_up_sorting)
   (selection)
   (dynamic_datasets)
   (bonus)))
