(ns rojo.client
  "Request client namespace"
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
            [slingshot.slingshot :refer :all]
            [rojo.utils :as utils]))

(def ^:private *ua*
  "Client User Agent"
  "http.clj/+io.trosa.rojo")

(defn ^:private request-token [creds]
  "Retrieve access token from the API"
  (try+
   (-> (client/post "https://www.reddit.com/api/v1/access_token"
                    {:basic-auth [(:user-client creds) (:user-secret creds)]
                     :headers {"User-Agent" *ua*}
                     :form-params {:grant_type "client_credentials"
                                   :device_id (str (java.util.UUID/randomUUID))}
                     :content-type "application/x-www-form-urlencoded"
                     :socket-timeout 10000
                     :conn-timeout 10000
                     :as :json})
       (get :body))
   (catch [:status 401] {}
     (throw (ex-info "Invalid credentials."
                     {:causes :unauthorized})))))

(defn ^:private get-request [token target]
  "Compute GET API call with authentificated token header"
  (try+
   (-> (client/get target
                   {:basic-auth [(:access-token token)]
                    :headers {"User-Agent" *ua*}
                    :socket-timeout 10000
                    :conn-timeout 10000
                    :as :json})
       (get :body))
   (catch [:status 400] {}
     (throw (ex-info "Invalid server response"
                     {:causes :server-error})))))

(defn ^:private post-request [token target]
  "Compute POST API call with authentificated token header"
  (try+
   (-> (client/post target
                    {:basic-auth [(:access-token token)]
                     :headers {"User-Agent" *ua*}
                     :socket-timeout 10000
                     :conn-timeout 10000
                     :as :json})
       (get :body))
   (catch [:status 400] {}
     (throw (ex-info "Invalid server response"
                     {:causes :server-error})))))

(defn ^:private stream-request [token target]
  "Stream JSON API as lazy sequence (stream)."
  (try+
   (-> (client/get target
                   {:basic-auth [(:access-token token)]
                    :headers {"User-Agent" *ua*}
                    :socket-timeout 10000
                    :conn-timeout 10000
                    :as :stream})
       (get :body))
   (catch [:status 400] {}
     (throw (ex-info "Invalid server response"
                     {:causes :server-error})))))
