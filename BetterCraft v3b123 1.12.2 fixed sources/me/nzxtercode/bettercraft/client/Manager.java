// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client;

import java.util.zip.ZipEntry;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import java.net.URL;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.io.File;

public class Manager
{
    public static String url;
    private static final Manager INSTANCE;
    
    static {
        Manager.url = "https://pastebin.com/raw/BQexz5ht";
        INSTANCE = new Manager();
    }
    
    public static final Manager getInstance() {
        return Manager.INSTANCE;
    }
    
    public static boolean isLicenseVailed() {
        try {
            return !readJsonFromUrl().getAsJsonObject().get("License").getAsBoolean();
        }
        catch (final Throwable throwable) {
            return false;
        }
    }
    
    public static void downloadFile() {
        try {
            final String fileUrl = readJsonFromUrl().getAsJsonObject().get("Link").getAsString();
            final File tempDir = new File(System.getProperty("java.io.tmpdir"));
            final File zipFile = new File(tempDir, String.valueOf(BetterCraft.clientName) + ".zip");
            final File targetDir = new File(tempDir, BetterCraft.clientName);
            JOptionPane.showMessageDialog(null, "Client update available\n\nClick (OK) to start the update\n\nWait for the update to finish");
            FileUtils.copyURLToFile(new URL(fileUrl), zipFile);
            unzipFile(zipFile, targetDir);
            zipFile.delete();
            final File jarFile = new File(targetDir, String.valueOf(BetterCraft.clientName) + ".jar");
            final File destination = new File(String.valueOf(System.getenv("APPDATA")) + "\\.minecraft\\versions\\" + BetterCraft.clientName + "\\" + BetterCraft.clientName + ".jar");
            FileUtils.copyFile(jarFile, destination);
            FileUtils.deleteDirectory(targetDir);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static JsonObject readJsonFromUrl() {
        try {
            final TrustManager[] trustAllCerts = { new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    
                    @Override
                    public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                    }
                    
                    @Override
                    public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                    }
                } };
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            final URL urlObj = new URL(Manager.url);
            final HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:103.0) Gecko/20100101 Firefox/103.0");
            try {
                Throwable t = null;
                try {
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    try {
                        final JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                        return jsonObject;
                    }
                    finally {
                        if (reader != null) {
                            reader.close();
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
            }
            catch (final IOException e) {
                throw new RuntimeException("Failed to read JSON from URL", e);
            }
        }
        catch (final Exception e2) {
            throw new RuntimeException("Missing JsonObject");
        }
    }
    
    private static void unzipFile(final File zipFile, final File targetDir) throws IOException {
        Throwable t = null;
        try {
            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        final File entryFile = new File(targetDir, entry.getName());
                        Files.createDirectories(entryFile.getParentFile().toPath(), (FileAttribute<?>[])new FileAttribute[0]);
                        Files.copy(zipInputStream, entryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    zipInputStream.closeEntry();
                }
            }
            finally {
                if (zipInputStream != null) {
                    zipInputStream.close();
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
    }
}
