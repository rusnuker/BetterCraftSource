// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic;

import net.labymod.support.util.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.util.Collection;
import java.util.UUID;
import net.labymod.user.User;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import net.labymod.user.cosmetic.custom.handler.AngelWingsImageHandler;
import net.labymod.user.cosmetic.custom.handler.WatchImageHandler;
import net.labymod.user.cosmetic.custom.handler.CoverMaskImageHandler;
import net.labymod.user.cosmetic.custom.handler.KawaiiMaskImageHandler;
import net.labymod.user.cosmetic.custom.handler.StickerImageHandler;
import net.labymod.user.cosmetic.custom.handler.ShoesImageHandler;
import net.labymod.user.cosmetic.custom.handler.BandanaImageHandler;
import net.labymod.user.cosmetic.custom.handler.CloakImageHandler;
import net.labymod.user.cosmetic.custom.CosmeticImageHandler;
import java.util.List;

public class CosmeticImageManager
{
    private List<CosmeticImageHandler> cosmeticImageHandlers;
    private final CloakImageHandler cloakImageHandler;
    private final BandanaImageHandler bandanaImageHandler;
    private final ShoesImageHandler shoesImageHandler;
    private final StickerImageHandler stickerImageHandler;
    private final KawaiiMaskImageHandler kawaiiMaskImageHandler;
    private final CoverMaskImageHandler coverMaskImageHandler;
    private final WatchImageHandler watchImageHandler;
    private final AngelWingsImageHandler angelWingsImageHandler;
    
    public CosmeticImageManager(final String userAgent) {
        (this.cosmeticImageHandlers = new ArrayList<CosmeticImageHandler>()).add(this.cloakImageHandler = new CloakImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.bandanaImageHandler = new BandanaImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.shoesImageHandler = new ShoesImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.stickerImageHandler = new StickerImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.kawaiiMaskImageHandler = new KawaiiMaskImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.coverMaskImageHandler = new CoverMaskImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.watchImageHandler = new WatchImageHandler(userAgent));
        this.cosmeticImageHandlers.add(this.angelWingsImageHandler = new AngelWingsImageHandler(userAgent));
    }
    
    public void loadPlayersInView() {
        if (!LabyMod.getInstance().isInGame()) {
            return;
        }
        for (final EntityPlayer player : LabyModCore.getMinecraft().getWorld().playerEntities) {
            if (player.equals(LabyModCore.getMinecraft().getPlayer())) {
                continue;
            }
            final User user = LabyMod.getInstance().getUserManager().getUser(player.getUniqueID());
            for (final CosmeticImageHandler handler : this.cosmeticImageHandlers) {
                handler.validate(user);
            }
        }
    }
    
    public void unloadUnusedTextures(final boolean forceNotIngame, final boolean forceSelf) {
        for (final CosmeticImageHandler handler : this.cosmeticImageHandlers) {
            if (!handler.isCanUnload() && !forceSelf) {
                continue;
            }
            try {
                final List<UUID> toRemove = new ArrayList<UUID>(handler.getResourceLocations().keySet());
                if (LabyMod.getInstance().isInGame() && !forceNotIngame) {
                    for (final EntityPlayer player : LabyModCore.getMinecraft().getWorld().playerEntities) {
                        final UUID uuid = player.getUniqueID();
                        if (toRemove.contains(uuid)) {
                            toRemove.remove(uuid);
                        }
                    }
                }
                int noImage = 0;
                for (final UUID rem : toRemove) {
                    if (rem.equals(LabyMod.getInstance().getPlayerUUID()) && !forceSelf) {
                        continue;
                    }
                    final ResourceLocation resourceLocation = handler.getResourceLocations().get(rem);
                    if (resourceLocation == null || !resourceLocation.getResourcePath().startsWith(String.valueOf(handler.getResourceName()) + "/")) {
                        ++noImage;
                    }
                    else {
                        Minecraft.getMinecraft().getTextureManager().deleteTexture(resourceLocation);
                        Debug.log(Debug.EnumDebugMode.COSMETIC_IMAGE_MANAGER, "Unloaded " + resourceLocation.getResourcePath());
                    }
                    handler.getResourceLocations().remove(rem);
                    final User user = LabyMod.getInstance().getUserManager().getUser(rem);
                    if (user == null) {
                        continue;
                    }
                    user.unloadCosmeticTextures();
                }
                handler.unload();
                Debug.log(Debug.EnumDebugMode.COSMETIC_IMAGE_MANAGER, "Unloaded " + toRemove.size() + " unused " + handler.getResourceName() + " and " + noImage + " had no labymod textures! " + handler.getResourceLocations().size() + " " + handler.getResourceName() + " left.");
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
        }
    }
    
    public CloakImageHandler getCloakImageHandler() {
        return this.cloakImageHandler;
    }
    
    public BandanaImageHandler getBandanaImageHandler() {
        return this.bandanaImageHandler;
    }
    
    public ShoesImageHandler getShoesImageHandler() {
        return this.shoesImageHandler;
    }
    
    public StickerImageHandler getStickerImageHandler() {
        return this.stickerImageHandler;
    }
    
    public KawaiiMaskImageHandler getKawaiiMaskImageHandler() {
        return this.kawaiiMaskImageHandler;
    }
    
    public CoverMaskImageHandler getCoverMaskImageHandler() {
        return this.coverMaskImageHandler;
    }
    
    public WatchImageHandler getWatchImageHandler() {
        return this.watchImageHandler;
    }
    
    public AngelWingsImageHandler getAngelWingsImageHandler() {
        return this.angelWingsImageHandler;
    }
}
