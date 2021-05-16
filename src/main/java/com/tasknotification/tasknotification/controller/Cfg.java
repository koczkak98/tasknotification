package com.tasknotification.tasknotification.controller;

public class Cfg {
    public static String getInitVector() { return System.getProperty("security.init.vector") ;}
    public static String getKey       () { return System.getProperty("security.key"         );}
}
