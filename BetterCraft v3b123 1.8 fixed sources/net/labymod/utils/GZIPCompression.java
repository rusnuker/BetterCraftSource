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
            final ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
            final GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(input);
            gzip.close();
            final byte[] compressed = bos.toByteArray();
            bos.close();
            return compressed;
        }
        catch (final IOException e) {
            e.printStackTrace();
            return input;
        }
    }
    
    public static byte[] decompress(final byte[] input) {
        try {
            final byte[] buffer = new byte[1024];
            final ByteArrayInputStream bis = new ByteArrayInputStream(input);
            final GZIPInputStream gis = new GZIPInputStream(bis);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            while ((len = gis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            gis.close();
            out.close();
            return out.toByteArray();
        }
        catch (final IOException e) {
            e.printStackTrace();
            return input;
        }
    }
}
