(ns org.altlaw.util.restlet.test-status-service-dev
  (:require org.altlaw.util.restlet.status-service-dev)
  (:use org.altlaw.util.restlet.server
        clojure.test)
  (:import (org.restlet Restlet)
           (org.restlet.resource StringRepresentation)
           (org.apache.commons.io IOUtils)
           (java.net URL)))

(defn make-error-app []
  (proxy [Restlet] []
    (handle [request response]
            (.setEntity response
                        (StringRepresentation. "Hello, World!"))
            (throw (Exception. "This is the error.")))))

(defn check-response []
  (let [response (IOUtils/toString ;; quick & dirty HTTP client
                  (.openStream (URL. "http://127.0.0.1:13999/")))]
    (is (.contains response "HTTP 500"))
    (is (.contains response "This is the error."))))

(deftest t-server-status-dev
  (let [component (make-component 13999 (make-error-app))]
    (.setStatusService
     component
     (.. Class
         (forName "org.altlaw.util.restlet.status_service_dev")
         newInstance))
    (.start component)
    (check-response)
    (stop-component component)))
