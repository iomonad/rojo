(ns rojo.methods
  "Method related namespace"
  (:require [rojo.client :as client]
            [rojo.methods.subreddit :as sub]
            [rojo.methods.search :as search]
            [rojo.methods.comments :as comment]
            [rojo.methods.stream :as stream]))

(comment
  "Currently a test namespace, will be removed
   on valid release.")

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (map :body (-> (comment/comments token "apg9qv")
                 :comments)))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (sub/list-posts token :sub "test"))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (stream/newly-pushed (sub/list-posts token :sub "test")
                       "t3_aq5ozu"))

(defn stream-callback [coll]
  (doseq [i coll]
    (println (str "NEW POST: " (:post i)))))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (stream/stream-posts token :interval 1 :callback stream-callback :sub "test"))
