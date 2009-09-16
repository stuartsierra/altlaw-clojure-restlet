(ns org.altlaw.util.restlet.server
  (:import (org.restlet Component)
           (org.restlet.data Protocol)))

(defn make-component
  "Creates and returns an new Component instance with HTTP and FILE
  connectors, set to run on the specified port.  Attaches app, a
  Restlet instance, to the default VirtualHost."
  [port app]
  (let [com (Component.)]
    (.. com getServers (add Protocol/HTTP port))
    (.. com getClients (add Protocol/FILE))
    (let [child-context (.createChildContext (.getContext com))]
      (.setContext app child-context))
    (.. com getDefaultHost (attach app))
    com))

(defn stop-component
  "Stops the Component c and ensures it releases all ports."
  [c]
  (.stop c)
  ;; make sure the server port is released
  (.. c getServers clear)
  (Thread/sleep 1000))

(defmacro with-server
  "Starts a Component running on port with app as the default Restlet,
  runs body, then shuts down the Component."
  [port app & body]
  `(let [server# (make-component ~port ~app)]
     (.start server#)
     (try ~@body
          (finally (stop-component c)))))
