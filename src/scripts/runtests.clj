(ns runtests
  (:use clojure.test)
  (:require org.altlaw.util.restlet.test-server))

(run-all-tests #"^org\.altlaw\..*$")
