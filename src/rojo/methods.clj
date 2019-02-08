(ns rojo.methods
  "Method related namespace"
  (:require [rojo.client :as client]
            [rojo.methods.subreddit :as sub]))

(let [token (client/request-token
             {:user-client "44ttWVuhyJ7S8w"
              :user-secret "MckC06DXMynW-Cdhfr2naP75eaU"})]
  (sub/search token :query "cats"))
