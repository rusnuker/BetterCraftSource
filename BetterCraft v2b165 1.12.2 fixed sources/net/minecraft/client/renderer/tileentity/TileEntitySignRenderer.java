// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import optifine.CustomColors;
import optifine.Config;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelSign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntitySign;

public class TileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign>
{
    private static final ResourceLocation SIGN_TEXTURE;
    private final ModelSign model;
    
    static {
        SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    }
    
    public TileEntitySignRenderer() {
        this.model = new ModelSign();
    }
    
    @Override
    public void func_192841_a(final TileEntitySign p_192841_1_, final double p_192841_2_, final double p_192841_4_, final double p_192841_6_, final float p_192841_8_, final int p_192841_9_, final float p_192841_10_) {
        final Block block = p_192841_1_.getBlockType();
        GlStateManager.pushMatrix();
        final float f = 0.6666667f;
        if (block == Blocks.STANDING_SIGN) {
            GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.5f, (float)p_192841_6_ + 0.5f);
            final float f2 = p_192841_1_.getBlockMetadata() * 360 / 16.0f;
            GlStateManager.rotate(-f2, 0.0f, 1.0f, 0.0f);
            this.model.signStick.showModel = true;
        }
        else {
            final int k = p_192841_1_.getBlockMetadata();
            float f3 = 0.0f;
            if (k == 2) {
                f3 = 180.0f;
            }
            if (k == 4) {
                f3 = 90.0f;
            }
            if (k == 5) {
                f3 = -90.0f;
            }
            GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.5f, (float)p_192841_6_ + 0.5f);
            GlStateManager.rotate(-f3, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.model.signStick.showModel = false;
        }
        if (p_192841_9_ >= 0) {
            this.bindTexture(TileEntitySignRenderer.DESTROY_STAGES[p_192841_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 2.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(TileEntitySignRenderer.SIGN_TEXTURE);
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.6666667f, -0.6666667f, -0.6666667f);
        this.model.renderSign();
        GlStateManager.popMatrix();
        final FontRenderer fontrenderer = this.getFontRenderer();
        final float f4 = 0.010416667f;
        GlStateManager.translate(0.0f, 0.33333334f, 0.046666667f);
        GlStateManager.scale(0.010416667f, -0.010416667f, 0.010416667f);
        GlStateManager.glNormal3f(0.0f, 0.0f, -0.010416667f);
        GlStateManager.depthMask(false);
        int i = 0;
        if (Config.isCustomColors()) {
            i = CustomColors.getSignTextColor(i);
        }
        if (p_192841_9_ < 0) {
            for (int j = 0; j < p_192841_1_.signText.length; ++j) {
                if (p_192841_1_.signText[j] != null) {
                    final ITextComponent itextcomponent = p_192841_1_.signText[j];
                    final List<ITextComponent> list = GuiUtilRenderComponents.splitText(itextcomponent, 90, fontrenderer, false, true);
                    String s = (list != null && !list.isEmpty()) ? list.get(0).getFormattedText() : "";
                    if (j == p_192841_1_.lineBeingEdited) {
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - p_192841_1_.signText.length * 5, i);
                    }
                    else {
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - p_192841_1_.signText.length * 5, i);
                    }
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        if (p_192841_9_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
