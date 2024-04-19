
(ns joy-of-clojure.chapter-four)

; 4.1 Understanding precision

; 4.4.1 Truncation

(let [imadeuapi 3.14159265358979323846264338327950288419716939937M]
  (println (class imadeuapi)) ; => java.math.BigDecimal 
  imadeuapi) ; => 3.14159265358979323846264338327950288419716939937M

(let [butieatedit 3.14159265358979323846264338327950288419716939937]
  (println (class butieatedit)) ; => java.lang.Double
  butieatedit) ; => .141592653589793

; 4.1.2 Promotion
(def clueless 9)

(class clueless)
; => java.lang.Long - Long by Default

(class (+ clueless 9000000000000000))
; => java.lang.Long - Long can hold large values

(class (+ clueless 90000000000000000000))
                                        ; => clojure.lang.BigInt - But when too l
arge, the type promotes to BigInt

(class (+ clueless 9.0))
; => java.lang.Double - Floating-point doubles are contagious

(class (+ clueless 90000000000000000000.00))
; => java.lang.Double

; 4.1.3 Overflow
(+ Long/MAX_VALUE Long/MAX_VALUE)
; => java.lang.ArithmeticException: integer overflow

; Clojure provides unchecked integer and long
; These unchecked operation WILL overflow if given excessively large values:
(unchecked-add Long/MAX_VALUE Long/MAX_VALUE)
; => -2

(unchecked-add Long/MAX_VALUE 1)
; => -9223372036854775808

; Underflow - Only occurs with floating-point numbers
(float 0.0000000000000000000000000000000000000000000001)
; => 0.0

1.0E-430
; => 0.0

; 4.1.5 Rounding errors
(let [approx-interval (/ 209715 2097152) ; Patriot's approx 0.1
      actual-interval (/ 1 10) ; Clojure can accurately represent 0.1
      hours (* 3600 100 10)
      actual-total (double (* hours actual-interval))
      approx-total (double (* hours approx-interval))]
  (- actual-total approx-total))
; => 0.34332275390625
; A deviation of 0.34 seconds - This was the problem in the Patriot missile in the First Gulf War

; One way to contribute to rounding errors is to introduce dobules and floats into an operation
; In Clojure, any computation involving even a single double results in a value that's double
(+ 0.1M 0.1M 0.1M 0.1 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M)
; => 0.9999999999999999

(class (+ 0.1M 0.1M 0.1M 0.1 0.1M 0.1M 0.1M 0.1M 0.1M 0.1M))
; => java.lang.Double

; 4.2 Rationals

; 4.2.1 Why be rational
1.0E-430000000M
; => 1.0E-430000000M

1.0E-4300000000M
; => java.lang.RuntimeException: java.lang.NumberFormatException - Exponent overflow.

(def a 1.0e50)
(def b -1.0e50)
(def c 17.0e00)

(+ (+ a b) c)
; => 17.0

(+ a (+ b c))
; => 0.0
; Associativity should guarantee 17.0 for both

; 4.2.2 How to be rational
(def a (rationalize 1.0e50))
(def b (rationalize -1.0e50))
(def c (rationalize 17.0e00))

(+ (+ a b) c)
; => 17N

(+ a (+ b c))
; => 17N
; Associativity preserved

; There are few rules of thumb to remember if you want to mantain perfect accurracy in your computations:
; - NEVER use Java math libraries unless they return results of BigDecimal, and even then be suspicious
; - Don't rationalize values that are Java float or double primitives
; - If you must write your own high-precision calculations, do so with rationals
; - Only convert to a floating-point representation as a last resort

; You can extract the constitiun parts of a rational:
(numerator (/ 123 10))
; => 123
(denominator (/ 123 10))
; => 10

; Remember, rationals are good for accuracy is not as fast as floats or doubles
; If speed for calculations matter the most, then use float or doubles
; But if it's accuracy, then stick with rationals

; 4.3 When to use keywords
:a-keyword
::also-a-keyword

; 4.3.1 Application of keywords
; Keywords are almost always used as map keys

; As Keys
(def population {:youngs 2700 :elderly 900})
(get population :youngs)

(println (/ (get population :youngs)
            (get population :elderly))
         "Elderly per capita")

; As Functions
(:elderly population)
(println (/ (:youngs population)
            (:elderly population))
         "Elderly per capita")
; Using keywords as map-lookup fucntions leads to much more concise code

; As Enumarations
; Clojure code uses keywords as enumarations values, such as :small, :medium and :large. This provide a nice visual delineation in the source code

; As Directives
(defn pour [lb ub]
  (cond
    (= ub :toujours) (iterate inc lb)
    :else (range lb ub)))

; Called with lower and upper bounds, returns a range
(pour 1 10)

; Called with a keyword argument, iterates forever
(pour 1 :toujours)

; Another bonus with pour is that the macro cond uses a directive :else to mark the default conditional case.

; 4.4 Symbolic resolution
(identical? 'goat 'goat)
; => false

(= 'goat 'goat)
; => true

(name 'goat)

; The identical? functions only return true when the symbols are the same object
(let [x 'goat, y x]
  (identical? x y))
; => true

; 4.4.1 Metadata
; Clojure lets you attach metadata to various objects
(let [x (with-meta 'goat {:ornery true})
      y (with-meta 'goat {:ornery false})]
  [(= x y)
   (identical? x y)
   (meta x)
   (meta y)])
; => [true false {:ornery true} {:ornery false}]

; 4.5 Regular expressions

; 4.5.1 Syntax
#"an example pattern"
(class #"an example pattern")
; => java.util.regex.Pattern

; In Java, any backslashes intended for consumption by the regex compiler must be doubled
(java.util.regex.Pattern/compile "\\d")
; => #"\d"
; This isn't necessary in Clojure regex literals, as show by the undoubled return value

; 4.5.2 Regular-expression functions
(re-seq #"\w+" "one-two/three")
; => ("one" "two" "three")

; Capturing group in the regex causes each returned item to be a vector
(re-seq #"\w*(\w)" "one-two/three")
; => (["one" "e"] ["two" "o"] ["three" "e"])




