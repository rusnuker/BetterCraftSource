// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

import net.labymod.ingamechat.namehistory.NameHistory;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.lang.reflect.Type;
import com.mojang.util.UUIDTypeAdapter;
import com.google.gson.GsonBuilder;
import java.util.concurrent.ExecutorService;
import java.util.UUID;
import java.util.Map;
import com.google.gson.Gson;

public class UUIDFetcher
{
    public static final long FEBRUARY_2015 = 1422748800000L;
    private static Gson gson;
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static Map<String, UUID> uuidCache;
    private static Map<UUID, String> nameCache;
    private static ExecutorService pool;
    public String name;
    public UUID id;
    public long changedToAt;
    
    static {
        UUIDFetcher.gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
        UUIDFetcher.uuidCache = new HashMap<String, UUID>();
        UUIDFetcher.nameCache = new HashMap<UUID, String>();
        UUIDFetcher.pool = Executors.newCachedThreadPool();
    }
    
    public static void getUUID(final String name, final Consumer<UUID> action) {
        UUIDFetcher.pool.execute(new Runnable() {
            @Override
            public void run() {
                action.accept(UUIDFetcher.getUUID(name));
            }
        });
    }
    
    public static UUID getUUID(final String name) {
        return getUUIDAt(name, System.currentTimeMillis());
    }
    
    public static void getUUIDAt(final String name, final long timestamp, final Consumer<UUID> action) {
        UUIDFetcher.pool.execute(new Runnable() {
            @Override
            public void run() {
                action.accept(UUIDFetcher.getUUIDAt(name, timestamp));
            }
        });
    }
    
    public static UUID getUUIDAt(String name, final long timestamp) {
        name = name.toLowerCase();
        if (UUIDFetcher.uuidCache.containsKey(name)) {
            return UUIDFetcher.uuidCache.get(name);
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, timestamp / 1000L)).openConnection();
            connection.setReadTimeout(5000);
            final UUIDFetcher data = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);
            if (data == null) {
                return null;
            }
            UUIDFetcher.uuidCache.put(name, data.id);
            UUIDFetcher.nameCache.put(data.id, data.name);
            return data.id;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getCorrectUsername(String name, final long timestamp) {
        name = name.toLowerCase();
        if (UUIDFetcher.uuidCache.containsKey(name) && UUIDFetcher.nameCache.containsKey(UUIDFetcher.uuidCache.get(name))) {
            return UUIDFetcher.nameCache.get(UUIDFetcher.uuidCache.get(name));
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, timestamp / 1000L)).openConnection();
            connection.setReadTimeout(5000);
            final UUIDFetcher data = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);
            if (data == null) {
                return null;
            }
            UUIDFetcher.uuidCache.put(name, data.id);
            UUIDFetcher.nameCache.put(data.id, data.name);
            return data.name;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void getName(final UUID uuid, final Consumer<String> action) {
        UUIDFetcher.pool.execute(new Runnable() {
            @Override
            public void run() {
                action.accept(UUIDFetcher.getName(uuid));
            }
        });
    }
    
    public static void getCorrectUsername(final String name, final Consumer<String> action) {
        UUIDFetcher.pool.execute(new Runnable() {
            @Override
            public void run() {
                action.accept(UUIDFetcher.getCorrectUsername(name, System.currentTimeMillis()));
            }
        });
    }
    
    public static String getName(final UUID uuid) {
        if (UUIDFetcher.nameCache.containsKey(uuid)) {
            return UUIDFetcher.nameCache.get(uuid);
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/user/profiles/%s/names", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);
            final UUIDFetcher[] nameHistory = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher[].class);
            final UUIDFetcher currentNameData = nameHistory[nameHistory.length - 1];
            UUIDFetcher.uuidCache.put(currentNameData.name.toLowerCase(), uuid);
            UUIDFetcher.nameCache.put(uuid, currentNameData.name);
            return currentNameData.name;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static NameHistory getHistory(final UUID uuid) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/user/profiles/%s/names", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);
            final UUIDFetcher[] nameHistory = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher[].class);
            return new NameHistory(uuid, nameHistory);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
