// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.awt.Color;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionSlider extends GuiButton
{
    private float sliderValue;
    public boolean dragging;
    private final GameSettings.Options options;
    private final float minValue;
    private final float maxValue;
    
    public GuiOptionSlider(final int buttonId, final int x, final int y, final GameSettings.Options optionIn) {
        this(buttonId, x, y, optionIn, 0.0f, 1.0f);
    }
    
    public GuiOptionSlider(final int buttonId, final int x, final int y, final GameSettings.Options optionIn, final float minValueIn, final float maxValue) {
        super(buttonId, x, y, 150, 20, "");
        this.sliderValue = 1.0f;
        this.options = optionIn;
        this.minValue = minValueIn;
        this.maxValue = maxValue;
        final Minecraft minecraft = Minecraft.getMinecraft();
        this.sliderValue = optionIn.normalizeValue(minecraft.gameSettings.getOptionFloatValue(optionIn));
        this.displayString = minecraft.gameSettings.getKeyBinding(optionIn);
        this.enableHoverAnimation = false;
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
                final float f = this.options.denormalizeValue(this.sliderValue);
                mc.gameSettings.setOptionFloatValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = mc.gameSettings.getKeyBinding(this.options);
            }
            mc.getTextureManager().bindTexture(GuiOptionSlider.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float p_191745_4_) {
        super.drawButton(mc, mouseX, mouseY, p_191745_4_);
        RenderUtils.drawRoundedRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 1 - 1, this.fadeY + 1 - 1, this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 7 + 1, this.fadeY + this.height - 1 + 1, 1.0f, ColorUtils.rainbowColor(200000000L, 1.0f));
        final int color = new Color(79, 32, 79, 120).getRGB();
        RenderUtils.drawRoundedRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 1, this.fadeY + 1, this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 7, this.fadeY + this.height - 1, 2.0f, new Color(color));
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
            mc.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = mc.gameSettings.getKeyBinding(this.options);
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
}
