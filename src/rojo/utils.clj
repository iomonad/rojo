(ns rojo.utils
  "Library utils.")

(defn valid-rate? [rate]
  "Rate limit guard util"
  (if (and (integer? rate)
           (<= 1 rate)
           (>= 100 rate))
    rate
    (throw
     (ex-info "Invalid rate, must be between [1-100]."
              {:causes :invalid-rate}))))
