(ns com.leaf.storm.clojure.DemoClojureScript
  (:gen-class)
 )
(defn hello-word [x]
      (println str "I am clojureScript,hello" x ))
(hello-word ["ann"])