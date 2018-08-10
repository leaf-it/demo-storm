package com.leaf.storm;

//import com.leaf.storm.clojure.DemoClojureCLJC;
//import com.leaf.storm.clojure.DemoClojureCore;
import com.leaf.storm.kotlin.KotlinClassDemo;
import com.leaf.storm.kotlin.KotlinDemoKt;
import org.junit.Test;


public class Survey {


    @Test
    public  void  testKotlin(){
        KotlinDemoKt.helloStr("world");

        new KotlinClassDemo().sayHell("kotlin");
    }
    @Test
    public void  testClojure(){
//        DemoClojureCLJC demoClojureCLJC = new DemoClojureCLJC();
////        demoClojureCLJC.
//        DemoClojureCore.hello("ann");
//        new DemoClojureCore().hello("ann");
//        DemoClojureCore.hello("ann");
    }
    public  void testGo(){
    }
}
