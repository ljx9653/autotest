package com.my.aotutest;

import org.junit.Test;

/**
 * @author lijx
 * @date 2019/4/9 - 16:58
 */
public class JsonUtilTest {
    @Test
    public void getJsonKey() throws Exception {
        String s = "29:06\n" +
                "10:39\n" +
                "12:46\n" +
                "09:38\n" +
                "09:51\n" +
                "11:54\n" +
                "15:46\n" +
                "25:35\n" +
                "05:16\n" +
                "05:07\n" +
                "17:04";
        String[] split = s.split("\\s");
        for (String ss : split) {
            System.out.println(ss);
        }

    }


    public static void main(String[] args) {

    }
}
