package com.tasknotification.tasknotification.controller;

public class Cfg {
    public static String getInitVector() { return "aesInitVectorKey17192123252729311";} //return System.getProperty("security.init.vector") ;}
    public static String getKey       () { return "aesEncryptionKey17192123252729311";} //return System.getProperty("security.key"         );}


    public static int getObjectMaxCharLength () { return 45 ;} //return Integer.parseInt(System.getProperty("object.max.char.length"));}
    public static int getObjectMinCharLength () { return 2  ;} //return Integer.parseInt(System.getProperty("object.min.char.length"));}
    public static int getMessageMaxCharLength() { return 256;} //return Integer.parseInt(System.getProperty("message.max.char.length"));}
    public static int getMessageMinCharLength() { return 10 ;} //return Integer.parseInt(System.getProperty("message.min.char.length"));}
}
