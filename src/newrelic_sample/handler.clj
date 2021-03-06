(ns newrelic-sample.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [aleph.http :as http]
            [newrelic-sample.handler.one :refer [roll roll-another]]
            [newrelic-sample.handler.another :refer [plus echo test-api]]))

(s/defschema Result
  {:total s/Int
   :roll  [s/Int]})

(defn roll-dice [n s]
  (let [roll (repeatedly n (fn* [] (inc (rand-int s))))]
    {:total (reduce + roll)
     :roll  roll}))

(s/defschema Request
  {:num   s/Int
   :sides s/Int})

(def one
  (api
    {:swagger    {:ui   "/"
                  :spec "/swagger.json"
                  :data {:info {:title       "My-api"
                                :description "Compojure Api example"}
                         :tags [{:name "api", :description "some apis"}]}}}

    (context "/api" []
      :tags ["api"]

      roll
      roll-another)

    (context "/another" []
      :tags ["another"]

      plus
      echo
      test-api)))

(def two
  (api
    (context "/api2" []
      :tags ["api2"]

      (POST "/roll2" []
        :body [{:keys [num sides]} Request]
        :return Result
        :summary "::Given a correct request body with keys :num and :sides, returns result of roll"
        (ok (roll-dice num sides))))))

(def app
  (routes one two))
