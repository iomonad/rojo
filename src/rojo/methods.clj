(ns rojo.methods
  "Method related namespace"
  (:require [rojo.client :as client]))

(defn account-me [credentials]
  "Returns the identity of the user"
  (-> (client/get-request
       credentials
       (client/format-call "/me"))))

(defn account-karma [credentials]
  "Returns the identity of the user"
  (-> (client/get-request
       credentials
       (client/format-call "/me/karma"))))

(let [token (client/request-token
             {:user-client ""
              :user-secret ""})]
  (account-karma token))
