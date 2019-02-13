(ns rojo.methods.stream
  (:require [rojo.client :as client]
            [rojo.utils :as utils]
            [rojo.methods.subreddit :as sub]
            [clojure.core.async
             :refer [<! >!! chan sliding-buffer go-loop]]))

(comment
  "Naive streaming implementation of
   Reddit's API using core.async and
   tick methodology.

   Linear execution:
   1 - Memoize first poll result and return
       first paginated elements.
   2 - In a recur loop, recompute API call
       and compare memoized results based
       on `preceded` elements.
   3 - On freshly memoized elements, broadcast
       thems to channels (our usable stream outlet)
   4 - Recur.
   5 - OPTIMISATION - Determine retention factor
       to reduce memory usage of the data structure.")

(def ^:private stdout-outlet
  (fn [x] (println (str "STDOUT - " x))))

(defn ^:public stream-posts
  [credentials &
   {:keys [interval sub callback]
    :or {interval 0.8 sub "all"
         callback stdout-outlet}}]
  "Return an infinite stream of new post
   for the targed subreddit. The function
   attach a callback function, default is
   stdoud, refered as `stdoud-outlet`"
  (let [sem (chan (sliding-buffer 1))
        cbk (go-loop []
              (let [x (<! sem)]
                (callback x)) ; Callback call on message
              (recur))]
    (while true
      (let [payload (sub/list-posts
                     credentials
                     :sub sub :limit 25)]
        (>!! sem payload)
        (Thread/sleep
         (* interval 1000))))))

(defn ^:public stream-comment
  [credentials ^String id &
   {:keys [interval callback]
    :or {interval 0.8
         callback stdout-outlet}}]
  "Return an infinite stream of new comments
   for the targed post id. The function
   attach a callback function, default is
   stdoud, refered as `stdoud-outlet`")