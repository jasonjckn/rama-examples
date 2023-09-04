package rama.examples.tutorial;

import com.rpl.rama.*;
import com.rpl.rama.module.*;
import com.rpl.rama.ops.Ops;
import com.rpl.rama.test.*;

import java.io.IOException;
import java.util.*;

public class Test2 implements RamaModule {

    @Override
    public void define(Setup setup, Topologies topologies) {

        setup.declareDepot("*ev-stream", Depot.random());

        StreamTopology st = topologies.stream("s");
        st.pstate("$$pagehit", PState.mapSchema(String.class, Long.class));
        st.pstate("$$sessionHistory",
                PState.mapSchema(
                        String.class,
                        PState.listSchema(PState.mapSchema(String.class, Object.class))));


        st.source("*ev-stream")
                .out("*ev")
                .each((Map m) -> m.get("path"), "*ev").out("*path")
                .each((Map m) -> m.get("sessionId"), "*ev").out("*session-id")
                .hashPartition("*path")
                .compoundAgg("$$pagehit", CompoundAgg.map("*path", Agg.count()))
                .hashPartition("*session-id")
                .compoundAgg("$$sessionHistory", CompoundAgg.map("*session-id", Agg.list("*ev")));




//                .each((Map<String, Object> visit) -> visit.get("path"), "*pageVisit").out("*path")

//        st.source("*events").out("*event")
//                .select("*event", Path.key("path"))
//                .out("*event-path")
//                .hashPartition("*event-path")
//                .compoundAgg("$$pagehit", CompoundAgg.map("*event-path", Agg.count()));

    }


    public void main() throws Exception {

        try (InProcessCluster cluster = InProcessCluster.create()) {
            cluster.launchModule(new Test2(), new LaunchConfig(2, 2));
            String moduleName = Test2.class.getName();

            Depot d = cluster.clusterDepot(moduleName, "*ev-stream");

            Map<String, Object> e1 = new HashMap<String, Object>();
            e1.put("sessionId", "abc123");
            e1.put("path", "/posts");
            e1.put("duration", 4200);
            d.append(e1, AckLevel.ACK);

            Map<String, Object> e2 = new HashMap<String, Object>();
            e2.put("sessionId", "zzz");
            e2.put("path", "/posts");
            e2.put("duration", 9200);
            d.append(e2, AckLevel.ACK);

            Map<String, Object> e3 = new HashMap<String, Object>();
            e3.put("sessionId", "zzz");
            e3.put("path", "/posts");
            e3.put("duration", 9200);
            d.append(e3, AckLevel.ACK);

            PState pstate = cluster.clusterPState(moduleName, "$$pagehit");

            System.out.format("/posts = %s\n", (Long) pstate.selectOne(Path.key("/posts")));

            PState pstateSH = cluster.clusterPState(moduleName, "$$sessionHistory");

            System.out.format("zzz = %s\n", pstateSH.selectOne(Path.key("zzz")).toString());

//            System.out.format("/posts = %s", pstate.selectOne(Path.key("/posts")));

            // d.append(clojure.lang.RT.readString("{:path \"product\"}"));
            // d.append(clojure.lang.RT.readString("{:path \"about-us\"}"));


            cluster.destroyModule(Test2.class.getName());

        }

    }
}