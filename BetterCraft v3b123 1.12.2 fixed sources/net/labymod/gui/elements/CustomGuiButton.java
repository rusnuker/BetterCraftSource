// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import net.labymod.core.LabyModCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class CustomGuiButton extends GuiButton
{
    protected static final ResourceLocation BUTTON_TEXTURES;
    
    static {
        BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public CustomGuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        super(buttonId, x, y, buttonText);
    }
    
    public CustomGuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }
    
    public void setPosition(final int minX, final int minY, final int maxX, final int maxY) {
        LabyModCore.getMinecraft().setButtonXPosition(this, minX);
        LabyModCore.getMinecraft().setButtonYPosition(this, minY);
        this.width = maxX - minX;
        this.height = maxY - minY;
    }
    
    public void setXPosition(final int x) {
        LabyModCore.getMinecraft().setButtonXPosition(this, x);
    }
    
    public void setYPosition(final int y) {
        LabyModCore.getMinecraft().setButtonYPosition(this, y);
    }
    
    public int getXPosition() {
        return LabyModCore.getMinecraft().getXPosition(this);
    }
    
    public int getYPosition() {
        return LabyModCore.getMinecraft().getYPosition(this);
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getText() {
        return this.displayString;
    }
}
