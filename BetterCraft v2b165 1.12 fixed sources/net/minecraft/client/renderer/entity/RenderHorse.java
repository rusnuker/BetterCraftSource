// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.entity.passive.EntityHorse;

public class RenderHorse extends RenderLiving<EntityHorse>
{
    private static final Map<String, ResourceLocation> LAYERED_LOCATION_CACHE;
    
    static {
        LAYERED_LOCATION_CACHE = Maps.newHashMap();
    }
    
    public RenderHorse(final RenderManager p_i47205_1_) {
        super(p_i47205_1_, new ModelHorse(), 0.75f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityHorse entity) {
        final String s = entity.getHorseTexture();
        ResourceLocation resourcelocation = RenderHorse.LAYERED_LOCATION_CACHE.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(entity.getVariantTexturePaths()));
            RenderHorse.LAYERED_LOCATION_CACHE.put(s, resourcelocation);
        }
        return resourcelocation;
    }
}
