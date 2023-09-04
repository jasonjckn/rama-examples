(ns main
  (:require
   [virgil]
   [rpl.rama.util.helpers]
   [clojure.reflect :as r])
  (:import
   (com.rpl.rama Depot RamaModule)
   (com.rpl.rama.ops Ops)
   (com.rpl.rama.test InProcessCluster LaunchConfig)))

(defn my-hook []
  (let [module
        (eval '(
                ;; rama.examples.tutorial.HelloWorldModule.
                ;; rama.examples.wordcount.WordCountModule.
                ;; rama.examples.tutorial.Test1.
                ;; rama.examples.tutorial.Test2.
                rama.examples.tutorial.Test3.
                ;; rama.examples.tutorial.PageAnalyticsModule.
                ))
        ]
    (time
      (.main module))))

(defn -main [& _]
  (virgil/watch-and-recompile ["src/main/java"] :verbose false :post-hook #'my-hook))

#_ (virgil/watch-and-recompile ["src/main/java"] :verbose false :post-hook #'my-hook)


;; (r/reflect
;;   rpl.rama.util.helpers/atomic-println)

;; (rpl.rama.util.helpers/atomic-println "dh")
