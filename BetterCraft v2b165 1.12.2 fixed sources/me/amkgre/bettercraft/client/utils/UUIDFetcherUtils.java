// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.function.Consumer;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.lang.reflect.Type;
import com.mojang.util.UUIDTypeAdapter;
import com.google.gson.GsonBuilder;
import java.util.concurrent.ExecutorService;
import java.util.UUID;
import java.util.Map;
import com.google.gson.Gson;

public class UUIDFetcherUtils
{
    public static final long FEBRUARY_2015 = 1422748800000L;
    private static Gson gson;
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static Map<String, UUID> uuidCache;
    private static Map<UUID, String> nameCache;
    private static ExecutorService pool;
    private String name;
    private UUID id;
    
    static {
        UUIDFetcherUtils.gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
        UUIDFetcherUtils.uuidCache = new HashMap<String, UUID>();
        UUIDFetcherUtils.nameCache = new HashMap<UUID, String>();
        UUIDFetcherUtils.pool = Executors.newCachedThreadPool();
    }
    
    public static void getUUID(final String name, final Consumer<UUID> action) {
        UUIDFetcherUtils.pool.execute(() -> consumer.accept(getUUID(name2)));
    }
    
    public static UUID getUUID(final String name) {
        return getUUIDAt(name, System.currentTimeMillis());
    }
    
    public static void getUUIDAt(final String name, final long timestamp, final Consumer<UUID> action) {
        UUIDFetcherUtils.pool.execute(() -> consumer.accept(getUUIDAt(name2, timestamp2)));
    }
    
    public static UUID getUUIDAt(String name, final long timestamp) {
        if (UUIDFetcherUtils.uuidCache.containsKey(name = name.toLowerCase())) {
            return UUIDFetcherUtils.uuidCache.get(name);
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, timestamp / 1000L)).openConnection();
            connection.setReadTimeout(5000);
            final UUIDFetcherUtils data = UUIDFetcherUtils.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcherUtils.class);
            UUIDFetcherUtils.uuidCache.put(name, data.id);
            UUIDFetcherUtils.nameCache.put(data.id, data.name);
            return data.id;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void getName(final UUID uuid, final Consumer<String> action) {
        UUIDFetcherUtils.pool.execute(() -> consumer.accept(getName(uuid2)));
    }
    
    public static String getName(final UUID uuid) {
        if (UUIDFetcherUtils.nameCache.containsKey(uuid)) {
            return UUIDFetcherUtils.nameCache.get(uuid);
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/user/profiles/%s/names", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);
            final UUIDFetcherUtils[] nameHistory = UUIDFetcherUtils.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcherUtils[].class);
            final UUIDFetcherUtils currentNameData = nameHistory[nameHistory.length - 1];
            UUIDFetcherUtils.uuidCache.put(currentNameData.name.toLowerCase(), uuid);
            UUIDFetcherUtils.nameCache.put(uuid, currentNameData.name);
            return currentNameData.name;
        }
        catch (final Exception e) {
            return "Bert";
        }
    }
}
