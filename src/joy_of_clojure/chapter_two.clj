(ns joy-of-clojure.chapter-two)

; 2.3 Calling functions
(list 1 2 3 4 5)
(vector 6 7 8 9 0)

; 2.4 Vars are not variables
(def x 42)
(println x)

(def y)
(println y)

; 2.5 Functions

; 2.5.1 Anonymous Functions
(fn [x y]
  (println "Making a set")
  #{x y})

((fn [x y]
   (println "Making a set")
   #{x y}) 1 2)

; 2.5.2 Creating named function with def and defn
(def make-set
  (fn [x y]
    (println "Making a set")
    #{x y}))
(make-set 1 2)

(defn make-set
  "Takes two values and makes a set from them."
  [x y]
  (println "Making a set")
  #{x y})
(make-set 3 4)

; 2.5.3 Functions with multiple arities
(make-set 1)
(defn make-set
  ([x] #{x})
  ([x y] #{x y}))
(make-set 10)
(make-set 5 6)
(make-set 7 8 9)

(defn arity2+ [first second & more]
  (vector first second more))

(arity2+ 1 2)
(arity2+ 1 2 3)
(arity2+ 1 2 3 4 5)
; (arity2+ 1)

(defn make-set
  ([x] #{x}) 
  ([x & more] #{x more}))
(make-set 1)
(make-set 2 3)
(make-set 4 5 6 7 8)

; 2.5.4 In-place functions with #()
(def make-list0 #(list))
(make-list0)

(def make-list2 #(list %1 %2))
(make-list2 1 2)

(def make-listN #(list %&))
(make-listN 1 2 3 4 5 6 7)
(make-listN 1)

; 2.6 Locals, loops and Blocks
; 2.6.1 Blocks
(do
  (def a 5)
  (def b 4)
  (+ a b)
  [a b])

; 2.6.2 Locals
(let 
 [r 5
  pi 3.1415
  r-squared (* r r)]
  (println "radius is" r)
  (* pi r-squared))

; 2.6.3 Loops

; Recur
(defn print-down-from 
  "Prints the integers from x to 1, counting backwards"
  [x]
  (when (pos? x)
    (println x)
    (recur (dec x))))
(print-down-from 10)
(print-down-from 2)
(print-down-from 1)
(print-down-from 0)


(defn sum-down-from 
  "Sum of numbers with an auxiliary accumulator"
  [sum x]
  (if (pos? x)
    (recur (+ sum x) (dec x))
    sum))
(sum-down-from 0 10)
(sum-down-from 0 0)

; You should when in two cases:
; - No else part is associated with the result of a conditional
; - You require an implicit do in dorder to perform side effects - like printing.

; Loop
(defn sum-down-from
  "Sum of numbers with an internal auxiliary accumulator"
  [initial-x]
  (loop [sum 0, x initial-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))
(sum-down-from 10)
(sum-down-from 5)
(sum-down-from 0)

; Note that the two implementations of sum-down-from utilizes recur on tail position
; The tail position of an expression is when its value may be the return value of the entire expression

; 2.7 Quoting
(def age 26)
(age)
(quote age)
(quote (cons 1 [2 3]))
(cons 1 (2 3))
(quote (cons 1 (2 3)))
; Quote can be ' or `
(cons 1 '(2 3))
(cons 4 `(5 6))
`map

; 2.7.3 Unquoting
`(+ 10 (* 3 2))
`(+ 10 ~(* 3 2))

; 2.7.4 Unquote-splicing
(let [x '(2 3)] `(1 ~x)) ; => (1 (2 3))
(let [x '(2 3)] `(1 ~@x)) ; => (1 2 3)

; 2.7.5 Auto-gensym
`potion# ; => potion__7732__auto__
; it's random generated

; 2.8 Using host libraries via interop

; 2.8.1 Accessing static class members (Clojure only)
java.util.Locale/JAPAN

(Math/sqrt 9)

; 2.8.2 Creating instances
(new java.awt.Point 0 1)
(def some-map {"foo" 42 "bar" 9 "baz" "quux"})
(new java.util.HashMap some-map)

; A way more succint to create instances
(java.util.HashMap. some-map)

; 2.8.3 Accessing instance members with the . operator
(.-x (java.awt.Point. 10 20))
(.divide (java.math.BigDecimal. "42") 2M)

; 2.8.4 Setting intance fields
(let [origin (java.awt.Point. 0 0)]
  (set! (.-x origin) 15)
  (str origin))

; 2.8.5 The .. macro
; Java code: new java.util.Date().toString().endsWith("2024")
(.endsWith (.toString (java.util.Date.)) "2024") ; Clojure equivalent to java

; The code above, although correct, is difficult to read and will 
; only become more so when lengthen the chain of method calls.
; More simple way
(.. (java.util.Date.) toString (endsWith "2024"))

; 2.8.6 The doto macro
; In java it's common to initialize a fresh instance by calling a set of mutators
; java.util.HashMap props = new java.util.HashMap();
; props.put("HOME", "/home/me");
; props.put("SRC", "src");
; props.put("BIN", "classes");

; In Clojure, you can use the doto macro
(doto (java.util.HashMap.)
  (.put "HOME" "/home/me")
  (.put "SRC" "src")
  (.put "BIN" "classes"))

; 2.9 Exceptional circumstances

; 2.9.1 Throwing and catching
(throw (Exception. "I done throwed"))

(defn throw-catch [f]
  [(try
     (f)
     (catch ArithmeticException e "No dividing by zero!")
     (catch Exception e (str "You are so bad " (.getMessage e)))
     (finally (println "returning...")))])

(throw-catch #(/ 10 5))
(throw-catch #(/ 10 0))
(throw-catch #(/ 10 "a"))
(throw-catch #(throw (Exception. "Crybaby")))

