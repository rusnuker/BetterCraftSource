// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable>
{
    private static final ResourceLocation TEXTURE_BOOK;
    private final ModelBook modelBook;
    
    static {
        TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
    }
    
    public TileEntityEnchantmentTableRenderer() {
        this.modelBook = new ModelBook();
    }
    
    @Override
    public void func_192841_a(final TileEntityEnchantmentTable p_192841_1_, final double p_192841_2_, final double p_192841_4_, final double p_192841_6_, final float p_192841_8_, final int p_192841_9_, final float p_192841_10_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.75f, (float)p_192841_6_ + 0.5f);
        final float f = p_192841_1_.tickCount + p_192841_8_;
        GlStateManager.translate(0.0f, 0.1f + MathHelper.sin(f * 0.1f) * 0.01f, 0.0f);
        float f2;
        for (f2 = p_192841_1_.bookRotation - p_192841_1_.bookRotationPrev; f2 >= 3.1415927f; f2 -= 6.2831855f) {}
        while (f2 < -3.1415927f) {
            f2 += 6.2831855f;
        }
        final float f3 = p_192841_1_.bookRotationPrev + f2 * p_192841_8_;
        GlStateManager.rotate(-f3 * 57.295776f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(80.0f, 0.0f, 0.0f, 1.0f);
        this.bindTexture(TileEntityEnchantmentTableRenderer.TEXTURE_BOOK);
        float f4 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.25f;
        float f5 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.75f;
        f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6f - 0.3f;
        f5 = (f5 - MathHelper.fastFloor(f5)) * 1.6f - 0.3f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        final float f6 = p_192841_1_.bookSpreadPrev + (p_192841_1_.bookSpread - p_192841_1_.bookSpreadPrev) * p_192841_8_;
        GlStateManager.enableCull();
        this.modelBook.render(null, f, f4, f5, f6, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
}
