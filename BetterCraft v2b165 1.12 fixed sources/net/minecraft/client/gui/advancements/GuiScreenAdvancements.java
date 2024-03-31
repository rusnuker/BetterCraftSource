// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.advancements;

import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenAdvancements extends GuiScreen implements ClientAdvancementManager.IListener
{
    private static final ResourceLocation field_191943_f;
    private static final ResourceLocation field_191945_g;
    private final ClientAdvancementManager field_191946_h;
    private final Map<Advancement, GuiAdvancementTab> field_191947_i;
    private GuiAdvancementTab field_191940_s;
    private int field_191941_t;
    private int field_191942_u;
    private boolean field_191944_v;
    
    static {
        field_191943_f = new ResourceLocation("textures/gui/advancements/window.png");
        field_191945_g = new ResourceLocation("textures/gui/advancements/tabs.png");
    }
    
    public GuiScreenAdvancements(final ClientAdvancementManager p_i47383_1_) {
        this.field_191947_i = (Map<Advancement, GuiAdvancementTab>)Maps.newLinkedHashMap();
        this.field_191946_h = p_i47383_1_;
    }
    
    @Override
    public void initGui() {
        this.field_191947_i.clear();
        this.field_191940_s = null;
        this.field_191946_h.func_192798_a(this);
        if (this.field_191940_s == null && !this.field_191947_i.isEmpty()) {
            this.field_191946_h.func_194230_a(this.field_191947_i.values().iterator().next().func_193935_c(), true);
        }
        else {
            this.field_191946_h.func_194230_a((this.field_191940_s == null) ? null : this.field_191940_s.func_193935_c(), true);
        }
    }
    
    @Override
    public void onGuiClosed() {
        this.field_191946_h.func_192798_a(null);
        final NetHandlerPlayClient nethandlerplayclient = this.mc.getConnection();
        if (nethandlerplayclient != null) {
            nethandlerplayclient.sendPacket(CPacketSeenAdvancements.func_194164_a());
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = (GuiScreenAdvancements.width - 252) / 2;
            final int j = (GuiScreenAdvancements.height - 140) / 2;
            for (final GuiAdvancementTab guiadvancementtab : this.field_191947_i.values()) {
                if (guiadvancementtab.func_191793_c(i, j, mouseX, mouseY)) {
                    this.field_191946_h.func_194230_a(guiadvancementtab.func_193935_c(), true);
                    break;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == this.mc.gameSettings.field_194146_ao.getKeyCode()) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int i = (GuiScreenAdvancements.width - 252) / 2;
        final int j = (GuiScreenAdvancements.height - 140) / 2;
        if (Mouse.isButtonDown(0)) {
            if (!this.field_191944_v) {
                this.field_191944_v = true;
            }
            else if (this.field_191940_s != null) {
                this.field_191940_s.func_191797_b(mouseX - this.field_191941_t, mouseY - this.field_191942_u);
            }
            this.field_191941_t = mouseX;
            this.field_191942_u = mouseY;
        }
        else {
            this.field_191944_v = false;
        }
        this.drawDefaultBackground();
        this.func_191936_c(mouseX, mouseY, i, j);
        this.func_191934_b(i, j);
        this.func_191937_d(mouseX, mouseY, i, j);
    }
    
    private void func_191936_c(final int p_191936_1_, final int p_191936_2_, final int p_191936_3_, final int p_191936_4_) {
        final GuiAdvancementTab guiadvancementtab = this.field_191940_s;
        if (guiadvancementtab == null) {
            Gui.drawRect(p_191936_3_ + 9, p_191936_4_ + 18, p_191936_3_ + 9 + 234, p_191936_4_ + 18 + 113, -16777216);
            final String s = I18n.format("advancements.empty", new Object[0]);
            final int i = this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString(s, p_191936_3_ + 9 + 117 - i / 2, p_191936_4_ + 18 + 56 - this.fontRendererObj.FONT_HEIGHT / 2, -1);
            this.fontRendererObj.drawString(":(", p_191936_3_ + 9 + 117 - this.fontRendererObj.getStringWidth(":(") / 2, p_191936_4_ + 18 + 113 - this.fontRendererObj.FONT_HEIGHT, -1);
        }
        else {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), -400.0f);
            GlStateManager.enableDepth();
            guiadvancementtab.func_191799_a();
            GlStateManager.popMatrix();
            GlStateManager.depthFunc(515);
            GlStateManager.disableDepth();
        }
    }
    
    public void func_191934_b(final int p_191934_1_, final int p_191934_2_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
        this.mc.getTextureManager().bindTexture(GuiScreenAdvancements.field_191943_f);
        this.drawTexturedModalRect(p_191934_1_, p_191934_2_, 0, 0, 252, 140);
        if (this.field_191947_i.size() > 1) {
            this.mc.getTextureManager().bindTexture(GuiScreenAdvancements.field_191945_g);
            for (final GuiAdvancementTab guiadvancementtab : this.field_191947_i.values()) {
                guiadvancementtab.func_191798_a(p_191934_1_, p_191934_2_, guiadvancementtab == this.field_191940_s);
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();
            for (final GuiAdvancementTab guiadvancementtab2 : this.field_191947_i.values()) {
                guiadvancementtab2.func_191796_a(p_191934_1_, p_191934_2_, this.itemRender);
            }
            GlStateManager.disableBlend();
        }
        this.fontRendererObj.drawString(I18n.format("gui.advancements", new Object[0]), p_191934_1_ + 8, p_191934_2_ + 6, 4210752);
    }
    
    private void func_191937_d(final int p_191937_1_, final int p_191937_2_, final int p_191937_3_, final int p_191937_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.field_191940_s != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableDepth();
            GlStateManager.translate((float)(p_191937_3_ + 9), (float)(p_191937_4_ + 18), 400.0f);
            this.field_191940_s.func_192991_a(p_191937_1_ - p_191937_3_ - 9, p_191937_2_ - p_191937_4_ - 18, p_191937_3_, p_191937_4_);
            GlStateManager.disableDepth();
            GlStateManager.popMatrix();
        }
        if (this.field_191947_i.size() > 1) {
            for (final GuiAdvancementTab guiadvancementtab : this.field_191947_i.values()) {
                if (guiadvancementtab.func_191793_c(p_191937_3_, p_191937_4_, p_191937_1_, p_191937_2_)) {
                    this.drawCreativeTabHoveringText(guiadvancementtab.func_191795_d(), p_191937_1_, p_191937_2_);
                }
            }
        }
    }
    
    @Override
    public void func_191931_a(final Advancement p_191931_1_) {
        final GuiAdvancementTab guiadvancementtab = GuiAdvancementTab.func_193936_a(this.mc, this, this.field_191947_i.size(), p_191931_1_);
        if (guiadvancementtab != null) {
            this.field_191947_i.put(p_191931_1_, guiadvancementtab);
        }
    }
    
    @Override
    public void func_191928_b(final Advancement p_191928_1_) {
    }
    
    @Override
    public void func_191932_c(final Advancement p_191932_1_) {
        final GuiAdvancementTab guiadvancementtab = this.func_191935_f(p_191932_1_);
        if (guiadvancementtab != null) {
            guiadvancementtab.func_191800_a(p_191932_1_);
        }
    }
    
    @Override
    public void func_191929_d(final Advancement p_191929_1_) {
    }
    
    @Override
    public void func_191933_a(final Advancement p_191933_1_, final AdvancementProgress p_191933_2_) {
        final GuiAdvancement guiadvancement = this.func_191938_e(p_191933_1_);
        if (guiadvancement != null) {
            guiadvancement.func_191824_a(p_191933_2_);
        }
    }
    
    @Override
    public void func_193982_e(@Nullable final Advancement p_193982_1_) {
        this.field_191940_s = this.field_191947_i.get(p_193982_1_);
    }
    
    @Override
    public void func_191930_a() {
        this.field_191947_i.clear();
        this.field_191940_s = null;
    }
    
    @Nullable
    public GuiAdvancement func_191938_e(final Advancement p_191938_1_) {
        final GuiAdvancementTab guiadvancementtab = this.func_191935_f(p_191938_1_);
        return (guiadvancementtab == null) ? null : guiadvancementtab.func_191794_b(p_191938_1_);
    }
    
    @Nullable
    private GuiAdvancementTab func_191935_f(Advancement p_191935_1_) {
        while (p_191935_1_.func_192070_b() != null) {
            p_191935_1_ = p_191935_1_.func_192070_b();
        }
        return this.field_191947_i.get(p_191935_1_);
    }
}
