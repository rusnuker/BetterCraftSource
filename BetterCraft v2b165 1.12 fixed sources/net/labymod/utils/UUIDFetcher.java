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
import java.net.Proxy;
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
    
    public static UUID getUUID(final String name, final Proxy proxy) {
        return getUUIDAt(name, System.currentTimeMillis(), proxy);
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
        if (UUIDFetcher.uuidCache.containsKey(name = name.toLowerCase())) {
            return UUIDFetcher.uuidCache.get(name);
        }
        UUIDFetcher uuidfetcher;
        try {
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, timestamp / 1000L)).openConnection();
            httpurlconnection.setReadTimeout(5000);
            uuidfetcher = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream())), UUIDFetcher.class);
            if (uuidfetcher == null) {
                return null;
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
        UUIDFetcher.uuidCache.put(name, uuidfetcher.id);
        UUIDFetcher.nameCache.put(uuidfetcher.id, uuidfetcher.name);
        return uuidfetcher.id;
    }
    
    public static UUID getUUIDAt(String name, final long timestamp, final Proxy proxy) {
        if (UUIDFetcher.uuidCache.containsKey(name = name.toLowerCase())) {
            return UUIDFetcher.uuidCache.get(name);
        }
        UUIDFetcher uuidfetcher;
        try {
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, timestamp / 1000L)).openConnection(proxy);
            httpurlconnection.setReadTimeout(5000);
            uuidfetcher = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream())), UUIDFetcher.class);
            if (uuidfetcher == null) {
                return null;
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
        UUIDFetcher.uuidCache.put(name, uuidfetcher.id);
        UUIDFetcher.nameCache.put(uuidfetcher.id, uuidfetcher.name);
        return uuidfetcher.id;
    }
    
    public static String getCorrectUsername(String name, final long timestamp) {
        if (UUIDFetcher.uuidCache.containsKey(name = name.toLowerCase()) && UUIDFetcher.nameCache.containsKey(UUIDFetcher.uuidCache.get(name))) {
            return UUIDFetcher.nameCache.get(UUIDFetcher.uuidCache.get(name));
        }
        UUIDFetcher uuidfetcher;
        try {
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d", name, timestamp / 1000L)).openConnection();
            httpurlconnection.setReadTimeout(5000);
            uuidfetcher = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream())), UUIDFetcher.class);
            if (uuidfetcher == null) {
                return null;
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
        UUIDFetcher.uuidCache.put(name, uuidfetcher.id);
        UUIDFetcher.nameCache.put(uuidfetcher.id, uuidfetcher.name);
        return uuidfetcher.name;
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
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/user/profiles/%s/names", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            httpurlconnection.setReadTimeout(5000);
            final UUIDFetcher[] auuidfetcher = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream())), UUIDFetcher[].class);
            final UUIDFetcher uuidfetcher = auuidfetcher[auuidfetcher.length - 1];
            UUIDFetcher.uuidCache.put(uuidfetcher.name.toLowerCase(), uuid);
            UUIDFetcher.nameCache.put(uuid, uuidfetcher.name);
            return uuidfetcher.name;
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    public static NameHistory getHistory(final UUID uuid) {
        try {
            final HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(String.format("https://api.mojang.com/user/profiles/%s/names", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            httpurlconnection.setReadTimeout(5000);
            final UUIDFetcher[] auuidfetcher = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream())), UUIDFetcher[].class);
            return new NameHistory(uuid, auuidfetcher);
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
