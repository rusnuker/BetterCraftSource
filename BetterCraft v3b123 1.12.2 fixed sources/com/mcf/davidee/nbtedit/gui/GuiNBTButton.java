// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import com.mcf.davidee.nbtedit.NBTStringHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiNBTButton extends Gui
{
    public static final int WIDTH = 9;
    public static final int HEIGHT = 9;
    private Minecraft mc;
    private byte id;
    private int x;
    private int y;
    private boolean enabled;
    private long hoverTime;
    
    public GuiNBTButton(final byte id, final int x, final int y) {
        this.mc = Minecraft.getMinecraft();
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public void draw(final int mx, final int my) {
        this.mc.renderEngine.bindTexture(GuiNBTNode.WIDGET_TEXTURE);
        if (this.inBounds(mx, my)) {
            Gui.drawRect(this.x, this.y, this.x + 9, this.y + 9, -2130706433);
            if (this.hoverTime == -1L) {
                this.hoverTime = System.currentTimeMillis();
            }
        }
        else {
            this.hoverTime = -1L;
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.enabled) {
            this.drawTexturedModalRect(this.x, this.y, (this.id - 1) * 9, 18, 9, 9);
        }
        if (this.hoverTime != -1L && System.currentTimeMillis() - this.hoverTime > 300L) {
            this.drawToolTip(mx, my);
        }
    }
    
    private void drawToolTip(final int mx, final int my) {
        final String s = NBTStringHelper.getButtonName(this.id);
        final int width = this.mc.fontRendererObj.getStringWidth(s);
        Gui.drawRect(mx + 4, my + 7, mx + 5 + width, my + 17, -16777216);
        this.mc.fontRendererObj.drawString(s, mx + 5, my + 8, 16777215);
    }
    
    public void setEnabled(final boolean aFlag) {
        this.enabled = aFlag;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean inBounds(final int mx, final int my) {
        return this.enabled && mx >= this.x && my >= this.y && mx < this.x + 9 && my < this.y + 9;
    }
    
    public byte getId() {
        return this.id;
    }
}
