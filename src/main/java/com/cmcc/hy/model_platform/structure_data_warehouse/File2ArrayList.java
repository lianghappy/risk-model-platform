package com.cmcc.hy.model_platform.structure_data_warehouse;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class File2ArrayList extends ArrayList<String> {

    public static String read(String fileName) {
        /**
        * @Description:
        * @Param:[fileName]
        * @return:java.lang.String
        * @Author: Mr.Q
        * @Date:
        */
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in= new BufferedReader(new FileReader(new File(fileName).getAbsoluteFile()));
            try {
                String s;
                while((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public File2ArrayList(String fileName, String splitter) {
        /**
        * @Description:
        * @Param:[fileName, splitter]
        * @return:
        * @Author: Mr.Q
        * @Date:
        */
        super(Arrays.asList(read(fileName).split(splitter)));
        // Regular expression split() often leaves an empty
        // String at the first position:
        if (get(0).equals("")) remove(0);
    }

    public File2ArrayList(String fileName) {
        /**
        * @Description:
        * @Param:[fileName]
        * @return:
        * @Author: Mr.Q
        * @Date:
        */
        this(fileName, "\n");
    }

   /* public static void main(String[] args) {
        File2ArrayList text = new File2ArrayList("/home/qianhuhai/Documents/phone_region.csv");
        Iterator<String> s1=text.iterator();
        while (s1.hasNext()) {
            System.out.println(s1.next());
        }
    }*/
}