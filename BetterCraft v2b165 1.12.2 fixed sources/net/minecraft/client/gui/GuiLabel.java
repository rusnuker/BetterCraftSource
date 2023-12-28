// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import com.google.common.collect.Lists;
import java.util.List;

public class GuiLabel extends Gui
{
    protected int width;
    protected int height;
    public int x;
    public int y;
    private final List<String> labels;
    public int id;
    private boolean centered;
    public boolean visible;
    private boolean labelBgEnabled;
    private final int textColor;
    private int backColor;
    private int ulColor;
    private int brColor;
    private final FontRenderer fontRenderer;
    private int border;
    
    public GuiLabel(final FontRenderer fontRendererObj, final int p_i45540_2_, final int p_i45540_3_, final int p_i45540_4_, final int p_i45540_5_, final int p_i45540_6_, final int p_i45540_7_) {
        this.width = 200;
        this.height = 20;
        this.visible = true;
        this.fontRenderer = fontRendererObj;
        this.id = p_i45540_2_;
        this.x = p_i45540_3_;
        this.y = p_i45540_4_;
        this.width = p_i45540_5_;
        this.height = p_i45540_6_;
        this.labels = (List<String>)Lists.newArrayList();
        this.centered = false;
        this.labelBgEnabled = false;
        this.textColor = p_i45540_7_;
        this.backColor = -1;
        this.ulColor = -1;
        this.brColor = -1;
        this.border = 0;
    }
    
    public void addLine(final String p_175202_1_) {
        this.labels.add(I18n.format(p_175202_1_, new Object[0]));
    }
    
    public GuiLabel setCentered() {
        this.centered = true;
        return this;
    }
    
    public void drawLabel(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.drawLabelBackground(mc, mouseX, mouseY);
            final int i = this.y + this.height / 2 + this.border / 2;
            final int j = i - this.labels.size() * 10 / 2;
            for (int k = 0; k < this.labels.size(); ++k) {
                if (this.centered) {
                    Gui.drawCenteredString(this.fontRenderer, this.labels.get(k), this.x + this.width / 2, j + k * 10, this.textColor);
                }
                else {
                    Gui.drawString(this.fontRenderer, this.labels.get(k), this.x, j + k * 10, this.textColor);
                }
            }
        }
    }
    
    protected void drawLabelBackground(final Minecraft mcIn, final int p_146160_2_, final int p_146160_3_) {
        if (this.labelBgEnabled) {
            final int i = this.width + this.border * 2;
            final int j = this.height + this.border * 2;
            final int k = this.x - this.border;
            final int l = this.y - this.border;
            Gui.drawRect(k, l, k + i, l + j, this.backColor);
            this.drawHorizontalLine(k, k + i, l, this.ulColor);
            this.drawHorizontalLine(k, k + i, l + j, this.brColor);
            this.drawVerticalLine(k, l, l + j, this.ulColor);
            this.drawVerticalLine(k + i, l, l + j, this.brColor);
        }
    }
}
