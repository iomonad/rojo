(ns rojo.methods.search
  "Subreddit related methods"
  (:require [rojo.client :as client]
            [rojo.utils :as utils]))

(defn ^:public search
  [credentials & {:keys [query exact include_over_18 limit]
                  :or {query "" ; Empty resulting nil fields
                       exact false
                       include_over_18 true
                       limit 25}}]
  {:pre [(utils/valid-limit? limit)]}
  "Compute search request from query string
   like webapp internal search bar, with pagination
   support and NSFW tags filtering."
  (-> (client/get-request
       credentials
       (client/format-call
        (str "search.json?limit=" limit "&q=" query)
          :base "https://reddit.com/")
       :p {:exact exact
           :include_over_18 include_over_18})
      :data :children
      (utils/parse-posts)))
