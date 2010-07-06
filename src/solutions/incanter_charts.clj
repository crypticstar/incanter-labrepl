(ns
  ^{:author "Allie Terrell"
     :doc "Not meant to be run. Allows for labrepl users to copy and paste" 
            " examples without worrying about the line numbers online."}
  solutions.incanter-charts
  (:use incanter.core incanter.io incanter.charts incanter.pdf))

;; Popular Programming Language Paradigms
; 2
(def pl_data_dump (read-dataset "http://bit.ly/cR4sNd"  :header true :delim \tab))

; 3
(def paradigms_grouped (sel pl_data_dump :cols :language_paradigms
                                          :filter #(not (=  "" (first %)))))

; 4
(def paradigms (to-dataset (reduce concat
                             (map  #(vec  (.split % ",")) paradigms_grouped))))

; 5
(def ordered_count (->> paradigms ($rollup :count :count :col-0)
                     ($order :count :desc)
                     ($where {:col-0 {:$nin
                                      #{"Multi-paradigm programming language"}}})))

; 6
(def pl_chart (bar-chart :col-0 :count
                :data  (sel ordered_count :rows (range  5))
                :x-label "Paradigms"))
(view pl_chart)

; 7
(set-theme-bw pl_chart)
(view pl_chart)

;; State Population Box and Whisker Plots
; 1
(def pop_data_dump (read-dataset "http://bit.ly/cqSGMz"  :header true :delim \,))

; 2
(def pop_data (sel pop_data_dump :cols [:REGION :DIVISION :NAME
                                        :POPESTIMATE2000 :POPESTIMATE2001
                                        :POPESTIMATE2002 :POPESTIMATE2003
                                        :POPESTIMATE2004 :POPESTIMATE2005
                                        :POPESTIMATE2006 :POPESTIMATE2007
                                        :POPESTIMATE2008 :POPESTIMATE2009]))

; 3
(with-data (sel pop_data :except-rows  [0  1 2  3 4  56])
           (def pop_plot (box-plot :POPESTIMATE2009
                                   :group-by :REGION
                                   :title "United States Population by Region"
                                   :legend true))
           (view pop_plot))

; 4
(save-pdf pop_plot "./pop_plot.pdf")