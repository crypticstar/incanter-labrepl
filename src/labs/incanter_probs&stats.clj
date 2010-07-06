(ns ^{:skip-wiki true} labs.incanter-probs&stats
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "Incanter includes a " (c stats) " namespace which provides various probability functions, random number generation, and a host of statistics-related functions."
       " This lab will use a few of Incanter's statistics functions to draw conclusions about the " (c :hair-eye-color) " dataset seen earlier and to run trials of the Monty Hall problem."]])

(defn chi-square
  []
  [[:h3 "Chi-Square Test"]
   [:ol
    [:li "The " [:a {:href "http://en.wikipedia.org/wiki/Chi-square_test"} "chi-square test"] " will be used here for testing data independence."
         " In particular, we'll be using the " (c :hair-eye-color) " dataset from the " (c datasets) " library and testing for a correlation between hair color and eye color."
         " First, we need to load in the associated namespaces:"
         (code (use '[incanter core stats charts datasets]))
         "Then load in the correct dataset and group it by gender:"
         (code (def by-gender ($group-by 2 (get-dataset :hair-eye-color))))
         "Then partition out the male data from the female data:"
         (code (def male-data (by-gender {:gender "male"}))
(def female-data (by-gender {:gender "female"})))]
    [:li "We can now extract the hair color, eye color, and count for both males and females:"
         (code "(def m-count (sel male-data :cols :count))
(def m-eye (sel male-data :cols :eye))
(def m-hair (sel male-data :cols :hair))

(def f-count (sel female-data :cols :count))
(def f-eye (sel female-data :cols :eye))
(def f-hair (sel female-data :cols :hair))")
         "To view:"
         (code "(view (bar-chart m-hair m-count
                 :group-by m-eye
                 :legend true
                 :title \"Male Hair and Eye Color\"
                 :x-label \"Hair Color\"
                 :y-label \"Number of males\"))

(view (bar-chart f-hair f-count
                 :group-by f-eye
                 :legend true
                 :title \"Female Hair and Eye Color\"
                 :x-label \"Hair Color\"
                 :y-label \"Number of females\"))")]
    [:li "Since " (c chisq-test) " takes two contingency tables, we need to turn our vectors into matrices:"
         (code (def m-table (matrix m-count 4))
(def f-table (matrix f-count 4)))]
    [:li (c chisq-test) " can now be run on both matrices:"
         (code (def m-test (chisq-test :table m-table))
(def f-test (chisq-test :table f-table)))]
    [:li (c chisq-test) " returns many values."
         " The most important ones to look at are " (c :X-sq) " (the test statistics), " (c :p-value) ", and " (c :df) " (the degrees of freedom):"
         (code "(:X-sq m-test) ;; 106.66
(:p-value m-test) ;; 7.01E-19
(:df m-test) ;; 9

(:X-sq f-test) ;; 41.28
(:p-value f-test) ;; 4.45E-6
(:df f-test) ;; 9")
         "Having both " (c :p-value) " results be below 0.05 indicates that there is some sort of association between hair color and eye color."]]])

(defn conditional-probability
  []
  [[:h3 "Conditional Probability"]
   [:ol
    [:li "One of the most debated problems in probability is the " [:a {:href "http://en.wikipedia.org/wiki/Monty_Hall_problem"} "Monty Hall problem"] "."
         " Using a " [:a {:href "http://en.wikipedia.org/wiki/Monte_carlo_simulation"} "Monte Carlo simulation"] ", we can use Incanter samples to simulate how the problem would play out over many iterations."
         " Load the required namespaces:"
         (code (use '(incanter core stats charts)))]
    [:li "Next, we need to create the simulation."
         " Note that we sample from [1, 2, 3] to represent the 3 doors, and n represents the number of trials to run."
         (code (def n 10000)
(def initial-guesses (sample [1 2 3] :size n))
(def prize-doors (sample [1 2 3] :size n))
(def switches (sample [true false] :size n)))]
    [:li "We'll use a function to determine if we win when we switch:"
         (code "(defn switch-win? [initial-guess switch prize-door]
  (if (and switch (not= initial-guess prize-door)) 1 0))")
         "We can do the same for determining if we win when we stay:"
         (code "(defn stay-win? [initial-guess switch prize-door]
  (if (and (not switch) (= initial-guess prize-door)) 1 0))")]
    [:li "Using these functions, we can now calculate the joint probability if we switch:"
         (code (def prob-switch-win (/ (sum (map switch-win?
                              initial-guesses
                              switches
                              prize-doors))
                        n)))
         "And for if we stay:"
         (code (def prob-stay-win (/ (sum (map stay-win?
                            initial-guesses
                            switches
                            prize-doors))
                      n)))]
    [:li "To calculate the conditional probabilities, we need to calculate the number of switches and then divide the probability resulting from the previous step with this number:"
         (code "(def prob-switch (/ (sum (indicator true? switches)) n))
(def prob-win-given-switch (/ prob-switch-win prob-switch))

(def prob-stay (/ (sum (indicator false? switches)) n))
(def prob-win-given-stay (/ prob-stay-win prob-stay))")]
    [:li "To view the outcome:"
         (code (view (pie-chart ["Switch" "Stay"]
                 [prob-win-given-switch prob-win-given-stay])))
         " Here, we can see that switching doors wins two-thirds of the time, while staying with the initial door only wins one-third of the time."]]])

;(defn string-metrics
;  []
;  [[:h3 "String Metrics"]
;   [:ol
;    [:li "Incanter also provides functions for measuring string similarity."]]])

(defn instructions
  []
  (concat
   (overview)
   (chi-square)
   (conditional-probability)
   ;(string-metrics)
   ))