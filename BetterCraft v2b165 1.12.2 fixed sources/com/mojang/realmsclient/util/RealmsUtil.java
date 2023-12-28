// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.util;

import org.apache.logging.log4j.LogManager;
import com.mojang.util.UUIDTypeAdapter;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import net.minecraft.realms.Realms;
import java.net.URI;
import java.util.HashMap;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.mojang.authlib.GameProfile;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public class RealmsUtil
{
    private static final YggdrasilAuthenticationService authenticationService;
    private static final MinecraftSessionService sessionService;
    public static LoadingCache<String, GameProfile> gameProfileCache;
    private static final Logger LOGGER;
    private static final int MINUTES = 60;
    private static final int HOURS = 3600;
    private static final int DAYS = 86400;
    
    public static String uuidToName(final String uuid) throws Exception {
        final GameProfile gameProfile = RealmsUtil.gameProfileCache.get(uuid);
        return gameProfile.getName();
    }
    
    public static Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(final String uuid) {
        try {
            final GameProfile gameProfile = RealmsUtil.gameProfileCache.get(uuid);
            return RealmsUtil.sessionService.getTextures(gameProfile, false);
        }
        catch (final Exception e) {
            return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
        }
    }
    
    public static void browseTo(final String uri) {
        try {
            final URI link = new URI(uri);
            final Class<?> desktopClass = Class.forName("java.awt.Desktop");
            final Object o = desktopClass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            desktopClass.getMethod("browse", URI.class).invoke(o, link);
        }
        catch (final Throwable ignored) {
            RealmsUtil.LOGGER.error("Couldn't open link");
        }
    }
    
    public static String convertToAgePresentation(final Long timeDiff) {
        if (timeDiff < 0L) {
            return "right now";
        }
        final long timeDiffInSeconds = timeDiff / 1000L;
        if (timeDiffInSeconds < 60L) {
            return ((timeDiffInSeconds == 1L) ? "1 second" : (timeDiffInSeconds + " seconds")) + " ago";
        }
        if (timeDiffInSeconds < 3600L) {
            final long minutes = timeDiffInSeconds / 60L;
            return ((minutes == 1L) ? "1 minute" : (minutes + " minutes")) + " ago";
        }
        if (timeDiffInSeconds < 86400L) {
            final long hours = timeDiffInSeconds / 3600L;
            return ((hours == 1L) ? "1 hour" : (hours + " hours")) + " ago";
        }
        final long days = timeDiffInSeconds / 86400L;
        return ((days == 1L) ? "1 day" : (days + " days")) + " ago";
    }
    
    static {
        authenticationService = new YggdrasilAuthenticationService(Realms.getProxy(), UUID.randomUUID().toString());
        sessionService = RealmsUtil.authenticationService.createMinecraftSessionService();
        RealmsUtil.gameProfileCache = CacheBuilder.newBuilder().expireAfterWrite(60L, TimeUnit.MINUTES).build((CacheLoader<? super String, GameProfile>)new CacheLoader<String, GameProfile>() {
            @Override
            public GameProfile load(final String uuid) throws Exception {
                final GameProfile profile = RealmsUtil.sessionService.fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(uuid), null), false);
                if (profile == null) {
                    throw new Exception("Couldn't get profile");
                }
                return profile;
            }
        });
        LOGGER = LogManager.getLogger();
    }
}
