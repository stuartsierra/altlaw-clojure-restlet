(ns org.altlaw.util.restlet.test-server
  (:use org.altlaw.util.restlet.server
        clojure.test)
  (:import (org.restlet Restlet)
           (org.restlet.resource StringRepresentation)
           (org.apache.commons.io IOUtils)
           (java.net URL)))

(defn make-dummy-app []
  (proxy [Restlet] []
    (handle [request response]
            (.setEntity response
                        (StringRepresentation. "Hello, World!")))))

(defn check-response []
  (is (= "Hello, World!"
         (IOUtils/toString  ;; quick & dirty HTTP client
          (.openStream (URL. "http://127.0.0.1:13999/"))))))

(deftest t-make-server
  (let [component (make-component 13999 (make-dummy-app))]
    (.start component)
    (check-response)
    (stop-component component)))

(deftest t-with-server
  (with-server 13999 (make-dummy-app)
    (check-response)))
