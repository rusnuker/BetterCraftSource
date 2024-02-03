// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.texture;

import java.beans.ConstructorProperties;
import java.util.concurrent.Executors;
import java.util.HashMap;
import com.mojang.authlib.properties.Property;
import com.google.common.collect.Multimap;
import net.labymod.core.LabyModCore;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.google.common.collect.Maps;
import net.labymod.main.LabyMod;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.labymod.utils.UUIDFetcher;
import net.labymod.utils.Consumer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.resources.SkinManager;
import java.util.concurrent.ExecutorService;
import net.minecraft.util.ResourceLocation;
import java.util.UUID;
import java.util.Map;

public class PlayerSkinTextureCache
{
    private Map<UUID, ResourceLocation> loadedSkins;
    private Map<String, ResourceLocation> loadedUsernameSkins;
    private ExecutorService executorService;
    private final SkinManager skinManager;
    private final MinecraftSessionService minecraftSessionService;
    
    public ResourceLocation getSkinTexture(final GameProfile gameProfile) {
        if (gameProfile == null) {
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        final UUID uuid = gameProfile.getId();
        ResourceLocation loadedSkinResource = this.loadedSkins.get(uuid);
        if (loadedSkinResource == null) {
            this.loadedSkins.put(uuid, loadedSkinResource = DefaultPlayerSkin.getDefaultSkinLegacy());
            this.requestTexture(gameProfile);
        }
        return loadedSkinResource;
    }
    
    public ResourceLocation getSkinTexture(final UUID uuid) {
        if (uuid == null) {
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        ResourceLocation loadedSkinResource = this.loadedSkins.get(uuid);
        if (loadedSkinResource == null) {
            this.loadedSkins.put(uuid, loadedSkinResource = DefaultPlayerSkin.getDefaultSkinLegacy());
            this.requestTexture(new GameProfile(uuid, "Steve"));
        }
        return loadedSkinResource;
    }
    
    public ResourceLocation getSkinTexture(final String username) {
        if (username == null) {
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        ResourceLocation loadedSkinResource = this.loadedUsernameSkins.get(username);
        if (loadedSkinResource == null) {
            this.loadedUsernameSkins.put(username, loadedSkinResource = DefaultPlayerSkin.getDefaultSkinLegacy());
            UUIDFetcher.getUUID(username, new Consumer<UUID>() {
                @Override
                public void accept(final UUID uuid) {
                    final String username = UUIDFetcher.getName(uuid);
                    final GameProfile gameProfile = new GameProfile(uuid, username);
                    PlayerSkinTextureCache.this.requestTexture(gameProfile);
                }
            });
        }
        return loadedSkinResource;
    }
    
    public ResourceLocation getCachedSkinTexture(final GameProfile gameProfile) {
        if (gameProfile == null) {
            return DefaultPlayerSkin.getDefaultSkinLegacy();
        }
        final Minecraft minecraft = Minecraft.getMinecraft();
        final Map<?, ?> map = minecraft.getSkinManager().loadSkinFromCache(gameProfile);
        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
        }
        final UUID uuid = EntityPlayer.getUUID(gameProfile);
        return DefaultPlayerSkin.getDefaultSkin(uuid);
    }
    
    private void requestTexture(final GameProfile gameProfile) {
        MinecraftProfileTexture minecraftProfileTexture = null;
        final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = this.skinManager.loadSkinFromCache(gameProfile);
        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            minecraftProfileTexture = map.get(MinecraftProfileTexture.Type.SKIN);
        }
        if (minecraftProfileTexture == null) {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!gameProfile.getId().equals(LabyMod.getInstance().getPlayerUUID())) {
                        PlayerSkinTextureCache.this.minecraftSessionService.fillProfileProperties(gameProfile, false);
                    }
                    final MinecraftProfileTexture requestedProfileTexture = PlayerSkinTextureCache.this.getMinecraftProfileTexture(gameProfile, MinecraftProfileTexture.Type.SKIN);
                    if (requestedProfileTexture != null) {
                        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                            @Override
                            public void run() {
                                PlayerSkinTextureCache.this.loadSkinTexture(gameProfile, requestedProfileTexture);
                            }
                        });
                    }
                }
            });
        }
        else {
            this.loadSkinTexture(gameProfile, minecraftProfileTexture);
        }
    }
    
    private void loadSkinTexture(final GameProfile gameProfile, final MinecraftProfileTexture profileTexture) {
        this.skinManager.loadSkin(profileTexture, MinecraftProfileTexture.Type.SKIN, new SkinManager.SkinAvailableCallback() {
            @Override
            public void skinAvailable(final MinecraftProfileTexture.Type typeIn, final ResourceLocation location, final MinecraftProfileTexture profileTexture) {
                if (typeIn == MinecraftProfileTexture.Type.SKIN) {
                    PlayerSkinTextureCache.this.loadedSkins.put(gameProfile.getId(), location);
                    PlayerSkinTextureCache.this.loadedUsernameSkins.put(gameProfile.getName(), location);
                }
            }
        });
    }
    
    private MinecraftProfileTexture getMinecraftProfileTexture(final GameProfile gameProfile, final MinecraftProfileTexture.Type type) {
        final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)Maps.newHashMap();
        try {
            map.putAll(this.minecraftSessionService.getTextures(gameProfile, false));
        }
        catch (final InsecureTextureException ex) {}
        if (map.isEmpty() && gameProfile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
            gameProfile.getProperties().clear();
            gameProfile.getProperties().putAll(LabyModCore.getMinecraft().getPropertyMap());
            map.putAll(this.minecraftSessionService.getTextures(gameProfile, false));
        }
        return map.get(type);
    }
    
    @ConstructorProperties({ "skinManager", "minecraftSessionService" })
    public PlayerSkinTextureCache(final SkinManager skinManager, final MinecraftSessionService minecraftSessionService) {
        this.loadedSkins = new HashMap<UUID, ResourceLocation>();
        this.loadedUsernameSkins = new HashMap<String, ResourceLocation>();
        this.executorService = Executors.newFixedThreadPool(3);
        this.skinManager = skinManager;
        this.minecraftSessionService = minecraftSessionService;
    }
}
