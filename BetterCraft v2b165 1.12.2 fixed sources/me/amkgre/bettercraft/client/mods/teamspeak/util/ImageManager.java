// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerImageImpl;
import java.net.URL;
import com.google.common.base.Strings;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerImage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import javax.imageio.ImageIO;
import com.google.common.primitives.UnsignedInts;
import org.apache.commons.codec.binary.Base64;
import com.google.common.base.Charsets;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;
import java.io.File;

public class ImageManager
{
    private static final File CACHE_DIRECTORY;
    private static final Cache<Integer, CachedImage> ICON_CACHE;
    private static final Cache<String, CachedImage> AVATAR_CACHE;
    private static final Cache<String, CachedServerImage> SERVER_IMAGE_CACHE;
    private static final char[] TEAMSPEAK_HEX;
    
    static {
        CACHE_DIRECTORY = new File(Utils.getTeamspeakDirectory(), "cache");
        ICON_CACHE = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).maximumSize(500L).build();
        AVATAR_CACHE = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).maximumSize(300L).build();
        SERVER_IMAGE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(30L, TimeUnit.MINUTES).maximumSize(10L).build();
        TEAMSPEAK_HEX = "abcdefghijklmnop".toCharArray();
    }
    
    public static BufferedImage resolveIcon(final String serverUniqueId, final int iconId) {
        if (iconId == 0) {
            return null;
        }
        CachedImage cachedImage = ImageManager.ICON_CACHE.getIfPresent(iconId);
        if (cachedImage == null) {
            cachedImage = new CachedImage(null);
            ImageManager.ICON_CACHE.put(iconId, cachedImage);
            final File cacheFolder = new File(ImageManager.CACHE_DIRECTORY, Base64.encodeBase64String(serverUniqueId.getBytes(Charsets.UTF_8)));
            final File iconFolder = new File(cacheFolder, "icons");
            final long unsignedNumber = UnsignedInts.toLong(iconId);
            final File icon = new File(iconFolder, "icon_" + unsignedNumber);
            if (cacheFolder.exists() && iconFolder.exists() && icon.exists()) {
                try {
                    cachedImage.image = ImageIO.read(icon);
                }
                catch (final Throwable t) {}
            }
            if (cachedImage.image == null) {
                LogManager.getLogger().error("Could not resolve TS icon " + unsignedNumber);
            }
        }
        return cachedImage.image;
    }
    
    public static BufferedImage resolveAvatar(final String serverUniqueId, final String clientUniqueId) {
        if (StringUtils.isEmpty(clientUniqueId)) {
            return null;
        }
        CachedImage cachedImage = ImageManager.AVATAR_CACHE.getIfPresent(clientUniqueId);
        if (cachedImage == null) {
            cachedImage = new CachedImage(null);
            ImageManager.AVATAR_CACHE.put(clientUniqueId, cachedImage);
            final String base64 = String.valueOf(clientUniqueId) + "=";
            final byte[] bytes = Base64.decodeBase64(base64);
            final char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; ++j) {
                final int v = bytes[j] & 0xFF;
                hexChars[j * 2] = ImageManager.TEAMSPEAK_HEX[v >>> 4];
                hexChars[j * 2 + 1] = ImageManager.TEAMSPEAK_HEX[v & 0xF];
            }
            final String avatarName = "avatar_" + new String(hexChars);
            final File cacheFolder = new File(ImageManager.CACHE_DIRECTORY, Base64.encodeBase64String(serverUniqueId.getBytes(Charsets.UTF_8)));
            final File clientFolder = new File(cacheFolder, "clients");
            final File icon = new File(clientFolder, avatarName);
            if (cacheFolder.exists() && clientFolder.exists() && icon.exists()) {
                try {
                    cachedImage.image = ImageIO.read(icon);
                }
                catch (final Throwable t) {}
            }
        }
        return cachedImage.image;
    }
    
    public static ServerImage resolveServerImage(final String pointingURL, final String imageURL) {
        if (Strings.isNullOrEmpty(imageURL)) {
            return null;
        }
        CachedServerImage cachedServerImage = ImageManager.SERVER_IMAGE_CACHE.getIfPresent(imageURL);
        if (cachedServerImage == null) {
            cachedServerImage = new CachedServerImage(null, null);
            ImageManager.SERVER_IMAGE_CACHE.put(imageURL, cachedServerImage);
            final CachedServerImage finalCachedServerImage = cachedServerImage;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final BufferedImage image = Utils.readImage(imageURL);
                        URL url = null;
                        if (!Strings.isNullOrEmpty(pointingURL)) {
                            url = new URL(pointingURL);
                        }
                        finalCachedServerImage.image = new ServerImageImpl(url, image);
                    }
                    catch (final Throwable e) {
                        LogManager.getLogger().error("Could not resolve image at " + imageURL, e);
                    }
                }
            }, "TS Image Resolver").start();
        }
        return cachedServerImage.image;
    }
    
    private static class CachedServerImage
    {
        public ServerImage image;
        
        private CachedServerImage(final ServerImage image) {
            this.image = image;
        }
    }
    
    private static class CachedImage
    {
        public BufferedImage image;
    }
}
