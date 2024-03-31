// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon;

import net.labymod.settings.elements.SettingsElement;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import com.google.gson.JsonObject;
import java.util.zip.ZipEntry;
import java.util.jar.JarFile;
import net.labymod.support.util.Debug;
import net.labymod.core.asm.LabyModCoreMod;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.util.HashMap;
import com.google.gson.JsonParser;
import java.io.File;
import java.util.UUID;
import java.util.Map;
import net.labymod.api.LabyModAddon;

public class LabyModOFAddon extends LabyModAddon
{
    public static final Map<String, UUID> OPTIFINE_VERSIONS;
    private static final File FILE_OF_HANDLER;
    private static final JsonParser parser;
    private static File OPTIFINE_FORGE;
    public static boolean INSTALL;
    public static String INSTALLED_VERSION;
    
    static {
        OPTIFINE_VERSIONS = new HashMap<String, UUID>() {
            {
                this.put("1.8.9", UUID.fromString("2cc09032-995f-4b57-a2a1-f1399addbb21"));
                this.put("1.12.2", UUID.fromString("7d62bffd-fe3f-4667-8200-e8decb384fa0"));
            }
        };
        FILE_OF_HANDLER = new File("LabyMod/ofhandler/", "ofhandler.jar");
        parser = new JsonParser();
        LabyModOFAddon.OPTIFINE_FORGE = null;
        LabyModOFAddon.INSTALL = false;
        LabyModOFAddon.INSTALLED_VERSION = null;
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void init(final String addonName, final UUID uuid) {
        this.about = new About(uuid, addonName);
        this.about.loaded = true;
        LabyModOFAddon.INSTALLED_VERSION = ((addonName == null) ? null : addonName.replaceAll("_", ""));
    }
    
    public static void downloadOFHandler() {
        if (!LabyModOFAddon.FILE_OF_HANDLER.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LabyModOFAddon.FILE_OF_HANDLER.getParentFile().mkdir();
                        final FileOutputStream fos = new FileOutputStream(LabyModOFAddon.FILE_OF_HANDLER);
                        final HttpURLConnection web = (HttpURLConnection)new URL("http://dl.labymod.net/latest/install/ofhandler.jar").openConnection();
                        web.setRequestProperty("User-Agent", Source.getUserAgent());
                        final ReadableByteChannel readableByteChannel = Channels.newChannel(web.getInputStream());
                        fos.getChannel().transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
                        fos.close();
                    }
                    catch (final Exception error) {
                        error.printStackTrace();
                    }
                }
            }).start();
        }
    }
    
    public static void executeOfHandler(final boolean installOptifine) {
        final boolean isVanillaForge = !LabyModCoreMod.isForge();
        try {
            Debug.log(Debug.EnumDebugMode.ADDON, installOptifine ? (isVanillaForge ? "Installing optifine for vanillaforge" : "Uninstall optifine for vanillaforge") : (isVanillaForge ? "Installing optifine for forge" : "Uninstall optifine for forge"));
            if (isVanillaForge) {
                if (!LabyModOFAddon.FILE_OF_HANDLER.exists()) {
                    Debug.log(Debug.EnumDebugMode.ADDON, "OfHandler not found:" + LabyModOFAddon.FILE_OF_HANDLER.getAbsolutePath());
                    return;
                }
                if (Source.RUNNING_JAR == null) {
                    Debug.log(Debug.EnumDebugMode.ADDON, "Can't execute ofhandler: Running jar not found!");
                    return;
                }
                if (!Source.RUNNING_JAR.getName().endsWith(".jar")) {
                    Debug.log(Debug.EnumDebugMode.ADDON, "Can't execute ofhandler: " + Source.RUNNING_JAR.getAbsolutePath() + " is not a jar file!");
                    return;
                }
                final String argument = "java -jar " + LabyModOFAddon.FILE_OF_HANDLER.getAbsolutePath() + " " + installOptifine + " " + Source.ABOUT_MC_VERSION + " " + Source.RUNNING_JAR.getAbsolutePath();
                Debug.log(Debug.EnumDebugMode.ADDON, "Execute ofhandler: " + argument);
                Runtime.getRuntime().exec(argument);
                Debug.log(Debug.EnumDebugMode.ADDON, "OfHandler executed!");
            }
            else if (installOptifine) {
                final File optifineMods = new File("mods/", "optifine.jar");
                final File optifineTemp = new File("LabyMod/ofhandler/", "optifine.jar");
                Debug.log(Debug.EnumDebugMode.ADDON, "Copy " + optifineTemp.getAbsolutePath() + " to " + optifineMods.getAbsolutePath());
                if (optifineTemp.exists()) {
                    optifineTemp.renameTo(optifineMods);
                }
                new File("LabyMod/ofhandler/").delete();
            }
            else if (LabyModOFAddon.OPTIFINE_FORGE != null) {
                if (!LabyModOFAddon.FILE_OF_HANDLER.exists()) {
                    Debug.log(Debug.EnumDebugMode.ADDON, "OfHandler not found:" + LabyModOFAddon.FILE_OF_HANDLER.getAbsolutePath());
                    return;
                }
                final String argument = "java -jar " + LabyModOFAddon.FILE_OF_HANDLER.getAbsolutePath() + " del " + LabyModOFAddon.OPTIFINE_FORGE.getAbsolutePath();
                Debug.log(Debug.EnumDebugMode.ADDON, "Execute ofhandler: " + argument);
                Runtime.getRuntime().exec(argument);
                Debug.log(Debug.EnumDebugMode.ADDON, "OfHandler executed!");
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addOptifineVersion() {
        if (LabyModOFAddon.FILE_OF_HANDLER.getParentFile().exists()) {
            LabyModOFAddon.FILE_OF_HANDLER.getParentFile().delete();
        }
        if (LabyModCoreMod.isForge()) {
            final File modsFolder = new File("mods/");
            if (!modsFolder.exists()) {
                return;
            }
            File[] listFiles;
            for (int length = (listFiles = modsFolder.listFiles()).length, i = 0; i < length; ++i) {
                final File mod = listFiles[i];
                if (mod.getName().endsWith(".jar")) {
                    try {
                        final JarFile jarFile = new JarFile(mod);
                        if (jarFile.getJarEntry("changelog.txt") == null || jarFile.getJarEntry("buildof.txt") == null) {
                            jarFile.close();
                        }
                        else {
                            addOptifineUsingJarFile(jarFile);
                            LabyModOFAddon.OPTIFINE_FORGE = mod;
                        }
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static void addOptifineByVanillaForge(final String version) {
        if (LabyModOFAddon.FILE_OF_HANDLER.getParentFile().exists()) {
            LabyModOFAddon.FILE_OF_HANDLER.getParentFile().delete();
        }
        try {
            Debug.log(Debug.EnumDebugMode.ADDON, "Detected game version: " + version);
            if (version == null || version.isEmpty()) {
                Debug.log(Debug.EnumDebugMode.ADDON, "Can't check installed optifine version: version is not present!");
                return;
            }
            final File runningJar = new File("versions/" + version + "/" + version + ".jar");
            if (!runningJar.exists()) {
                Debug.log(Debug.EnumDebugMode.ADDON, "Can't check installed optifine version: running jar file " + runningJar.getAbsolutePath() + " doesn't exists!");
                return;
            }
            Source.RUNNING_JAR = runningJar;
            final JarFile jarFile = new JarFile(runningJar);
            if (jarFile.getJarEntry("changelog.txt") == null || jarFile.getJarEntry("buildof.txt") == null) {
                Debug.log(Debug.EnumDebugMode.ADDON, "Optifine is not installed");
                jarFile.close();
                return;
            }
            addOptifineUsingJarFile(jarFile);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addOptifineUsingJarFile(final JarFile jarFile) throws Exception {
        String fileContent = null;
        String changeLogContents = getStringByInputStream(jarFile.getInputStream(jarFile.getJarEntry("changelog.txt")));
        changeLogContents = changeLogContents.replace("\r", "");
        String[] split;
        for (int length = (split = changeLogContents.split("\n")).length, i = 0; i < length; ++i) {
            final String line = split[i];
            if (line.startsWith("OptiFine ")) {
                final String version = line.split("OptiFine ")[1].split("_")[0];
                if (version != null && !version.isEmpty() && LabyModOFAddon.OPTIFINE_VERSIONS.containsKey(version)) {
                    fileContent = "{\"uuid\":\"" + LabyModOFAddon.OPTIFINE_VERSIONS.get(version) + "\",\"name\":\"" + line + "\"}";
                    break;
                }
            }
        }
        jarFile.close();
        if (fileContent == null) {
            return;
        }
        final JsonObject object = (JsonObject)LabyModOFAddon.parser.parse(fileContent);
        String name = null;
        UUID uuid = null;
        if (!object.has("name")) {
            if (jarFile != null) {
                Debug.log(Debug.EnumDebugMode.ADDON, "Name not set in " + jarFile.getName());
                jarFile.close();
            }
            return;
        }
        name = object.get("name").getAsString();
        if (object.has("uuid")) {
            uuid = UUID.fromString(object.get("uuid").getAsString());
            Debug.log(Debug.EnumDebugMode.ADDON, "Optifine is installed!");
            final LabyModOFAddon ofAddon = new LabyModOFAddon();
            ofAddon.init(name, uuid);
            AddonLoader.getAddons().add(ofAddon);
            return;
        }
        if (jarFile != null) {
            Debug.log(Debug.EnumDebugMode.ADDON, "UUID not set in " + name);
            jarFile.close();
        }
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
    
    @Override
    public void loadConfig() {
    }
    
    @Override
    protected void fillSettings(final List<SettingsElement> subSettings) {
    }
}
