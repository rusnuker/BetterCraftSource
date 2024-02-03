// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.custom;

import net.labymod.user.User;
import net.labymod.main.LabyMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.Minecraft;
import net.labymod.support.util.Debug;
import net.labymod.utils.texture.ThreadDownloadTextureImage;
import org.apache.commons.io.FilenameUtils;
import net.labymod.utils.Consumer;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import java.util.UUID;
import java.util.Map;

public abstract class CosmeticImageHandler
{
    protected Map<UUID, ResourceLocation> resourceLocations;
    private final String userAgent;
    private final String resourceName;
    private final boolean canUnload;
    
    public CosmeticImageHandler(final String userAgent, final String resourceName, final boolean canUnload) {
        this.resourceLocations = new HashMap<UUID, ResourceLocation>();
        this.userAgent = userAgent;
        this.resourceName = resourceName;
        this.canUnload = canUnload;
    }
    
    public void loadUserTexture(final UUID uuid, final String url) {
        if (url == null || (this.resourceLocations.containsKey(uuid) && !this.canUnload)) {
            return;
        }
        this.resolveImageTexture(url, new Consumer<ResourceLocation>() {
            @Override
            public void accept(final ResourceLocation accepted) {
                if (accepted != null) {
                    CosmeticImageHandler.this.resourceLocations.put(uuid, accepted);
                }
            }
        });
    }
    
    private void resolveImageTexture(final String url, final Consumer<ResourceLocation> callback) {
        final ResourceLocation cosmeticResourceLocation = new ResourceLocation(String.valueOf(this.resourceName) + "/" + FilenameUtils.getBaseName(url));
        final ThreadDownloadTextureImage textureCosmetic = new ThreadDownloadTextureImage(url, cosmeticResourceLocation, new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean success) {
                callback.accept(cosmeticResourceLocation);
            }
        }, this.userAgent);
        textureCosmetic.setTextureImageParser(this.geTextureImageParser());
        textureCosmetic.setDebugMode(Debug.EnumDebugMode.COSMETIC_IMAGE_MANAGER);
        Debug.log(Debug.EnumDebugMode.COSMETIC_IMAGE_MANAGER, "Load " + cosmeticResourceLocation.getResourcePath());
        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.loadTexture(cosmeticResourceLocation, textureCosmetic);
    }
    
    public ResourceLocation getResourceLocation(final AbstractClientPlayer player) {
        final User user = LabyMod.getInstance().getUserManager().getUser(player.getUniqueID());
        final UserTextureContainer container = this.getContainer(user);
        if (container == null) {
            return null;
        }
        container.validateTexture(this);
        return this.resourceLocations.get(container.getFileName());
    }
    
    public abstract UserTextureContainer getContainer(final User p0);
    
    public void validate(final User user) {
        final UserTextureContainer container = this.getContainer(user);
        if (container != null) {
            container.validateTexture(this);
        }
    }
    
    public abstract void unload();
    
    public abstract ThreadDownloadTextureImage.TextureImageParser geTextureImageParser();
    
    public Map<UUID, ResourceLocation> getResourceLocations() {
        return this.resourceLocations;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public boolean isCanUnload() {
        return this.canUnload;
    }
}
