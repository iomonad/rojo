# Rojo [![Dependencies Status](https://jarkeeper.com/iomonad/rojo/status.svg)](https://jarkeeper.com/iomonad/rojo) [![Clojars Project](https://img.shields.io/clojars/v/rojo.svg)](https://clojars.org/rojo) [![GitHub release](https://img.shields.io/github/release/iomonad/rojo.svg)](https://github.com/iomonad/rojo)
<a href="https://github.com/iomonad/rojo"><img
  src="http://i.imgur.com/sdO8tAw.png" alt="Reddit"
  width="80" height="80" align="right"></a> 

 > Simple and idiomatic Reddit API wrapper. 

## Usage

Rojo is available from Clojars.org. To use it, add the following as a dependency in your favorite build tool.

```clojure
[rojo $VERSION]
```
# Basic

In a first time, you should [generate](https://old.reddit.com/prefs/apps/) your credentials.
Once created, you are be able to instanciate your first client:

```clojure
(def creds {:user-client "44tt[..]"
            :user-secret "MckC06DXMynW-[..]"})

(let [token (client/request-token
              creds)]
(sub/search token :query "cats"))
```

## License

Copyright &copy; iomonad.

Distributed under the Eclipse Public License, the same as Clojure.
