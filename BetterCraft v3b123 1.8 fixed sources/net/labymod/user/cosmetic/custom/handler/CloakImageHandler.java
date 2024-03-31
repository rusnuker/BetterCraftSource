// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.custom.handler;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import net.labymod.user.User;
import net.labymod.main.LabyMod;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.labymod.utils.texture.ThreadDownloadTextureImage;
import net.labymod.user.cosmetic.custom.CosmeticImageHandler;

public class CloakImageHandler extends CosmeticImageHandler
{
    private static final ThreadDownloadTextureImage.TextureImageParser TEXTURE_IMAGE_PARSER;
    private EnumCapePriority priority;
    
    static {
        TEXTURE_IMAGE_PARSER = new ThreadDownloadTextureImage.TextureImageParser() {
            @Override
            public BufferedImage parse(final BufferedImage input) {
                int imageWidth = 64;
                int imageHeight = 32;
                final BufferedImage srcImg = input;
                for (int srcWidth = srcImg.getWidth(), srcHeight = srcImg.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
                final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
                final Graphics g = imgNew.getGraphics();
                g.drawImage(input, 0, 0, null);
                g.dispose();
                return imgNew;
            }
        };
    }
    
    public CloakImageHandler(final String userAgent) {
        super(userAgent, "labymodcloak", true);
        try {
            this.priority = EnumCapePriority.valueOf(LabyMod.getSettings().capePriority);
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    @Override
    public UserTextureContainer getContainer(final User user) {
        return user.getCloakContainer();
    }
    
    public boolean canRenderMojangCape(final User user, final AbstractClientPlayer player) {
        final NetHandlerPlayClient netHandlerPlayClient = LabyModCore.getMinecraft().getConnection();
        final NetworkPlayerInfo networkPlayerInfo = (netHandlerPlayClient != null) ? netHandlerPlayClient.getPlayerInfo(player.getUniqueID()) : null;
        final ResourceLocation locationOptifine = player.getLocationCape();
        final ResourceLocation locationMinecon = (networkPlayerInfo == null) ? null : networkPlayerInfo.getLocationCape();
        final ResourceLocation locationLabyMod = this.resourceLocations.get(user.getCloakContainer().getFileName());
        user.setMojangCapeModified(locationMinecon == null || !locationMinecon.equals(locationOptifine));
        final boolean enabled = LabyMod.getSettings().cosmetics && LabyMod.getSettings().cosmeticsCustomTextures;
        if (enabled && locationLabyMod == null) {
            user.getCloakContainer().validateTexture(this);
        }
        boolean canRenderMojangCape = locationLabyMod == null || !enabled;
        if (this.priority != EnumCapePriority.LABYMOD && (locationMinecon != null || locationOptifine != null)) {
            canRenderMojangCape = true;
        }
        return canRenderMojangCape;
    }
    
    @Override
    public void unload() {
    }
    
    @Override
    public ThreadDownloadTextureImage.TextureImageParser geTextureImageParser() {
        return CloakImageHandler.TEXTURE_IMAGE_PARSER;
    }
    
    public void setPriority(final EnumCapePriority priority) {
        this.priority = priority;
    }
    
    public enum EnumCapePriority
    {
        LABYMOD("LABYMOD", 0), 
        OTHER("OTHER", 1);
        
        private EnumCapePriority(final String s, final int n) {
        }
    }
}
