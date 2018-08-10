(com.leaf.storm.clojure.DemoClojure
  (:gen-class
    :methods [#^{:static true} [hello [String] String]]))

(defn -hello
  "Say Hello."
  [x]
  (str "Hello " x))
(defn -main [] (println "Hello from main"))