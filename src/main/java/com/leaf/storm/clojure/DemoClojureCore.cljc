(ns com.leaf.storm.clojure.DemoClojureCore
    (:gen-class
      :methods [#^{:static true} [hello [String] String]]))

(defn -hello
      "Say Hello."
      [x]
      (str "Hello " x))
(defn -main [] (println "Hello from main"))



