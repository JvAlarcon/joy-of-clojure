(ns joy-of-clojure.chapter-five)

(def ds (into-array [:willie :barnabas :adam]))
(seq ds)
(aset ds 1 :quentin)
(seq ds)

(def ds [:willie :barnabas :adam])
ds
(def ds1 (replace {:barnabas :quentin} ds))
ds
ds1

(= [1 2 3] '(1 2 3)) ; => true
(= [1 2 3] #{1 2 3}) ; => false

(vec (range 10))

(let [my-vector [:a  :b :c]]
  (into my-vector (range 10)))

(into (vector-of :int) [Math/PI 2 1.3])

(into (vector-of :char) [100 101 102 245])

(into (vector-of :int) [1 2 623879184509187538351714814091290])

(def a-to-j (vec (map char (range 65 75))))
a-to-j

(nth a-to-j 4)
(get a-to-j 4)
(a-to-j 4)

(seq a-to-j)
(rseq a-to-j)

(assoc a-to-j 3 "No longer D")
(replace {2 :a, 4 :b} [1 2 3 2 3 4 1 3 4])

(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])
(get-in matrix [1 2])
(get-in matrix [0 2])
(get-in matrix [2 0])

(assoc-in matrix [1 2] 'x)

(update-in matrix [1 2] * 100)

(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
                        size
                        yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %))
                deltas))))

(neighbors 3 [1 1])

(def my-stack [1 2 3])
(peek my-stack)
(pop my-stack)
(conj my-stack 4)
(+ (peek my-stack) (peek (pop my-stack)))

(subvec a-to-j 3 6)

(first {:width 10, :height 20, :depth 15})
(vector? (first {:width 10, :height 20, :depth 15}))
(doseq [[dimension amount] {:width 10, :height 20, :depth 15}]
  (println (str (name dimension) ":") amount "inches"))


(defmethod print-method clojure.lang.PersistentQueue
  [q, w]
  (print-method '<- w)
  (print-method (seq q) w)
  (print-method '-< w))
clojure.lang.PersistentQueue/EMPTY

(def schedule
  (conj clojure.lang.PersistentQueue/EMPTY
        :wake-up :shower :brush-teeth))
schedule
(peek schedule)
(pop schedule)
(rest schedule) ; don't use that for queue
