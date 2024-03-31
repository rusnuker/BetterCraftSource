// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.Random;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticEnderCrystal extends CosmeticBase
{
    private static final ResourceLocation TEXTURE;
    public int innerRotation;
    private EnderCrystalModel enderCrystalModel;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/endercrystal.png");
    }
    
    public CosmeticEnderCrystal(final RenderPlayer player) {
        super(player);
        this.enderCrystalModel = new EnderCrystalModel(player);
        this.innerRotation = new Random().nextInt(100000);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.enderCrystalModel = new EnderCrystalModel(this.playerRenderer);
        ++this.innerRotation;
        GlStateManager.pushMatrix();
        this.playerRenderer.bindTexture(CosmeticEnderCrystal.TEXTURE);
        if (player.isSneaking()) {
            GL11.glTranslated(0.0, 0.3, 0.0);
        }
        GL11.glTranslated(0.0, -0.9, 0.0);
        final float f = (float)this.innerRotation;
        float f2 = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        f2 += f2 * f2;
        this.enderCrystalModel.render(player, 0.0f, f * 0.05f, f2 * 0.002f, 0.0f, 0.0f, 0.1f);
        GL11.glPopMatrix();
    }
    
    @Override
    public int getId() {
        return 11;
    }
    
    public static class EnderCrystalModel extends CosmeticModelBase
    {
        ModelRenderer glass;
        
        public EnderCrystalModel(final RenderPlayer player) {
            super(player);
            this.glass = new ModelRenderer(this, "glass");
            this.glass.setTextureOffset(0, 0).addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(p_78088_3_, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, 0.8f + p_78088_4_, 0.0f);
            GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
            this.glass.render(scale);
            final float f = 0.875f;
            GlStateManager.scale(f, f, f);
            GlStateManager.rotate(60.0f, 0.7071f, 0.0f, 0.7071f);
            GlStateManager.rotate(p_78088_3_, 0.0f, 1.0f, 0.0f);
            this.glass.render(scale);
            GlStateManager.popMatrix();
        }
    }
}
