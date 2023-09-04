package rama.examples.tutorial;

import com.rpl.rama.*;
import com.rpl.rama.module.*;
import com.rpl.rama.test.*;
import java.util.*;

public class PageAnalyticsModule implements RamaModule {
    @Override
    public void define(Setup setup, Topologies topologies) {
        setup.declareDepot("*depot", Depot.random());

        StreamTopology s = topologies.stream("s");
        s.pstate("$$pageViewCount", PState.mapSchema(String.class, Long.class));
        s.pstate("$$sessionHistory",
                 PState.mapSchema(
                   String.class,
                   PState.listSchema(PState.mapSchema(String.class, Object.class))));


        s.source("*depot").out("*pageVisit")
                .each((Map<String, Object> visit) -> visit.get("sessionId"), "*pageVisit").out("*sessionId")
                .each((Map<String, Object> visit) -> visit.get("path"), "*pageVisit").out("*path")
                .compoundAgg("$$pageViewCount", CompoundAgg.map("*path", Agg.count()))
                .compoundAgg("$$sessionHistory", CompoundAgg.map("*sessionId", Agg.list("*pageVisit")))
        ;
    }

    public void main() throws Exception {
        try (InProcessCluster cluster = InProcessCluster.create()) {
            cluster.launchModule(new PageAnalyticsModule(), new LaunchConfig(2, 2));
            String moduleName = PageAnalyticsModule.class.getName();
            Depot d = cluster.clusterDepot(moduleName, "*depot");


            Map<String, Object> e1 = new HashMap<String, Object>();
            e1.put("sessionId", "abc123");
            e1.put("path", "/posts");
            e1.put("duration", 4200);
            d.append(e1, AckLevel.ACK);

            Map<String, Object> e2 = new HashMap<String, Object>();
            e2.put("sessionId", "oenutho");
            e2.put("path", "/posts");
            e2.put("duration", 9200);
            d.append(e2, AckLevel.ACK);

            Map<String, Object> e3 = new HashMap<String, Object>();
            e3.put("sessionId", "zzz");
            e3.put("path", "/posts");
            e3.put("duration", 9200);
            d.append(e3, AckLevel.ACK);


            PState pstate = cluster.clusterPState(moduleName, "$$pageViewCount");

            System.out.format("/posts = %s\n", (Long) pstate.selectOne(Path.key("/posts")));

        }
    }
}