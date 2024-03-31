// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;

public class GZIPCompression
{
    public static byte[] compress(final byte[] input) {
        try {
            final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(input.length);
            final GZIPOutputStream gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream);
            gzipoutputstream.write(input);
            gzipoutputstream.close();
            final byte[] abyte = bytearrayoutputstream.toByteArray();
            bytearrayoutputstream.close();
            return abyte;
        }
        catch (final IOException ioexception) {
            ioexception.printStackTrace();
            return input;
        }
    }
    
    public static byte[] decompress(final byte[] input) {
        try {
            final byte[] abyte = new byte[1024];
            final ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(input);
            final GZIPInputStream gzipinputstream = new GZIPInputStream(bytearrayinputstream);
            final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            int i;
            while ((i = gzipinputstream.read(abyte)) > 0) {
                bytearrayoutputstream.write(abyte, 0, i);
            }
            gzipinputstream.close();
            bytearrayoutputstream.close();
            return bytearrayoutputstream.toByteArray();
        }
        catch (final IOException ioexception) {
            ioexception.printStackTrace();
            return input;
        }
    }
}
