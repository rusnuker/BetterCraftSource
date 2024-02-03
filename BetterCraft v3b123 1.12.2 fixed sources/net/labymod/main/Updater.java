// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.util.List;
import java.io.IOException;
import java.util.Base64;
import java.io.File;
import java.util.ArrayList;
import net.labymod.support.report.ReportArguments;
import java.util.Iterator;
import com.google.gson.JsonObject;
import java.net.URLConnection;
import net.labymod.support.util.Debug;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Updater
{
    public static final short[] CLIENT_VERSION;
    private static final JsonParser JSON_PARSER;
    private static final Gson GSON;
    private String latestVersionString;
    private short[] latestVersion;
    
    static {
        CLIENT_VERSION = getShortVersionOfString("3.6.6");
        JSON_PARSER = new JsonParser();
        GSON = new Gson();
    }
    
    public Updater() {
        this.latestVersionString = "Unknown";
        this.latestVersion = null;
    }
    
    public boolean isUpdateAvailable() {
        return this.latestVersion != null && isClientOutdated(Updater.CLIENT_VERSION, this.latestVersion);
    }
    
    public void checkUpdate() throws Exception {
        final URLConnection connection = new URL("http://dl.labymod.net/versions.json").openConnection();
        connection.setRequestProperty("User-Agent", Source.getUserAgent());
        connection.connect();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String latestVersionsContents = "";
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            latestVersionsContents = String.valueOf(latestVersionsContents) + (latestVersionsContents.equals("") ? "" : "\n") + line;
        }
        if (latestVersionsContents.isEmpty()) {
            return;
        }
        final Map<String, String> mcVerToLMVerMap = new HashMap<String, String>();
        final JsonObject jsonObject = Updater.JSON_PARSER.parse(latestVersionsContents).getAsJsonObject();
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final String mcVersion = entry.getKey();
            final JsonObject object = entry.getValue().getAsJsonObject();
            final JsonElement versionElement = object.get("version");
            if (versionElement != null && mcVersion != null) {
                mcVerToLMVerMap.put(mcVersion, versionElement.getAsString());
            }
        }
        final String newVersionString = mcVerToLMVerMap.get(Source.ABOUT_MC_VERSION);
        if (newVersionString != null) {
            this.latestVersionString = newVersionString;
            this.latestVersion = getShortVersionOfString(newVersionString);
            final boolean updateAvailable = this.isUpdateAvailable();
            this.printInfo(updateAvailable);
            if (updateAvailable) {
                this.downloadUpdaterFile();
                this.addUpdaterHook();
            }
        }
        else {
            Debug.log(Debug.EnumDebugMode.UPDATER, "Minecraft " + Source.ABOUT_MC_VERSION + " has no valid version entry");
        }
    }
    
    public void addUpdaterHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Debug.log(Debug.EnumDebugMode.UPDATER, "Executing LabyMod Updater");
                Updater.this.executeUpdater();
                Debug.log(Debug.EnumDebugMode.UPDATER, "Updating complete");
            }
        }));
    }
    
    public void executeReport(final ReportArguments reportArguments) {
        final String json = Updater.GSON.toJson(reportArguments);
        this.execute(true, json);
    }
    
    public void executeUpdater() {
        this.execute(false, null);
    }
    
    public void execute(final boolean report, final String json) {
        try {
            final List<String> arguments = new ArrayList<String>();
            arguments.add("java");
            arguments.add("-jar");
            arguments.add(new File("LabyMod/Updater.jar").getAbsolutePath());
            if (report) {
                arguments.add("report");
                arguments.add(Base64.getEncoder().encodeToString(json.getBytes()));
            }
            final ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.start();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public void printInfo(final boolean updateAvailable) {
        Debug.log(Debug.EnumDebugMode.UPDATER, "The latest LabyMod version is v" + this.latestVersionString + ", you are currently using LabyMod version v" + "3.6.6");
        if (updateAvailable) {
            Debug.log(Debug.EnumDebugMode.UPDATER, "You are outdated!");
        }
        else {
            Debug.log(Debug.EnumDebugMode.UPDATER, "You are using the latest version.");
        }
    }
    
    public void downloadUpdaterFile() throws Exception {
        final FileOutputStream fos = new FileOutputStream("LabyMod/Updater.jar");
        final HttpURLConnection web = (HttpURLConnection)new URL("http://dl.labymod.net/latest/install/updater.jar").openConnection();
        web.setRequestProperty("User-Agent", Source.getUserAgent());
        final ReadableByteChannel readableByteChannel = Channels.newChannel(web.getInputStream());
        fos.getChannel().transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
        fos.close();
        Debug.log(Debug.EnumDebugMode.UPDATER, "Downloaded latest Updater http://dl.labymod.net/latest/install/updater.jar");
    }
    
    public static short[] getShortVersionOfString(final String versionString) {
        if (!versionString.contains(".")) {
            return new short[0];
        }
        final String[] versionParts = versionString.split("\\.");
        final short[] shortArray = new short[versionParts.length];
        int slot = 0;
        String[] array;
        for (int length = (array = versionParts).length, i = 0; i < length; ++i) {
            final String part = array[i];
            shortArray[slot] = Short.valueOf(part);
            ++slot;
        }
        return shortArray;
    }
    
    public static boolean isClientOutdated(final short[] clientVersion, final short[] latestVersion) {
        final short latestVersionMajor = latestVersion[0];
        final short latestVersionMinor = (short)((latestVersion.length > 1) ? latestVersion[1] : 0);
        final short latestVersionPatch = (short)((latestVersion.length > 2) ? latestVersion[2] : 0);
        final short clientVersionMajor = clientVersion[0];
        final short clientVersionMinor = clientVersion[1];
        final short clientVersionPatch = clientVersion[2];
        return latestVersionMajor > clientVersionMajor || (latestVersionMajor == clientVersionMajor && latestVersionMinor > clientVersionMinor) || (latestVersionMajor == clientVersionMajor && latestVersionMinor == clientVersionMinor && latestVersionPatch > clientVersionPatch);
    }
    
    public String getLatestVersionString() {
        return this.latestVersionString;
    }
    
    public short[] getLatestVersion() {
        return this.latestVersion;
    }
}
