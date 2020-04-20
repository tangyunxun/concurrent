package com.tyx.testSomeThing;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Axun
 * @Create 2020/2/23 18:41
 */
public class TestHashMap {
    public static void main(String [] args){
        Map<String, Integer> map = new HashMap<String, Integer>(2);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        int i = 1;
        System.out.println(++i);
        System.out.println(i);
        map.put(null, null);
        map.remove(null);
        map.get(null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println(calendar.getTimeInMillis());

    }
}
