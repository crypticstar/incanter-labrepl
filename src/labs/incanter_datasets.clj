(ns ^{:skip-wiki true} labs.incanter-datasets
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "In this lab, you will learn to create, manipulate and save datasets."
       " In particular, we'll work with a set of data which categorizes people by hair color, eye color, and gender."
       " With it, we'll see which hair color/eye color combinations are the most prevalent, regardless of gender."]])

(defn creating
  []
  [[:h3 "Creating Datasets"]
   [:ol
    [:li "The dataset functionality we'll initially be working with is contained in the " (c core) " namespace."
     (code (use 'incanter.core))]
    [:li "A dataset is defined as a mapping between a set of column names and values.
          These can be defined as two sets of sequences:"
		     (code "(dataset [\"a\" \"b\" \"c\"]
         [[1 2 3]
          [4 5 6]
          [7 8 9]])")
     "This will return a map of type " (c dataset) "."]
    [:li "The " (c to-dataset) " function will convert the input to type " (c dataset) "."
         " Here, " (c to-dataset) " takes a sequence of hash maps, where the keys in each hash map correspond to a column:"
          (code "(to-dataset [{:a 1 :b 2 :c 3}
             {:a 4 :b 5 :c 6}
             {:a 7 :b 8 :c 9}])")]
    [:li "Datasets can also be made by conjugating together sequences that represent columns or rows:"
          (code "(conj-cols [1 4 7]
           [2 5 8]
           [3 6 9])")
          (code "(conj-rows [1 2 3]
           [4 5 6]
           [7 8 9])")]
    [:li "Datasets external to Incanter may also be loaded in."
         " As we saw in the previous labrepl, the statistical computing program R provides several sample datasets with it."
         " Incanter provides a number of these datasets in the " (c datasets) " namespace:"
         (code (use 'incanter.datasets)(def data (get-dataset :hair-eye-color)))]
    [:li "You can view a dataset at any time in an easy-to-read GUI format by using the " (c view) " function:"
         (code (view data))]]])

(defn rolling_up_sorting
  []
  [[:h3 "Rolling Up & Sorting"]
   [:ol
    [:li "The " (c $rollup) " function allows for data to be summarized according to some function over some columns."
         " The following will sum together tuples based on hair and eye colors:"
         (code (def data-summary ($rollup :sum :count [:hair :eye] data)))]
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
     [:li "Datasets - like other Clojure data structures - are seq-able and can use seq-able functions."
          (code (first data-summary))]
     [:li "Datasets also have functions that are unique to their type, such as " (c sel) " to make it easier to access a specific element."
          " We can use these on any dataset we have, like the one stored in " (c data-summary) " from the previous section, specifying the " (c :rows) " and " (c :cols) ":"
          (code (sel data-summary :rows 0))]
     [:li "To find tuples in the dataset that meet a certain criteria, use " (c $where) ":"
          (code ($where {:count {:$gt 20}} data-summary))
          " This will find all the rows where the value in the " (c :count) " column is greater than 20."]]])

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
    [:li "Look at the API online. What other types of input can " (c to-dataset) " take?"]
    [:li "When creating a dataset with " (c to-dataset) ", what is the behavior when a hash map is passed in, but the key values in the two maps aren't in the same order?"
         " What about when there aren't the same number of keys in each hash map?"
         " What about when there are different keys in each hash map?"]
    [:li "What delimiters can be specified for the function " (c read-dataset) "?"]
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
