(ns org.altlaw.util.restlet.test-status-service-dev
  (:require org.altlaw.util.restlet.status-service-dev)
  (:use org.altlaw.util.restlet.server
        clojure.test)
  (:import (org.restlet Restlet Application)
           (org.restlet.resource StringRepresentation)
           (org.restlet.data Request Response Method)
           (org.apache.commons.io IOUtils)
           (java.net URL)))

(defn make-error-restlet []
  (proxy [Restlet] []
    (handle [request response]
            (.setEntity response
                        (StringRepresentation. "Hello, World!"))
            (throw (Exception. "This is the error.")))))

(defn make-status-service []
  (doto (.. Class
            (forName "org.altlaw.util.restlet.status_service_dev")
            newInstance)
    (.setOverwrite true)))

(defn make-error-app []
  (doto (Application.)
    (.setRoot (make-error-restlet))
    (.setStatusService (make-status-service))))

(deftest t-status-service-dev
  (let [app (make-error-app)
        request (Request. Method/GET "http://host/")
        response (Response. request)]
    (.handle app request response)
    (is (= 500 (.. response getStatus getCode)))
    (is (.contains (.. response getEntity getText)
                   "This is the error."))))
