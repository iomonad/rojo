(ns rojo.client
  "Request client namespace"
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
            [slingshot.slingshot :refer :all]
            [rojo.constant :refer :all]
            [rojo.utils :as u])
  (:gen-class))

(defn format-call
  [^String res & {:keys [base] :or {base api-basename}}]
  "Format correct URL API call"
  (str base res))

(defn request-token [creds]
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

(defn get-request [token target & {:keys [p] :or {p {}}}]
  "Compute GET API call with authentificated token header"
  (try+
   (-> (client/get target
                   {:basic-auth [(:access-token token)]
                    :headers {"User-Agent" *ua*}
                    :socket-timeout 10000
                    :conn-timeout 10000
                    :form-params p
                    :content-type :json
                    :accept :json
                    :as :json})
       (get :body))
   (catch [:status 401] {}
     (throw (ex-info "Invalid server response - Check your creds"
                     {:causes :unauthorized})))))

(defn post-request [token target & {:keys [p] :or {p {}}}]
  "Compute POST API call with authentificated token header"
  (try+
   (-> (client/post target
                    {:basic-auth [(:access-token token)]
                     :headers {"User-Agent" *ua*}
                     :socket-timeout 10000
                     :conn-timeout 10000
                     :form-params p
                     :accept :json
                     :as :json})
       ;(get :body)
       )
   (catch [:status 400] {}
     (throw (ex-info "Invalid server response"
                     {:causes :server-error})))
   (catch [:status 301] {}
     (throw (ex-info "Moved permanently"
                     {:causes :moved-permanently})))))
