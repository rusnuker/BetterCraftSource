// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticModelBase;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticDragonWings extends CosmeticBase
{
    private WingsModel wingsModel;
    private static final ResourceLocation TEXTURE;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/wings.png");
    }
    
    public CosmeticDragonWings(final RenderPlayer player) {
        super(player);
        this.wingsModel = new WingsModel(player);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 0.125, 0.0);
        GlStateManager.enableLighting();
        if (player.isSneaking()) {
            GL11.glTranslated(0.0, 0.125, 0.0);
        }
        final float[] color = { 1.0f, 1.0f, 1.0f };
        GL11.glColor3f(color[0], color[1], color[2]);
        GlStateManager.enableBlend();
        this.playerRenderer.bindTexture(CosmeticDragonWings.TEXTURE);
        this.wingsModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
        GlStateManager.popMatrix();
    }
    
    private float interpolate(final float yaw1, final float yaw2, final float percent) {
        float f2 = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (f2 < 0.0f) {
            f2 += 360.0f;
        }
        return f2;
    }
    
    @Override
    public int getId() {
        return 10;
    }
    
    public static class WingsModel extends CosmeticModelBase
    {
        private ModelRenderer wing;
        private ModelRenderer wingTip;
        
        public WingsModel(final RenderPlayer player) {
            super(player);
            this.setTextureOffset("wing.bone", 0, 0);
            this.setTextureOffset("wing.skin", -10, 8);
            this.setTextureOffset("wingtip.bone", 0, 5);
            this.setTextureOffset("wingtip.skin", -10, 18);
            (this.wing = new ModelRenderer(this, "wing")).setTextureSize(30, 30);
            this.wing.setRotationPoint(-2.0f, 0.0f, 0.0f);
            this.wing.addBox("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2);
            this.wing.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
            (this.wingTip = new ModelRenderer(this, "wingtip")).setTextureSize(30, 30);
            this.wingTip.setRotationPoint(-10.0f, 0.0f, 0.0f);
            this.wingTip.addBox("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1);
            this.wingTip.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
            this.wing.addChild(this.wingTip);
        }
        
        @Override
        public void render(final Entity entityIn, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float scale) {
            for (int j2 = 0; j2 < 2; ++j2) {
                final float f11 = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f;
                this.wing.rotateAngleX = (float)Math.toRadians(-80.0) - (float)Math.cos(f11) * 0.2f;
                this.wing.rotateAngleY = (float)Math.toRadians(20.0) + (float)Math.sin(f11) * 0.4f;
                this.wing.rotateAngleZ = (float)Math.toRadians(20.0);
                this.wingTip.rotateAngleZ = -(float)(Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
                this.wing.render(0.0625f);
                GlStateManager.scale(-1.0f, 1.0f, 1.0f);
                if (j2 != 0) {}
            }
        }
    }
}
