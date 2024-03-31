// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.passive.EntityParrot;

public class RenderParrot extends RenderLiving<EntityParrot>
{
    public static final ResourceLocation[] field_192862_a;
    
    static {
        field_192862_a = new ResourceLocation[] { new ResourceLocation("textures/entity/parrot/parrot_red_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_green.png"), new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_grey.png") };
    }
    
    public RenderParrot(final RenderManager p_i47375_1_) {
        super(p_i47375_1_, new ModelParrot(), 0.3f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityParrot entity) {
        return RenderParrot.field_192862_a[entity.func_191998_ds()];
    }
    
    public float handleRotationFloat(final EntityParrot livingBase, final float partialTicks) {
        return this.func_192861_b(livingBase, partialTicks);
    }
    
    private float func_192861_b(final EntityParrot p_192861_1_, final float p_192861_2_) {
        final float f = p_192861_1_.field_192011_bE + (p_192861_1_.field_192008_bB - p_192861_1_.field_192011_bE) * p_192861_2_;
        final float f2 = p_192861_1_.field_192010_bD + (p_192861_1_.field_192009_bC - p_192861_1_.field_192010_bD) * p_192861_2_;
        return (MathHelper.sin(f) + 1.0f) * f2;
    }
}
