// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.texture;

import java.util.Iterator;
import net.labymod.utils.Consumer;
import net.labymod.main.ModTextures;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ReflectionHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.ITextureObject;
import javax.net.ssl.SSLSocketFactory;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class DynamicTextureManager
{
    protected final Map<String, DynamicModTexture> resourceLocations;
    private final String resourceName;
    private final ResourceLocation defaultTexture;
    private String userAgent;
    private SSLSocketFactory socketFactory;
    private ThreadDownloadTextureImage.TextureImageParser textureImageParser;
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;
    
    public DynamicTextureManager(final String resourceName, final ResourceLocation defaultTexture) {
        this.userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36";
        this.socketFactory = null;
        this.textureImageParser = null;
        this.resourceLocations = new HashMap<String, DynamicModTexture>();
        this.mapTextureObjects = new HashMap<ResourceLocation, ITextureObject>();
        this.resourceName = resourceName;
        this.defaultTexture = defaultTexture;
    }
    
    public void init() {
        try {
            final Field field = ReflectionHelper.findField(TextureManager.class, LabyModCore.getMappingAdapter().getMapTextureObjects());
            field.setAccessible(true);
            this.mapTextureObjects = (Map)field.get(Minecraft.getMinecraft().getTextureManager());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @Deprecated
    public ResourceLocation getHeadTexture(final GameProfile gameProfile) {
        return ModTextures.MISC_HEAD_QUESTION;
    }
    
    public ResourceLocation getTexture(final String identifier, final String url) {
        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        DynamicModTexture dynamicModTexture = this.resourceLocations.get(identifier);
        final boolean unloadImage = dynamicModTexture != null && dynamicModTexture.getUrl() != null && !dynamicModTexture.getUrl().equals(url);
        if (dynamicModTexture == null || unloadImage) {
            if (unloadImage) {
                textureManager.deleteTexture(dynamicModTexture.getResourceLocation());
                dynamicModTexture.setResourceLocation(this.defaultTexture);
                dynamicModTexture.setUrl(null);
            }
            else {
                dynamicModTexture = new DynamicModTexture(this.defaultTexture, null);
            }
            this.resourceLocations.put(identifier, dynamicModTexture);
            this.resolveImageTexture(identifier, url);
        }
        return dynamicModTexture.getResourceLocation();
    }
    
    private void resolveImageTexture(final String identifier, final String url) {
        if (identifier == null || url == null) {
            return;
        }
        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        final ResourceLocation resourceLocation = new ResourceLocation(String.valueOf(this.resourceName) + "/" + this.getHash(url));
        final ThreadDownloadTextureImage threadDownloadImageData = new ThreadDownloadTextureImage(url, resourceLocation, new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted) {
                DynamicTextureManager.this.resourceLocations.put(identifier, new DynamicModTexture(resourceLocation, url));
            }
        }, this.userAgent);
        threadDownloadImageData.setSocketFactory(this.socketFactory);
        threadDownloadImageData.setTextureImageParser(this.textureImageParser);
        textureManager.loadTexture(resourceLocation, threadDownloadImageData);
    }
    
    public void unloadAll() {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                try {
                    for (final Map.Entry<String, DynamicModTexture> entry : DynamicTextureManager.this.resourceLocations.entrySet()) {
                        final ResourceLocation resourceLocation = entry.getValue().getResourceLocation();
                        DynamicTextureManager.this.mapTextureObjects.remove(resourceLocation);
                    }
                    DynamicTextureManager.this.resourceLocations.clear();
                }
                catch (final Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }
    
    private int getHash(final String url) {
        int hash = 7;
        for (int i = 0; i < url.length(); ++i) {
            hash = hash * 31 + url.charAt(i);
        }
        return hash;
    }
    
    public String getResourceName() {
        return this.resourceName;
    }
    
    public ResourceLocation getDefaultTexture() {
        return this.defaultTexture;
    }
    
    public String getUserAgent() {
        return this.userAgent;
    }
    
    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }
    
    public SSLSocketFactory getSocketFactory() {
        return this.socketFactory;
    }
    
    public void setSocketFactory(final SSLSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }
    
    public ThreadDownloadTextureImage.TextureImageParser getTextureImageParser() {
        return this.textureImageParser;
    }
    
    public void setTextureImageParser(final ThreadDownloadTextureImage.TextureImageParser textureImageParser) {
        this.textureImageParser = textureImageParser;
    }
    
    public Map<String, DynamicModTexture> getResourceLocations() {
        return this.resourceLocations;
    }
    
    public Map<ResourceLocation, ITextureObject> getMapTextureObjects() {
        return this.mapTextureObjects;
    }
}
