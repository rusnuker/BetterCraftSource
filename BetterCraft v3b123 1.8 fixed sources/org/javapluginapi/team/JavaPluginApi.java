// 
// Decompiled by Procyon v0.6.0
// 

package org.javapluginapi.team;

import org.javapluginapi.team.api.Plugin;
import org.javapluginapi.team.api.PluginDescription;
import java.util.Objects;
import java.util.function.Consumer;
import me.nzxtercode.bettercraft.client.BetterCraft;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class JavaPluginApi
{
    private static final Logger logger;
    private static JavaPluginApi api1;
    public static PluginLoader loader;
    public static String outName;
    private static final File PLUGIN_DIR;
    
    static {
        logger = LogManager.getLogger();
        JavaPluginApi.api1 = getApi(pluginloader -> new PluginLoader());
        JavaPluginApi.outName = "[Plugin] ";
        BetterCraft.getInstance();
        PLUGIN_DIR = new File(BetterCraft.clientName, "plugins");
    }
    
    public static JavaPluginApi getApi(final Consumer<PluginLoader> consumer) {
        consumer.accept(JavaPluginApi.loader = new PluginLoader());
        return (JavaPluginApi.api1 == null) ? new JavaPluginApi() : JavaPluginApi.api1;
    }
    
    public static final JavaPluginApi getInstance() {
        return JavaPluginApi.api1;
    }
    
    public static void init() {
        if (!JavaPluginApi.PLUGIN_DIR.exists()) {
            JavaPluginApi.PLUGIN_DIR.mkdir();
        }
        JavaPluginApi.api1.loadAll(JavaPluginApi.PLUGIN_DIR);
    }
    
    public static void stopPlugin() {
        JavaPluginApi.api1.unloadAll(JavaPluginApi.PLUGIN_DIR);
    }
    
    public File[] loadAll(final File pluginFolder) {
        File[] array;
        for (int length = (array = Objects.requireNonNull(pluginFolder.listFiles())).length, i = 0; i < length; ++i) {
            final File fileIndex = array[i];
            if (fileIndex.getName().endsWith(".jar")) {
                JavaPluginApi.loader.load(fileIndex);
                JavaPluginApi.logger.info(String.valueOf(JavaPluginApi.outName) + "Load (Datei: " + fileIndex.getName() + " Name: " + Plugin.descriptionFile.get().getName() + " Author: " + Plugin.descriptionFile.get().getAuthor() + " Version: " + Plugin.descriptionFile.get().getVersion() + ")");
            }
        }
        final Logger logger = JavaPluginApi.logger;
        final StringBuilder sb = new StringBuilder(String.valueOf(JavaPluginApi.outName));
        getInstance();
        logger.info(sb.append(JavaPluginApi.loader.map.size()).append(" loaded").toString());
        return pluginFolder.listFiles();
    }
    
    public File[] unloadAll(final File pluginFolder) {
        File[] array;
        for (int length = (array = Objects.requireNonNull(pluginFolder.listFiles())).length, i = 0; i < length; ++i) {
            final File fileIndex = array[i];
            JavaPluginApi.loader.unload(fileIndex);
            JavaPluginApi.logger.info(String.valueOf(JavaPluginApi.outName) + "Unload (Datei: " + fileIndex.getName() + " Name: " + Plugin.descriptionFile.get().getName() + " Author: " + Plugin.descriptionFile.get().getAuthor() + " Version: " + Plugin.descriptionFile.get().getVersion() + ")");
        }
        final Logger logger = JavaPluginApi.logger;
        final StringBuilder sb = new StringBuilder(String.valueOf(JavaPluginApi.outName));
        getInstance();
        logger.info(sb.append(JavaPluginApi.loader.map.size()).append(" unloaded").toString());
        return pluginFolder.listFiles();
    }
    
    public File[] reloadAll(final File pluginFolder) {
        File[] array;
        for (int length = (array = Objects.requireNonNull(pluginFolder.listFiles())).length, i = 0; i < length; ++i) {
            final File fileIndex = array[i];
            JavaPluginApi.loader.reload(fileIndex);
            JavaPluginApi.logger.info(String.valueOf(JavaPluginApi.outName) + "Reload (Datei: " + fileIndex.getName() + " Name: " + Plugin.descriptionFile.get().getName() + " Author: " + Plugin.descriptionFile.get().getAuthor() + " Version: " + Plugin.descriptionFile.get().getVersion() + ")");
        }
        final Logger logger = JavaPluginApi.logger;
        final StringBuilder sb = new StringBuilder(String.valueOf(JavaPluginApi.outName));
        getInstance();
        logger.info(sb.append(JavaPluginApi.loader.map.size()).append(" reloaded").toString());
        return pluginFolder.listFiles();
    }
}
