(ns
  ^{:author "David Liebke"
   :doc "Not meant to be run. Allows for labrepl users to copy and paste" 
          " examples without worrying about the line numbers online."}
  solutions.incanter-probsstats
  (:use incanter.core incanter.charts incanter.stats incanter.datasets))

;; Chi-Square Test
; 1
(def  by-gender ($group-by 2 (get-dataset :hair-eye-color)))

(def  male-data (by-gender {:gender  "male"}))
(def  female-data (by-gender {:gender  "female"}))

; 2
(def m-count (sel male-data :cols :count))
(def m-eye (sel male-data :cols :eye))
(def m-hair (sel male-data :cols :hair))

(def f-count (sel female-data :cols :count))
(def f-eye (sel female-data :cols :eye))
(def f-hair (sel female-data :cols :hair))

(view (bar-chart m-hair m-count
                 :group-by m-eye
                 :legend true
                 :title "Male Hair and Eye Color"
                 :x-label "Hair Color"
                 :y-label "Number of males"))

(view (bar-chart f-hair f-count
                 :group-by f-eye
                 :legend true
                 :title "Female Hair and Eye Color"
                 :x-label "Hair Color"
                 :y-label "Number of females"))

; 3
(def m-table (matrix m-count 4))
(def f-table (matrix f-count 4))

; 4
(def m-test (chisq-test :table  m-table))
(def f-test (chisq-test :table  f-table))

; 5
(:X-sq m-test) ;; 106.66
(:p-value m-test) ;; 7.01E-19
(:df m-test) ;; 9

(:X-sq f-test) ;; 41.28
(:p-value f-test) ;; 4.45E-6
(:df f-test) ;; 9

;; Conditional Probability
; 2
(def n 10000)
(def initial-guesses (sample [1 2 3] :size n))
(def prize-doors (sample [1 2 3] :size n))
(def switches (sample [true false] :size n))

; 3
(defn switch-win? [initial-guess switch prize-door]
  (if (and switch (not= initial-guess prize-door)) 1 0))

(defn stay-win? [initial-guess switch prize-door]
  (if (and (not switch) (= initial-guess prize-door)) 1 0))

; 4
(def prob-switch-win (/ (sum (map switch-win? initial-guesses switches prize-doors)) n))

(def prob-stay-win (/ (sum (map  stay-win? initial-guesses switches prize-doors)) n))

; 5
(def prob-switch (/ (sum (indicator true? switches)) n))
(def prob-win-given-switch (/ prob-switch-win prob-switch))

(def prob-stay (/ (sum (indicator false? switches)) n))
(def prob-win-given-stay (/ prob-stay-win prob-stay))

; 6
(view (pie-chart ["Switch" "Stay"] [prob-win-given-switch prob-win-given-stay]))