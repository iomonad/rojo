(ns rojo.methods
  "Method related namespace"
  (:require [rojo.client :as client]
            [rojo.methods.subreddit :as sub]
            [rojo.methods.search :as search]
            [rojo.methods.comments :as comment]
            [rojo.methods.stream :as stream]))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (map :body (-> (comment/comments token "apg9qv")
                 :comments)))

(defn stream-callback [coll]
  (println (str "GOT PAYLOAD: " (count coll))))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (stream/stream-posts token :interval 1 :callback stream-callback))
