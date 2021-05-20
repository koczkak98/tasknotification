package com.tasknotification.tasknotification.controller;

public class Cfg {
    public static String getInitVector() { return "aesInitVectorKey17192123252729311";} //return System.getProperty("security.init.vector") ;}
    public static String getKey       () { return "aesEncryptionKey17192123252729311";} //return System.getProperty("security.key"         );}

    public static int getMessageMaxCharLength() { return 10000;} //return Integer.parseInt(System.getProperty("message.max.char.length"));}

}
