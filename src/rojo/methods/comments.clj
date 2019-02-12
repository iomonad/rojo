(ns rojo.methods.comments
  "Comments related methods"
  (:require [rojo.client :as client]
            [rojo.utils :as utils]
            [slingshot.slingshot :only [try+]]))

(defn ^:public comments
  [credentials ^String id
   & {:keys [limit] :or {limit nil}}]
  "List comments from submission ID"
  (try+
   (let [target (if (nil? limit)
                  (str "comments/" id ".json")
                  (str "comments/" id ".json?limit=" limit))]
     (-> (client/get-request
          credentials (client/format-call
                       target :base "https://reddit.com/")
          :p {})
         (utils/parse-comments)))
   (catch [:status 404] {}
     (throw (ex-info "Comment don't exists."
                     {:causes :not-found})))))

(comment
  "Work in progress comment posting
   method. Will make an more idiomatic
   dispatcher"
  (defn ^:public post
    [credentials ^String thing
     ^String message]
    "Post comment reply on reddit entity,
   see: https://www.reddit.com/dev/api/#fullnames"
    {:pre [(not-empty thing)
           (not-empty message)]}
    (try+
     (-> (client/post-request
          credentials (client/format-call
                       "/api/comment" :base "https://reddit.com/"))
         :p {:parent thing
             :text message})
     (catch [:status 404] {}
       (throw (ex-info "Thing don't exists."
                       {:causes :not-found})))
     (catch [:status 501] {}
       (throw (ex-info "Unauthorized"
                       {:causes :unauthorized}))))))
