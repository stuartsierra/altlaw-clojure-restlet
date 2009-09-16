(ns runtests
  (:use clojure.test)
  (:require org.altlaw.util.restlet.test-server
            org.altlaw.util.restlet.test-status-service-dev))

(run-all-tests #"^org\.altlaw\.util\.restlet.*$")
