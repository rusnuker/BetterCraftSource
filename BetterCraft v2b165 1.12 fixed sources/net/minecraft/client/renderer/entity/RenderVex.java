// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelVex;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityVex;

public class RenderVex extends RenderBiped<EntityVex>
{
    private static final ResourceLocation field_191343_a;
    private static final ResourceLocation field_191344_j;
    private int field_191345_k;
    
    static {
        field_191343_a = new ResourceLocation("textures/entity/illager/vex.png");
        field_191344_j = new ResourceLocation("textures/entity/illager/vex_charging.png");
    }
    
    public RenderVex(final RenderManager p_i47190_1_) {
        super(p_i47190_1_, new ModelVex(), 0.3f);
        this.field_191345_k = ((ModelVex)this.mainModel).func_191228_a();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityVex entity) {
        return entity.func_190647_dj() ? RenderVex.field_191344_j : RenderVex.field_191343_a;
    }
    
    @Override
    public void doRender(final EntityVex entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final int i = ((ModelVex)this.mainModel).func_191228_a();
        if (i != this.field_191345_k) {
            this.mainModel = new ModelVex();
            this.field_191345_k = i;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected void preRenderCallback(final EntityVex entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }
}
