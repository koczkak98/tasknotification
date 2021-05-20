package com.tasknotification.tasknotification.controller;

import org.apache.tomcat.util.codec.binary.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Security {
    private static final String initVector = Cfg.getInitVector();
    private static final String key        = Cfg.getKey()       ;

    public static String encrypt(String toBeEncrypted)
    {
        String          es ;
        byte[]          eb ;
        IvParameterSpec iv ;
        SecretKeySpec   sks;
        Cipher          ci ;

        try {
            iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            sks = new SecretKeySpec(key.getBytes("UTF-8"), "AES-256");

            ci = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            ci.init(Cipher.ENCRYPT_MODE, sks, iv);

            eb = ci.doFinal(toBeEncrypted.getBytes());
            es = Base64.encodeBase64String(eb);
            return es;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] encrypted)
    {
        String          or ;
        IvParameterSpec iv ;
        SecretKeySpec   sks;
        Cipher          ci ;

        try {
            iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            sks = new SecretKeySpec(key.getBytes("UTF-8"), "AES-256");

            ci = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            ci.init(Cipher.DECRYPT_MODE, sks, iv);

            or = new String(ci.doFinal(Base64.decodeBase64(encrypted)));
            return or;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
