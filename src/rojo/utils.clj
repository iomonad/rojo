(ns rojo.utils
  "Library utils.")

(defn ^:private transduce-post [coll]
  "Reformat API response to post ID as key"
  (into (sorted-map)
        {:post (get-in coll [:name])
         :content (dissoc coll :name)}))

(defn parse-posts [coll]
  "Parse response into pretty data structure"
  (let [raw (map #(get-in % [:data])
                 coll)]
    (map transduce-post raw)))
