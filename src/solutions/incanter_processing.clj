(ns
  ^{:author "Allie Terrell"
   :doc "Not meant to be run. Allows for labrepl users to copy and paste" 
          " examples without worrying about the line numbers online."}
  solutions.incanter-processing)

;; Processing Monster
; 2
(let [delay 20
      sktch (sketch
              (setup []
                (doto this
                  (size 400 400)
                  (background 0)
                  (stroke-weight 8)
                  (framerate 30)
                  smooth)))]
  (view sktch :size [400 400]))

; 3
(let [delay 20
      sktch (sketch
              (setup []
                (doto this
                  (size 400 400)
                  (background 0)
                  (stroke-weight 8)
                  (framerate 30)
                  smooth))
              (draw []
								(doto this
								  (background 0)
								  (fill 255 255 255)
								  (ellipse @X @Y @radius @radius)
								  (fill 0 0 0)
								  (line (- @X 30) (- @Y 10) (- @X 20) (- @Y 10))
								  (line @X (- @Y 10) (+ @X 10) (- @Y 10)))))]
  (view sktch :size [400 400]))

; 4
(let [radius (ref 90.0)
      X (ref nil)
      Y (ref nil)
      nX (ref nil)
      nY (ref nil)
      delay 20
      sktch (sketch
              (setup []
                (doto this
                  (size 400 400)
                  (background 0)
                  (stroke-weight 8)
                  (framerate 50)
                  smooth)
                (dosync
                  (ref-set X (/ (width this) 2))
                  (ref-set Y (/ (width this) 2))
                  (ref-set nX @X)
                  (ref-set nY @Y)))
              (draw []
                (dosync
                  (ref-set X (+ @X (/ (- @nX @X) delay)))
                  (ref-set Y (+ @Y (/ (- @nY @Y) delay))))
                (doto this
                  (background 0)
                  (fill 255 255 255)
                  (ellipse @X @Y @radius @radius)
                  (fill 0 0 0)
                  (line (- @X 30) (- @Y 10) (- @X 20) (- @Y 10))
                  (line @X (- @Y 10) (+ @X 10) (- @Y 10))))
              (mouseMoved [mouse-event]
                (dosync
                  (ref-set nX (mouse-x mouse-event))
                  (ref-set nY (mouse-y mouse-event)))))]
  (view sktch :size [400 400]))

; 5
(let [radius (ref 90.0)
      X (ref nil)
      Y (ref nil)
      nX (ref nil)
      nY (ref nil)
      lookingLeft (ref -1)
      delay 20
      sktch (sketch
              (setup []
                (doto this
                  (size 400 400)
                  (background 0)
                  (stroke-weight 8)
                  (framerate 50)
                  smooth)
                (dosync
                  (ref-set X (/ (width this) 2))
                  (ref-set Y (/ (width this) 2))
                  (ref-set nX @X)
                  (ref-set nY @Y)))
              (draw []
                (dosync
                  (ref-set X (+ @X (/ (- @nX @X) delay)))
                  (ref-set Y (+ @Y (/ (- @nY @Y) delay))))
                (doto this
                  (background 0)
                  (fill 255 255 255)
                  (ellipse @X @Y @radius @radius)
                  (fill 0 0 0)
                  (line (- @X (* @lookingLeft 30)) (- @Y 10)
                        (- @X (* @lookingLeft 20)) (- @Y 10))
                  (line @X (- @Y 10)
                        (+ @X (* @lookingLeft 10)) (- @Y 10))))
              (mouseMoved [mouse-event]
                (dosync
                  (if (> (- @X (mouse-x mouse-event)) 0)
                    (ref-set lookingLeft 1)
                    (ref-set lookingLeft -1))
                  (ref-set nX (mouse-x mouse-event))
                  (ref-set nY (mouse-y mouse-event)))))]
  (view sktch :size [400 400]))

; 6
(let [radius (ref 90.0)
      X (ref nil)
      Y (ref nil)
      nX (ref nil)
      nY (ref nil)
      lookingLeft (ref -1)
      transparent (ref 255)
      delay 20
      sktch (sketch
              (setup []
                (doto this
                  (size 400 400)
                  (background 0)
                  (stroke-weight 8)
                  (framerate 50)
                  smooth)
                (dosync
                  (ref-set X (/ (width this) 2))
                  (ref-set Y (/ (width this) 2))
                  (ref-set nX @X)
                  (ref-set nY @Y)))
              (draw []
                (dosync
                  (ref-set X (+ @X (/ (- @nX @X) delay)))
                  (ref-set Y (+ @Y (/ (- @nY @Y) delay))))
                (doto this
                  (background 0)
                  (fill 255 255 255 @transparent)
                  (ellipse @X @Y @radius @radius)
                  (fill 0 0 0)
                  (line (- @X (* @lookingLeft 30)) (- @Y 10)
                        (- @X (* @lookingLeft 20)) (- @Y 10))
                  (line @X (- @Y 10)
                        (+ @X (* @lookingLeft 10)) (- @Y 10))))
              (mouseMoved [mouse-event]
                (dosync
                  (if (> (- @X (mouse-x mouse-event)) 0)
                    (ref-set lookingLeft 1)
                    (ref-set lookingLeft -1))
                  (ref-set nX (mouse-x mouse-event))
                  (ref-set nY (mouse-y mouse-event))))
              (mouseDragged [mouse-event]
                (dosync
                  (ref-set transparent (- @transparent 0.5))))
              (mouseReleased [mouse-event]
                (dosync
                  (ref-set transparent 255))))]
  (view sktch :size [400 400]))

;; U.S. Data
; 2
(let [map-image (ref nil)
      sktch (sketch
              (setup []
                (dosync
                  (ref-set map-image
                           (load-image this
                                       "http://benfry.com/writing/map/map.png")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  no-loop)))]
    (view sktch :title "US Map with Population Data" :size [640 430]))

; 3
(let [map-image (ref nil)
      data-dump (sel (read-dataset "http://bit.ly/cqSGMz"
                   :delim \,
                   :header true) :except-rows [0 1 2 3 4 13 56])
      raw-data (sel data-dump :cols :POPESTIMATE2009)
      sktch (sketch
              (setup []
                (dosync
                  (ref-set map-image
                           (load-image this
                                       "http://benfry.com/writing/map/map.png")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  no-loop)))]
    (view sktch :title "US Map with Population Data" :size [640 430]))

; 4
(let [map-image (ref nil)
      data-dump (sel (read-dataset "http://bit.ly/cqSGMz"
                   :delim \,
                   :header true) :except-rows [0 1 2 3 4 13 56])
      raw-data (sel data-dump :cols :POPESTIMATE2009)
      max-val (reduce max raw-data)
      min-val (reduce min raw-data)
      percents (map #(norm % min-val max-val) raw-data)
      sktch (sketch
              (setup []
                (dosync
                  (ref-set map-image
                           (load-image this
                                       "http://benfry.com/writing/map/map.png")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  no-loop)))]
    (view sktch :title "US Map with Population Data" :size [640 430]))

; 5
(let [map-image (ref nil)
      data-dump (sel (read-dataset "http://bit.ly/cqSGMz"
                   :delim \,
                   :header true) :except-rows [0 1 2 3 4 13 56])
      raw-data (sel data-dump :cols :POPESTIMATE2009)
      max-val (reduce max raw-data)
      min-val (reduce min raw-data)
      percents (map #(norm % min-val max-val) raw-data)
      locations (sel (to-matrix (read-dataset
                            "http://benfry.com/writing/map/locations.tsv"
                            :delim \tab))
      data (bind-columns locations
                   (map #(remap % min-val max-val 10 50) raw-data)
                   percents)
      sktch (sketch
              (setup []
                (dosync
                  (ref-set map-image
                           (load-image this
                                       "http://benfry.com/writing/map/map.png")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  no-loop)))]
    (view sktch :title \"US Map with Population Data\" :size [640 430]))

; 6
(let [map-image (ref nil)
      data-dump (sel (read-dataset "http://bit.ly/cqSGMz"
                                 :delim \,
                                 :header true) :except-rows [0 1 2 3 4 13 56])
      raw-data (sel data-dump :cols :POPESTIMATE2009)
      max-val (reduce max raw-data)
      min-val (reduce min raw-data)
      percents (map #(norm % min-val max-val) raw-data)
      locations (sel (to-matrix (read-dataset
                                  "http://benfry.com/writing/map/locations.tsv"
                                  :delim \tab))
                     :cols [1 2])
      data (bind-columns locations
                         (map #(remap % min-val max-val 10 50) raw-data)
                         percents)
      draw-data (fn [sketch data]
                  (doseq [[x y value percent] data]
                    (fill sketch (lerp-color (color 0xFF0000)
                                             (color 0x0000A0)
                                             percent))
                    (ellipse sketch x y value value)))
      sktch (sketch
              (setup []
                (dosync
                  (ref-set map-image
                           (load-image this
                                       "http://benfry.com/writing/map/map.png")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  (draw-data data)
                  no-loop)))]
    (view sktch :title "US Map with Population Data" :size [640 430]))