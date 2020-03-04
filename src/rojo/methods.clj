(ns rojo.methods
  "Method related namespace"
  (:require [rojo.client :as client]
            [rojo.methods.subreddit :as sub]
            [rojo.methods.search :as search]
            [rojo.methods.comments :as comment]
            [rojo.methods.stream :as stream]))

(defn stream-callback [coll]
  (doseq [i coll]
    (println (str "NEW POST ->> " (:post i)))))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (stream/stream-posts token :interval 3 :callback stream-callback :sub "all"))
