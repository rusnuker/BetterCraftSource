// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityDonkey;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.entity.passive.AbstractHorse;

public class RenderAbstractHorse extends RenderLiving<AbstractHorse>
{
    private static final Map<Class<?>, ResourceLocation> field_191359_a;
    private final float field_191360_j;
    
    static {
        (field_191359_a = Maps.newHashMap()).put(EntityDonkey.class, new ResourceLocation("textures/entity/horse/donkey.png"));
        RenderAbstractHorse.field_191359_a.put(EntityMule.class, new ResourceLocation("textures/entity/horse/mule.png"));
        RenderAbstractHorse.field_191359_a.put(EntityZombieHorse.class, new ResourceLocation("textures/entity/horse/horse_zombie.png"));
        RenderAbstractHorse.field_191359_a.put(EntitySkeletonHorse.class, new ResourceLocation("textures/entity/horse/horse_skeleton.png"));
    }
    
    public RenderAbstractHorse(final RenderManager p_i47212_1_) {
        this(p_i47212_1_, 1.0f);
    }
    
    public RenderAbstractHorse(final RenderManager p_i47213_1_, final float p_i47213_2_) {
        super(p_i47213_1_, new ModelHorse(), 0.75f);
        this.field_191360_j = p_i47213_2_;
    }
    
    @Override
    protected void preRenderCallback(final AbstractHorse entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(this.field_191360_j, this.field_191360_j, this.field_191360_j);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final AbstractHorse entity) {
        return RenderAbstractHorse.field_191359_a.get(entity.getClass());
    }
}
