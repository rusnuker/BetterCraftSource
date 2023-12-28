// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiCharacterButton extends Gui
{
    public static final int WIDTH = 14;
    public static final int HEIGHT = 14;
    private Minecraft mc;
    private byte id;
    private int x;
    private int y;
    private boolean enabled;
    
    public GuiCharacterButton(final byte id, final int x, final int y) {
        this.mc = Minecraft.getMinecraft();
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public void draw(final int mx, final int my) {
        this.mc.renderEngine.bindTexture(GuiNBTNode.WIDGET_TEXTURE);
        if (this.inBounds(mx, my)) {
            Gui.drawRect(this.x, this.y, this.x + 14, this.y + 14, -2130706433);
        }
        if (this.enabled) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
        }
        this.drawTexturedModalRect(this.x, this.y, this.id * 14, 27, 14, 14);
    }
    
    public void setEnabled(final boolean aFlag) {
        this.enabled = aFlag;
    }
    
    public boolean inBounds(final int mx, final int my) {
        return this.enabled && mx >= this.x && my >= this.y && mx < this.x + 14 && my < this.y + 14;
    }
    
    public byte getId() {
        return this.id;
    }
}
