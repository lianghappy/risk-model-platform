package com.cmcc.hy.model_platform.structure_data_warehouse;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class Query implements Serializable {

    public  void Query() {

    }

    public String phoneRegionChange(String p1, ArrayList<String> p2){
        /**
        * @Description:
        * @Param:[p1, p2]
        * @return:java.lang.String
        * @Author: Mr.Q
        * @Date:
        */
        String p1Short;
        try {
            p1Short = p1.substring(0, 7);
        } catch (StringIndexOutOfBoundsException e) {
            p1Short = "*";

        }
        Iterator<String> s1=p2.iterator();
        while (s1.hasNext()) {
            String[] s2=s1.next().split(",");
            if (s2[1].contains(p1Short)) {
                 return s2[2];

            }

        }
        return "\"其他\"";
        }

    }

