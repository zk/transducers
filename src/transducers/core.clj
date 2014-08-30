(ns transducers.core)

(defn mean-reducer [memo x]
     (-> memo
      (update-in [:sum] + x)
      (update-in [:count] inc)))

(reduce mean-reducer {:sum 0 :count 0} (range 10))
;; => {:count 10, :sum 45}

(defn increment-transducer [f1]
  (fn [result input]    ;; A transducer returns a reducing fn.
    (f1 result          ;; That reducing fn will still call 'f1',
      (inc input))))    ;; but only after it increments the input.

(defn identity-xducer [f]
  (fn [result input]
    (f result (identity input))))

(defn fitler-xducer [f]
  (fn [result input]
    (f result)))

(reduce (increment-transducer mean-reducer)
  {:sum 0 :count 0}
  (range 10))

;; => {:count 10, :sum 55}

;; Oh, so it gives you the ability to **affect the input before the
;; reducing function is called**. How is this different than chaining
;; calls together?

;; In scope for a transducer: Reducing function, result, and single
;; input element.

;; Affect a single element and the result single, not on the
;; collection as a whole, allows use of same code in the lazy seqs and
;; channels.

;; I get why it's useful to separate operations on a collection from
;; the actually collection (and output) itself, but it seems like more
;; ceremony. Is it better overall to name the pipeline of operations
;; we create?

;; `sequence` and `transduce` functions to 'use' transducersxms


;; Questions

;; * What happens when you don't run the reducing function?
;; * Can you "skip" elements? Probably not.
