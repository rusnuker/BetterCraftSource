// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.advancements;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiAdvancementTab extends Gui
{
    private final Minecraft field_191802_a;
    private final GuiScreenAdvancements field_193938_f;
    private final AdvancementTabType field_191803_f;
    private final int field_191804_g;
    private final Advancement field_191805_h;
    private final DisplayInfo field_191806_i;
    private final ItemStack field_191807_j;
    private final String field_191808_k;
    private final GuiAdvancement field_191809_l;
    private final Map<Advancement, GuiAdvancement> field_191810_m;
    private int field_191811_n;
    private int field_191812_o;
    private int field_193939_q;
    private int field_193940_r;
    private int field_191813_p;
    private int field_191814_q;
    private float field_191815_r;
    private boolean field_192992_s;
    
    public GuiAdvancementTab(final Minecraft p_i47589_1_, final GuiScreenAdvancements p_i47589_2_, final AdvancementTabType p_i47589_3_, final int p_i47589_4_, final Advancement p_i47589_5_, final DisplayInfo p_i47589_6_) {
        this.field_191810_m = (Map<Advancement, GuiAdvancement>)Maps.newLinkedHashMap();
        this.field_193939_q = Integer.MAX_VALUE;
        this.field_193940_r = Integer.MAX_VALUE;
        this.field_191813_p = Integer.MIN_VALUE;
        this.field_191814_q = Integer.MIN_VALUE;
        this.field_191802_a = p_i47589_1_;
        this.field_193938_f = p_i47589_2_;
        this.field_191803_f = p_i47589_3_;
        this.field_191804_g = p_i47589_4_;
        this.field_191805_h = p_i47589_5_;
        this.field_191806_i = p_i47589_6_;
        this.field_191807_j = p_i47589_6_.func_192298_b();
        this.field_191808_k = p_i47589_6_.func_192297_a().getFormattedText();
        this.func_193937_a(this.field_191809_l = new GuiAdvancement(this, p_i47589_1_, p_i47589_5_, p_i47589_6_), p_i47589_5_);
    }
    
    public Advancement func_193935_c() {
        return this.field_191805_h;
    }
    
    public String func_191795_d() {
        return this.field_191808_k;
    }
    
    public void func_191798_a(final int p_191798_1_, final int p_191798_2_, final boolean p_191798_3_) {
        this.field_191803_f.func_192651_a(this, p_191798_1_, p_191798_2_, p_191798_3_, this.field_191804_g);
    }
    
    public void func_191796_a(final int p_191796_1_, final int p_191796_2_, final RenderItem p_191796_3_) {
        this.field_191803_f.func_192652_a(p_191796_1_, p_191796_2_, this.field_191804_g, p_191796_3_, this.field_191807_j);
    }
    
    public void func_191799_a() {
        if (!this.field_192992_s) {
            this.field_191811_n = 117 - (this.field_191813_p + this.field_193939_q) / 2;
            this.field_191812_o = 56 - (this.field_191814_q + this.field_193940_r) / 2;
            this.field_192992_s = true;
        }
        GlStateManager.depthFunc(518);
        Gui.drawRect(0, 0, 234, 113, -16777216);
        GlStateManager.depthFunc(515);
        final ResourceLocation resourcelocation = this.field_191806_i.func_192293_c();
        if (resourcelocation != null) {
            this.field_191802_a.getTextureManager().bindTexture(resourcelocation);
        }
        else {
            this.field_191802_a.getTextureManager().bindTexture(TextureManager.field_194008_a);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int i = this.field_191811_n % 16;
        final int j = this.field_191812_o % 16;
        for (int k = -1; k <= 15; ++k) {
            for (int l = -1; l <= 8; ++l) {
                Gui.drawModalRectWithCustomSizedTexture(i + 16 * k, j + 16 * l, 0.0f, 0.0f, 16, 16, 16.0f, 16.0f);
            }
        }
        this.field_191809_l.func_191819_a(this.field_191811_n, this.field_191812_o, true);
        this.field_191809_l.func_191819_a(this.field_191811_n, this.field_191812_o, false);
        this.field_191809_l.func_191817_b(this.field_191811_n, this.field_191812_o);
    }
    
    public void func_192991_a(final int p_192991_1_, final int p_192991_2_, final int p_192991_3_, final int p_192991_4_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 200.0f);
        Gui.drawRect(0, 0, 234, 113, MathHelper.floor(this.field_191815_r * 255.0f) << 24);
        boolean flag = false;
        if (p_192991_1_ > 0 && p_192991_1_ < 234 && p_192991_2_ > 0 && p_192991_2_ < 113) {
            for (final GuiAdvancement guiadvancement : this.field_191810_m.values()) {
                if (guiadvancement.func_191816_c(this.field_191811_n, this.field_191812_o, p_192991_1_, p_192991_2_)) {
                    flag = true;
                    guiadvancement.func_191821_a(this.field_191811_n, this.field_191812_o, this.field_191815_r, p_192991_3_, p_192991_4_);
                    break;
                }
            }
        }
        GlStateManager.popMatrix();
        if (flag) {
            this.field_191815_r = MathHelper.clamp(this.field_191815_r + 0.02f, 0.0f, 0.3f);
        }
        else {
            this.field_191815_r = MathHelper.clamp(this.field_191815_r - 0.04f, 0.0f, 1.0f);
        }
    }
    
    public boolean func_191793_c(final int p_191793_1_, final int p_191793_2_, final int p_191793_3_, final int p_191793_4_) {
        return this.field_191803_f.func_192654_a(p_191793_1_, p_191793_2_, this.field_191804_g, p_191793_3_, p_191793_4_);
    }
    
    @Nullable
    public static GuiAdvancementTab func_193936_a(final Minecraft p_193936_0_, final GuiScreenAdvancements p_193936_1_, int p_193936_2_, final Advancement p_193936_3_) {
        if (p_193936_3_.func_192068_c() == null) {
            return null;
        }
        AdvancementTabType[] values;
        for (int length = (values = AdvancementTabType.values()).length, i = 0; i < length; ++i) {
            final AdvancementTabType advancementtabtype = values[i];
            if (p_193936_2_ < advancementtabtype.func_192650_a()) {
                return new GuiAdvancementTab(p_193936_0_, p_193936_1_, advancementtabtype, p_193936_2_, p_193936_3_, p_193936_3_.func_192068_c());
            }
            p_193936_2_ -= advancementtabtype.func_192650_a();
        }
        return null;
    }
    
    public void func_191797_b(final int p_191797_1_, final int p_191797_2_) {
        if (this.field_191813_p - this.field_193939_q > 234) {
            this.field_191811_n = MathHelper.clamp(this.field_191811_n + p_191797_1_, -(this.field_191813_p - 234), 0);
        }
        if (this.field_191814_q - this.field_193940_r > 113) {
            this.field_191812_o = MathHelper.clamp(this.field_191812_o + p_191797_2_, -(this.field_191814_q - 113), 0);
        }
    }
    
    public void func_191800_a(final Advancement p_191800_1_) {
        if (p_191800_1_.func_192068_c() != null) {
            final GuiAdvancement guiadvancement = new GuiAdvancement(this, this.field_191802_a, p_191800_1_, p_191800_1_.func_192068_c());
            this.func_193937_a(guiadvancement, p_191800_1_);
        }
    }
    
    private void func_193937_a(final GuiAdvancement p_193937_1_, final Advancement p_193937_2_) {
        this.field_191810_m.put(p_193937_2_, p_193937_1_);
        final int i = p_193937_1_.func_191823_d();
        final int j = i + 28;
        final int k = p_193937_1_.func_191820_c();
        final int l = k + 27;
        this.field_193939_q = Math.min(this.field_193939_q, i);
        this.field_191813_p = Math.max(this.field_191813_p, j);
        this.field_193940_r = Math.min(this.field_193940_r, k);
        this.field_191814_q = Math.max(this.field_191814_q, l);
        for (final GuiAdvancement guiadvancement : this.field_191810_m.values()) {
            guiadvancement.func_191825_b();
        }
    }
    
    @Nullable
    public GuiAdvancement func_191794_b(final Advancement p_191794_1_) {
        return this.field_191810_m.get(p_191794_1_);
    }
    
    public GuiScreenAdvancements func_193934_g() {
        return this.field_193938_f;
    }
}
