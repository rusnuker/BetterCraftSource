// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.custom.handler;

import net.labymod.utils.texture.ThreadDownloadTextureImage;
import java.util.UUID;
import net.labymod.user.User;
import java.util.HashMap;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import java.util.Map;
import net.labymod.user.cosmetic.custom.CosmeticImageHandler;

public class StickerImageHandler extends CosmeticImageHandler
{
    private Map<Short, UserTextureContainer> userTextureContainers;
    
    public StickerImageHandler(final String userAgent) {
        super(userAgent, "labymodsticker", false);
        this.userTextureContainers = new HashMap<Short, UserTextureContainer>();
    }
    
    public UserTextureContainer getContainer(final User user, final short stickerId) {
        UserTextureContainer container = this.userTextureContainers.get(stickerId);
        if (container != null) {
            return container;
        }
        container = new UserTextureContainer("sticker", new UUID(stickerId, 0L));
        container.resolved();
        this.userTextureContainers.put(stickerId, container);
        return container;
    }
    
    @Override
    public UserTextureContainer getContainer(final User user) {
        final short playingSticker = user.getPlayingSticker();
        if (playingSticker == -1) {
            return null;
        }
        return this.getContainer(user, playingSticker);
    }
    
    @Override
    public void unload() {
        this.userTextureContainers.clear();
    }
    
    @Override
    public ThreadDownloadTextureImage.TextureImageParser geTextureImageParser() {
        return null;
    }
}
