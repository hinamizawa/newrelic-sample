(ns newrelic-sample.core
  (:require [aleph.http :as http]
            [newrelic-sample.handler :refer [app]]
            [clojure.core.async :refer [go-loop <! timeout]])
  (:import com.newrelic.api.agent.Trace))


(defn ^{Trace {:dispatcher true}} test-fn []
  (rand-int 100))

(defn -main []
  (go-loop []
    (<! (timeout 100))
    (test-fn)
    (recur))
  (http/start-server app {:port 9876}))
