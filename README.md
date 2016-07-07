# immutant-hystrix-event-stream

Hystrix Metrics Event Stream ring handler for immutant

## Usage

```clojure
(ns hystrix-stream-clj.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [com.netflix.hystrix.core :refer [defcommand]]
            [immutant-hystrix-event-stream.core :as hystrix]
            [immutant.web :refer :all]))

(defcommand hello
  "Safe hello!"
  []
  "Hello world!")

(defroutes app-routes
  (GET "/" [] (hello))
  (GET "/hystrix.stream" [] hystrix/stream)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn -main [& args]
  (run app {:host "localhost" :port 8080 :path "/"}))
```

## License

Copyright © 2016 Eunmin Kim

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
