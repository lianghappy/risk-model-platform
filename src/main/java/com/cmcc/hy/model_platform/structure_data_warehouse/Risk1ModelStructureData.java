package com.cmcc.hy.model_platform.structure_data_warehouse;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

import org.apache.spark.api.java.function.Function;
import com.mongodb.spark.MongoSpark;
import org.bson.Document;

import static java.util.Arrays.asList;
import java.io.Serializable;

/**
* @Description: 通过基础表转换和计算得到的模型1的表,读取mongodb的数据
* @Param:
* @return:
* @Author: Mr.Q
* @Date:
*/
public class Risk1ModelStructureData implements Serializable {

    public Risk1ModelStructureData() {

    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Risk model 1 structure data").setMaster("local")
                .set("spark.mongodb.input.uri", "mongodb://dbadmin:qazxsw!@172.28.19.202:27017/admin.DwStructureData")
                .set("spark.mongodb.output.uri", "mongodb://dbadmin:qazxsw!@172.28.19.202:27017/admin.DwStructureData");

        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        JavaRDD<Document> rdd = MongoSpark.load(sc);
        System.out.println("count::"+rdd.count());
        System.out.println("flag::"+rdd.first().toJson());


    }

}
