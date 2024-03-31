// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;

public class TutorialToast implements IToast
{
    private final Icons field_193671_c;
    private final String field_193672_d;
    private final String field_193673_e;
    private Visibility field_193674_f;
    private long field_193675_g;
    private float field_193676_h;
    private float field_193677_i;
    private final boolean field_193678_j;
    
    public TutorialToast(final Icons p_i47487_1_, final ITextComponent p_i47487_2_, @Nullable final ITextComponent p_i47487_3_, final boolean p_i47487_4_) {
        this.field_193674_f = Visibility.SHOW;
        this.field_193671_c = p_i47487_1_;
        this.field_193672_d = p_i47487_2_.getFormattedText();
        this.field_193673_e = ((p_i47487_3_ == null) ? null : p_i47487_3_.getFormattedText());
        this.field_193678_j = p_i47487_4_;
    }
    
    @Override
    public Visibility func_193653_a(final GuiToast p_193653_1_, final long p_193653_2_) {
        p_193653_1_.func_192989_b().getTextureManager().bindTexture(TutorialToast.field_193654_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        p_193653_1_.drawTexturedModalRect(0, 0, 0, 96, 160, 32);
        this.field_193671_c.func_193697_a(p_193653_1_, 6, 6);
        if (this.field_193673_e == null) {
            p_193653_1_.func_192989_b().fontRendererObj.drawString(this.field_193672_d, 30, 12, -11534256);
        }
        else {
            p_193653_1_.func_192989_b().fontRendererObj.drawString(this.field_193672_d, 30, 7, -11534256);
            p_193653_1_.func_192989_b().fontRendererObj.drawString(this.field_193673_e, 30, 18, -16777216);
        }
        if (this.field_193678_j) {
            Gui.drawRect(3, 28, 157, 29, -1);
            final float f = (float)MathHelper.clampedLerp(this.field_193676_h, this.field_193677_i, (p_193653_2_ - this.field_193675_g) / 100.0f);
            int i;
            if (this.field_193677_i >= this.field_193676_h) {
                i = -16755456;
            }
            else {
                i = -11206656;
            }
            Gui.drawRect(3, 28, (int)(3.0f + 154.0f * f), 29, i);
            this.field_193676_h = f;
            this.field_193675_g = p_193653_2_;
        }
        return this.field_193674_f;
    }
    
    public void func_193670_a() {
        this.field_193674_f = Visibility.HIDE;
    }
    
    public void func_193669_a(final float p_193669_1_) {
        this.field_193677_i = p_193669_1_;
    }
    
    public enum Icons
    {
        MOVEMENT_KEYS("MOVEMENT_KEYS", 0, 0, 0), 
        MOUSE("MOUSE", 1, 1, 0), 
        TREE("TREE", 2, 2, 0), 
        RECIPE_BOOK("RECIPE_BOOK", 3, 0, 1), 
        WOODEN_PLANKS("WOODEN_PLANKS", 4, 1, 1);
        
        private final int field_193703_f;
        private final int field_193704_g;
        
        private Icons(final String s, final int n, final int p_i47576_3_, final int p_i47576_4_) {
            this.field_193703_f = p_i47576_3_;
            this.field_193704_g = p_i47576_4_;
        }
        
        public void func_193697_a(final Gui p_193697_1_, final int p_193697_2_, final int p_193697_3_) {
            GlStateManager.enableBlend();
            p_193697_1_.drawTexturedModalRect(p_193697_2_, p_193697_3_, 176 + this.field_193703_f * 20, this.field_193704_g * 20, 20, 20);
            GlStateManager.enableBlend();
        }
    }
}
