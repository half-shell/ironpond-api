(ns ironpond-api.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.log :as log]
            [ring.util.response :as ring-resp]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  (ring-resp/response "Hello World!"))

(defn game
  [request]
  (log/spy request)
  (ring-resp/response {:status 200 :body "OK"}))

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/log-request])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/about" :get (conj common-interceptors `about-page)]
              ["/game" :post (conj common-interceptors `game)]})

(def service {:env :prod
              ::http/routes routes
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})

;; For testing purposes
;; (require '[io.pedestal.test :refer [response-for]])
;; (def tempserver (::server/service-fn (server/create-servlet service/service)))
;; Note that the body does not seem to be correctly passed along
;; (response-for tempserver :post "/game?bli=45" :body "i like trains" :headers {"Content-Type" "text/plain"})
