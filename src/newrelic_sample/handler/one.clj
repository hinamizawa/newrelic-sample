(ns newrelic-sample.handler.one
  (:require
    [compojure.api.sweet :refer :all]
    [ring.util.http-response :refer :all]
    [schema.core :as s]))

(s/defschema Result
  {:total s/Int
   :roll  [s/Int]})

(s/defschema Request
  {:num   s/Int
   :sides s/Int})

(defn- roll-dice [n s]
  (let [roll (repeatedly n (fn* [] (inc (rand-int s))))]
    {:total (reduce + roll)
     :roll  roll}))

(defroutes roll-another
  (GET "/roll/:n/:s" []
    :path-params [n :- s/Int
                  s :- s/Int]
    :return Result
    :summary "Rolls :n dice of sides :s"
    (ok (roll-dice n s))))

(defroutes roll
  (POST "/roll" []
    :body [{:keys [num sides]} Request]
    :return Result
    :summary "Given a correct request body with keys :num and :sides, returns result of roll"
    (ok (roll-dice num sides))))
