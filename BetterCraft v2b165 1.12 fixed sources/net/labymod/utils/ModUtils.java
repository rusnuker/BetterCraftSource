// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.util.Iterator;
import net.labymod.user.User;
import net.labymod.main.LabyMod;
import java.util.Formatter;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
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
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
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
        final String[] astring;
        if (ip.contains(".") && (astring = ip.split("\\.")).length >= 2) {
            return String.valueOf(String.valueOf(astring[astring.length - 2])) + "." + astring[astring.length - 1];
        }
        return ip.toLowerCase();
    }
    
    public static List<String> extractUrls(final String text) {
        final ArrayList<String> list = new ArrayList<String>();
        final String s = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        final Pattern pattern = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", 2);
        final Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            list.add(text.substring(matcher.start(0), matcher.end(0)));
        }
        return list;
    }
    
    public static String download(final String urlStr) {
        try {
            final URL url = new URL(urlStr);
            final HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            httpurlconnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            final String s = httpurlconnection.getContentEncoding();
            InputStream inputstream = null;
            InputStream inputStream;
            if (s != null && s.equalsIgnoreCase("gzip")) {
                inputStream = new GZIPInputStream(httpurlconnection.getInputStream());
            }
            else if (s != null && s.equalsIgnoreCase("deflate")) {
                final InputStream inputStream2;
                final Inflater inflater;
                inputStream = new InflaterInputStream(inputStream2, inflater);
                inputStream2 = httpurlconnection.getInputStream();
                inflater = new Inflater(true);
            }
            else {
                inputStream = httpurlconnection.getInputStream();
            }
            inputstream = inputStream;
            return IOUtils.toString(inputstream);
        }
        catch (final Exception var5) {
            return null;
        }
    }
    
    public static String getContentString(final String page) {
        try {
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(page).openConnection();
            httpurlconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpurlconnection.connect();
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream(), Charset.forName("UTF-8")));
            String s1 = "";
            String s2;
            while ((s2 = bufferedreader.readLine()) != null) {
                s1 = String.valueOf(String.valueOf(s1)) + s2;
            }
            return s1;
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }
    
    public static ArrayList<String> getContentList(final String page) {
        try {
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(page).openConnection();
            httpurlconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpurlconnection.connect();
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream(), Charset.forName("UTF-8")));
            final ArrayList<String> arraylist = new ArrayList<String>();
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                arraylist.add(s);
            }
            return arraylist;
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return new ArrayList<String>();
        }
    }
    
    public static String parseTimeNormalize(final long time) {
        final long i = time / 60L % 60L;
        final long j = time % 60L;
        final long k = time / 600L / 60L % 24L;
        final long l = time / 600L / 60L / 24L;
        String s = "";
        if (l != 0L) {
            s = String.valueOf(String.valueOf(s)) + l + "d ";
        }
        if (k != 0L) {
            s = String.valueOf(String.valueOf(s)) + k + "h ";
        }
        if (i != 0L) {
            s = String.valueOf(String.valueOf(s)) + i + "m ";
        }
        if (j != 0L) {
            s = String.valueOf(String.valueOf(s)) + j + "s";
        }
        return s;
    }
    
    public static String performPost(final URL url, final String parameters, final String contentType, final boolean returnErrorPage) throws IOException {
        final HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
        final byte[] abyte = parameters.getBytes(Charset.forName("UTF-8"));
        httpurlconnection.setConnectTimeout(15000);
        httpurlconnection.setReadTimeout(15000);
        httpurlconnection.setRequestMethod("POST");
        httpurlconnection.setRequestProperty("Content-Type", String.valueOf(String.valueOf(contentType)) + "; charset=utf-8");
        httpurlconnection.setRequestProperty("Content-Length", new StringBuilder().append(abyte.length).toString());
        httpurlconnection.setRequestProperty("Content-Language", "en-US");
        httpurlconnection.setUseCaches(false);
        httpurlconnection.setDoInput(true);
        httpurlconnection.setDoOutput(true);
        final DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
        dataoutputstream.write(abyte);
        dataoutputstream.flush();
        dataoutputstream.close();
        BufferedReader bufferedreader;
        try {
            bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
        }
        catch (final IOException ioexception) {
            if (!returnErrorPage) {
                throw ioexception;
            }
            final InputStream inputstream = httpurlconnection.getErrorStream();
            if (inputstream == null) {
                throw ioexception;
            }
            bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        }
        final StringBuilder stringbuilder = new StringBuilder();
        String s;
        while ((s = bufferedreader.readLine()) != null) {
            stringbuilder.append(s);
            stringbuilder.append('\r');
        }
        bufferedreader.close();
        return stringbuilder.toString();
    }
    
    public static URL constantURL(final String input) {
        try {
            return new URL(input);
        }
        catch (final MalformedURLException var2) {
            return null;
        }
    }
    
    public static String jsonPost(final String urlStr, final String json) throws Exception {
        final URL url = new URL(urlStr);
        final HttpsURLConnection httpsurlconnection = (HttpsURLConnection)url.openConnection();
        httpsurlconnection.setDoOutput(true);
        httpsurlconnection.setDoInput(true);
        httpsurlconnection.setRequestProperty("Content-Type", "application/json");
        httpsurlconnection.setRequestMethod("POST");
        final OutputStreamWriter outputstreamwriter = new OutputStreamWriter(httpsurlconnection.getOutputStream());
        outputstreamwriter.write(json);
        outputstreamwriter.close();
        final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpsurlconnection.getInputStream()));
        final StringBuffer stringbuffer = new StringBuffer();
        for (String s = bufferedreader.readLine(); s != null; s = bufferedreader.readLine()) {
            stringbuffer.append(s);
        }
        bufferedreader.close();
        return stringbuffer.toString();
    }
    
    public static String normalizeString(final String input) {
        final char[] achar = input.toLowerCase().toCharArray();
        achar[0] = Character.toUpperCase(achar[0]);
        return new String(achar);
    }
    
    public static String sha1(final String string) {
        String s = "";
        try {
            final MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
            messagedigest.reset();
            messagedigest.update(string.getBytes(StandardCharsets.UTF_8));
            s = byteToHex(messagedigest.digest());
        }
        catch (final NoSuchAlgorithmException nosuchalgorithmexception) {
            nosuchalgorithmexception.printStackTrace();
        }
        return s;
    }
    
    private static String byteToHex(final byte[] hash) {
        final Formatter formatter = new Formatter();
        for (final byte b0 : hash) {
            formatter.format("%02x", b0);
        }
        final String s = formatter.toString();
        formatter.close();
        return s;
    }
    
    public static String translateAlternateColorCodes(final char altColorChar, final String textToTranslate) {
        final char[] achar = textToTranslate.toCharArray();
        for (int i = 0; i < achar.length - 1; ++i) {
            if (achar[i] == altColorChar) {
                if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(achar[i + 1]) > -1) {
                    achar[i] = '§';
                    achar[i + 1] = Character.toLowerCase(achar[i + 1]);
                }
            }
        }
        return new String(achar);
    }
    
    public static String parseTimer(final int seconds) {
        return (seconds >= 3600) ? String.format("%02d:%02d", seconds / 60 / 60, seconds / 60, seconds % 60) : String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
    
    public static String humanReadableByteCount(final long bytes, final boolean si, final boolean space) {
        final int n;
        final int i = n = (si ? 1000 : 1024);
        if (bytes < i) {
            return String.valueOf(String.valueOf(bytes)) + " B";
        }
        final int j = (int)(Math.log((double)bytes) / Math.log(i));
        final String s = String.valueOf(String.valueOf((si ? "kMGTPE" : "KMGTPE").charAt(j - 1))) + (si ? "" : "i");
        return String.format("%.1f" + (space ? " " : "") + "%sB", bytes / Math.pow(i, j), s);
    }
    
    public static String getTimeDiff(final long timestamp) {
        if (timestamp == 0L) {
            return "time_unknown";
        }
        final long i = System.currentTimeMillis() - timestamp;
        if (i == 0L) {
            return "time_now";
        }
        if (i < 0L) {
            return "time_future";
        }
        final long j = i / 1000L;
        final long k = j / 60L;
        final long l = k / 60L;
        final long i2 = l / 24L;
        final long j2 = i2 / 31L;
        final long k2 = j2 / 12L;
        String s = null;
        s = ((j2 >= 12L) ? (String.valueOf(String.valueOf(k2)) + " " + "time_" + ((k2 == 1L) ? "year" : "years")) : ((i2 >= 31L) ? (String.valueOf(String.valueOf(j2)) + " " + "time_" + ((j2 == 1L) ? "month" : "months")) : ((l >= 24L) ? (String.valueOf(String.valueOf(i2)) + " " + "time_" + ((i2 == 1L) ? "day" : "days")) : ((k >= 60L) ? (String.valueOf(String.valueOf(l)) + " " + "time_" + ((l == 1L) ? "hour" : "hours")) : ((j >= 60L) ? (String.valueOf(String.valueOf(k)) + " " + "time_" + ((k == 1L) ? "minute" : "minutes")) : (String.valueOf(String.valueOf(j)) + " " + "time_" + ((j == 1L) ? "second" : "seconds")))))));
        s = "time_ago";
        return s;
    }
    
    public static String getStringByInputStream(final InputStream inputStream) {
        final StringBuilder stringbuilder = new StringBuilder();
        try {
            int i;
            while ((i = inputStream.read()) != -1) {
                stringbuilder.append((char)i);
            }
        }
        catch (final IOException ioexception) {
            ioexception.printStackTrace();
        }
        return stringbuilder.toString();
    }
    
    public static void checkValidCosmetics(final LabyMod labyMod) {
        for (final User user : labyMod.getUserManager().getUsers().values()) {
            if (user.hasCosmeticById(21)) {
                if (user.getUuid().getLeastSignificantBits() == -4859828808212867370L) {
                    continue;
                }
                user.getCosmetics().remove(21);
            }
        }
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
