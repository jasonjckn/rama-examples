(ns main
  (:require
   [virgil]
   [clojure.reflect :as r])
  (:import
   (com.rpl.rama Depot RamaModule)
   (com.rpl.rama.ops Ops)
   (com.rpl.rama.test InProcessCluster LaunchConfig)))

(defn my-hook []
  (let [module (eval '(rama.examples.tutorial.HelloWorldModule.))]
    (time
      (.main module (into-array String [])))))

(defn -main [& _]
  (virgil/watch-and-recompile ["src/main/java"]
                              :verbose true
                              :post-hook my-hook))
