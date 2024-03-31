// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.texture;

import java.beans.ConstructorProperties;
import net.minecraft.util.ResourceLocation;

public class DynamicModTexture
{
    private ResourceLocation resourceLocation;
    private String url;
    
    @ConstructorProperties({ "resourceLocation", "url" })
    public DynamicModTexture(final ResourceLocation resourceLocation, final String url) {
        this.resourceLocation = resourceLocation;
        this.url = url;
    }
    
    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
    
    public void setResourceLocation(final ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
}
