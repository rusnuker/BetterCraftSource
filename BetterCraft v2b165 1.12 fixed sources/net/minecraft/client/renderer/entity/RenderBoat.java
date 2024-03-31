// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.IMultipassModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityBoat;

public class RenderBoat extends Render<EntityBoat>
{
    private static final ResourceLocation[] BOAT_TEXTURES;
    protected ModelBase modelBoat;
    
    static {
        BOAT_TEXTURES = new ResourceLocation[] { new ResourceLocation("textures/entity/boat/boat_oak.png"), new ResourceLocation("textures/entity/boat/boat_spruce.png"), new ResourceLocation("textures/entity/boat/boat_birch.png"), new ResourceLocation("textures/entity/boat/boat_jungle.png"), new ResourceLocation("textures/entity/boat/boat_acacia.png"), new ResourceLocation("textures/entity/boat/boat_darkoak.png") };
    }
    
    public RenderBoat(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelBoat = new ModelBoat();
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityBoat entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.setupTranslation(x, y, z);
        this.setupRotation(entity, entityYaw, partialTicks);
        this.bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.modelBoat.render(entity, partialTicks, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    public void setupRotation(final EntityBoat p_188311_1_, final float p_188311_2_, final float p_188311_3_) {
        GlStateManager.rotate(180.0f - p_188311_2_, 0.0f, 1.0f, 0.0f);
        final float f = p_188311_1_.getTimeSinceHit() - p_188311_3_;
        float f2 = p_188311_1_.getDamageTaken() - p_188311_3_;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f) * f * f2 / 10.0f * p_188311_1_.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
    }
    
    public void setupTranslation(final double p_188309_1_, final double p_188309_3_, final double p_188309_5_) {
        GlStateManager.translate((float)p_188309_1_, (float)p_188309_3_ + 0.375f, (float)p_188309_5_);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityBoat entity) {
        return RenderBoat.BOAT_TEXTURES[entity.getBoatType().ordinal()];
    }
    
    @Override
    public boolean isMultipass() {
        return true;
    }
    
    @Override
    public void renderMultipass(final EntityBoat p_188300_1_, final double p_188300_2_, final double p_188300_4_, final double p_188300_6_, final float p_188300_8_, final float p_188300_9_) {
        GlStateManager.pushMatrix();
        this.setupTranslation(p_188300_2_, p_188300_4_, p_188300_6_);
        this.setupRotation(p_188300_1_, p_188300_8_, p_188300_9_);
        this.bindEntityTexture(p_188300_1_);
        ((IMultipassModel)this.modelBoat).renderMultipass(p_188300_1_, p_188300_9_, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
}
