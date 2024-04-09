(ns joy-of-clojure.chapter-three)

; 3.1 Truthiness

; 3.1.1 What is truth?
(if true :truthy :falsey) ; => :truthy
(if [] :truthy :falsey) ; => :truthy
(if nil :truthy :falsey) ; => :falsey
(if false :truthy :falsey) ; => :falsey
; Every object is true all the time, unless it's nil or false

; 3.1.2 Don't create Boolean objects
(def evil-false (Boolean. "false")) ; NEVER DO THIS
evil-false ; Looks like false
; Sometimes it even act like false
(= false evil-false)

; But once it gains your trust, it'll show you just how wicked it is by actiing like true
(if evil-false :truthy :falsey) ; => :truthy

; This is the right way
(if (Boolean/valueOf "false") :truthy :falsey) ; => :falsey

; 3.1.3 nil vs false
(if (nil? nil) "Actually nil, not false" "Actually false, not nil")
; => "Actually nil, not false"

; 3.2 Nil pun with care
(seq [1 2 3]) ; => (1 2 3)
(seq []) ; => nil

(defn print-seq [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))
(print-seq [7 8 9 0])
(print-seq [])

; 3.3 Destructuring

; 3.3.2 Destructuring with a vector
; Without destructuring:
(def guys-whole-name ["Guy" "Lewis" "Steele"])

(str (nth guys-whole-name 2) ", "
     (nth guys-whole-name 0) " "
     (nth guys-whole-name 1))

; With destructuring:
(let [[f-name m-name l-name] guys-whole-name]
  (str l-name ", " f-name " " m-name))

(let [[a b c & more] (range 0 10)]
  (println "a b c are:" a b c)
  (println "more is:" more))

(let [range-vec (vec (range 10))
      [a b c & more :as all] range-vec]
  (println "a b c are:" a b c)
  (println "more is:" more)
  (println "all is:" all))

; 3.3.3 Destructuring with a map
(def guys-name-map
  {:f-name "Guy" :m-name "Lewis" :l-name "Steele"})

(let [{f-name :f-name m-name :m-name l-name :l-name} guys-name-map]
  (str l-name ", " f-name " " m-name))

(let [{:keys [f-name m-name l-name]} guys-name-map]
  (str l-name ", " f-name " " m-name))

(let [{f-name :f-name, :as whole-name} guys-name-map]
  (println "First name is" f-name)
  (println "Whole name is below:")
  whole-name)

(let [{:keys [title f-name m-name l-name],
       :or {title "Mr."}} guys-name-map]
  (println title f-name m-name l-name))

; Also works with lists
(defn whole-name [& args]
  (let [{:keys [f-name m-name l-name]} args]
    (str l-name ", " f-name " " m-name)))

(whole-name :f-name "Guy" :m-name "Lewis" :l-name "Steele")

; Associative destructuring - You can use map declaring the local names but in a vector
(let [{first-thing 0, last-thing 3} [1 2 3 4]]
  [first-thing last-thing])

; 3.3.4 Destructuring in function parameters
(defn print-last-name [{:keys [l-name]}]
  (println l-name))

(print-last-name guys-name-map)

; 3.4 Using the REPL to experiment

; 3.4.1 Experimenting with seqs
(defn xors [max-x max-y]
  (for [x (range max-x) y (range max-y)]
    [x y (bit-xor x y)]))

(xors 2 2)
