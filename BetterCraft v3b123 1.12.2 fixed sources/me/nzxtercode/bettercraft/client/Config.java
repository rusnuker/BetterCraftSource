// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client;

import java.io.Writer;
import java.io.FileWriter;
import java.io.Reader;
import com.google.gson.JsonParser;
import java.io.FileReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.File;
import com.google.gson.GsonBuilder;

public class Config
{
    private static final Config INSTANCE;
    private static final GsonBuilder GSON_BUILDER;
    public static File ROOT_DIR;
    public static File ADDON_DIR;
    private static File configFile;
    private static File settingsFile;
    private static File miscFile;
    private static File musicFile;
    private static File cosmeticsFile;
    private static File modsFile;
    private static File backgroundFile;
    private static File colorFile;
    
    static {
        INSTANCE = new Config();
        GSON_BUILDER = new GsonBuilder();
        BetterCraft.getInstance();
        Config.ROOT_DIR = new File(BetterCraft.clientName);
        BetterCraft.getInstance();
        Config.ADDON_DIR = new File(BetterCraft.clientName, "Addons");
        BetterCraft.getInstance();
        Config.configFile = new File(BetterCraft.clientName, "config.json");
        BetterCraft.getInstance();
        Config.settingsFile = new File(BetterCraft.clientName, "settings.json");
        BetterCraft.getInstance();
        Config.miscFile = new File(BetterCraft.clientName, "misc.json");
        BetterCraft.getInstance();
        Config.musicFile = new File(BetterCraft.clientName, "music.json");
        BetterCraft.getInstance();
        Config.cosmeticsFile = new File(BetterCraft.clientName, "cosmetics.json");
        BetterCraft.getInstance();
        Config.modsFile = new File(BetterCraft.clientName, "mods.json");
        BetterCraft.getInstance();
        Config.backgroundFile = new File(BetterCraft.clientName, "background.json");
        BetterCraft.getInstance();
        Config.colorFile = new File(BetterCraft.clientName, "colors.json");
    }
    
    public static final Config getInstance() {
        return Config.INSTANCE;
    }
    
    public static void init() {
        if (!Config.ROOT_DIR.exists()) {
            Config.ROOT_DIR.mkdirs();
        }
        if (!Files.exists(Paths.get(Config.configFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.settingsFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.miscFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.musicFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.cosmeticsFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.modsFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.backgroundFile.getPath(), new String[0]), new LinkOption[0]) && !Files.exists(Paths.get(Config.colorFile.getPath(), new String[0]), new LinkOption[0])) {
            try {
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/config.json"), Paths.get(Config.configFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/settings.json"), Paths.get(Config.settingsFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/misc.json"), Paths.get(Config.miscFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/music.json"), Paths.get(Config.musicFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/cosmetics.json"), Paths.get(Config.cosmeticsFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/mods.json"), Paths.get(Config.modsFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/background.json"), Paths.get(Config.backgroundFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Config.class.getResourceAsStream("/assets/minecraft/client/jsons/colors.json"), Paths.get(Config.colorFile.getPath(), new String[0]), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (final IOException ex) {}
        }
    }
    
    public void editConfig(final String name, final Consumer<JsonObject> consumer) {
        final JsonObject json = read(Config.configFile).getAsJsonObject();
        final JsonObject jsonObject = json.has(name) ? json.get(name).getAsJsonObject() : new JsonObject();
        consumer.accept(jsonObject);
        json.add(name, jsonObject);
        write(Config.configFile, json);
    }
    
    public final JsonObject getConfig(final String name) {
        final JsonObject json = read(Config.configFile).getAsJsonObject();
        return json.get(name).getAsJsonObject();
    }
    
    public void editMisc(final String name, final Consumer<JsonObject> consumer) {
        final JsonObject json = read(Config.miscFile).getAsJsonObject();
        final JsonObject jsonObject = json.has(name) ? json.get(name).getAsJsonObject() : new JsonObject();
        consumer.accept(jsonObject);
        json.add(name, jsonObject);
        write(Config.miscFile, json);
    }
    
    public final JsonObject getMisc(final String name) {
        final JsonObject json = read(Config.miscFile).getAsJsonObject();
        return json.get(name).getAsJsonObject();
    }
    
    public void editSettings(final String name, final Consumer<JsonObject> consumer) {
        final JsonObject json = read(Config.settingsFile).getAsJsonObject();
        final JsonObject jsonObject = json.has(name) ? json.get(name).getAsJsonObject() : new JsonObject();
        consumer.accept(jsonObject);
        json.add(name, jsonObject);
        write(Config.settingsFile, json);
    }
    
    public final JsonObject getSettings(final String name) {
        final JsonObject json = read(Config.settingsFile).getAsJsonObject();
        return json.get(name).getAsJsonObject();
    }
    
    public void editMusic(final String name, final Consumer<JsonObject> consumer) {
        final JsonObject json = read(Config.musicFile).getAsJsonObject();
        final JsonObject jsonObject = json.has(name) ? json.get(name).getAsJsonObject() : new JsonObject();
        consumer.accept(jsonObject);
        json.add(name, jsonObject);
        write(Config.musicFile, json);
    }
    
    public final JsonObject getMusic(final String name) {
        final JsonObject json = read(Config.musicFile).getAsJsonObject();
        return json.get(name).getAsJsonObject();
    }
    
    public void editBackground(final String name, final Consumer<JsonObject> consumer) {
        final JsonObject json = read(Config.backgroundFile).getAsJsonObject();
        final JsonObject jsonObject = json.has(name) ? json.get(name).getAsJsonObject() : new JsonObject();
        consumer.accept(jsonObject);
        json.add(name, jsonObject);
        write(Config.backgroundFile, json);
    }
    
    public final JsonObject getBackground(final String name) {
        final JsonObject json = read(Config.backgroundFile).getAsJsonObject();
        return json.get(name).getAsJsonObject();
    }
    
    public void editColor(final String name, final Consumer<JsonObject> consumer) {
        final JsonObject json = read(Config.colorFile).getAsJsonObject();
        final JsonObject jsonObject = json.has(name) ? json.get(name).getAsJsonObject() : new JsonObject();
        consumer.accept(jsonObject);
        json.add(name, jsonObject);
        write(Config.colorFile, json);
    }
    
    public final JsonObject getColor(final String name) {
        final JsonObject json = read(Config.colorFile).getAsJsonObject();
        return json.get(name).getAsJsonObject();
    }
    
    public static final JsonElement read(final File file) {
        try {
            final Reader reader = new FileReader(file);
            final JsonElement jsonElement = new JsonParser().parse(reader);
            reader.close();
            return jsonElement;
        }
        catch (final Throwable throwable) {
            return null;
        }
    }
    
    public static final void write(final File file, final JsonElement jsonObject) {
        try {
            final Writer writer = new FileWriter(file);
            writer.write(Config.GSON_BUILDER.setPrettyPrinting().create().toJson(jsonObject).toString());
            writer.close();
        }
        catch (final Throwable t) {}
    }
}
