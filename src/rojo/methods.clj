(ns rojo.methods
  "Method related namespace"
  (:require [rojo.client :as client]
            [rojo.methods.subreddit :as sub]
            [rojo.methods.search :as search]))

(let [token (client/request-token
              {:user-client "44ttWVuhyJ7S8w"
               :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (->> (search/search token :query "HPPD" :limit 100)
       (map :post)))

(let [token (client/request-token
              {:user-client "44ttWVuhyJ7S8w"
               :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (->> (sub/list-posts token :sub "aww" :limit 5 :sort_by :controversial)
       (map :post)))
