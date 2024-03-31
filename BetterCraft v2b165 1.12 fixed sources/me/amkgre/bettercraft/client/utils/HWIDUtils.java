// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class HWIDUtils
{
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "";
        final String main = String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        byte[] array;
        for (int length = (array = md5).length, j = 0; j < length; ++j) {
            final byte b = array[j];
            s = String.valueOf(s) + Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s = String.valueOf(s) + "-";
            }
            ++i;
        }
        return s;
    }
}
