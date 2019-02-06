(ns rojo.client
  "Request client namespace"
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
            [slingshot.slingshot :refer :all]
            [rojo.utils :as u]))

(def ^:private *ua*
  "Client User Agent"
  "http.clj/+io.trosa.rojo")

(def ^:private *api-basename*
  "API url basename"
  "https://reddit.com/api/v1")

(defn format-call [^String res]
  "Format correct URL API call"
  (str *api-basename* res))

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
                     :content-type :json
                     :as :json})
       (get :body))
   (catch [:status 400] {}
     (throw (ex-info "Invalid server response"
                     {:causes :server-error})))))

(defn stream-request [token target]
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
(comment
  (defmacro reddit-method [name creds req limit]
    `(def ~name
       (fn []
         (if (u/valid-rate? ~limit)
           (-> (get-request ~creds
                            (str ~req "?limit=" ~limit))
               (println)))))))
