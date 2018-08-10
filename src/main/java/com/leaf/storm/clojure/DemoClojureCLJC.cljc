(ns com.leaf.storm.clojure.DemoClojureCLJC
    (:gen-class)
    )
(defn hello-world [x]
  (str "I am DemoClojureCLJC,hello " x))
(print (hello-world "ann"))