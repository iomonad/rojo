(ns rojo.methods.subreddit
  "Subreddit related methods"
  (:require [rojo.client :as client]
            [rojo.utils :as utils]))

(defn ^:private sformat [^String sub]
  "Format subreddit route"
  (str "https://reddit.com/r/" sub "/"))


(defn ^:public about
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return informations about subreddit"
  (-> (client/get-request
       credentials
       (client/format-call
        "/about.json" :base (sformat sub)))
      :data))


(defn ^:public rules
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return subreddit rules"
  (-> (client/get-request
       credentials
       (client/format-call
        "/rules.json" :base (sformat sub)))
      :data))


(comment
  "Private post listing internals")

(defn ^:private new
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return subreddit new posts"
  (-> (client/get-request
       credentials
       (client/format-call
        "/new.json" :base (sformat sub)))
      :data :children
      (utils/parse-posts)))


(defn ^:private top
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return subreddit top posts"
  (-> (client/get-request
       credentials
       (client/format-call
        "/top.json" :base (sformat sub)))
      :data :children
      (utils/parse-posts)))


(defn ^:private hot
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return subreddit hot posts"
  (-> (client/get-request
       credentials
       (client/format-call
        "/hot.json" :base (sformat sub)))
      :data :children
      (utils/parse-posts)))


(defn ^:private rising
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return subreddit rising posts"
  (-> (client/get-request
       credentials
       (client/format-call
        "/rising.json" :base (sformat sub)))
      :data :children
      (utils/parse-posts)))


(defn ^:private controversial
  [credentials & {:keys [sub] :or {sub "all"}}]
  "Return subreddit controversial posts"
  (-> (client/get-request
       credentials
       (client/format-call
        "/controversial.json" :base (sformat sub)))
      :data :children
      (utils/parse-posts)))


(defn ^:public list [creds & {:keys [sub sort_by]
                              :or {sub "all" sort_by :top}}]
  "Return listing of the subreddit posts, depending
   the sort_by optional parameter."
  (case sort_by
    :new (rojo.methods.subreddit/new creds :sub sub)
    :top (top creds :sub sub)
    :hot (hot creds :sub sub)
    :rising (rising creds :sub sub)
    :controversial (controversial creds :sub sub)
    (top creds :sub sub)))


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
