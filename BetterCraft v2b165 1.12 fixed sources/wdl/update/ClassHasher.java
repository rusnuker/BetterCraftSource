// 
// Decompiled by Procyon v0.6.0
// 

package wdl.update;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;

public class ClassHasher
{
    private static final char[] hexArray;
    
    static {
        hexArray = "0123456789ABCDEF".toCharArray();
    }
    
    public static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; ++j) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = ClassHasher.hexArray[v >>> 4];
            hexChars[j * 2 + 1] = ClassHasher.hexArray[v & 0xF];
        }
        return new String(hexChars);
    }
    
    public static String hash(final String relativeTo, final String file) throws ClassNotFoundException, FileNotFoundException, Exception {
        final Class<?> clazz = Class.forName(relativeTo);
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        InputStream stream = null;
        try {
            stream = clazz.getResourceAsStream(file);
            if (stream == null) {
                throw new FileNotFoundException(String.valueOf(file) + " relative to " + relativeTo);
            }
            DigestInputStream digestStream = null;
            try {
                digestStream = new DigestInputStream(stream, digest);
                while (digestStream.read() != -1) {}
            }
            finally {
                if (digestStream != null) {
                    digestStream.close();
                }
            }
            if (digestStream != null) {
                digestStream.close();
            }
        }
        finally {
            if (stream != null) {
                stream.close();
            }
        }
        if (stream != null) {
            stream.close();
        }
        return bytesToHex(digest.digest());
    }
}
