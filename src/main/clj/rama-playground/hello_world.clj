(ns rama-playground.hello-world
  (:import
   (com.rpl.rama Depot RamaModule)
   (com.rpl.rama.ops Ops)
   (com.rpl.rama.impl NativeAnyArityRamaFunction Util)
   (com.rpl.rama.test InProcessCluster LaunchConfig)))

;; https://github.com/redplanetlabs/rama-examples/blob/master/src/main/java/rama/examples/tutorial/HelloWorldModule.java


(defn println$* [& args]
  (apply println "!" args)
  args)


(defn ->rama-fn [var$]
  (let [s (symbol var$)]
    (NativeAnyArityRamaFunction. (com.rpl.rama.impl.Util/getVarContents (namespace s) (name s)))))


#_ {:clj-kondo/ignore true}
(deftype HelloWorldModule []
  RamaModule
  (define [_ setup topologies]
    (.declareDepot setup "*depot" (Depot/random))
    (let [s (.stream topologies "s")]
      (-> s
          (.source "*depot")
          (.out (into-array String ["*data"]))
          ;; (.each Ops/PRINTLN "*data")
          (.each (->rama-fn #'println$*) "*data")))))




(with-open [cluster (InProcessCluster/create)]
  (.launchModule cluster (eval '(HelloWorldModule.)) (LaunchConfig. 1 1))
  (let [module-name (.getName HelloWorldModule)
        depot       (.clusterDepot cluster module-name "*depot")]
    (doseq [i (range 10)]
      (.append depot i))))
