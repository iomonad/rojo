(ns rojo.methods.search
  "Subreddit related methods"
  (:require [rojo.client :as client]
            [rojo.utils :as utils]))

(defn ^:public search
  [credentials & {:keys [query exact include_over_18]
                  :or {query "" ; Empty resulting nil fields
                       exact false
                       include_over_18 true}}]
  "Compute search request"
  (-> (client/get-request
       credentials
       (client/format-call
        (str "search.json?q=" query) :base "https://reddit.com/")
       :p {:exact exact
           :include_over_18 include_over_18})
      :data :children
      (utils/parse-posts)))
