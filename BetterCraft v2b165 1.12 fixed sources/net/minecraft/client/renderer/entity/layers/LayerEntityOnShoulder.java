// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.EntityList;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.GlStateManager;
import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class LayerEntityOnShoulder implements LayerRenderer<EntityPlayer>
{
    private final RenderManager field_192867_c;
    protected RenderLivingBase<? extends EntityLivingBase> field_192865_a;
    private ModelBase field_192868_d;
    private ResourceLocation field_192869_e;
    private UUID field_192870_f;
    private Class<?> field_192871_g;
    protected RenderLivingBase<? extends EntityLivingBase> field_192866_b;
    private ModelBase field_192872_h;
    private ResourceLocation field_192873_i;
    private UUID field_192874_j;
    private Class<?> field_192875_k;
    
    public LayerEntityOnShoulder(final RenderManager p_i47370_1_) {
        this.field_192867_c = p_i47370_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entitylivingbaseIn.func_192023_dk() != null || entitylivingbaseIn.func_192025_dl() != null) {
            GlStateManager.enableRescaleNormal();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final NBTTagCompound nbttagcompound = entitylivingbaseIn.func_192023_dk();
            if (!nbttagcompound.hasNoTags()) {
                final DataHolder layerentityonshoulder$dataholder = this.func_192864_a(entitylivingbaseIn, this.field_192870_f, nbttagcompound, this.field_192865_a, this.field_192868_d, this.field_192869_e, this.field_192871_g, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, true);
                this.field_192870_f = layerentityonshoulder$dataholder.field_192882_a;
                this.field_192865_a = layerentityonshoulder$dataholder.field_192883_b;
                this.field_192869_e = layerentityonshoulder$dataholder.field_192885_d;
                this.field_192868_d = layerentityonshoulder$dataholder.field_192884_c;
                this.field_192871_g = layerentityonshoulder$dataholder.field_192886_e;
            }
            final NBTTagCompound nbttagcompound2 = entitylivingbaseIn.func_192025_dl();
            if (!nbttagcompound2.hasNoTags()) {
                final DataHolder layerentityonshoulder$dataholder2 = this.func_192864_a(entitylivingbaseIn, this.field_192874_j, nbttagcompound2, this.field_192866_b, this.field_192872_h, this.field_192873_i, this.field_192875_k, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, false);
                this.field_192874_j = layerentityonshoulder$dataholder2.field_192882_a;
                this.field_192866_b = layerentityonshoulder$dataholder2.field_192883_b;
                this.field_192873_i = layerentityonshoulder$dataholder2.field_192885_d;
                this.field_192872_h = layerentityonshoulder$dataholder2.field_192884_c;
                this.field_192875_k = layerentityonshoulder$dataholder2.field_192886_e;
            }
            GlStateManager.disableRescaleNormal();
        }
    }
    
    private DataHolder func_192864_a(final EntityPlayer p_192864_1_, @Nullable UUID p_192864_2_, final NBTTagCompound p_192864_3_, RenderLivingBase<? extends EntityLivingBase> p_192864_4_, ModelBase p_192864_5_, ResourceLocation p_192864_6_, Class<?> p_192864_7_, final float p_192864_8_, final float p_192864_9_, final float p_192864_10_, float p_192864_11_, final float p_192864_12_, final float p_192864_13_, final float p_192864_14_, final boolean p_192864_15_) {
        if (p_192864_2_ == null || !p_192864_2_.equals(p_192864_3_.getUniqueId("UUID"))) {
            p_192864_2_ = p_192864_3_.getUniqueId("UUID");
            p_192864_7_ = EntityList.func_192839_a(p_192864_3_.getString("id"));
            if (p_192864_7_ == EntityParrot.class) {
                p_192864_4_ = new RenderParrot(this.field_192867_c);
                p_192864_5_ = new ModelParrot();
                p_192864_6_ = RenderParrot.field_192862_a[p_192864_3_.getInteger("Variant")];
            }
        }
        p_192864_4_.bindTexture(p_192864_6_);
        GlStateManager.pushMatrix();
        final float f = p_192864_1_.isSneaking() ? -1.3f : -1.5f;
        final float f2 = p_192864_15_ ? 0.4f : -0.4f;
        GlStateManager.translate(f2, f, 0.0f);
        if (p_192864_7_ == EntityParrot.class) {
            p_192864_11_ = 0.0f;
        }
        p_192864_5_.setLivingAnimations(p_192864_1_, p_192864_8_, p_192864_9_, p_192864_10_);
        p_192864_5_.setRotationAngles(p_192864_8_, p_192864_9_, p_192864_11_, p_192864_12_, p_192864_13_, p_192864_14_, p_192864_1_);
        p_192864_5_.render(p_192864_1_, p_192864_8_, p_192864_9_, p_192864_11_, p_192864_12_, p_192864_13_, p_192864_14_);
        GlStateManager.popMatrix();
        return new DataHolder(p_192864_2_, p_192864_4_, p_192864_5_, p_192864_6_, p_192864_7_);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    class DataHolder
    {
        public UUID field_192882_a;
        public RenderLivingBase<? extends EntityLivingBase> field_192883_b;
        public ModelBase field_192884_c;
        public ResourceLocation field_192885_d;
        public Class<?> field_192886_e;
        
        public DataHolder(final UUID p_i47463_2_, final RenderLivingBase<? extends EntityLivingBase> p_i47463_3_, final ModelBase p_i47463_4_, final ResourceLocation p_i47463_5_, final Class<?> p_i47463_6_) {
            this.field_192882_a = p_i47463_2_;
            this.field_192883_b = p_i47463_3_;
            this.field_192884_c = p_i47463_4_;
            this.field_192885_d = p_i47463_5_;
            this.field_192886_e = p_i47463_6_;
        }
    }
}
