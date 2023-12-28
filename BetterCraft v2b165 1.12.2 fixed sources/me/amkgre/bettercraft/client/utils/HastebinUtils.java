// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class HastebinUtils
{
    private static final Gson GSON;
    
    static {
        GSON = new Gson();
    }
    
    public static String uploadToHastebin(final String text) throws IOException {
        final URL url = new URL("https://hastebin.com/documents");
        final HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("User-Agent", "Fusion Config Exporter (+http://fusion.cool/)");
        con.setRequestProperty("Content-Type", "text/plain");
        Throwable t = null;
        try {
            final OutputStream out = con.getOutputStream();
            try {
                out.write(text.getBytes());
            }
            finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        finally {
            if (t == null) {
                final Throwable t2;
                t = t2;
            }
            else {
                final Throwable t2;
                if (t != t2) {
                    t.addSuppressed(t2);
                }
            }
        }
        Throwable t3 = null;
        Response response;
        try {
            final InputStream in = con.getInputStream();
            try {
                response = HastebinUtils.GSON.fromJson(IOUtils.toString(in), Response.class);
            }
            finally {
                if (in != null) {
                    in.close();
                }
            }
        }
        finally {
            if (t3 == null) {
                final Throwable t4;
                t3 = t4;
            }
            else {
                final Throwable t4;
                if (t3 != t4) {
                    t3.addSuppressed(t4);
                }
            }
        }
        final String retUrl = "https://hastebin.com/" + response.key;
        ClipboardUtils.setClipboard(retUrl);
        return retUrl;
    }
    
    private class Response
    {
        private String key;
    }
}
