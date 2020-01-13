(ns newrelic-sample.core
  (:require [aleph.http :as http]
            [newrelic-sample.handler :refer [app]]
            [new-reliquary.ring :refer [wrap-newrelic-transaction]]
            [new-reliquary.core :refer [with-newrelic-transaction]]
            [clojure.core.async :refer [go-loop <! timeout]])
  (:import com.newrelic.api.agent.Trace))


(defn ^{Trace {:dispatcher true}} test-fn []
  (rand-int 100))

(defn -main []
  (go-loop []
    (<! (timeout 100))
    (with-newrelic-transaction test-fn)
    (recur))
  (http/start-server (wrap-newrelic-transaction app) {:port 9876}))
