// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import net.minecraft.client.gui.GuiYesNo;
import java.awt.Desktop;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.Minecraft;
import java.net.URI;
import net.labymod.main.lang.LanguageManager;
import java.util.Formatter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.OutputStreamWriter;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.util.zip.InflaterInputStream;
import java.util.zip.Inflater;
import java.util.zip.GZIPInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class ModUtils
{
    public static String getProfileNameByIp(String ip) {
        if (ip == null) {
            return null;
        }
        if (ip.contains(":")) {
            ip = ip.split(":")[0];
        }
        if (ip.contains(".")) {
            final String[] parts = ip.split("\\.");
            if (parts.length >= 2) {
                return String.valueOf(parts[parts.length - 2]) + "." + parts[parts.length - 1];
            }
        }
        return ip.toLowerCase();
    }
    
    public static List<String> extractUrls(final String text) {
        final List<String> containedUrls = new ArrayList<String>();
        final String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        final Pattern pattern = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", 2);
        final Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }
        return containedUrls;
    }
    
    public static String download(final String urlStr) {
        try {
            final URL url = new URL(urlStr);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            final String encoding = conn.getContentEncoding();
            InputStream inStr = null;
            if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
                inStr = new GZIPInputStream(conn.getInputStream());
            }
            else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
                inStr = new InflaterInputStream(conn.getInputStream(), new Inflater(true));
            }
            else {
                inStr = conn.getInputStream();
            }
            return IOUtils.toString(inStr);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    public static String getContentString(final String page) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(page).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            final BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String s = "";
            String line;
            while ((line = r.readLine()) != null) {
                s = String.valueOf(s) + line;
            }
            return s;
        }
        catch (final Exception error) {
            error.printStackTrace();
            return "";
        }
    }
    
    public static ArrayList<String> getContentList(final String page) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(page).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            final BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            final ArrayList<String> s = new ArrayList<String>();
            String line;
            while ((line = r.readLine()) != null) {
                s.add(line);
            }
            return s;
        }
        catch (final Exception error) {
            error.printStackTrace();
            return new ArrayList<String>();
        }
    }
    
    public static String parseTimeNormalize(final long time) {
        final long formatb = time / 60L % 60L;
        final long formatc = time % 60L;
        final long formatd = time / 600L / 60L % 24L;
        final long formate = time / 600L / 60L / 24L;
        String out = "";
        if (formate != 0L) {
            out = String.valueOf(out) + formate + "d ";
        }
        if (formatd != 0L) {
            out = String.valueOf(out) + formatd + "h ";
        }
        if (formatb != 0L) {
            out = String.valueOf(out) + formatb + "m ";
        }
        if (formatc != 0L) {
            out = String.valueOf(out) + formatc + "s";
        }
        return out;
    }
    
    public static String performPost(final URL url, final String parameters, final String contentType, final boolean returnErrorPage) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        final byte[] paramAsBytes = parameters.getBytes(Charset.forName("UTF-8"));
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", String.valueOf(contentType) + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", new StringBuilder().append(paramAsBytes.length).toString());
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        final DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(paramAsBytes);
        writer.flush();
        writer.close();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        catch (final IOException e) {
            if (!returnErrorPage) {
                throw e;
            }
            final InputStream stream = connection.getErrorStream();
            if (stream == null) {
                throw e;
            }
            reader = new BufferedReader(new InputStreamReader(stream));
        }
        final StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        reader.close();
        return response.toString();
    }
    
    public static URL constantURL(final String input) {
        try {
            return new URL(input);
        }
        catch (final MalformedURLException ex) {
            return null;
        }
    }
    
    public static String jsonPost(final String urlStr, final String json) throws Exception {
        final URL url = new URL(urlStr);
        final HttpsURLConnection httpConnection = (HttpsURLConnection)url.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setRequestMethod("POST");
        final OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
        out.write(json);
        out.close();
        final BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        final StringBuffer sb = new StringBuffer();
        for (String str = br.readLine(); str != null; str = br.readLine()) {
            sb.append(str);
        }
        br.close();
        return sb.toString();
    }
    
    public static String normalizeString(final String input) {
        final char[] c = input.toLowerCase().toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }
    
    public static String sha1(final String string) {
        String sha1 = "";
        try {
            final MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (final UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        return sha1;
    }
    
    private static String byteToHex(final byte[] hash) {
        final Formatter formatter = new Formatter();
        for (final byte b : hash) {
            formatter.format("%02x", b);
        }
        final String result = formatter.toString();
        formatter.close();
        return result;
    }
    
    public static String translateAlternateColorCodes(final char altColorChar, final String textToTranslate) {
        final char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = '§';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
    
    public static String parseTimer(final int seconds) {
        return (seconds >= 3600) ? String.format("%02d:%02d", seconds / 60 / 60, seconds / 60, seconds % 60) : String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
    
    public static String humanReadableByteCount(final long bytes, final boolean si, final boolean space) {
        final int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return String.valueOf(bytes) + " B";
        }
        final int exp = (int)(Math.log((double)bytes) / Math.log(unit));
        final String pre = String.valueOf((si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)) + (si ? "" : "i");
        return String.format("%.1f" + (space ? " " : "") + "%sB", bytes / Math.pow(unit, exp), pre);
    }
    
    public static String getTimeDiff(final long timestamp) {
        if (timestamp == 0L) {
            return LanguageManager.translate("time_unknown");
        }
        final long time = System.currentTimeMillis() - timestamp;
        if (time == 0L) {
            return LanguageManager.translate("time_now");
        }
        if (time < 0L) {
            return LanguageManager.translate("time_future");
        }
        final long secs = time / 1000L;
        final long mins = secs / 60L;
        final long hours = mins / 60L;
        final long days = hours / 24L;
        final long months = days / 31L;
        final long years = months / 12L;
        String date = null;
        if (months >= 12L) {
            date = String.valueOf(years) + " " + LanguageManager.translate("time_" + ((years == 1L) ? "year" : "years"));
        }
        else if (days >= 31L) {
            date = String.valueOf(months) + " " + LanguageManager.translate("time_" + ((months == 1L) ? "month" : "months"));
        }
        else if (hours >= 24L) {
            date = String.valueOf(days) + " " + LanguageManager.translate("time_" + ((days == 1L) ? "day" : "days"));
        }
        else if (mins >= 60L) {
            date = String.valueOf(hours) + " " + LanguageManager.translate("time_" + ((hours == 1L) ? "hour" : "hours"));
        }
        else if (secs >= 60L) {
            date = String.valueOf(mins) + " " + LanguageManager.translate("time_" + ((mins == 1L) ? "minute" : "minutes"));
        }
        else {
            date = String.valueOf(secs) + " " + LanguageManager.translate("time_" + ((secs == 1L) ? "second" : "seconds"));
        }
        date = LanguageManager.translate("time_ago", date).toLowerCase();
        return date;
    }
    
    public static String getStringByInputStream(final InputStream inputStream) {
        final StringBuilder sb = new StringBuilder();
        try {
            int ch;
            while ((ch = inputStream.read()) != -1) {
                sb.append((char)ch);
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    public static void openWebpage(final URI uri, final boolean request) {
        if (request) {
            final GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                @Override
                public void confirmClicked(final boolean result, final int id) {
                    if (result) {
                        final Desktop desktop1 = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        if (desktop1 != null && desktop1.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop1.browse(uri);
                            }
                            catch (final Exception exception1) {
                                exception1.printStackTrace();
                            }
                        }
                    }
                    Minecraft.getMinecraft().displayGuiScreen(guiscreen);
                }
            }, "Do you want to open this link in your default browser?", String.valueOf(ModColor.cl("b")) + uri.toString(), 31102009));
        }
        else {
            final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                }
                catch (final Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    public static ArrayList<String> extractDomains(String value) {
        value = value.replaceAll(ModColor.RED + "[a-z0-9]", "");
        final ArrayList<String> arraylist = new ArrayList<String>();
        if (value == null) {
            return arraylist;
        }
        final String s = "(?i)\\b((?:[a-z][\\w-]+:(?:\\/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}\\/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»\u201c\u201d\u2018\u2019]))";
        final Pattern pattern = Pattern.compile(s, 2);
        final Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            arraylist.add(value.substring(matcher.start(0), matcher.end(0)));
        }
        return arraylist;
    }
    
    public static class ConvertJsonToObject
    {
        private static Gson gson;
        
        static {
            ConvertJsonToObject.gson = new GsonBuilder().setPrettyPrinting().create();
        }
        
        public static final <T> T getFromJSON(final String json, final Class<T> clazz) {
            return ConvertJsonToObject.gson.fromJson(json, clazz);
        }
        
        public static final <T> String toJSON(final T clazz) {
            return ConvertJsonToObject.gson.toJson(clazz);
        }
    }
}
