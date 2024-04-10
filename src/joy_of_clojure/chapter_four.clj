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
; => clojure.lang.BigInt - But when too large, the type promotes to BigInt

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

; There are few rules of thumb to remember if you want to maintin perfect accurracy in your computations:
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
