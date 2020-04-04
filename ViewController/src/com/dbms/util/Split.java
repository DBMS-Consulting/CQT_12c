package com.dbms.util;

import java.util.Arrays;
import java.util.List;

public class Split {
    public Split() {
        super();
    }

    public static void main(String[] args) {
        Split split = new Split();
        
        String value = "ACIRED^DSAFDA";
        List<String> list1 = Arrays.asList(value.split("\\s*^\\s*"));
        System.out.println(list1);
    }
}
