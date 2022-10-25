package com.example.myapplication.utils;

public class DateUtil {

    public static String hInteger(int p) {
        return p < 10 ? "0" + p : Integer.toString(p);
    }
}
