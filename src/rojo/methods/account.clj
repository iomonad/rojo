(ns rojo.methods.account
  "Account methods related namespace"
  (:require [rojo.client :as client]))

(defn ^:public me [credentials]
  "Returns the identity of the user"
  (-> (client/get-request
       credentials
       (client/format-call "/me"))))

(defn ^:public blocked [credentials]
  "Returns the identity of the user"
  (-> (client/get-request
       credentials
       (client/format-call "/me/blocked"))))

(defn ^:public friends [credentials]
  "Returns friends of the user"
  (-> (client/get-request
       credentials
       (client/format-call "/me/friends"))))

(defn ^:public karma [credentials]
  "Returns the karma of the user"
  (-> (client/get-request
       credentials
       (client/format-call "/me/karma"))))
