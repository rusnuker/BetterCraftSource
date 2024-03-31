// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.client.model.ModelBed;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityBed;

public class TileEntityBedRenderer extends TileEntitySpecialRenderer<TileEntityBed>
{
    private static final ResourceLocation[] field_193848_a;
    private ModelBed field_193849_d;
    private int field_193850_e;
    
    static {
        final EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        field_193848_a = new ResourceLocation[aenumdyecolor.length];
        EnumDyeColor[] array;
        for (int length = (array = aenumdyecolor).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = array[i];
            TileEntityBedRenderer.field_193848_a[enumdyecolor.getMetadata()] = new ResourceLocation("textures/entity/bed/" + enumdyecolor.func_192396_c() + ".png");
        }
    }
    
    public TileEntityBedRenderer() {
        this.field_193849_d = new ModelBed();
        this.field_193850_e = this.field_193849_d.func_193770_a();
    }
    
    @Override
    public void func_192841_a(final TileEntityBed p_192841_1_, final double p_192841_2_, final double p_192841_4_, final double p_192841_6_, final float p_192841_8_, final int p_192841_9_, final float p_192841_10_) {
        if (this.field_193850_e != this.field_193849_d.func_193770_a()) {
            this.field_193849_d = new ModelBed();
            this.field_193850_e = this.field_193849_d.func_193770_a();
        }
        final boolean flag = p_192841_1_.getWorld() != null;
        final boolean flag2 = !flag || p_192841_1_.func_193050_e();
        final EnumDyeColor enumdyecolor = (p_192841_1_ != null) ? p_192841_1_.func_193048_a() : EnumDyeColor.RED;
        final int i = flag ? (p_192841_1_.getBlockMetadata() & 0x3) : 0;
        if (p_192841_9_ >= 0) {
            this.bindTexture(TileEntityBedRenderer.DESTROY_STAGES[p_192841_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            final ResourceLocation resourcelocation = TileEntityBedRenderer.field_193848_a[enumdyecolor.getMetadata()];
            if (resourcelocation != null) {
                this.bindTexture(resourcelocation);
            }
        }
        if (flag) {
            this.func_193847_a(flag2, p_192841_2_, p_192841_4_, p_192841_6_, i, p_192841_10_);
        }
        else {
            GlStateManager.pushMatrix();
            this.func_193847_a(true, p_192841_2_, p_192841_4_, p_192841_6_, i, p_192841_10_);
            this.func_193847_a(false, p_192841_2_, p_192841_4_, p_192841_6_ - 1.0, i, p_192841_10_);
            GlStateManager.popMatrix();
        }
        if (p_192841_9_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    private void func_193847_a(final boolean p_193847_1_, final double p_193847_2_, final double p_193847_4_, final double p_193847_6_, final int p_193847_8_, final float p_193847_9_) {
        this.field_193849_d.func_193769_a(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (p_193847_8_ == EnumFacing.NORTH.getHorizontalIndex()) {
            f = 0.0f;
        }
        else if (p_193847_8_ == EnumFacing.SOUTH.getHorizontalIndex()) {
            f = 180.0f;
            f2 = 1.0f;
            f3 = 1.0f;
        }
        else if (p_193847_8_ == EnumFacing.WEST.getHorizontalIndex()) {
            f = -90.0f;
            f3 = 1.0f;
        }
        else if (p_193847_8_ == EnumFacing.EAST.getHorizontalIndex()) {
            f = 90.0f;
            f2 = 1.0f;
        }
        GlStateManager.translate((float)p_193847_2_ + f2, (float)p_193847_4_ + 0.5625f, (float)p_193847_6_ + f3);
        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        this.field_193849_d.func_193771_b();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_193847_9_);
        GlStateManager.popMatrix();
    }
}
