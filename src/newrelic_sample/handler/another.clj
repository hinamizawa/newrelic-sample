(ns newrelic-sample.handler.another
  (:require
    [compojure.api.sweet :refer :all]
    [ring.util.http-response :refer :all]
    [schema.core :as s]))


(s/defschema Pizza
  {:name                         s/Str
   (s/optional-key :description) s/Str
   :size                         (s/enum :L :M :S)
   :origin                       {:country (s/enum :FI :PO)
                                  :city    s/Str}})

(defroutes plus
  (GET "/plus" []
    :return {:result Long}
    :query-params [x :- Long, y :- Long]
    :summary "adds two numbers together"
    (ok {:result (+ x y)})))

(defroutes test-api
  (GET "/test" []
    :return {:result Long}
    :query-params [num :- Long]
    :summary "test-api"
    (ok {:result (cond
                   (zero? num) (/ 1 0)
                   (= 1 num) (throw (Exception. "my exception message"))
                   (= 2 num) (throw
                               (ex-info "The ice cream has melted!"
                                        {:causes              #{:fridge-door-open :dangerously-high-temperature}
                                         :current-temperature {:value 25 :unit :celsius}}))
                   :else 42)})))

(defroutes echo
  (POST "/echo" []
    :return Pizza
    :body [pizza Pizza]
    :summary "echoes a Pizza"
    (ok pizza)))