(ns rojo.utils
  "Library utils.")

(defn ^:private transduce-post [coll]
  "Reformat API response to post ID as key"
  (into (sorted-map)
        {:post (get-in coll [:name])
         :content (dissoc coll :name)}))

(defn ^:public parse-posts [coll]
  "Parse response into pretty data structure"
  (let [raw (map #(get-in % [:data])
                 coll)]
    (map transduce-post raw)))

(defn ^:public parse-comments [coll]
  (let [post (first coll)
        comments (last coll)]
    (into (sorted-map)
          {:post (first (mapv :data(-> post :data :children)))
           :comments (map :data (-> comments :data :children))})))

(defn ^:public valid-limit?
  [^Integer limit]
  "Trivial predicate for limit
   interval checking"
  (<= 0 limit 100))
