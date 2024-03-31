// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntitySpectralArrow;

public class RenderSpectralArrow extends RenderArrow<EntitySpectralArrow>
{
    public static final ResourceLocation RES_SPECTRAL_ARROW;
    
    static {
        RES_SPECTRAL_ARROW = new ResourceLocation("textures/entity/projectiles/spectral_arrow.png");
    }
    
    public RenderSpectralArrow(final RenderManager manager) {
        super(manager);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySpectralArrow entity) {
        return RenderSpectralArrow.RES_SPECTRAL_ARROW;
    }
}
