// 
// Decompiled by Procyon v0.6.0
// 

package org.javapluginapi.team;

import java.util.Enumeration;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Paths;
import java.lang.reflect.Constructor;
import org.javapluginapi.team.api.PluginDescription;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.net.URL;
import org.javapluginapi.team.api.PluginException;
import com.google.gson.GsonBuilder;
import org.javapluginapi.team.api.Plugin;
import java.io.File;
import java.util.HashMap;
import com.google.gson.Gson;

public class PluginLoader
{
    private final PluginLogger logger;
    private static final Gson GSON;
    public final HashMap<File, Plugin> map;
    
    static {
        GSON = new GsonBuilder().serializeNulls().create();
    }
    
    public PluginLoader() {
        this.logger = new PluginLogger();
        this.map = new HashMap<File, Plugin>();
    }
    
    public Plugin load(final File file) {
        if (!file.getName().endsWith(".jar")) {
            throw new PluginException("File have to be a Jar! " + file.getName());
        }
        try {
            if (this.map.containsKey(file)) {
                throw new PluginException(String.valueOf(file.getName()) + " " + "Plugin already loaded.");
            }
            final PluginDescription pluginDescriptionFile = this.getPluginDescriptionFile(file);
            final ClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() }, this.getClass().getClassLoader());
            final Class<?> clazz = Class.forName(pluginDescriptionFile.getMain(), true, loader);
            final Class<? extends Plugin> instanceClass = clazz.asSubclass(Plugin.class);
            final Constructor<? extends Plugin> instanceClassConstructor = instanceClass.getConstructor((Class<?>[])new Class[0]);
            final Plugin plugin = (Plugin)instanceClassConstructor.newInstance(new Object[0]);
            plugin.setDescriptionFile(pluginDescriptionFile);
            this.map.put(file, plugin);
            plugin.onEnable();
            return plugin;
        }
        catch (final MalformedURLException e) {
            throw new PluginException("Failed to convert the file path to a URL.", e);
        }
        catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
            throw new PluginException("Failed to create a new instance of the plugin.", e2);
        }
    }
    
    public Plugin loadFromURL(final String url, final File filePath) {
        InputStream in = null;
        try {
            in = new URL(url).openStream();
            Files.copy(in, Paths.get(filePath.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        final File file = Paths.get(filePath.getPath(), new String[0]).toFile();
        if (!file.getName().endsWith(".jar")) {
            throw new PluginException("File have to be a Jar! " + file.getName());
        }
        try {
            if (this.map.containsKey(file)) {
                throw new PluginException(String.valueOf(file.getName()) + " " + "Plugin already loaded.");
            }
            final PluginDescription pluginDescriptionFile = this.getPluginDescriptionFile(file);
            final ClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() }, this.getClass().getClassLoader());
            final Class<?> clazz = Class.forName(pluginDescriptionFile.getMain(), true, loader);
            final Class<? extends Plugin> instanceClass = clazz.asSubclass(Plugin.class);
            final Constructor<? extends Plugin> instanceClassConstructor = instanceClass.getConstructor((Class<?>[])new Class[0]);
            final Plugin plugin = (Plugin)instanceClassConstructor.newInstance(new Object[0]);
            plugin.setDescriptionFile(pluginDescriptionFile);
            this.map.put(file, plugin);
            plugin.onEnable();
            return plugin;
        }
        catch (final MalformedURLException e2) {
            throw new PluginException("Failed to convert the file path to a URL.", e2);
        }
        catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e3) {
            throw new PluginException("Failed to create a new instance of the plugin.", e3);
        }
    }
    
    public Plugin unload(final File file) {
        if (!file.getName().endsWith(".jar")) {
            throw new PluginException("File have to be a Jar! " + file.getName());
        }
        if (!this.map.containsKey(file)) {
            throw new PluginException("Can't unload a Plugin that wasn't loaded in the first place.");
        }
        final Plugin plugin = this.map.get(file);
        plugin.onDisable();
        this.map.remove(file);
        return plugin;
    }
    
    public void reload(final File file) {
        this.unload(file);
        this.load(file);
    }
    
    public PluginDescription getPluginDescriptionFile(final File file) {
        try {
            final ZipFile zipFile = new ZipFile(file);
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            PluginDescription pluginJson = null;
            while (entries.hasMoreElements() && pluginJson == null) {
                final ZipEntry entry = (ZipEntry)entries.nextElement();
                if (!entry.isDirectory() && entry.getName().equals("plugin.json")) {
                    final InputStream stream = zipFile.getInputStream(entry);
                    try {
                        pluginJson = PluginLoader.GSON.fromJson(new InputStreamReader(stream), PluginDescription.class);
                    }
                    catch (final JsonParseException jsonParseException) {
                        throw new PluginException("Failed to parse JSON:", jsonParseException);
                    }
                }
            }
            if (pluginJson == null) {
                zipFile.close();
                throw new PluginException("Failed to find plugin.json in the root of the jar.");
            }
            zipFile.close();
            return pluginJson;
        }
        catch (final IOException e) {
            throw new PluginException("Failed to open the jar as a zip:", e);
        }
    }
}
