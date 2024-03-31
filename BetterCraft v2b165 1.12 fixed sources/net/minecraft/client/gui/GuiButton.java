// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.client.audio.SoundHandler;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import org.lwjgl.opengl.GL11;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import me.amkgre.bettercraft.client.gui.GuiBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui
{
    protected static final ResourceLocation BUTTON_TEXTURES;
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    public boolean hovered;
    private double borderedRectX;
    private double borderedRectY;
    public boolean enableHoverAnimation;
    protected int fadeY;
    
    static {
        BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public GuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.borderedRectX = 0.0;
        this.borderedRectY = 0.0;
        this.enableHoverAnimation = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
        if (this.yPosition < GuiScreen.height / 2) {
            this.fadeY = -heightIn;
        }
        else {
            this.fadeY = GuiScreen.height;
        }
    }
    
    public GuiButton(final int buttonId, final int x, final float y, final int widthIn, final int heightIn, final String buttonText) {
        this(buttonId, x, (int)y, widthIn, heightIn, buttonText);
    }
    
    public GuiButton(final int buttonId, final float x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this(buttonId, (int)x, y, widthIn, heightIn, buttonText);
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
    
    public void drawBorderedRect(final double x, final double y, final double x1, final double y1, final double size, final int borderC, final int insideC) {
        this.drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
        this.drawRect(x + size, y + size, x1, y, borderC);
        this.drawRect(x, y, x + size, y1, borderC);
        this.drawRect(x1, y1, x1 - size, y + size - 1.0, borderC);
        this.drawRect(x, y1 - size, x1, y1, borderC);
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float p_191745_4_) {
        final FontRenderer fontrenderer = mc.fontRendererObj;
        int fade = 100;
        if (GuiBackground.transbutton) {
            if (this.visible) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final boolean changed = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final boolean forherEnabled = mouseX < this.xPosition || mouseY < this.yPosition || mouseX >= this.xPosition + this.width || mouseY >= this.yPosition + this.height;
                this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                this.mouseDragged(mc, mouseX, mouseY);
                int color = this.enabled ? (changed ? 1084926634 : 279620266) : -1879048192;
                int j = Color.white.getRGB();
                if (!this.enabled) {
                    color = 1610612736;
                    j = Color.gray.getRGB();
                    this.borderedRectX = 0.0;
                    this.borderedRectY = 0.0;
                }
                else if (this.hovered) {
                    j = Color.white.getRGB();
                }
                if (!forherEnabled) {
                    this.drawBorderedRect(this.xPosition + this.width - this.borderedRectX - 1.0, this.yPosition + this.borderedRectY, this.xPosition + 1 + this.borderedRectX, this.yPosition + this.height - this.borderedRectY, 1.0, Color.white.getRGB(), 0);
                    this.drawBorderedRect(this.xPosition + this.width - this.borderedRectX - 1.0, this.yPosition + this.borderedRectY, this.xPosition + 1 + this.borderedRectX, this.yPosition + this.height - this.borderedRectY, 1.0, ColorUtils.rainbowColor(200000000L, 1.0f).getRGB(), color);
                    if (this.borderedRectX != 8.0) {
                        ++this.borderedRectX;
                    }
                    if (this.borderedRectY != 1.0) {
                        ++this.borderedRectY;
                    }
                }
                else {
                    if (this.borderedRectX != 0.0) {
                        --this.borderedRectX;
                    }
                    if (this.borderedRectY != 0.0) {
                        --this.borderedRectY;
                    }
                    this.drawBorderedRect(this.xPosition + this.width - this.borderedRectX - 1.0, this.yPosition + this.borderedRectY, this.xPosition + 1 + this.borderedRectX, this.yPosition + this.height - this.borderedRectY, 1.0, Color.white.getRGB(), 0);
                    this.drawBorderedRect(this.xPosition + this.width - this.borderedRectX - 1.0, this.yPosition + this.borderedRectY, this.xPosition + 1 + this.borderedRectX, this.yPosition + this.height - this.borderedRectY, 1.0, ColorUtils.rainbowColor(200000000L, 1.0f).getRGB(), color);
                }
                if (!this.enableHoverAnimation) {
                    this.borderedRectX = 0.0;
                    this.borderedRectY = 0.0;
                }
                Gui.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        else if (GuiBackground.oldbutton) {
            if (this.visible) {
                if (!(this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height))) {
                    if (fade != 100) {
                        this.fadeY += 5;
                    }
                }
                else {
                    if (fade <= 40) {
                        return;
                    }
                    if (fade != 70) {
                        fade -= 50;
                    }
                }
                final Color a = new Color(255, 120, 255, fade);
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, a.getRGB());
                Gui.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, Color.WHITE.getRGB());
            }
        }
        else if (GuiBackground.mcbutton) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
                final int i = this.getHoverState(this.hovered);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                int k = 14737632;
                if (!this.enabled) {
                    k = 10526880;
                }
                else if (this.hovered) {
                    k = 16777120;
                }
                Gui.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, k);
            }
        }
        else if (this.visible) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean changed = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean forherEnabled = mouseX < this.xPosition || mouseY < this.yPosition || mouseX >= this.xPosition + this.width || mouseY >= this.yPosition + this.height;
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = this.enabled ? (changed ? 1084926634 : new Color(79, 32, 79, 120).getRGB()) : -1879048192;
            int j = Color.white.getRGB();
            if (!this.enabled) {
                color = 1610612736;
                j = Color.gray.getRGB();
                this.borderedRectX = 0.0;
                this.borderedRectY = 0.0;
            }
            else if (this.hovered) {
                j = Color.white.getRGB();
            }
            if (this.yPosition < GuiScreen.height / 2) {
                if (this.fadeY < this.yPosition) {
                    this.fadeY += GuiScreen.width / 100;
                }
                else {
                    this.fadeY = this.yPosition;
                }
            }
            else if (this.fadeY > this.yPosition) {
                this.fadeY -= GuiScreen.width / 100;
            }
            else {
                this.fadeY = this.yPosition;
            }
            if (GuiBackground.animbutton) {
                RenderUtils.drawRoundedRect(this.xPosition - 1, this.fadeY - 1, this.xPosition + this.width + 1, this.fadeY + this.height + 1, 4.0f, ColorUtils.rainbowColor(200000000L, 1.0f));
                RenderUtils.drawRoundedRect(this.xPosition, this.fadeY, this.xPosition + this.width, this.fadeY + this.height, 5.0f, new Color(color));
                if (!this.enableHoverAnimation) {
                    this.borderedRectX = 0.0;
                    this.borderedRectY = 0.0;
                }
                Gui.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.fadeY + (this.height - 8) / 2, j);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else {
                RenderUtils.drawRoundedRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, 4.0f, ColorUtils.rainbowColor(200000000L, 1.0f));
                RenderUtils.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 5.0f, new Color(color));
                if (!this.enableHoverAnimation) {
                    this.borderedRectX = 0.0;
                    this.borderedRectY = 0.0;
                }
                Gui.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
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
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
    
    public int getButtonWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public void setxPosition(final int xPosition) {
        this.xPosition = xPosition;
    }
    
    public void setDisplayString(final String displayString) {
        this.displayString = displayString;
    }
}
