(defproject newrelic-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.6.532"]
                 [metosin/compojure-api "2.0.0-alpha30"]
                 [com.newrelic.agent.java/newrelic-api "5.9.0"]
                 [hinamizawa/new-reliquary "0.1.3"]]
  :ring {:handler my-api.handler/app}
  :uberjar-name "server.jar"
  :alias {"test" ^{:doc "call midje test"} ["midje"]}
  :java-agents [[com.newrelic.agent.java/newrelic-agent "5.9.0"]]
  :plugins [[com.pupeno/jar-copier "0.4.0"]
            [lein-ring "0.12.5"]]
  :prep-tasks ["javac" "compile" "jar-copier"]
  :jar-copier {:java-agents true
               :destination "resources/jars"}
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [ring/ring-mock "0.3.2"]
                                  [midje "1.8.3"]
                                  [aleph "0.4.6"]]
                   :plugins      [[lein-ring "0.12.5"]
                                  [lein-midje "3.2"]]}}
  :main newrelic-sample.core)
