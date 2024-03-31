// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import me.nzxtercode.bettercraft.client.Config;
import me.nzxtercode.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import me.nzxtercode.bettercraft.client.gui.section.GuiUISettings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures;
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    public boolean hovered;
    public int hoverTime;
    public boolean animation;
    public int renderedY;
    
    static {
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        this.renderedY = ((this.yPosition >= GuiScreen.height / 2) ? GuiScreen.height : (-heightIn));
    }
    
    protected int getHoverState(final boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        }
        else if (mouseOver) {
            i = 2;
        }
        return i;
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (GuiUISettings.enabledUI[2]) {
            if (this.yPosition >= GuiScreen.height / 2) {
                if (this.renderedY > this.yPosition) {
                    this.renderedY -= 7;
                }
                else {
                    this.renderedY = this.yPosition;
                }
            }
            else if (this.renderedY < this.yPosition) {
                this.renderedY += 7;
            }
            else {
                this.renderedY = this.yPosition;
            }
            if (!GuiUISettings.enabledAnimations[0]) {
                this.renderedY = this.yPosition;
            }
            if (!this.visible) {
                return;
            }
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = this.getHoverState(this.hovered);
            final int color = ColorUtils.rainbowEffect();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            int r = this.enabled ? 1621797546 : -1867863382;
            if (!this.enabled) {
                r += 1867863382;
            }
            else if (this.hovered) {
                r -= Integer.MIN_VALUE;
            }
            Gui.drawRect(this.xPosition + this.hoverTime, this.renderedY, this.xPosition + this.width - this.hoverTime, this.renderedY + this.height, r);
            Gui.drawRect(this.xPosition + this.hoverTime, this.renderedY, this.xPosition + this.width - this.hoverTime, this.renderedY + 1, color);
            Gui.drawRect(this.xPosition + this.hoverTime, this.renderedY + this.height, this.xPosition + this.width - this.hoverTime, this.renderedY + this.height - 1, color);
            this.mouseDragged(mc, mouseX, mouseY);
            if (this.hovered) {
                Gui.drawRect(this.xPosition + this.hoverTime, this.renderedY, this.xPosition + this.hoverTime + 1, this.renderedY + this.height, color);
                Gui.drawRect(this.xPosition + this.width - this.hoverTime, this.renderedY, this.xPosition + this.width - this.hoverTime - 1, this.renderedY + this.height, color);
                ++this.hoverTime;
            }
            if (!this.hovered) {
                --this.hoverTime;
            }
            if (this.hoverTime * 30 > 255) {
                this.hoverTime = 8;
            }
            if (this.hoverTime < 0) {
                this.hoverTime = 0;
            }
            final double scale = Math.min(1.0, 1.0 - this.hoverTime / 3 / 8.0);
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            Gui.drawCenteredString(fontrenderer, this.displayString, (int)((this.xPosition + this.width / 2) / scale), (int)((int)((this.renderedY + (this.height - 8) / 2) / scale) + (mc.fontRendererObj.FONT_HEIGHT - mc.fontRendererObj.FONT_HEIGHT * scale)), Config.getInstance().getColor("Buttons").get("string").getAsInt());
            GlStateManager.popMatrix();
        }
        else if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;
            if (!this.enabled) {
                j = 10526880;
            }
            else if (this.hovered) {
                j = 16777120;
            }
            Gui.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }
    
    public void setDisplayString(final String displayString) {
        this.displayString = displayString;
    }
    
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY) {
    }
    
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
    
    public boolean isMouseOver() {
        return this.hovered;
    }
    
    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }
    
    public int getButtonWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
}
