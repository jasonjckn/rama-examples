package rama.examples.wordcount;

import com.rpl.rama.*;
import com.rpl.rama.ops.*;
import com.rpl.rama.module.*;
import com.rpl.rama.test.*;

import java.util.HashMap;
import java.util.Map;

public class WordCountModule implements RamaModule {
    @Override
    public void define(Setup setup, Topologies topologies) {
        setup.declareDepot("*sentenceDepot", Depot.random());

        StreamTopology wordCount = topologies.stream("wordCount");
        wordCount.pstate("$$wordCounts", PState.mapSchema(String.class, Long.class));

        wordCount.source("*sentenceDepot").out("*sentence")
                 .each((String sentence, OutputCollector collector) -> {
                     for(String word: sentence.split(" ")) {
                       collector.emit(word);
                     }
                 }, "*sentence").out("*word")
                 .hashPartition("*word")
                 .compoundAgg("$$wordCounts", CompoundAgg.map("*word", Agg.count()));
    }

    public static void main(String[] args) throws Exception {

        try (InProcessCluster cluster = InProcessCluster.create()) {
            while(true) {
                System.out.println("----------------------------------------------------------------");

                Long t1 = System.currentTimeMillis();
                main2(cluster);
                Long t2 = System.currentTimeMillis();
                System.out.format("Time elapsed from createModule -> destroyModule: %.2f seconds", (t2-t1)/1000.0);
                // Thread.sleep(1000);
            }
        }
    }
    public static void main2(InProcessCluster cluster ) throws Exception {
        cluster.launchModule(new WordCountModule(), new LaunchConfig(4, 2));
        String moduleName = WordCountModule.class.getName();
        Depot depot = cluster.clusterDepot(moduleName, "*sentenceDepot");
        depot.append("hello world");
        depot.append("hello world again");
        depot.append("say hello to the planet");
        depot.append("red planet labs");

        PState wc = cluster.clusterPState(moduleName, "$$wordCounts");

        cluster.destroyModule(moduleName);

    }


}