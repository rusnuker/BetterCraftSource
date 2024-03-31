// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class CosmeticCreeperLightning implements LayerRenderer<AbstractClientPlayer>
{
    private static final ResourceLocation TEXTURE;
    private final RenderPlayer renderPlayer;
    private ModelPlayer playerModel;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/creeper.png");
    }
    
    public CosmeticCreeperLightning(final RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
        this.playerModel = renderPlayer.getMainModel();
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        final boolean flag = entitylivingbaseIn.isInvisible();
        GlStateManager.depthMask(!flag);
        this.playerModel = this.renderPlayer.getMainModel();
        this.renderPlayer.bindTexture(CosmeticCreeperLightning.TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        final float f2 = entitylivingbaseIn.ticksExisted + partialTicks;
        final float velocity = 0.004f;
        GlStateManager.translate(f2 * -velocity, f2 * velocity, 0.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        final float f3 = 0.5f;
        GlStateManager.color(f3, f3, f3, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(1, 1);
        this.playerModel.setModelAttributes(this.renderPlayer.getMainModel());
        this.playerModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(flag);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
