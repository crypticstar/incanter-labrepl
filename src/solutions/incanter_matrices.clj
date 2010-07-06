(ns
  ^{:author "Allie Terrell"
   :doc "Not meant to be run. Allows for labrepl users to copy and paste" 
          " examples without worrying about the line numbers online."}
  solutions.incanter-matrices)

;; Creating Matrices
; 1
(def A (matrix [[1 2 3] [4 5 6] [7 8 9]]))

(def A2 (matrix [1 2 3 4 5 6 7 8 9] 3))

(matrix [1 2 3 4 5 6 7 8 9])

; 2
(matrix 0 3  4)

; 3
(identity-matrix 4)

; 4
(diag [1 2 3 4 5])

(diag A)

; 5
(symmetric-matrix 
  [1
   2 3
   4 5 6
   7 8 9 10])

;; Trace
; 1
(diag A)

; 2
(defn  tr [m] (apply  + (diag m)))

;; Determinant
; 1
(defn getMinor [m r c]
  (sel m :except-rows r :except-cols c))

; 2
(defn getCofactors [m]
  (map #(cons (* (Math/pow -1 %) (sel m 0 %)) (getMinor m 0 %))
        (range  (sqrt (length m)))))

; 3
(defn determinant [m]
  (if ((= 2 (first (dim m)) (second (dim m)))
    (- (* (sel m 0 0) (sel m 1  1)) (* (sel m 0  1) (sel m 1 0))))))

; 4
(defn determinant [m]
  (if (= 2 (first (dim m)) (second (dim m)))
    (- (* (sel m 0 0) (sel m 1 1)) (* (sel m 0 1) (sel m 1 0)))
    (map #(* (first %) (trampoline determinant (rest %))) (getCofactors m))))

; 5
(defn determinant [m]
  (if (= 2 (first (dim m)) (second (dim m)))
    (- (* (sel m 0 0) (sel m 1 1)) (* (sel m 0 1) (sel m 1 0)))
    (apply + (map #(* (first %) (trampoline determinant (rest %)))
                   (getCofactors m)))))

;; Linear Independence
; 1
(defn independent? [& vectors])

; 2
(defn independent? [& vectors]
  (to-matrix (apply conj-cols vectors)))

; 3
(defn independent? [& vectors]
  (det (to-matrix (apply conj-cols vectors))))

; 4
(defn independent? [& vectors]
  (if (zero? (det (to-matrix (apply conj-cols vectors))))
    false
    true))