// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.io.File;

public class Utils
{
    private static final File TEAMSPEAK_DIRECTORY;
    private static final DateFormat timeFormatter;
    
    static {
        timeFormatter = DateFormat.getTimeInstance(2);
        File TEAMSPEAK_DIRECTORY2;
        if (getPlatform() == Platform.WINDOWS) {
            final String registryKey = "Software\\TeamSpeak 3 Client";
            final int hKey = findHKey("Software\\TeamSpeak 3 Client");
            if (hKey == -1) {
                TEAMSPEAK_DIRECTORY2 = getTeamSpeakDirectoryInAppData();
            }
            else {
                try {
                    final String configLocation = WinRegistry.readString(hKey, "Software\\TeamSpeak 3 Client", "ConfigLocation");
                    if ("0".equals(configLocation)) {
                        TEAMSPEAK_DIRECTORY2 = getTeamSpeakDirectoryInAppData();
                    }
                    else {
                        final String installPath = WinRegistry.readString(hKey, "Software\\TeamSpeak 3 Client", "");
                        final File possibleConfigDirectory = new File(installPath, "config");
                        if (possibleConfigDirectory.exists()) {
                            TEAMSPEAK_DIRECTORY2 = possibleConfigDirectory;
                        }
                        else {
                            TEAMSPEAK_DIRECTORY2 = getTeamSpeakDirectoryInAppData();
                        }
                    }
                }
                catch (final Exception ignored) {
                    TEAMSPEAK_DIRECTORY2 = getTeamSpeakDirectoryInAppData();
                }
            }
        }
        else {
            TEAMSPEAK_DIRECTORY2 = getTeamSpeakDirectoryInAppData();
        }
        TEAMSPEAK_DIRECTORY = TEAMSPEAK_DIRECTORY2;
    }
    
    public static String getOSName() {
        return System.getProperty("os.name");
    }
    
    public static Platform getPlatform() {
        final String osName = getOSName().toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            return Platform.WINDOWS;
        }
        if (osName.contains("mac")) {
            return Platform.MAC;
        }
        if (osName.contains("linux") || osName.contains("sunos") || osName.contains("unix")) {
            return Platform.LINUX;
        }
        if (osName.contains("solaris")) {
            return Platform.SOLARIS;
        }
        return Platform.UNKNOWN;
    }
    
    public static File getTeamspeakDirectory() {
        return Utils.TEAMSPEAK_DIRECTORY;
    }
    
    public static String getChatTimeString() {
        return "<" + Utils.timeFormatter.format(new Date(System.currentTimeMillis())) + "> ";
    }
    
    public static BufferedImage readImage(final String imageUrl) throws IOException {
        final URL url = new URL(imageUrl);
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            final BufferedImage bufferedImage = ImageIO.read(inputStream);
            return bufferedImage;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    private static int findHKey(final String key) {
        if (existsHKey(-2147483646, key)) {
            return -2147483646;
        }
        if (existsHKey(-2147483647, key)) {
            return -2147483647;
        }
        return -1;
    }
    
    private static boolean existsHKey(final int hkey, final String key) {
        try {
            WinRegistry.readString(hkey, key, "");
            return true;
        }
        catch (final Exception ignored) {
            return false;
        }
    }
    
    private static File getTeamSpeakDirectoryInAppData() {
        final String userHome = System.getProperty("user.home", ".");
        final String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            final String applicationData = System.getenv("APPDATA");
            if (applicationData != null) {
                return new File(applicationData, "TS3Client/");
            }
            return new File(userHome, "TS3Client/");
        }
        else {
            if (osName.contains("mac")) {
                return new File(userHome, "Library/Application Support/TeamSpeak 3");
            }
            if (osName.contains("linux") || osName.contains("sunos") || osName.contains("unix") || osName.contains("solaris")) {
                return new File(userHome, "TeamSpeak 3/");
            }
            return new File(userHome, "TeamSpeak 3");
        }
    }
    
    public enum Platform
    {
        WINDOWS("WINDOWS", 0), 
        MAC("MAC", 1), 
        LINUX("LINUX", 2), 
        SOLARIS("SOLARIS", 3), 
        UNKNOWN("UNKNOWN", 4);
        
        private Platform(final String s, final int n) {
        }
    }
}
