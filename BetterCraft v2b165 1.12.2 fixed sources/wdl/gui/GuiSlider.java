// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

class GuiSlider extends GuiButton
{
    private float sliderValue;
    private boolean dragging;
    private final String text;
    private final int max;
    
    public GuiSlider(final int id, final int x, final int y, final int width, final int height, final String text, final int value, final int max) {
        super(id, x, y, width, height, text);
        this.text = text;
        this.max = max;
        this.setValue(value);
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
                this.dragging = true;
                this.displayString = I18n.format(this.text, this.getValue());
            }
            mc.getTextureManager().bindTexture(GuiSlider.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.enabled) {
                this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
            else {
                this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 46, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 46, 4, 20);
            }
        }
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
            this.displayString = I18n.format(this.text, this.getValue());
            return this.dragging = true;
        }
        return false;
    }
    
    public int getValue() {
        return (int)(this.sliderValue * this.max);
    }
    
    public void setValue(final int value) {
        this.sliderValue = value / (float)this.max;
        this.displayString = I18n.format(this.text, this.getValue());
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
}
