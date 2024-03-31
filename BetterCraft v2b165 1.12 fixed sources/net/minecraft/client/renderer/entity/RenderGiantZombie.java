// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityGiantZombie;

public class RenderGiantZombie extends RenderLiving<EntityGiantZombie>
{
    private static final ResourceLocation ZOMBIE_TEXTURES;
    private final float scale;
    
    static {
        ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    }
    
    public RenderGiantZombie(final RenderManager p_i47206_1_, final float p_i47206_2_) {
        super(p_i47206_1_, new ModelZombie(), 0.5f * p_i47206_2_);
        this.scale = p_i47206_2_;
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = (T)new ModelZombie(0.5f, true);
                this.modelArmor = (T)new ModelZombie(1.0f, true);
            }
        });
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    @Override
    protected void preRenderCallback(final EntityGiantZombie entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(this.scale, this.scale, this.scale);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityGiantZombie entity) {
        return RenderGiantZombie.ZOMBIE_TEXTURES;
    }
}
