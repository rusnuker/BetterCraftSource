// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.custom.handler;

import net.labymod.utils.texture.ThreadDownloadTextureImage;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import net.labymod.user.User;
import net.labymod.user.cosmetic.custom.CosmeticImageHandler;

public class KawaiiMaskImageHandler extends CosmeticImageHandler
{
    public KawaiiMaskImageHandler(final String userAgent) {
        super(userAgent, "labymodkawaiimask", false);
    }
    
    @Override
    public UserTextureContainer getContainer(final User user) {
        return user.getKawaiiMaskContainer();
    }
    
    @Override
    public void unload() {
    }
    
    @Override
    public ThreadDownloadTextureImage.TextureImageParser geTextureImageParser() {
        return null;
    }
}
