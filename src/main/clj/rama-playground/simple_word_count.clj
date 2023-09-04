(ns rama-playground.simple-word-count
  (:import
   (com.rpl.rama Depot RamaModule)
   (com.rpl.rama.ops Ops)
   (com.rpl.rama.impl NativeAnyArityRamaFunction Util)
   (com.rpl.rama.impl AggImpl )

   (java.util Collections )
   (com.rpl.rama.test InProcessCluster LaunchConfig)
   (com.rpl.rama Agg CompoundAgg Depot PState Path RamaModule)
   (com.rpl.rama.test InProcessCluster LaunchConfig)))

(defn ->rama-fn [var$]
  (let [s (symbol var$)]
    (NativeAnyArityRamaFunction. (com.rpl.rama.impl.Util/getVarContents (namespace s) (name s)))))

(defn ->rama-agg [var$]
  (let [s (symbol var$)]
    (AggImpl. (com.rpl.rama.impl.Util/getVarContents (namespace s) (name s))
              (Collections/emptyList))))


;; https://github.com/redplanetlabs/rama-examples/blob/master/src/main/java/rama/examples/tutorial/SimpleWordCountModule.java

;; (alter-var-root #'*data-readers*
;;                 assoc 'rpl/vars #(if (string? %)
;;                                    (into-array String [%])
;;                                    (into-array String %)))
(defn _out [s & vars]
  (.out s (into-array String vars)))

;; (rpl.rama.api.api4/+count 1)
;; (require '[clojure.reflect :refer [reflect]])
;; (reflect rpl.rama.api.api4/+count)

;; (type rpl.rama.api.api4/+count)

;; ((:f rpl.rama.api.api4/+count) 1 nil 1)




(deftype SimpleWordCountModule []
  RamaModule
  (define [_ setup topologies]
    (.declareDepot setup "*depot" (Depot/random))
    (let [s (.stream topologies "s")]
      (.pstate s "$$wordCounts" (PState/mapSchema String Long))
      (-> s
          (.source "*depot")
          (_out "*token")
          (.hashPartition "*token")
          (.compoundAgg "$$wordCounts" (CompoundAgg/map
                                         (to-array ["*token" (Agg/count)])))))))

(println "-----")
(with-open [cluster (InProcessCluster/create)]
  (.launchModule cluster (eval (SimpleWordCountModule.)) (LaunchConfig. 1 1))
  (let [module-name (.getName SimpleWordCountModule)
        depot       (.clusterDepot cluster module-name "*depot")
        wc          (.clusterPState cluster module-name "$$wordCounts")]
    (doto depot
      (.append "one")
      (.append "two")
      (.append "two")
      (.append "three")
      (.append "three")
      (.append "three"))

    (println "one:" (.selectOne wc (Path/key (into-array String ["one"]))))
    (println "two:" (.selectOne wc (Path/key (into-array String ["two"]))))
    (println "three:" (.selectOne wc (Path/key (into-array String ["three"]))))))
