(ns rojo.methods
  "Method related namespace"
  (:require [rojo.methods.account :refer :all]))

(let [token (client/request-token
             {:user-client ""
              :user-secret ""})]
  (karma token))
