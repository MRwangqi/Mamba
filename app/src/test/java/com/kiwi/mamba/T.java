package com.kiwi.mamba;


import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class T {


    @Test
    public void set() {
        open("张三", 12);
    }

    public void open(String name, int age) {
        test(name, age);
    }

    public void test(Object... args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
}
