package com.tyx.testSomeThing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author Axun
 * @Create 2020/1/3 11:08
 */
public class T1 {
    public static void main(String [] args){
        Integer a = 1;
        Integer b = 1;
        Integer c = new Integer(10);
        Integer d = new Integer(10);
        System.out.println(a==b);
        System.out.println(c==d);
        System.out.println(c==10);
        System.out.println(c.equals(10));

    }
}
