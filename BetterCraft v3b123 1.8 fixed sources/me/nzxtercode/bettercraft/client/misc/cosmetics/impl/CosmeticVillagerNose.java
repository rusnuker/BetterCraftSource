// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticVillagerNose extends CosmeticBase
{
    private static final ResourceLocation TEXTURE;
    private NoseModel noseModel;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/villagernose.png");
    }
    
    public CosmeticVillagerNose(final RenderPlayer player) {
        super(player);
        this.noseModel = new NoseModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.noseModel = new NoseModel(this.playerRenderer);
        this.playerRenderer.bindTexture(CosmeticVillagerNose.TEXTURE);
        this.noseModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
    }
    
    @Override
    public int getId() {
        return 24;
    }
    
    public static class NoseModel extends CosmeticModelBase
    {
        private final ModelRenderer villagerNose;
        
        public NoseModel(final RenderPlayer player) {
            super(player);
            (this.villagerNose = new ModelRenderer(this).setTextureSize(64, 64)).setRotationPoint(0.0f, -2.0f, 0.0f);
            this.villagerNose.setTextureOffset(24, 0).addBox(-1.0f, -2.0f, -6.0f, 2, 4, 2, 0.0f);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            this.villagerNose.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.villagerNose.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.villagerNose.rotationPointX = 0.0f;
            this.villagerNose.rotationPointY = 0.0f;
            this.villagerNose.render(scale);
        }
    }
}
