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
  [credentials limit & {:keys [sub] :or {sub "all"}}]
  "Return subreddit new posts"
  (-> (client/get-request
       credentials
       (client/format-call
        (str "/new.json?limit=" limit)
        :base (sformat sub)))
      :data :children
      (utils/parse-posts)))

(defn ^:private top
  [credentials limit & {:keys [sub] :or {sub "all"}}]
  "Return subreddit top posts"
  (-> (client/get-request
       credentials
       (client/format-call
        (str "/top.json?limit=" limit)
        :base (sformat sub)))
      :data :children
      (utils/parse-posts)))

(defn ^:private hot
  [credentials limit & {:keys [sub] :or {sub "all"}}]
  "Return subreddit hot posts"
  (-> (client/get-request
       credentials
       (client/format-call
        (str "/hot.json?limit=" limit)
        :base (sformat sub)))
      :data :children
      (utils/parse-posts)))

(defn ^:private rising
  [credentials limit & {:keys [sub] :or {sub "all"}}]
  "Return subreddit rising posts"
  (-> (client/get-request
       credentials
       (client/format-call
        (str "/rising.json?limit=" limit)
        :base (sformat sub)))
      :data :children
      (utils/parse-posts)))

(defn ^:private controversial
  [credentials limit & {:keys [sub] :or {sub "all"}}]
  "Return subreddit controversial posts"
  (-> (client/get-request
       credentials
       (client/format-call
        (str "/controversial.json?limit=" 25)
        :base (sformat sub)))
      :data :children
      (utils/parse-posts)))

(comment
  "External listing methods")

(defn ^:public list-posts
  [creds & {:keys [sub sort_by limit]
            :or {sub "all" sort_by :top
                 limit 25}}]
  "Return listing of the subreddit posts, depending
   the sort_by optional parameter."
  {:pre [utils/valid-limit? limit]}
  (case sort_by
    :new (rojo.methods.subreddit/new creds limit :sub sub)
    :top (top creds limit :sub sub)
    :hot (hot creds limit :sub sub)
    :rising (rising creds limit :sub sub)
    :controversial (controversial creds limit :sub sub)
    (top creds limit :sub sub)))
