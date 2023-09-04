package rama.examples.tutorial;

import com.rpl.rama.*;
import com.rpl.rama.impl.NativeAnyArityRamaFunction;
import com.rpl.rama.impl.Util;
import com.rpl.rama.module.*;
import com.rpl.rama.ops.Ops;
import com.rpl.rama.ops.OutputCollector;
import com.rpl.rama.ops.RamaFunction1;
import com.rpl.rama.ops.RamaOperation1;
import com.rpl.rama.test.*;

public class HelloWorldModule implements RamaModule {
    @Override
    public void define(Setup setup, Topologies topologies) {
        setup.declareDepot("*depot", Depot.random());
        StreamTopology s = topologies.stream("s");
//        NativeAnyArityRamaFunction PRINTLN =
//                new NativeAnyArityRamaFunction(Util.getVarContents(
//                        "rpl.rama.util.helpers",
//                        "atomic-println"));

                NativeAnyArityRamaFunction PRINTLN =
                        new NativeAnyArityRamaFunction( Util.getVarContents(
                                "rpl.rama.util.helpers",
                "atomic-println"));


        RamaFunction1<String, Object> f1 = new RamaFunction1<String, Object>() {

            @Override
            public Object invoke(String arg0) {
                System.out.println(arg0);
                return null;
            }
        };


        Block.Impl x = s.source("*depot").out("*data");
//        x.each(f1, "*data");
//        x.each(f1, "*data");
        x.each(Ops.PRINTLN, "*data");
        x.each(Ops.SIZE, "*data");

        // rpl.rama.util.helpers$atomic_println


    }

    public void main(String[] args) throws Exception {
        try (InProcessCluster cluster = InProcessCluster.create()) {
            cluster.launchModule(new HelloWorldModule(), new LaunchConfig(1, 1));

            String moduleName = HelloWorldModule.class.getName();
            Depot depot = cluster.clusterDepot(moduleName, "*depot");
            depot.append("Hello world!!");
            depot.append("2");
            depot.append("2");
            depot.append("2");
            depot.append("2");
            depot.append("2");
            depot.append("2");


        }
    }
}