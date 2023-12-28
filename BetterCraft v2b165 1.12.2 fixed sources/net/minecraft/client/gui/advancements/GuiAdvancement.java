// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.advancements;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.Advancement;
import java.util.regex.Pattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiAdvancement extends Gui
{
    private static final ResourceLocation field_191827_a;
    private static final Pattern field_192996_f;
    private final GuiAdvancementTab field_191828_f;
    private final Advancement field_191829_g;
    private final DisplayInfo field_191830_h;
    private final String field_191831_i;
    private final int field_191832_j;
    private final List<String> field_192997_l;
    private final Minecraft field_191833_k;
    private GuiAdvancement field_191834_l;
    private final List<GuiAdvancement> field_191835_m;
    private AdvancementProgress field_191836_n;
    private final int field_191837_o;
    private final int field_191826_p;
    
    static {
        field_191827_a = new ResourceLocation("textures/gui/advancements/widgets.png");
        field_192996_f = Pattern.compile("(.+) \\S+");
    }
    
    public GuiAdvancement(final GuiAdvancementTab p_i47385_1_, final Minecraft p_i47385_2_, final Advancement p_i47385_3_, final DisplayInfo p_i47385_4_) {
        this.field_191835_m = (List<GuiAdvancement>)Lists.newArrayList();
        this.field_191828_f = p_i47385_1_;
        this.field_191829_g = p_i47385_3_;
        this.field_191830_h = p_i47385_4_;
        this.field_191833_k = p_i47385_2_;
        this.field_191831_i = p_i47385_2_.fontRendererObj.trimStringToWidth(p_i47385_4_.func_192297_a().getFormattedText(), 163);
        this.field_191837_o = MathHelper.floor(p_i47385_4_.func_192299_e() * 28.0f);
        this.field_191826_p = MathHelper.floor(p_i47385_4_.func_192296_f() * 27.0f);
        final int i = p_i47385_3_.func_193124_g();
        final int j = String.valueOf(i).length();
        final int k = (i > 1) ? (p_i47385_2_.fontRendererObj.getStringWidth("  ") + p_i47385_2_.fontRendererObj.getStringWidth("0") * j * 2 + p_i47385_2_.fontRendererObj.getStringWidth("/")) : 0;
        int l = 29 + p_i47385_2_.fontRendererObj.getStringWidth(this.field_191831_i) + k;
        final String s = p_i47385_4_.func_193222_b().getFormattedText();
        this.field_192997_l = this.func_192995_a(s, l);
        for (final String s2 : this.field_192997_l) {
            l = Math.max(l, p_i47385_2_.fontRendererObj.getStringWidth(s2));
        }
        this.field_191832_j = l + 3 + 5;
    }
    
    private List<String> func_192995_a(final String p_192995_1_, final int p_192995_2_) {
        if (p_192995_1_.isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> list = this.field_191833_k.fontRendererObj.listFormattedStringToWidth(p_192995_1_, p_192995_2_);
        if (list.size() < 2) {
            return list;
        }
        final String s = list.get(0);
        final String s2 = list.get(1);
        final int i = this.field_191833_k.fontRendererObj.getStringWidth(String.valueOf(s) + ' ' + s2.split(" ")[0]);
        if (i - p_192995_2_ <= 10) {
            return this.field_191833_k.fontRendererObj.listFormattedStringToWidth(p_192995_1_, i);
        }
        final Matcher matcher = GuiAdvancement.field_192996_f.matcher(s);
        if (matcher.matches()) {
            final int j = this.field_191833_k.fontRendererObj.getStringWidth(matcher.group(1));
            if (p_192995_2_ - j <= 10) {
                return this.field_191833_k.fontRendererObj.listFormattedStringToWidth(p_192995_1_, j);
            }
        }
        return list;
    }
    
    @Nullable
    private GuiAdvancement func_191818_a(Advancement p_191818_1_) {
        do {
            p_191818_1_ = p_191818_1_.func_192070_b();
        } while (p_191818_1_ != null && p_191818_1_.func_192068_c() == null);
        if (p_191818_1_ != null && p_191818_1_.func_192068_c() != null) {
            return this.field_191828_f.func_191794_b(p_191818_1_);
        }
        return null;
    }
    
    public void func_191819_a(final int p_191819_1_, final int p_191819_2_, final boolean p_191819_3_) {
        if (this.field_191834_l != null) {
            final int i = p_191819_1_ + this.field_191834_l.field_191837_o + 13;
            final int j = p_191819_1_ + this.field_191834_l.field_191837_o + 26 + 4;
            final int k = p_191819_2_ + this.field_191834_l.field_191826_p + 13;
            final int l = p_191819_1_ + this.field_191837_o + 13;
            final int i2 = p_191819_2_ + this.field_191826_p + 13;
            final int j2 = p_191819_3_ ? -16777216 : -1;
            if (p_191819_3_) {
                this.drawHorizontalLine(j, i, k - 1, j2);
                this.drawHorizontalLine(j + 1, i, k, j2);
                this.drawHorizontalLine(j, i, k + 1, j2);
                this.drawHorizontalLine(l, j - 1, i2 - 1, j2);
                this.drawHorizontalLine(l, j - 1, i2, j2);
                this.drawHorizontalLine(l, j - 1, i2 + 1, j2);
                this.drawVerticalLine(j - 1, i2, k, j2);
                this.drawVerticalLine(j + 1, i2, k, j2);
            }
            else {
                this.drawHorizontalLine(j, i, k, j2);
                this.drawHorizontalLine(l, j, i2, j2);
                this.drawVerticalLine(j, i2, k, j2);
            }
        }
        for (final GuiAdvancement guiadvancement : this.field_191835_m) {
            guiadvancement.func_191819_a(p_191819_1_, p_191819_2_, p_191819_3_);
        }
    }
    
    public void func_191817_b(final int p_191817_1_, final int p_191817_2_) {
        if (!this.field_191830_h.func_193224_j() || (this.field_191836_n != null && this.field_191836_n.func_192105_a())) {
            final float f = (this.field_191836_n == null) ? 0.0f : this.field_191836_n.func_192103_c();
            AdvancementState advancementstate;
            if (f >= 1.0f) {
                advancementstate = AdvancementState.OBTAINED;
            }
            else {
                advancementstate = AdvancementState.UNOBTAINED;
            }
            this.field_191833_k.getTextureManager().bindTexture(GuiAdvancement.field_191827_a);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            this.drawTexturedModalRect(p_191817_1_ + this.field_191837_o + 3, p_191817_2_ + this.field_191826_p, this.field_191830_h.func_192291_d().func_192309_b(), 128 + advancementstate.func_192667_a() * 26, 26, 26);
            RenderHelper.enableGUIStandardItemLighting();
            this.field_191833_k.getRenderItem().renderItemAndEffectIntoGUI(null, this.field_191830_h.func_192298_b(), p_191817_1_ + this.field_191837_o + 8, p_191817_2_ + this.field_191826_p + 5);
        }
        for (final GuiAdvancement guiadvancement : this.field_191835_m) {
            guiadvancement.func_191817_b(p_191817_1_, p_191817_2_);
        }
    }
    
    public void func_191824_a(final AdvancementProgress p_191824_1_) {
        this.field_191836_n = p_191824_1_;
    }
    
    public void func_191822_a(final GuiAdvancement p_191822_1_) {
        this.field_191835_m.add(p_191822_1_);
    }
    
    public void func_191821_a(final int p_191821_1_, final int p_191821_2_, final float p_191821_3_, final int p_191821_4_, final int p_191821_5_) {
        final int n = p_191821_4_ + p_191821_1_ + this.field_191837_o + this.field_191832_j + 26;
        this.field_191828_f.func_193934_g();
        final boolean flag = n >= GuiScreenAdvancements.width;
        final String s = (this.field_191836_n == null) ? null : this.field_191836_n.func_193126_d();
        final int i = (s == null) ? 0 : this.field_191833_k.fontRendererObj.getStringWidth(s);
        final boolean flag2 = 113 - p_191821_2_ - this.field_191826_p - 26 <= 6 + this.field_192997_l.size() * this.field_191833_k.fontRendererObj.FONT_HEIGHT;
        final float f = (this.field_191836_n == null) ? 0.0f : this.field_191836_n.func_192103_c();
        int j = MathHelper.floor(f * this.field_191832_j);
        AdvancementState advancementstate;
        AdvancementState advancementstate2;
        AdvancementState advancementstate3;
        if (f >= 1.0f) {
            j = this.field_191832_j / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.OBTAINED;
            advancementstate3 = AdvancementState.OBTAINED;
        }
        else if (j < 2) {
            j = this.field_191832_j / 2;
            advancementstate = AdvancementState.UNOBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
            advancementstate3 = AdvancementState.UNOBTAINED;
        }
        else if (j > this.field_191832_j - 2) {
            j = this.field_191832_j / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.OBTAINED;
            advancementstate3 = AdvancementState.UNOBTAINED;
        }
        else {
            advancementstate = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
            advancementstate3 = AdvancementState.UNOBTAINED;
        }
        final int k = this.field_191832_j - j;
        this.field_191833_k.getTextureManager().bindTexture(GuiAdvancement.field_191827_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        final int l = p_191821_2_ + this.field_191826_p;
        int i2;
        if (flag) {
            i2 = p_191821_1_ + this.field_191837_o - this.field_191832_j + 26 + 6;
        }
        else {
            i2 = p_191821_1_ + this.field_191837_o;
        }
        final int j2 = 32 + this.field_192997_l.size() * this.field_191833_k.fontRendererObj.FONT_HEIGHT;
        if (!this.field_192997_l.isEmpty()) {
            if (flag2) {
                this.func_192994_a(i2, l + 26 - j2, this.field_191832_j, j2, 10, 200, 26, 0, 52);
            }
            else {
                this.func_192994_a(i2, l, this.field_191832_j, j2, 10, 200, 26, 0, 52);
            }
        }
        this.drawTexturedModalRect(i2, l, 0, advancementstate.func_192667_a() * 26, j, 26);
        this.drawTexturedModalRect(i2 + j, l, 200 - k, advancementstate2.func_192667_a() * 26, k, 26);
        this.drawTexturedModalRect(p_191821_1_ + this.field_191837_o + 3, p_191821_2_ + this.field_191826_p, this.field_191830_h.func_192291_d().func_192309_b(), 128 + advancementstate3.func_192667_a() * 26, 26, 26);
        if (flag) {
            this.field_191833_k.fontRendererObj.drawString(this.field_191831_i, (float)(i2 + 5), (float)(p_191821_2_ + this.field_191826_p + 9), -1, true);
            if (s != null) {
                this.field_191833_k.fontRendererObj.drawString(s, (float)(p_191821_1_ + this.field_191837_o - i), (float)(p_191821_2_ + this.field_191826_p + 9), -1, true);
            }
        }
        else {
            this.field_191833_k.fontRendererObj.drawString(this.field_191831_i, (float)(p_191821_1_ + this.field_191837_o + 32), (float)(p_191821_2_ + this.field_191826_p + 9), -1, true);
            if (s != null) {
                this.field_191833_k.fontRendererObj.drawString(s, (float)(p_191821_1_ + this.field_191837_o + this.field_191832_j - i - 5), (float)(p_191821_2_ + this.field_191826_p + 9), -1, true);
            }
        }
        if (flag2) {
            for (int k2 = 0; k2 < this.field_192997_l.size(); ++k2) {
                this.field_191833_k.fontRendererObj.drawString(this.field_192997_l.get(k2), (float)(i2 + 5), (float)(l + 26 - j2 + 7 + k2 * this.field_191833_k.fontRendererObj.FONT_HEIGHT), -5592406, false);
            }
        }
        else {
            for (int l2 = 0; l2 < this.field_192997_l.size(); ++l2) {
                this.field_191833_k.fontRendererObj.drawString(this.field_192997_l.get(l2), (float)(i2 + 5), (float)(p_191821_2_ + this.field_191826_p + 9 + 17 + l2 * this.field_191833_k.fontRendererObj.FONT_HEIGHT), -5592406, false);
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
        this.field_191833_k.getRenderItem().renderItemAndEffectIntoGUI(null, this.field_191830_h.func_192298_b(), p_191821_1_ + this.field_191837_o + 8, p_191821_2_ + this.field_191826_p + 5);
    }
    
    protected void func_192994_a(final int p_192994_1_, final int p_192994_2_, final int p_192994_3_, final int p_192994_4_, final int p_192994_5_, final int p_192994_6_, final int p_192994_7_, final int p_192994_8_, final int p_192994_9_) {
        this.drawTexturedModalRect(p_192994_1_, p_192994_2_, p_192994_8_, p_192994_9_, p_192994_5_, p_192994_5_);
        this.func_192993_a(p_192994_1_ + p_192994_5_, p_192994_2_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        this.drawTexturedModalRect(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_, p_192994_5_, p_192994_5_);
        this.drawTexturedModalRect(p_192994_1_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        this.func_192993_a(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        this.drawTexturedModalRect(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        this.func_192993_a(p_192994_1_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        this.func_192993_a(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        this.func_192993_a(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
    }
    
    protected void func_192993_a(final int p_192993_1_, final int p_192993_2_, final int p_192993_3_, final int p_192993_4_, final int p_192993_5_, final int p_192993_6_, final int p_192993_7_, final int p_192993_8_) {
        for (int i = 0; i < p_192993_3_; i += p_192993_7_) {
            final int j = p_192993_1_ + i;
            final int k = Math.min(p_192993_7_, p_192993_3_ - i);
            for (int l = 0; l < p_192993_4_; l += p_192993_8_) {
                final int i2 = p_192993_2_ + l;
                final int j2 = Math.min(p_192993_8_, p_192993_4_ - l);
                this.drawTexturedModalRect(j, i2, p_192993_5_, p_192993_6_, k, j2);
            }
        }
    }
    
    public boolean func_191816_c(final int p_191816_1_, final int p_191816_2_, final int p_191816_3_, final int p_191816_4_) {
        if (!this.field_191830_h.func_193224_j() || (this.field_191836_n != null && this.field_191836_n.func_192105_a())) {
            final int i = p_191816_1_ + this.field_191837_o;
            final int j = i + 26;
            final int k = p_191816_2_ + this.field_191826_p;
            final int l = k + 26;
            return p_191816_3_ >= i && p_191816_3_ <= j && p_191816_4_ >= k && p_191816_4_ <= l;
        }
        return false;
    }
    
    public void func_191825_b() {
        if (this.field_191834_l == null && this.field_191829_g.func_192070_b() != null) {
            this.field_191834_l = this.func_191818_a(this.field_191829_g);
            if (this.field_191834_l != null) {
                this.field_191834_l.func_191822_a(this);
            }
        }
    }
    
    public int func_191820_c() {
        return this.field_191826_p;
    }
    
    public int func_191823_d() {
        return this.field_191837_o;
    }
}
