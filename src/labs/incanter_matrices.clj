(ns ^{:skip-wiki true} labs.incanter-matrices
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "In this lab, we will learn how to create and manipulate matrices."
       " In particular, we will look at how to write three common matrix functions: trace, the determinant, and linear indepencence."]])

(defn creating
  []
  [[:h3 "Creating Matrices"]
   [:ol
    [:li "Matrices are created using the " (c matrix) " function, which requires the " (c core) " namespace."
         " They are composed of a sequence of rows, where each row is a one-dimensional matrix."
         " These one-dimensional matrices are a sequence of numbers:"
         (code (matrix [[1 2 3] [4 5 6] [7 8 9]]))
         "Matrices can also be created by passing in a single sequence of numbers and the desired number of columns:"
         (code (matrix [1 2 3 4 5 6 7 8 9] 3))
         "Both of these will produce the same 3x3 matrix."]
    [:li "An identity matrix can easily be constructed using the " (c identity-matrix) " function, passing in the dimension:"
         (code (identity-matrix 4))]
    [:li "Symmetric matrices are created using the " (c symmetric-matrix) " function."
         " The matrix should be represented as a single sequence of numbers which form the lower triangle of the resulting matrix."
         " Numbers should be passed in using " [:a {:href "http://en.wikipedia.org/wiki/Row-major_order#Row-major_order"} "row-major order"] ":"
         (code "(def A (symmetric-matrix 
  [1
   2 3
   4 5 6
   7 8 9 10]))")]]])

(defn trace
  []
  [[:h3 "Trace"]
   [:ol
    [:li "The trace of a matrix is found by adding together all of the numbers on the diagonal from (0,0) to (n,n)."
         " Incanter provides the " (c diag) " function to access those elements:"
         (code (diag A))]
    [:li "To find the trace of the matrix, add all the results from " (c diag) " together:"
         (code (defn tr [m] (apply + (diag m))))]
    [:li "Applying " (c tr) " to " (c A) " will result in 15."
         " To test your answer against other matrices, you can use Incanter's " (c trace) " function as a standard for comparison."]]])

(defn determinant
  []
  [[:h3 "Determinant"]
   [:ol
    [:li "Information on the determinant of a matrix can be found " [:a {:href "http://en.wikipedia.org/wiki/Determinant"} "here"] "." 
         " A common way of being taught to take the determinant by hand is called the " [:a {:href "http://en.wikipedia.org/wiki/Laplace_expansion"} "Laplace Expansion"] " and involves finding the minors of the matrix and recursively finding their determinants until the minors are 2x2."
         " At that point, the base case is to solve " (c ad-bc) ", where a is in position (0,0), b at (0,1), c at (1,0), and d at (1,1)."
         " For information on what the minor of a matrix is, see " [:a {:href "http://en.wikipedia.org/wiki/Minor_%28linear_algebra%29"} "here"] "." 
         " We'll start by finding the minor of a matrix given a specific row and column to remove:"
         (code "(defn getMinor [m r c] 
 (sel m :except-rows r :except-cols c))")]
    [:li "Next, we'll need to get all the minors for a specific matrix, as well as their signed coefficients."
         " Minors and their coefficients taken together are called " [:a {:href "http://en.wikipedia.org/wiki/Cofactor_%28linear_algebra%29"} "cofactors"] "." 
         " Because we only need a cofactor for every column along the top row, we can map each column number to " (c getMinor) " and leave the row as 0:" 
         (code "(defn getCofactors [m]
    (map #(cons (* (Math/pow -1 %) (sel m 0 %)) (getMinor m 0 %))
        (range (sqrt (length m)))))")]
    [:li "We now need to write the determinant function that uses " (c getCofactors) "."
         " If our matrix is already 2x2, we simply need to calculate " (c ad-bc) ":"
         (code "(defn determinant [m]
  (if ((= 2 (first (dim m)) (second (dim m)))
    (- (* (sel m 0 0) (sel m 1 1)) (* (sel m 0 1) (sel m 1 0))))))")]
    [:li "If the matrix is larger than 2x2, all the cofactors will need to be found and recured on until they are 2x2." 
         " To recur on each cofactor, use map, multiplying together coefficients as you go:"
         (code "(defn determinant [m]
  (if (= 2 (first (dim m)) (second (dim m)))
    (- (* (sel m 0 0) (sel m 1 1)) (* (sel m 0 1) (sel m 1 0)))
    (map #(* (first %) (trampoline determinant (rest %))) (getCofactors m))))")]
    [:li "Finally, add the results together:"
         (code "(defn determinant [m]
  (if (= 2 (first (dim m)) (second (dim m)))
    (- (* (sel m 0 0) (sel m 1 1)) (* (sel m 0 1) (sel m 1 0)))
    (apply + (map #(* (first %) (trampoline determinant (rest %)))
                   (getCofactors m)))))")]
    [:li "Test your solution against Incater's " (c det) " using matrices that are no larger than 7x7."
         " This solution is very inefficient for larger matrices."
         " There may be slight rounding errors from precision loss."]]])

(defn independent
  []
  [[:h3 "Linear Independence"]
   [:ol
    [:li "Vectors are considered " [:a {:href "http://en.wikipedia.org/wiki/Linear_independence"} "linearly independent"] " of one another if the matrix they form when concatinated together has a determinant that is not zero."
         " To write a function to test this for a set of vectors, we'll need to accept an arbitrary number of arguments:" 
         (code "(defn independent? [& vectors])")]
    [:li "Next, we need to concatinate the vectors together to form a single matrix:"
         (code "(defn independent? [& vectors]
  (to-matrix (apply conj-cols vectors)))")]
    [:li "Once we've done this, we can take the determinent of the matrix we just created with " (c conj-cols) ":"
         (code "(defn independent? [& vectors]
  (det (to-matrix (apply conj-cols vectors))))")]
    [:li "Now, if the determinent equals 0, the vectors are not independent."
         " Otherwise, they are:"
         (code "(defn independent? [& vectors]
  (if (zero? (det (to-matrix (apply conj-cols vectors))))
	false
	true))")
         "When testing this, remember that the vectors passed in must make a square matrix."
         " Thus, the length of all of your vectors should be the same, and the number of elements in your vector should equal the number of vectors you pass in."]]]) 

;(defn inverse
;  []
;  [[:h3 "Inverse"]
;   [:ol
;    [:li "The inverse of a matrix can be found in several ways."
;         " The option we will focus on uses " [:a {:href "http://en.wikipedia.org/wiki/Eigenvalue_decomposition"} "eigenvalue decomposition"] "."
;         " Using " (c decomp-eigenvalue) " will return the eigenvalues and eigenvectors associated with the matrix:"]]]) 

;(defn inverse
;  []
;  [[:h3 "Inverse"]
;   [:ol
;    [:li "As mentioned in the previous section, the solution for determinant we wrote only works for smaller matrices."
;         " A much more efficient way of calculating the determinant of a matrix is by using the LU decomposition."
;         " Incanter provides many decomposition functions, including " (c decomp-lu) "."
;         " The following will find matrices " (c L) " and " (c U) " and store " (c U) "; " (c L) " is not needed to calculate the determinant:" 
;         (code "(defn det2 [m]
; (let [{u :U} (decomp-lu m)]))")]
;    [:li "The determinant can be found by multiplying together all of the values on the diagonal of " (c U) ":"
;         (code "(defn det2 [m]
; (let [{u :U} (decomp-lu m)]
;   (reduce * (diag u))))")]]])

(defn instructions
  []
  (concat
   (overview)
   (creating) 
   (trace)
   (determinant)
   (independent)))