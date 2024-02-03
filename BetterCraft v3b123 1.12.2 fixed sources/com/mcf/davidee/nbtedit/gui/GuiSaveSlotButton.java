// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import com.mcf.davidee.nbtedit.nbt.SaveStates;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiSaveSlotButton extends Gui
{
    public static final ResourceLocation TEXTURE;
    private static final int X_SIZE = 14;
    private static final int HEIGHT = 20;
    private static final int MAX_WIDTH = 150;
    private static final int MIN_WIDTH = 82;
    private static final int GAP = 3;
    private final Minecraft mc;
    public final SaveStates.SaveState save;
    private final int rightX;
    private int x;
    private int y;
    private int width;
    private String text;
    private boolean xVisible;
    private int tickCount;
    
    static {
        TEXTURE = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public GuiSaveSlotButton(final SaveStates.SaveState save, final int rightX, final int y) {
        this.save = save;
        this.rightX = rightX;
        this.y = y;
        this.mc = Minecraft.getMinecraft();
        this.xVisible = !save.tag.hasNoTags();
        this.text = String.valueOf(save.tag.hasNoTags() ? "Save " : "Load ") + save.name;
        this.tickCount = -1;
        this.updatePosition();
    }
    
    public void draw(final int mx, final int my) {
        int textColor = this.inBounds(mx, my) ? 16777120 : 16777215;
        this.renderVanillaButton(this.x, this.y, 0, 66, this.width, 20);
        Gui.drawCenteredString(this.mc.fontRendererObj, this.text, this.x + this.width / 2, this.y + 6, textColor);
        if (this.tickCount != -1 && this.tickCount / 6 % 2 == 0) {
            this.mc.fontRendererObj.drawStringWithShadow("_", (float)(this.x + (this.width + this.mc.fontRendererObj.getStringWidth(this.text)) / 2 + 1), (float)(this.y + 6), 16777215);
        }
        if (this.xVisible) {
            textColor = (this.inBoundsOfX(mx, my) ? 16777120 : 16777215);
            this.renderVanillaButton(this.leftBoundOfX(), this.topBoundOfX(), 0, 66, 14, 14);
            Gui.drawCenteredString(this.mc.fontRendererObj, "x", this.x - 3 - 7, this.y + 6, textColor);
        }
    }
    
    private void renderVanillaButton(final int x, final int y, final int u, final int v, final int width, final int height) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(GuiSaveSlotButton.TEXTURE);
        this.drawTexturedModalRect(x, y, u, v, width / 2, height / 2);
        this.drawTexturedModalRect(x + width / 2, y, u + 200 - width / 2, v, width / 2, height / 2);
        this.drawTexturedModalRect(x, y + height / 2, u, v + 20 - height / 2, width / 2, height / 2);
        this.drawTexturedModalRect(x + width / 2, y + height / 2, u + 200 - width / 2, v + 20 - height / 2, width / 2, height / 2);
    }
    
    private int leftBoundOfX() {
        return this.x - 14 - 3;
    }
    
    private int topBoundOfX() {
        return this.y + 3;
    }
    
    public boolean inBoundsOfX(final int mx, final int my) {
        final int buttonX = this.leftBoundOfX();
        final int buttonY = this.topBoundOfX();
        return this.xVisible && mx >= buttonX && my >= buttonY && mx < buttonX + 14 && my < buttonY + 14;
    }
    
    public boolean inBounds(final int mx, final int my) {
        return mx >= this.x && my >= this.y && mx < this.x + this.width && my < this.y + 20;
    }
    
    private void updatePosition() {
        this.width = this.mc.fontRendererObj.getStringWidth(this.text) + 24;
        if (this.width % 2 == 1) {
            ++this.width;
        }
        this.width = MathHelper.clamp_int(this.width, 82, 150);
        this.x = this.rightX - this.width;
    }
    
    public void reset() {
        this.xVisible = false;
        this.save.tag = new NBTTagCompound();
        this.text = "Save " + this.save.name;
        this.updatePosition();
    }
    
    public void saved() {
        this.xVisible = true;
        this.text = "Load " + this.save.name;
        this.updatePosition();
    }
    
    public void keyTyped(final char c, final int key) {
        if (key == 14) {
            this.backSpace();
        }
        if (Character.isDigit(c) || Character.isLetter(c)) {
            final SaveStates.SaveState save = this.save;
            save.name = String.valueOf(save.name) + c;
            this.text = String.valueOf(this.save.tag.hasNoTags() ? "Save " : "Load ") + this.save.name;
            this.updatePosition();
        }
    }
    
    public void backSpace() {
        if (this.save.name.length() > 0) {
            this.save.name = this.save.name.substring(0, this.save.name.length() - 1);
            this.text = String.valueOf(this.save.tag.hasNoTags() ? "Save " : "Load ") + this.save.name;
            this.updatePosition();
        }
    }
    
    public void startEditing() {
        this.tickCount = 0;
    }
    
    public void stopEditing() {
        this.tickCount = -1;
    }
    
    public void update() {
        ++this.tickCount;
    }
}
