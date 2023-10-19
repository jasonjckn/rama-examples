package rama.examples.wordcount;

import com.rpl.rama.*;
import com.rpl.rama.ops.*;
import com.rpl.rama.module.*;
import com.rpl.rama.test.*;

import java.util.Arrays;
import java.util.List;

public class WordCountModule implements RamaModule {
    @Override
    public void define(Setup setup, Topologies topologies) {
        setup.declareDepot("*sentenceDepot", Depot.random());

        StreamTopology wordCount = topologies.stream("s");
        wordCount.pstate("$$wordCounts", PState.mapSchema(String.class, Long.class));

        wordCount.source("*sentenceDepot")
                .out("*sentence")
                .each((String sentence, OutputCollector collector) -> {
                    for (String word : sentence.split(" ")) {
                        collector.emit(word);
                    }
                }, "*sentence").out("*word")
                .hashPartition("*word")
//                .compoundAgg(CompoundAgg.map("*word", Agg.count()))
//                .out("*wordcount").pathPartition("")
                .compoundAgg("$$wordCounts", CompoundAgg.map("*word", Agg.count()));
    }

    public void main(String[] args) throws Exception {

        try (InProcessCluster cluster = InProcessCluster.create()) {
            cluster.launchModule(new WordCountModule(), new LaunchConfig(4, 2));
            String moduleName = WordCountModule.class.getName();
            Depot depot = cluster.clusterDepot(moduleName, "*sentenceDepot");
            depot.append("hello world");
            depot.append("hello world again");
            depot.append("say hello to the planet");
            depot.append("red planet labs");

            PState wc = cluster.clusterPState(moduleName, "$$wordCounts");

            for(String s : Arrays.asList("hello", "red")) {
                System.out.format("%s = %s\n", s, wc.selectOne(Path.key(s)));
            }


            cluster.destroyModule(moduleName);
        }
    }
}
