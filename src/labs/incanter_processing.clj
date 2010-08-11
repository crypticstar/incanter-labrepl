(ns ^{:skip-wiki true} labs.incanter-processing
  (:use labrepl.util))

(defn overview
  []
  [[:h3 "Overview"]
   [:p "This labrepl will teach you how to use " [:a {:href "http://processing.org/"} "Processing"] ", a Java-based graphical programming language."
       " Processing is frequently used for images, animations, and data visualization."
       " It is built upon the idea of using sketches to rapidly build and test visualizations."
       " In this labrepl, you'll have the opportunity to make both animations and inforgraphics using Processing."]])

(defn monster
  []
  [[:h3 "Processing Monster"]
   [:ol
    [:li "One creative way to learn the basics of Processing is to build a " [:a {:href "http://www.rmx.cz/monsters/"} "Processing monster"] "."
         " You can click on some of the monsters on the site to see a demo of how they work, as well as the code behind them."    
         " Each monster must be black and white, and it must react in some way to mouse interaction."
         " We will work on building up a small monster that meets these criteria."
         " It will consist of an ellipse with lines for eyes that follows our cursor, gradually disappearing if we hold down the rick mouse button."]
    [:li "First, create a window for the monster to live in:"
         (code "(use '[incanter core processing])

(let [delay 20
      sktch (sketch
              (setup []
                (doto this
                  (size 400 400)
                  (background 0)
                  (stroke-weight 8)
                  (framerate 30)
                  smooth)))]
  (view sktch :size [400 400]))")
         "All of the code to define the monster and its interactions will be in a " (c let) " statement."
         " Every Processing programs must have a sketch item."
         " This will define what commands Processing needs to execute."
         (c setup) " is always executed at the start of a Processing program, while others (like " (c draw) ") are called frequently during the lifespan of the program."]
    [:li "Create variables in " (c let) " to keep track of the position of the monster and it's size:"
         (code "radius (ref 90.0)
X (ref nil)
Y (ref nil)")
         ;"The " (c X) " and " (c Y) " values will keep track of the position of the monster."
         " Then, add a " (c draw) " function to " (c sktch) ", just below the " (c setup) " function:"
         (code "(draw []
  (doto this
    (background 0)
    (fill 255 255 255)
    (ellipse @X @Y @radius @radius)
    (fill 0 0 0)
    (line (- @X 30) (- @Y 10) (- @X 20) (- @Y 10))
    (line @X (- @Y 10) (+ @X 10) (- @Y 10))))")
         "The monster we're going to create will start out as an ellipse with eyes."
         " The " (c draw) " function is responsible for changing the display, and is repeatedly called according to the frame rate of our sketch."]
    [:li "Add the following variables to " (c let) " so that we can keep track of the updated position of the monster:"
         (code "nX (ref nil)
nY (ref nil)")
         "Now add the " (c mouseMoved) " function; it is automatically called every time the mouse is moved:"
         (code "(mouseMoved [mouse-event]
  (dosync
    (ref-set nX (mouse-x mouse-event))
    (ref-set nY (mouse-y mouse-event))))")
         "Change the X and Y position of the monster to move towards the mouse, which can be added before the " (c doto) " statement in " (c draw) ":"
         (code "(dosync
  (ref-set X (+ @X (/ (- @nX @X) delay)))
  (ref-set Y (+ @Y (/ (- @nY @Y) delay))))")
         "In order to use this code, we'll need to initialize " (c nX) " and " (c nY) " in the " (c setup) " function:"
         (code (ref-set nX @X)
(ref-set nY @Y))
         "Now, our monster should move towards our mouse:"
         (showme "(let [radius (ref 90.0)
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
  (view sktch :size [400 400]))")]
    [:li "Add a value in " (c let) " to determine which direction our monster is heading:"
         (code lookingLeft (ref -1))
         "This value will be changed to 1 when the monster is facing right, and -1 when facing left."
         " Check what direction the monster should be facing at the top of our " (dosync) " statement in the " (c mouseMoved) " function:"
         (code "(if (> (- @X (mouse-x mouse-event)) 0)
  (ref-set lookingLeft 1)
  (ref-set lookingLeft -1))")
         "Using the " (c lookingLeft) " value, we can structure the creation of our two lines to change position based on the direction we're facing:"
         (code "(line (- @X (* @lookingLeft 30)) (- @Y 10) (- @X (* @lookingLeft 20)) (- @Y 10))
(line @X (- @Y 10) (+ @X (* @lookingLeft 10)) (- @Y 10))")
         "The monster should now change where it's looking based on the direction it's moving."
         (showme "(let [radius (ref 90.0)
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
  (view sktch :size [400 400]))")]
    [:li "Add a value called " (c transparent) " that will measure how transparent the monster should be, with the full-opaque value (the default) being 255:"
         (code "transparent (ref 255)")
         "Next, modify the first " (c fill) " statement in " (c draw) " to accept a transparency value as well:"
         (code (fill 255 255 255 @transparent))
         "Add the following to " (c sktch) " to change the transparency of the monster when the mouse is clicked and dragged:"
         (code "(mouseDragged [mouse-event]
  (dosync
    (ref-set transparent (- @transparent 0.5))))")
         "The following will then return the monster to being fully opaque when the mouse is released:"
         (code "(mouseReleased [mouse-event]
  (dosync
    (ref-set transparent 255)))")
         "The monster should now be able to disappear and reappear using the mouse."
         (showme "(let [radius (ref 90.0)
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
  (view sktch :size [400 400]))")]]])

(defn data
  []
  [[:h3 "U.S. Data"]
   [:ol
    [:li "Processing is also useful for making data visualizations."
         " We'll be using materials from the " [:a {:href "http://benfry.com/"} "website of Ben Fry"] " (one of the creators of Processing) to add data to a map of the U.S."
         " Our data will come from the " [:a {:href "http://www.census.gov/"} "U.S. Census Bureau"] " and their " [:a {:href "http://www.census.gov/popest/datasets.html"} "population datasets"] ", the same data from the charts labrepl."
         " With it, we're going to create an infographic presenting the population of each U.S. state visually."]
    [:li "Load the " (c core) ", " (c processing) ", and " (c io) " libraries."
         " Then load the following map graphic, as taken from Ben Fry's website:"
         (code "(use '[incanter core processing io])

(let [map-image (ref nil)
      sktch (sketch
              (setup []
                (dosync
                  (ref-set map-image
                           (load-image this
                                       \"http://benfry.com/writing/map/map.png\")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  no-loop)))]
    (view sktch :title \"US Map with Population Data\" :size [640 430]))")]
    [:li "Next, import the data we'll be using and decide on a specific column to display on the map."
         " For now, we'll use the " (c :POPESTIMATE2009) " data:"
         (code "data-dump (sel (read-dataset \"http://bit.ly/cqSGMz\"
					         :delim \\,
					         :header true) :except-rows [0 1 2 3 4 13 56])
raw-data (sel data-dump :cols :POPESTIMATE2009)")
         "The " (c :except-rows) " excludes the values for each region of the U.S., DC, Puerto Rico, and the U.S. as a whole."]
    [:li "Find the min and max value in our dataset and add them to our " (c let) ":"
         (code "max-val (reduce max raw-data)
min-val (reduce min raw-data)")
         "Now normalize all of the data values to fall between the max and min in the " (c let) " statement:"
         (code "percents (map #(norm % min-val max-val) raw-data)")]
    [:li "Associate the percentages calculated in the previous step with states and their locations on the map."
         " Ben Fry has helpfully put together a dataset that indicates these values, which we can add to our " (c let) ":"
         (code "locations (sel (to-matrix (read-dataset
	                          \"http://benfry.com/writing/map/locations.tsv\"
	                          :delim \\tab))")
         "We can then bind " (c percents) " to " (c locations) ":"
         (code "data (bind-columns locations
                   (map #(remap % min-val max-val 10 50) raw-data)
                   percents)")]
    [:li "Finally, draw the data by first defining a function in " (c let) ":"
         (code "draw-data (fn [sketch data]
            (doseq [[x y value percent] data]
              (fill sketch (lerp-color (color 0xFF0000)
                                       (color 0x0000A0)
                                       percent))
              (ellipse sketch x y value value)))")
         "And then calling it in " (c draw) ":"
         (code "(draw-data data)")
         "The complete code can be found below:"
         (showme "(use '[incanter core processing io])

(let [map-image (ref nil)
      data-dump (sel (read-dataset \"http://bit.ly/cqSGMz\"
	                               :delim \\,
	                               :header true) :except-rows [0 1 2 3 4 13 56])
      raw-data (sel data-dump :cols :POPESTIMATE2009)
      max-val (reduce max raw-data)
      min-val (reduce min raw-data)
      percents (map #(norm % min-val max-val) raw-data)
      locations (sel (to-matrix (read-dataset
                                  \"http://benfry.com/writing/map/locations.tsv\"
                                  :delim \\tab))
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
                                       \"http://benfry.com/writing/map/map.png\")))
                (smooth this)
                (no-stroke this)
                (size this 640 400))
              (draw []
                (doto this
                  (background (color 0xFFFFFF))
                  (image @map-image 0 0)
                  (draw-data data)
                  no-loop)))]
    (view sktch :title \"US Map with Population Data\" :size [640 430]))")
         "The " (c raw-data) " column selection can be changed to one of the other column names in the dataset to map those values instead."]]])

(defn instructions
  []
  (concat
   (overview)
   (monster)
   (data)))