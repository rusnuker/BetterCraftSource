// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import com.google.gson.JsonArray;
import java.net.URLConnection;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import net.labymod.api.LabyModAPI;
import java.io.IOException;
import net.labymod.main.Updater;
import java.util.jar.JarEntry;
import net.labymod.utils.ModUtils;
import java.util.zip.ZipEntry;
import java.util.Scanner;
import java.io.InputStream;
import java.util.jar.JarFile;
import net.labymod.support.util.Debug;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import net.labymod.main.Source;
import net.labymod.addon.online.info.AddonInfo;
import net.labymod.api.LabyModAddon;
import java.lang.reflect.Method;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.io.File;

public class AddonLoader
{
    private static final File addonsDirectory;
    private static final File configDirectory;
    private static final File deleteQueueFile;
    private static List<String> transformerClasses;
    private static Map<UUID, String> mainClasses;
    private static Map<UUID, String> names;
    private static Map<UUID, File> files;
    private static Map<UUID, JsonObject> loadedOffline;
    private static final JsonParser parser;
    private static Method addURL;
    private static List<LabyModAddon> addons;
    private static List<AddonInfo> offlineAddons;
    private static List<String> disabledAddons;
    
    static {
        addonsDirectory = new File("LabyMod/", "addons-" + Source.ABOUT_MC_VERSION.split("\\.")[0] + "." + Source.ABOUT_MC_VERSION.split("\\.")[1]);
        configDirectory = new File(AddonLoader.addonsDirectory, "config");
        deleteQueueFile = new File(AddonLoader.addonsDirectory, ".delete");
        AddonLoader.transformerClasses = null;
        AddonLoader.mainClasses = new HashMap<UUID, String>();
        AddonLoader.names = new HashMap<UUID, String>();
        AddonLoader.files = new HashMap<UUID, File>();
        AddonLoader.loadedOffline = new HashMap<UUID, JsonObject>();
        parser = new JsonParser();
        AddonLoader.addURL = null;
        AddonLoader.addons = new ArrayList<LabyModAddon>();
        AddonLoader.offlineAddons = new ArrayList<AddonInfo>();
        try {
            (AddonLoader.addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class)).setAccessible(true);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
        loadDisabledAddons();
    }
    
    public static List<String> getTransformerClasses(final URLClassLoader classLoader) {
        if (AddonLoader.transformerClasses != null) {
            return AddonLoader.transformerClasses;
        }
        AddonLoader.transformerClasses = new ArrayList<String>();
        if (!AddonLoader.addonsDirectory.exists()) {
            if (!AddonLoader.addonsDirectory.getParentFile().exists()) {
                AddonLoader.addonsDirectory.getParentFile().mkdir();
            }
            AddonLoader.addonsDirectory.mkdirs();
        }
        if (!AddonLoader.configDirectory.exists()) {
            AddonLoader.configDirectory.mkdir();
        }
        checkFilesToDelete();
        final File[] dirFiles;
        final File[] listFiles = dirFiles = AddonLoader.addonsDirectory.listFiles();
        File[] array;
        for (int length = (array = listFiles).length, i = 0; i < length; ++i) {
            final File addonFile = array[i];
            if (addonFile.getName().endsWith(".jar")) {
                if (AddonLoader.disabledAddons != null) {
                    try {
                        final String md5 = getCheckSum(addonFile);
                        Debug.log(Debug.EnumDebugMode.ADDON, "Checksum of " + addonFile.getName() + ": " + md5);
                        if (AddonLoader.disabledAddons.contains(md5) || AddonLoader.disabledAddons.contains(addonFile.getName())) {
                            Debug.log(Debug.EnumDebugMode.ADDON, String.valueOf(addonFile.getName()) + " was blocked by LabyMod! Deleting now..");
                            addonFile.delete();
                            continue;
                        }
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                resolveJarFile(addonFile, classLoader);
            }
        }
        LabyModOFAddon.addOptifineVersion();
        if (System.getProperty("addonresources") != null) {
            final String addonResources = System.getProperty("addonresources");
            Debug.log(Debug.EnumDebugMode.ADDON, "Given addon resources: " + addonResources);
            String[] split;
            for (int length2 = (split = addonResources.split(",")).length, j = 0; j < length2; ++j) {
                final String resource = split[j];
                final InputStream addonResource = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
                Debug.log(Debug.EnumDebugMode.ADDON, "Loading resource " + resource + ": " + (addonResource != null));
                if (addonResource != null) {
                    final String addonResourceContent = getStringByInputStream(addonResource);
                    try {
                        loadByAddonInfo(null, null, addonResourceContent);
                    }
                    catch (final Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        return AddonLoader.transformerClasses;
    }
    
    private static void checkFilesToDelete() {
        try {
            final File file = getDeleteQueueFile();
            if (!file.exists()) {
                return;
            }
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                final File jarFile = new File(AddonLoader.addonsDirectory, line);
                if (jarFile.exists() && jarFile.delete()) {
                    Debug.log(Debug.EnumDebugMode.ADDON, "Addon " + jarFile.getName() + " successfully deleted!");
                }
                else {
                    Debug.log(Debug.EnumDebugMode.ADDON, "Error while deleting addon " + jarFile.getName());
                }
            }
            scanner.close();
            file.delete();
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    public static void resolveJarFile(final File addonFile, final ClassLoader classLoader) {
        try {
            Debug.log(Debug.EnumDebugMode.ADDON, "Adding " + addonFile.getName() + " to runtime..");
            final JarFile jarFile = new JarFile(addonFile);
            if (jarFile.getJarEntry("addon.json") == null) {
                Debug.log(Debug.EnumDebugMode.ADDON, "addon.json not found in " + addonFile.getName());
                jarFile.close();
                return;
            }
            final JarEntry addonJsonFile = jarFile.getJarEntry("addon.json");
            final String fileContent = ModUtils.getStringByInputStream(jarFile.getInputStream(addonJsonFile));
            AddonLoader.addURL.invoke(classLoader, addonFile.toURI().toURL());
            loadByAddonInfo(jarFile, addonFile, fileContent);
            jarFile.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void loadByAddonInfo(final JarFile jarFile, final File addonFile, final String content) throws IOException {
        final JsonObject object = (JsonObject)AddonLoader.parser.parse(content);
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
        boolean offline = false;
        if (object.has("uuid")) {
            final String uuidString = object.get("uuid").getAsString();
            if (uuidString.equals("%uuid%") || (addonFile != null && addonFile.getName().equals("debug.jar"))) {
                uuid = UUID.randomUUID();
                offline = true;
            }
            else {
                try {
                    uuid = UUID.fromString(uuidString);
                }
                catch (final IllegalArgumentException error) {
                    error.printStackTrace();
                    return;
                }
                if (object.has("offline")) {
                    offline = true;
                }
            }
            if (object.has("requiredVersion")) {
                final String requiredVersion = object.get("requiredVersion").getAsString();
                final boolean notCompatible = Updater.isClientOutdated(Updater.CLIENT_VERSION, Updater.getShortVersionOfString(requiredVersion));
                if (notCompatible) {
                    Debug.log(Debug.EnumDebugMode.ADDON, String.valueOf(name) + " addon requires LabyMod v" + requiredVersion + " but client is still on v" + "3.6.6" + "!");
                    if (jarFile != null) {
                        jarFile.close();
                    }
                    return;
                }
            }
            if (jarFile == null || offline) {
                AddonLoader.loadedOffline.put(uuid, object);
            }
            if (object.has("transformerClass")) {
                final String transformer = object.get("transformerClass").getAsString();
                AddonLoader.transformerClasses.add(transformer);
                Debug.log(Debug.EnumDebugMode.ADDON, "Added transformer: " + transformer);
            }
            if (object.has("mainClass")) {
                final String entryName = (name == null) ? addonFile.getName().substring(0, addonFile.getName().lastIndexOf(46)) : name;
                AddonLoader.mainClasses.put(uuid, object.get("mainClass").getAsString());
                AddonLoader.names.put(uuid, entryName);
                AddonLoader.files.put(uuid, addonFile);
            }
            else {
                Debug.log(Debug.EnumDebugMode.ADDON, "Main class not found of " + name);
            }
            if (jarFile != null) {
                jarFile.close();
            }
            return;
        }
        if (jarFile != null) {
            Debug.log(Debug.EnumDebugMode.ADDON, "UUID not set in " + name);
            jarFile.close();
        }
    }
    
    public static void enableAddons(final LabyModAPI labyModAPI) {
        if (AddonLoader.mainClasses.size() == 0) {
            Debug.log(Debug.EnumDebugMode.ADDON, "No addons found!");
            return;
        }
        for (final UUID mainClassEntry : AddonLoader.mainClasses.keySet()) {
            enableAddon(mainClassEntry, labyModAPI);
        }
        AddonLoader.mainClasses.clear();
    }
    
    public static LabyModAddon enableAddon(final UUID addonUUID, final LabyModAPI labyModAPI) {
        final String mainClassName = AddonLoader.mainClasses.get(addonUUID);
        final String addonName = AddonLoader.names.get(addonUUID);
        if (mainClassName == null || addonName == null) {
            Debug.log(Debug.EnumDebugMode.ADDON, "Addon " + addonUUID + " is not resolved!");
            return null;
        }
        try {
            final Class<?> mainClass = Class.forName(mainClassName);
            if (mainClass.isAssignableFrom(LabyModAddon.class)) {
                Debug.log(Debug.EnumDebugMode.ADDON, "Main-class " + mainClassName + " of addon " + addonName + " isn't a valid addon-class!");
                return null;
            }
            final LabyModAddon addon = (LabyModAddon)mainClass.newInstance();
            addon.api = labyModAPI;
            Debug.log(Debug.EnumDebugMode.ADDON, "Enabling and init addon " + addonName + "...");
            final long curr = System.currentTimeMillis();
            AddonLoader.addons.add(addon);
            addon.onEnable();
            addon.init(addonName, addonUUID);
            if (AddonLoader.loadedOffline.containsKey(addonUUID)) {
                final JsonObject jsonObject = AddonLoader.loadedOffline.get(addonUUID);
                final int version = jsonObject.has("version") ? jsonObject.get("version").getAsInt() : 1;
                final String author = jsonObject.has("author") ? jsonObject.get("author").getAsString() : "Unknown";
                final String description = jsonObject.has("description") ? jsonObject.get("description").getAsString() : "No description for this addon.";
                final int category = jsonObject.has("category") ? jsonObject.get("category").getAsInt() : 1;
                final String icon = jsonObject.has("icon") ? jsonObject.get("icon").getAsString() : null;
                final AddonInfo addonInfo = new AddonInfo(addonUUID, addonName, version, author, description, category);
                addonInfo.setOfflineIcon(icon);
                AddonLoader.offlineAddons.add(addonInfo);
            }
            Debug.log(Debug.EnumDebugMode.ADDON, "Successfull loaded addon " + addonName + "! (took " + (System.currentTimeMillis() - curr) + " ms)");
            return addon;
        }
        catch (final Throwable e) {
            Debug.log(Debug.EnumDebugMode.ADDON, "Failed enabling addon " + addonName + ":");
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean hasInstalled(final AddonInfo addonInfo) {
        for (final LabyModAddon installedAddon : AddonLoader.addons) {
            if (addonInfo != null && addonInfo.getUuid() != null && installedAddon != null && installedAddon.about != null && installedAddon.about.uuid != null && addonInfo.getUuid().equals(installedAddon.about.uuid)) {
                return true;
            }
        }
        return false;
    }
    
    public static LabyModAddon getInstalledAddonByInfo(final AddonInfo addonInfo) {
        if (addonInfo != null) {
            for (final LabyModAddon installedAddon : AddonLoader.addons) {
                if (installedAddon != null && installedAddon.about != null && addonInfo.getUuid().equals(installedAddon.about.uuid)) {
                    return installedAddon;
                }
            }
        }
        return null;
    }
    
    private static String getStringByInputStream(final InputStream inputStream) {
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
    
    private static void loadDisabledAddons() {
        try {
            final URLConnection connection = new URL("http://dl.labymod.net/disabled_addons.json").openConnection();
            connection.setRequestProperty("User-Agent", Source.getUserAgent());
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.connect();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json = String.valueOf(json) + (json.equals("") ? "" : "\n") + line;
            }
            if (json.isEmpty()) {
                return;
            }
            final List<String> list = new ArrayList<String>();
            final JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            if (array.size() == 0) {
                return;
            }
            final Iterator<JsonElement> iterator = array.iterator();
            while (iterator.hasNext()) {
                final String hash = iterator.next().getAsString();
                list.add(hash);
            }
            AddonLoader.disabledAddons = list;
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    public static LabyModAddon getAddonByUUID(final UUID uuid) {
        for (final LabyModAddon addon : AddonLoader.addons) {
            if (addon != null && addon.about != null && addon.about.uuid != null && addon.about.uuid.equals(uuid)) {
                return addon;
            }
        }
        return null;
    }
    
    public static UUID getUUIDByClass(final Class<?> type) {
        final File targetFile = getJarFileByClass(type);
        if (targetFile == null) {
            return null;
        }
        final String targetPath = targetFile.getAbsolutePath();
        for (final Map.Entry<UUID, File> entry : AddonLoader.files.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            final String path = entry.getValue().getAbsolutePath();
            if (path.equals(targetPath)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static File getJarFileByClass(final Class<?> type) {
        final URL resource = type.getResource(String.format("/%s.class", type.getName().replace(".", "/")));
        if (resource == null) {
            return null;
        }
        final String uri = resource.toString();
        if (!uri.startsWith("jar:file:")) {
            return null;
        }
        final int idx = uri.indexOf(33);
        if (idx == -1) {
            return null;
        }
        try {
            final String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx), Charset.defaultCharset().name());
            return new File(fileName);
        }
        catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static String getCheckSum(final File file) throws Exception {
        final FileInputStream fis = new FileInputStream(file);
        final String md5 = DigestUtils.md5Hex(fis);
        fis.close();
        return md5;
    }
    
    public static File getAddonsDirectory() {
        return AddonLoader.addonsDirectory;
    }
    
    public static File getConfigDirectory() {
        return AddonLoader.configDirectory;
    }
    
    public static File getDeleteQueueFile() {
        return AddonLoader.deleteQueueFile;
    }
    
    public static Map<UUID, File> getFiles() {
        return AddonLoader.files;
    }
    
    public static List<LabyModAddon> getAddons() {
        return AddonLoader.addons;
    }
    
    public static List<AddonInfo> getOfflineAddons() {
        return AddonLoader.offlineAddons;
    }
}
