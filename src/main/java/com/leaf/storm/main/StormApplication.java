package com.leaf.storm.main;

import com.leaf.storm.bolt.WordCountBoltCount;
import com.leaf.storm.bolt.WordCountSplitBolt;
import com.leaf.storm.spout.WordCountSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

//@SpringBootApplication
public class StormApplication {
    public static void main(String[] args) {
//        SpringApplication.run(StormApplication.class, args);
//    }
//    public void startStorm (){
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("wordspout",new WordCountSpout());
        topologyBuilder.setBolt("splitbolt",new WordCountSplitBolt())
                .shuffleGrouping("wordspout");
        topologyBuilder.setBolt("wordcountbout",new WordCountBoltCount())
                .fieldsGrouping("splitbolt",new Fields("word"));
        StormTopology wc = topologyBuilder.createTopology();
        Config config = new Config();
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcountbout",config,wc);
    }

}

