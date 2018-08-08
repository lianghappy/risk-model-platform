package com.cmcc.hy.model_platform.structure_data_warehouse;

import com.cmcc.hy.model_platform.structure_data_warehouse.Query;
import com.cmcc.hy.model_platform.structure_data_warehouse.File2ArrayList;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;

import static java.util.Arrays.asList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import com.alibaba.fastjson.*;
import com.mongodb.spark.MongoSpark;
import org.bson.json.JsonParseException;

/**
 * @Description: 获得的基础表
 * @Param:
 * @return:
 * @Author: Mr.Q
 * @Date:
 */
public class DigStructureData implements Serializable {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Dig structure data").setMaster("local")
                .set("spark.mongodb.input.uri", "mongodb://dbadmin:qazxsw!@172.28.19.202:27017/admin.DwStructureData")
                .set("spark.mongodb.output.uri", "mongodb://dbadmin:qazxsw!@172.28.19.202:27017/admin.DwStructureData");

        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        /*
       路径不存在则报错
         */
        try {
            //手机号归属地字段
//            File2ArrayList text = new File2ArrayList("/home/qianhuhai/Documents/phone_region.csv");

            //原始大网数据
            JavaRDD<PhoneRegion> dw_origin_data = sc.textFile(
                    "file:///home/qianhuhai/Documents/familycontact_timestamp/log=2018-04-24/000000_0", 3)
                    .map(new Function<String, PhoneRegion>() {
                        @Override
                        /**
                         * @Description: rdd of javabeans
                         * @Param:[s]
                         * @return:com.cmcc.hy.model_platform.structure_data_warehouse.PhoneRegion
                         * @Author: Mr.Q
                         * @Date:
                         */
                        public PhoneRegion call(String s) throws Exception {
                            String[] ss = s.split("\\|");
                            PhoneRegion phoneRegion = new PhoneRegion();
                            phoneRegion.setTime1(ss[0]);
                            phoneRegion.setSendTime(ss[1]);
                            phoneRegion.setPhoneNumber(ss[7]);
                            phoneRegion.setUserIP(ss[11]);
                            phoneRegion.setPhoneId(ss[12]);
                            phoneRegion.setSourceId(ss[4]);
                            phoneRegion.setUserName(ss[10]);
                            phoneRegion.setOperatorId(ss[5]);
                            phoneRegion.setJsonBody(ss[15]);
                            return phoneRegion;
                        }
                    });
            DataFrame prDF = sqlContext.createDataFrame(dw_origin_data, PhoneRegion.class);
            prDF.registerTempTable("prDF");


            DataFrame df = sqlContext.read().json("/home/qianhuhai/Documents/IdeaProjects/model_platform/src/main/resources/phone_region.json");
            df.registerTempTable("phone_region");


            DataFrame detailedInfo = sqlContext.sql("select time1,sendTime,phoneNumber,userName,userIP,phoneId,sourceId,operatorId,province," +
                    "city,jsonBody " +
                    " from prDF join phone_region on substr(phoneNumber,1,7)=phone");

            JavaRDD<String> dtRDD = detailedInfo.toJavaRDD().map(new Function<Row, String>() {
                @Override
                public String call(Row row) throws Exception {
                    String s = row.getString(0) + "|" + row.getString(1) + "|"
                            + row.getString(2) + "|" + row.getString(3) + "|"
                            + row.getString(4) + "|" + row.getString(5) + "|"
                            + row.getString(6) + "|" + row.getString(7) + "|"
                            + row.getString(8) + "|" + row.getString(9) + "|" + row.getString(10);
                    return s;
                }
            });

            JavaRDD<Document> structureDataEntity = dtRDD.mapPartitions(
                    new FlatMapFunction<Iterator<String>, Document>() {

                        private String structureDataString;
                        private static final String regex = "^[1-9]\\d*\\,";
                        private static final String regex1 = "^[1-9]\\d*";
                        @Override
                        /**
                         * @Description:
                         * @Param:[s1]
                         * @return:java.lang.Iterable<org.bson.Document>
                         * @Author: Mr.Q
                         * @Date:
                         */
                        public Iterable<Document> call(Iterator<String> s1) throws Exception {
                            ArrayList<Document> structureDataList = new ArrayList<>();

                            while (s1.hasNext()) {

                                String[] s = s1.next().split("\\|"); //分割数据
                                String time1 = s[0];
                                String phoneNumber = s[2];
                                String userName = s[3];
//                        String phoneRegion;

//                        phoneRegion = q.phoneRegionChange(phoneNumber, text);

                                String userIP = s[4];
                                String phoneId = s[5];
                                String sourceId = s[6];
                                String operatorId = s[7];

                                Integer year = Integer.parseInt(s[1].substring(0, 4)); //年

                                Integer month = Integer.parseInt(s[1].substring(5, 7));//月

                                Integer day = Integer.parseInt(s[1].substring(8, 10));

                                Integer hour = Integer.parseInt(s[1].substring(11, 13));
                                Integer minute = Integer.parseInt(s[1].substring(14, 16));
                                String province = s[8];
                                String city = s[9];
                                String os;
                                String sdkVersion ;
                                String devModel;
                                Double lac;
                                String devMac;
                                String wifiMac;
                                String imei;
                                String wifiSsid;
                                String idfv;
                                String imsi1=new String();
                                String imsi2 =new String();

                                if (!s[10].contains("\\N")) {
                                    JSONObject jsonBody = JSONObject.parseObject(s[10]);

                                    try {
                                        sdkVersion = jsonBody.getString("sdk_version");
                                        if (sdkVersion == null) {
                                            sdkVersion = "app";
                                        }

                                        os = jsonBody.getString("os");
                                        if (os == null) {
                                            os = "other";
                                        }
                                        devModel = jsonBody.getString("dev_model");
                                        if (devModel == null) {
                                            devModel = "other";
                                        }
                                        lac = jsonBody.getDouble("lac");

                                        devMac = jsonBody.getString("dev_mac");
                                        if (devMac == null) {
                                            devMac = "00:00:00:00:00:00";
                                        }
                                        else if(devMac.equals("null")) {
                                            devMac = "00:00:00:00:00:00";
                                        }
                                        wifiMac = jsonBody.getString("wifi_mac");
                                        if (wifiMac == null) {
                                            wifiMac = "00:00:00:00:00:00";
                                        }
                                        else if(wifiMac .equals("null")) {
                                            wifiMac = "00:00:00:00:00:00";
                                        }

                                        imei = jsonBody.getString("imei");
                                        if (imei == null) {
                                            imei = "000000000000000";
                                        } else if (imei.equals("")) {
                                            imei = "000000000000000";
                                        }


                                        wifiSsid = jsonBody.getString("wifi_ssid");
                                        if (wifiSsid == null) {
                                            wifiSsid = "other";
                                        }
                                        idfv = jsonBody.getString("idfv");
                                        if (idfv == null) {
                                            idfv = "00000000-0000-0000-0000-000000000000";
                                        }
                                        String imsi = jsonBody.getString("imsi");

                                        Pattern p=Pattern.compile(regex);
                                        Matcher m = p.matcher(imsi);
                                        Pattern p1 = Pattern.compile(regex1);
                                        Matcher m1 = p1.matcher(imsi);
                                        if ( m.find()) {
                                            try {
                                                String[] imsiSp=imsi.split(",");
                                                imsi1 = imsiSp[0];
                                                imsi2 = imsiSp[1];
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                                System.out.println("log1"+imsi+imsi1+imsi2);
                                            }

                                        } else if (m1.find()) {
                                            imsi1 = imsi;
                                            imsi2 = "000000000000000";
                                        } else {
                                                imsi1 = "000000000000000";
                                                imsi2 = "000000000000000";
                                        }
                                        structureDataString =
                                                "{" + "\"time1\":\"" + time1 + "\","
                                                        + "\"phonenumber\":\"" + phoneNumber + "\","
                                                        + "\"username\":\"" + userName + "\","
                                                        + "\"userip\":\"" + userIP + "\","
                                                        + "\"phoneid\":\"" + phoneId + "\","
                                                        + "\"sourceid\":\"" + sourceId + "\"," + "\"operatorid\":\"" + operatorId + "\"," +
                                                        "\"year\":" + year + "," + "\"month\":" + month + "," + "\"day\":" + day + ","
                                                        + "\"hour\":" + hour + "," + "\"minute\":" + minute + ","
                                                        + "\"province\":\"" + province + "\"," + "\"city\":\"" + city + "\","
                                                        + "\"os\":\"" + os + "\"," + "\"sdk_version\":\""+sdkVersion +"\","
                                                        +"\"dev_model\":\"" + devModel + "\"," +
                                                        "\"lac\":" + lac + "," + "\"dev_mac\":\"" + devMac + "\"," +
                                                        "\"wifi_mac\":\"" + wifiMac + "\"," + "\"imei\":\"" + imei + "\"," +
                                                        "\"wifi_ssid\":\"" + wifiSsid + "\"," + "\"idfv\":\"" + idfv + "\","
                                                        + "\"imsi1\":\"" + imsi1 + "\"," + "\"imsi2\":\"" +imsi2 +"\""+
                                                        "}";


                                        Document structureDataDocument = Document.parse(structureDataString);
                                        structureDataList.add(structureDataDocument);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    } catch (JsonParseException e) {
                                        e.getMessage();
                                        System.out.println("log:" + structureDataString);
                                    }
                                }
                            }
                    /*
                        创建实体,存入mongodb
                     */
                            return structureDataList;

                        }
                    });

            MongoSpark.save(structureDataEntity);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}

