(ns immutant-hystrix-event-stream.core
  (:require [immutant.web.sse :as sse]
            [clojure.core.async :refer [chan >!! <!!]])
  (:import [com.netflix.hystrix.contrib.metrics.eventstream
            HystrixMetricsPoller
            HystrixMetricsPoller$MetricsAsJsonPollerListener]))

(def ^:private delay-msec 500)

(defn stream
  "Hystrix Metrics Event Stream ring handler for immutant"
  [request]
  (let [ch (chan)
        poller (HystrixMetricsPoller.
                 (proxy [HystrixMetricsPoller$MetricsAsJsonPollerListener] []
                   (handleJsonMetric [json]
                     (>!! ch json)))
                 delay-msec)]
    (.start poller)
    (sse/as-channel request
      {:on-open (fn [stream]
                  (loop []
                    (sse/send! stream (<!! ch))
                    (recur)))
       :on-close (fn [_ _]
                   (.shutdown poller))})))
