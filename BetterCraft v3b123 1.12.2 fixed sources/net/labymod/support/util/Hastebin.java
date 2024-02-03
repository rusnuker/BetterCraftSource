// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support.util;

import java.awt.datatransfer.Clipboard;
import com.google.gson.JsonElement;
import java.awt.Component;
import javax.swing.JOptionPane;
import net.labymod.main.LabyMod;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonParser;

public class Hastebin
{
    private static final JsonParser JSON_PARSER;
    
    static {
        JSON_PARSER = new JsonParser();
    }
    
    public static void upload(final String content) {
        try {
            final byte[] postData = content.getBytes(StandardCharsets.UTF_8);
            final int postDataLength = postData.length;
            final String request = "https://paste.labymod.net/documents";
            final URL url = new URL("https://paste.labymod.net/documents");
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            final DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            final Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String json = "";
            int c;
            while ((c = in.read()) >= 0) {
                json = String.valueOf(json) + (char)c;
            }
            final JsonElement jsonElement = Hastebin.JSON_PARSER.parse(json);
            if (jsonElement.getAsJsonObject().has("key")) {
                final String key = jsonElement.getAsJsonObject().get("key").getAsString();
                final StringSelection selection = new StringSelection("https://paste.labymod.net/" + key);
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                LabyMod.getInstance().openWebpage("https://paste.labymod.net/" + key, false);
            }
            else {
                JOptionPane.showMessageDialog(null, json, "Error while uploading log", 0);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null, error.getMessage(), "Error while uploading log", 0);
        }
    }
}
