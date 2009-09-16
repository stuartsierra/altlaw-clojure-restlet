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

(deftest t-make-server
  (let [component (make-component 13999 (make-dummy-app))]
    (.start component)
    (is (= "Hello, World!"
           (IOUtils/toString
            (.openStream (URL. "http://127.0.0.1:13999/")))))
    (stop-component component)))
