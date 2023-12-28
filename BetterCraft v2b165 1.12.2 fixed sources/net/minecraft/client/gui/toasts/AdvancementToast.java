// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.toasts;

import java.util.Iterator;
import java.util.List;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.advancements.Advancement;

public class AdvancementToast implements IToast
{
    private final Advancement field_193679_c;
    private boolean field_194168_d;
    
    public AdvancementToast(final Advancement p_i47490_1_) {
        this.field_194168_d = false;
        this.field_193679_c = p_i47490_1_;
    }
    
    @Override
    public Visibility func_193653_a(final GuiToast p_193653_1_, final long p_193653_2_) {
        p_193653_1_.func_192989_b().getTextureManager().bindTexture(AdvancementToast.field_193654_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final DisplayInfo displayinfo = this.field_193679_c.func_192068_c();
        p_193653_1_.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
        if (displayinfo != null) {
            final List<String> list = p_193653_1_.func_192989_b().fontRendererObj.listFormattedStringToWidth(displayinfo.func_192297_a().getFormattedText(), 125);
            final int i = (displayinfo.func_192291_d() == FrameType.CHALLENGE) ? 16746751 : 16776960;
            if (list.size() == 1) {
                p_193653_1_.func_192989_b().fontRendererObj.drawString(I18n.format("advancements.toast." + displayinfo.func_192291_d().func_192307_a(), new Object[0]), 30, 7, i | 0xFF000000);
                p_193653_1_.func_192989_b().fontRendererObj.drawString(displayinfo.func_192297_a().getFormattedText(), 30, 18, -1);
            }
            else {
                final int j = 1500;
                final float f = 300.0f;
                if (p_193653_2_ < 1500L) {
                    final int k = MathHelper.floor(MathHelper.clamp((1500L - p_193653_2_) / 300.0f, 0.0f, 1.0f) * 255.0f) << 24 | 0x4000000;
                    p_193653_1_.func_192989_b().fontRendererObj.drawString(I18n.format("advancements.toast." + displayinfo.func_192291_d().func_192307_a(), new Object[0]), 30, 11, i | k);
                }
                else {
                    final int i2 = MathHelper.floor(MathHelper.clamp((p_193653_2_ - 1500L) / 300.0f, 0.0f, 1.0f) * 252.0f) << 24 | 0x4000000;
                    int l = 16 - list.size() * p_193653_1_.func_192989_b().fontRendererObj.FONT_HEIGHT / 2;
                    for (final String s : list) {
                        p_193653_1_.func_192989_b().fontRendererObj.drawString(s, 30, l, 0xFFFFFF | i2);
                        l += p_193653_1_.func_192989_b().fontRendererObj.FONT_HEIGHT;
                    }
                }
            }
            if (!this.field_194168_d && p_193653_2_ > 0L) {
                this.field_194168_d = true;
                if (displayinfo.func_192291_d() == FrameType.CHALLENGE) {
                    p_193653_1_.func_192989_b().getSoundHandler().playSound(PositionedSoundRecord.func_194007_a(SoundEvents.field_194228_if, 1.0f, 1.0f));
                }
            }
            RenderHelper.enableGUIStandardItemLighting();
            p_193653_1_.func_192989_b().getRenderItem().renderItemAndEffectIntoGUI(null, displayinfo.func_192298_b(), 8, 8);
            return (p_193653_2_ >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
        }
        return Visibility.HIDE;
    }
}
