# Rojo [![Dependencies Status](https://jarkeeper.com/iomonad/rojo/status.svg)](https://jarkeeper.com/iomonad/rojo) [![Clojars Project](https://img.shields.io/clojars/v/rojo.svg)](https://clojars.org/rojo) [![GitHub release](https://img.shields.io/github/release/iomonad/rojo.svg)](https://github.com/iomonad/rojo)
<a href="https://github.com/iomonad/rojo"><img
  src="http://i.imgur.com/sdO8tAw.png" alt="Reddit"
  width="80" height="80" align="right"></a> 

 > Simple and idiomatic Reddit API wrapper. 

## Usage

Rojo is available from Clojars.org. To use it, add the following as a dependency in your favorite build tool.

#### Leiningen/Boot
```
[rojo "0.x.x"]
```
#### Clojure CLI/deps.edn
```
rojo {:mvn/version "0.x.x"}
```
#### Gradle
```
compile 'rojo:rojo:0.x.x'
```
#### Maven
```
<dependency>
  <groupId>rojo</groupId>
  <artifactId>rojo</artifactId>
  <version>0.x.x</version>
</dependency>
```

# Basic

In a first time, you should [generate](https://old.reddit.com/prefs/apps/) your credentials.
Once created, you are be able to instanciate your first client:

```clojure
(def creds {:user-client "44tt[..]"
            :user-secret "MckC06DXMynW-[..]"})

(let [token (client/request-token
              creds)]
 ; Use you token with methods 
 )           
```

### Result data structure

Posts result list is transduced for convenient hash manipulation:

```clojure
[
  { :content { :banned_by , ... }, :post "t3_ap9ran" }
  { :content { :banned_by , ... }, :post "t3_apf8je" }
  { :content { :banned_by , ... }, :post "t3_ap4day" }
  { :content { :banned_by , ... }, :post "t3_apfbl8" }
  ...
]
```

### Searching

```clojure
(ns foobar
  (:require [rojo.methods.search :as s]))

(s/search token :query "lisp" :limit 50)
;; [{:content {...}, :post "t3_ap4dap} ...]
```
Note that all key paramaters are mandatory, using
fallback values, like 25 for limit.

### Subreddit

```clojure
(ns foobar
  (:require [rojo.methods.subreddit :as s]))
  
(s/list-posts token :sub "programming" :limit 5)
```

### Streaming

```bash 
echo TODO
```

## License

Copyright &copy; iomonad.

Distributed under the Eclipse Public License, the same as Clojure.
