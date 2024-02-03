// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiButton;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.settings.LabyModModuleEditorGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TabbedGuiButton extends CustomGuiButton
{
    private ResourceLocation icon;
    private IconRenderCallback iconRenderCallback;
    private boolean rightBound;
    
    public TabbedGuiButton(final ResourceLocation icon, final int buttonId, final int x, final int y, final String buttonText) {
        super(buttonId, x, y, buttonText);
        this.rightBound = false;
        this.icon = icon;
    }
    
    public TabbedGuiButton(final ResourceLocation icon, final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.rightBound = false;
        this.icon = icon;
    }
    
    public TabbedGuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(null, buttonId, x, y, buttonText);
    }
    
    public TabbedGuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this(null, buttonId, x, y, widthIn, heightIn, buttonText);
    }
    
    private boolean isHovered(int mouseX, int mouseY) {
        if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof LabyModModuleEditorGui) {
            final ScaledResolution scaled = LabyMod.getInstance().getDrawUtils().getScaledResolution();
            final double rescale = scaled.getScaleFactor() / LabyMod.getInstance().getDrawUtils().getCustomScaling();
            mouseX /= (int)rescale;
            mouseY /= (int)rescale;
        }
        final int xPosition = LabyModCore.getMinecraft().getXPosition(this);
        final int yPosition = LabyModCore.getMinecraft().getYPosition(this);
        return mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + this.width && mouseY < yPosition + this.height;
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        this.drawButton(mc, mouseX, mouseY);
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            final int xPosition = LabyModCore.getMinecraft().getXPosition(this);
            final int yPosition = LabyModCore.getMinecraft().getYPosition(this);
            final FontRenderer fontrenderer = LabyModCore.getMinecraft().getFontRenderer();
            mc.getTextureManager().bindTexture(TabbedGuiButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = this.isHovered(mouseX, mouseY);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(xPosition, yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(xPosition + this.width / 2, yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (!this.enabled) {
                j = 10526880;
            }
            else if (this.hovered) {
                j = 16777120;
            }
            if (this.icon == null) {
                Gui.drawCenteredString(fontrenderer, this.displayString, xPosition + this.width / 2, yPosition + (this.height - 8) / 2, j);
            }
            else {
                final int padding = 3;
                final int iconSize = this.height - padding * 2;
                final int stringWidth = LabyMod.getInstance().getDrawUtils().getStringWidth(this.displayString) + iconSize + padding;
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.icon);
                if (this.iconRenderCallback == null) {
                    LabyMod.getInstance().getDrawUtils().drawTexture(xPosition + this.width / 2 - stringWidth / 2, yPosition + padding, 256.0, 256.0, iconSize, iconSize);
                }
                else {
                    this.iconRenderCallback.render(xPosition + this.width / 2 - stringWidth / 2, yPosition + padding, iconSize);
                }
                this.drawString(fontrenderer, this.displayString, xPosition - 1 + this.width / 2 - stringWidth / 2 + iconSize + padding, yPosition + (this.height - 8) / 2, j);
            }
        }
    }
    
    public void setIconRenderCallback(final IconRenderCallback iconRenderCallback) {
        this.iconRenderCallback = iconRenderCallback;
    }
    
    public void setRightBound(final boolean rightBound) {
        this.rightBound = rightBound;
    }
    
    public boolean isRightBound() {
        return this.rightBound;
    }
    
    public interface IconRenderCallback
    {
        void render(final int p0, final int p1, final int p2);
    }
}
