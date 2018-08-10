package com.leaf.storm;

import com.google.common.collect.HashMultiset;
import  org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
//import org.apache.storm.hdfs.bolt.HdfsBolt;
//import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
//import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
//import org.apache.storm.hdfs.bolt.format.FileNameFormat;
//import org.apache.storm.hdfs.bolt.format.RecordFormat;
//import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
//import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
//import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
//import org.apache.storm.hdfs.bolt.sync.SyncPolicy;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

/**
 * Created by land.shen on 2018/5/31.
 */

public class WordCountDemo {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();


        builder.setSpout("source", new RandSentenceGenerator(), 1);


        builder.setBolt("split", new SplitSentence(), 1)
                .shuffleGrouping("source");
        builder.setBolt("count", new WordCount(), 1)
                .fieldsGrouping("split", new Fields("word"));

//        builder.setBolt("file",makehdfsBolt(builder),2).shuffleGrouping("count");

        Config conf = new Config();
        conf.setDebug(true);

       try {
           if (args != null && args.length > 0) {
               conf.setNumWorkers(3);
               if (args.length > 1) {
                   conf.setNumWorkers(Integer.parseInt(args[1]));
               }


               StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
           } else {
               conf.setMaxTaskParallelism(5);


               LocalCluster cluster = new LocalCluster();
               cluster.submitTopology("word-count", conf, builder.createTopology());


              // Thread.sleep(30000);
              // cluster.shutdown();
           }
       }
       catch (Exception e)
       {

       }
    }
//    public  static  HdfsBolt  makehdfsBolt(TopologyBuilder builder ){
//        RecordFormat format = new DelimitedRecordFormat()
//                .withFieldDelimiter("|");
//        FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(5.0f, FileSizeRotationPolicy.Units.MB);
//        SyncPolicy syncPolicy = new CountSyncPolicy(1000);
//        FileNameFormat fileNameFormat = new DefaultFileNameFormat()
//                .withPath("/foo/");
//
//        HdfsBolt bolt = new HdfsBolt()
//                .withFsUrl("hdfs://10.70.27.3:9000")
//                .withFileNameFormat(fileNameFormat)
//                .withRecordFormat(format)
//                .withRotationPolicy(rotationPolicy)
//                .withSyncPolicy(syncPolicy);
//        return bolt;
//    }
    public  static  class RandSentenceGenerator extends BaseRichSpout {
        private SpoutOutputCollector collector;
        private Random random;


        private String[] sentences;
        @Override
        public void open(Map map, TopologyContext ctx, SpoutOutputCollector collector) {
            this.collector = collector;
            this.random = new Random();
            this.sentences =
                    ("test hello world example\n" +
                            " sample hello another\n" +
                            " twitter storm one framework"
                    ).split("\n");
        }


        @Override
        public void nextTuple() {
            Utils.sleep(10);
            String sentence = sentences[random.nextInt(sentences.length)];
            collector.emit(new Values(sentence));
        }


        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("sentence"));
        }

    }

    public static class  SplitSentence extends BaseBasicBolt {

        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            String sentence = tuple.getString(0);
            for (String word : sentence.split("\\s")) {
                collector.emit(new Values(word));
            }
        }


        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }
     }
     public static class WordCount extends BaseBasicBolt {
         private Logger logger;
         private String name;
         private int task;


         private HashMultiset<String> words = HashMultiset.create();


         @Override
         public void prepare(Map conf, TopologyContext ctx) {
             super.prepare(conf, ctx);
             logger = Logger.getLogger(this.getClass());
             name = ctx.getThisComponentId();
             task = ctx.getThisTaskIndex();
         }


         @Override
         public void execute(Tuple tuple, BasicOutputCollector collector) {
             String source = tuple.getSourceComponent();
             if ("split".equals(source)) {
               //  words.add(tuple.getString(0));
                 collector.emit(new Values(tuple.getString(0),1));
             } else if ("ping".equals(source)) {
                 logger.warn("RESULT " + name + ":" + task + " :: " + words);
             }
         }


         @Override
         public void declareOutputFields(OutputFieldsDeclarer declarer) {
//             declarer.declare(new Fields("word", "count"));
         }
     }
}
