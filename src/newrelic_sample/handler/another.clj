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

(defroutes echo
  (POST "/echo" []
    :return Pizza
    :body [pizza Pizza]
    :summary "echoes a Pizza"
    (ok pizza)))