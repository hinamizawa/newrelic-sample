(ns newrelic-sample.core
  (:require [aleph.http :as http]
            [newrelic-sample.handler :refer [app]]
            [new-reliquary.ring :refer [wrap-newrelic-transaction]]
            [new-reliquary.core :refer [with-newrelic-transaction]]
            [new-reliquary.trace :refer [defn-traced]]
            [clojure.core.async :refer [go-loop <! timeout]])
  (:import com.newrelic.api.agent.Trace))


(defn-traced sub-test-fn
  [num]
  (* num num))

(defn -main []
  (go-loop []
    (<! (timeout 100))
    (sub-test-fn (rand-int 100))
    (recur))
  (http/start-server (wrap-newrelic-transaction app) {:port 9876}))
