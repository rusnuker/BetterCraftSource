// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.custom.handler;

import net.labymod.utils.texture.ThreadDownloadTextureImage;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import net.labymod.user.User;
import net.labymod.user.cosmetic.custom.CosmeticImageHandler;

public class BandanaImageHandler extends CosmeticImageHandler
{
    public BandanaImageHandler(final String userAgent) {
        super(userAgent, "labymodbandana", true);
    }
    
    @Override
    public UserTextureContainer getContainer(final User user) {
        return user.getBandanaContainer();
    }
    
    @Override
    public void unload() {
    }
    
    @Override
    public ThreadDownloadTextureImage.TextureImageParser geTextureImageParser() {
        return null;
    }
}
