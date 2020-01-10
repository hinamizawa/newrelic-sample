(ns newrelic-sample.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [aleph.http :as http]
            [new-reliquary.ring :refer [wrap-route-with-newrelic-transaction]]))

(s/defschema Result
  {:total s/Int
   :roll  [s/Int]})

(s/defschema Pizza
  {:name                         s/Str
   (s/optional-key :description) s/Str
   :size                         (s/enum :L :M :S)
   :origin                       {:country (s/enum :FI :PO)
                                  :city    s/Str}})

(defn roll-dice [n s]
  (let [roll (repeatedly n (fn* [] (inc (rand-int s))))]
    {:total (reduce + roll)
     :roll  roll}))

(s/defschema Request
  {:num   s/Int
   :sides s/Int})

(defroutes partial-routes
  (GET "/roll/:n/:s" []
    :path-params [n :- s/Int
                  s :- s/Int]
    :return Result
    :summary "Rolls :n dice of sides :s"
    :middleware [wrap-route-with-newrelic-transaction]
    (ok (roll-dice n s))))

(def one
  (api
    {:swagger
     {:ui   "/"
      :spec "/swagger.json"
      :data {:info {:title       "My-api"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/api" []
      :tags ["api"]

      partial-routes

      (POST "/roll" []
        :body [{:keys [num sides]} Request]
        :return Result
        :summary "Given a correct request body with keys :num and :sides, returns result of roll"
        :middleware [wrap-route-with-newrelic-transaction]
        (ok (roll-dice num sides))))

    (context "/another" []
      :tags ["another"]

      (GET "/plus" []
        :return {:result Long}
        :query-params [x :- Long, y :- Long]
        :summary "adds two numbers together"
        (ok {:result (+ x y)}))

      (POST "/echo" []
        :return Pizza
        :body [pizza Pizza]
        :summary "echoes a Pizza"
        (ok pizza)))))

(def two
  (api
    {:swagger
     {:ui   "/"
      :spec "/swagger2.json"
      :data {:info {:title       "My-api2"
                    :description "Compojure Api2 example"}
             :tags [{:name "api2", :description "some apis2"}]}}}

    (context "/api2" []
      :tags ["api2"]

      (POST "/roll2" []
        :body [{:keys [num sides]} Request]
        :return Result
        :summary "::Given a correct request body with keys :num and :sides, returns result of roll"
        (ok (roll-dice num sides))))))

(def app
  (routes one two))
