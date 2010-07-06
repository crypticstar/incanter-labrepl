(ns
  ^{:author "Allie Terrell"
       :doc "Not meant to be run. Allows for labrepl users to copy and paste 
              examples without worrying about the line numbers online."}
  solutions.incanter-datasets
  (:use incanter.core incanter.datasets incanter.charts))

;; Creating Datasets
; 2
(dataset ["a" "b" "c"]
         [[1 2 3]
          [4 5 6]
          [7  8 9]])

; 3
(to-dataset [{:a 1 :b 2 :c 3}
             {:a 4 :b 5 :c 6}
             {:a 7 :b 8 :c 9}])

; 4
(conj-cols [1 4 7]
           [2 5 8]
           [3 6 9])
(conj-rows [1 2 3]
           [4 5 6]
           [7 8 9])

;5
(def data (get-dataset :hair-eye-color))

; 6
(view data)

;; Rolling Up & Sorting
; 1
(def data-summary ($rollup :sum :count [:hair :eye] data))

; 2
($order :count :desc data-summary)

;; Selection
; 1
(first data-summary)

; 2
(sel data-summary :rows 0)

; 3
($where {:count {:$gt 20}} data-summary)

;; Dynamic Datasets
; 1
(let [data data-summary
      table (data-table data)]
  (view table)
  (sliders [hair ["black" "red"  "brown" "blond"]]
    (set-data table ($order :count :desc ($where {:hair hair} data)))))