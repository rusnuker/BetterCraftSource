// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import org.lwjgl.input.Mouse;
import net.labymod.core.WorldRendererAdapter;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.Tessellator;
import net.labymod.main.LabyMod;

public class Scrollbar
{
    private int listSize;
    private double entryHeight;
    private double scrollY;
    private double barLength;
    private double backLength;
    private int posTop;
    private int posBottom;
    private double left;
    private double top;
    private double right;
    private int speed;
    private double clickY;
    private boolean hold;
    private boolean requestBottom;
    private int spaceBelow;
    
    public void reset() {
        this.scrollY = 0.0;
    }
    
    public void init() {
        this.mouseInput();
    }
    
    public Scrollbar(final int entryHeight) {
        this.speed = 10;
        this.spaceBelow = 0;
        this.entryHeight = entryHeight;
        this.setDefaultPosition();
    }
    
    public void update(final int listSize) {
        if (this.listSize == listSize) {
            return;
        }
        this.listSize = listSize;
        if (this.requestBottom) {
            this.scrollY = -2.147483648E9;
            this.requestBottom = false;
            this.checkOutOfBorders();
        }
    }
    
    public void setPosition(final int left, final int top, final int right, final int bottom) {
        this.left = left;
        this.posTop = top;
        this.right = right;
        this.posBottom = bottom;
        this.calc();
    }
    
    public void calc() {
        final double totalPixels = this.listSize * this.entryHeight + this.spaceBelow;
        final double backLength;
        final double pixelInView = backLength = this.posBottom - this.posTop;
        if (pixelInView >= totalPixels) {
            return;
        }
        final double dPixelInView = pixelInView;
        final double dTotalPixels = totalPixels;
        final double scale = dPixelInView / dTotalPixels;
        final double barLength = scale * backLength;
        final double scroll = this.scrollY / scale * scale * scale;
        this.top = -scroll + this.posTop;
        this.barLength = barLength;
        this.backLength = backLength;
    }
    
    public void setDefaultPosition() {
        this.setPosition(LabyMod.getInstance().getDrawUtils().getWidth() / 2 + 150, 40, LabyMod.getInstance().getDrawUtils().getWidth() / 2 + 156, LabyMod.getInstance().getDrawUtils().getHeight() - 40);
    }
    
    public boolean isHidden() {
        return this.listSize == 0 || this.posBottom - this.posTop >= this.listSize * this.entryHeight + this.spaceBelow;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        this.mouseAction(mouseX, mouseY, EnumMouseAction.DRAGGING);
        this.draw();
    }
    
    public void draw() {
        this.checkOutOfBorders();
        if (this.isHidden()) {
            return;
        }
        this.calc();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter worldrenderer = LabyModCore.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(this.left, this.posBottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(this.right, this.posBottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(this.right, this.posTop, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
        worldrenderer.pos(this.left, this.posTop, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(this.left, this.top + this.barLength, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
        worldrenderer.pos(this.right, this.top + this.barLength, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
        worldrenderer.pos(this.right, this.top, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
        worldrenderer.pos(this.left, this.top, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
        tessellator.draw();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(this.left, this.top + this.barLength - 1.0, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
        worldrenderer.pos(this.right - 1.0, this.top + this.barLength - 1.0, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
        worldrenderer.pos(this.right - 1.0, this.top, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
        worldrenderer.pos(this.left, this.top, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
    
    public boolean isHoverSlider(final int mouseX, final int mouseY) {
        return mouseX < this.right && mouseX > this.left && mouseY > this.top && mouseY < this.top + this.barLength;
    }
    
    public boolean isHoverTotalScrollbar(final int mouseX, final int mouseY) {
        return mouseX < this.right && mouseX > this.left && mouseY > this.posTop && mouseY < this.posBottom;
    }
    
    public void mouseAction(final int mouseX, final int mouseY, final EnumMouseAction mouseAction) {
        this.calc();
        final double scale = this.backLength / (this.listSize * this.entryHeight + this.spaceBelow);
        final double value = (int)(-mouseY / scale);
        switch (mouseAction) {
            case CLICKED: {
                if (this.hold) {
                    this.hold = false;
                    break;
                }
                if (this.isHoverSlider(mouseX, mouseY)) {
                    this.hold = true;
                    this.clickY = value - this.scrollY;
                    break;
                }
                break;
            }
            case DRAGGING: {
                if (this.hold) {
                    this.scrollY = value - this.clickY;
                    break;
                }
                break;
            }
            case RELEASED: {
                this.hold = false;
                break;
            }
        }
        this.checkOutOfBorders();
    }
    
    public void mouseInput() {
        final int wheel = Mouse.getEventDWheel();
        if (wheel > 0) {
            this.scrollY += this.speed;
        }
        else if (wheel < 0) {
            this.scrollY -= this.speed;
        }
        if (wheel != 0) {
            this.checkOutOfBorders();
        }
    }
    
    public void checkOutOfBorders() {
        if (this.listSize * this.entryHeight + this.spaceBelow + this.scrollY < this.posBottom - this.posTop) {
            this.scrollY += this.posBottom - this.posTop - (this.listSize * this.entryHeight + this.spaceBelow + this.scrollY);
        }
        if (this.scrollY > 0.0) {
            this.scrollY = 0.0;
        }
    }
    
    public void setPosition(final double left, final double top, final double right, final double bottom) {
        this.setPosition((int)left, (int)top, (int)right, (int)bottom);
    }
    
    public void requestBottom() {
        this.requestBottom = true;
    }
    
    public void scrollTo(final int index) {
        this.scrollY += this.posBottom - this.posTop - (index * this.entryHeight + this.spaceBelow + this.scrollY) - (this.entryHeight + this.spaceBelow);
        this.checkOutOfBorders();
    }
    
    public int getListSize() {
        return this.listSize;
    }
    
    public double getEntryHeight() {
        return this.entryHeight;
    }
    
    public double getScrollY() {
        return this.scrollY;
    }
    
    public double getBarLength() {
        return this.barLength;
    }
    
    public double getBackLength() {
        return this.backLength;
    }
    
    public int getPosTop() {
        return this.posTop;
    }
    
    public int getPosBottom() {
        return this.posBottom;
    }
    
    public double getLeft() {
        return this.left;
    }
    
    public double getTop() {
        return this.top;
    }
    
    public double getRight() {
        return this.right;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public double getClickY() {
        return this.clickY;
    }
    
    public boolean isHold() {
        return this.hold;
    }
    
    public boolean isRequestBottom() {
        return this.requestBottom;
    }
    
    public int getSpaceBelow() {
        return this.spaceBelow;
    }
    
    public void setListSize(final int listSize) {
        this.listSize = listSize;
    }
    
    public void setEntryHeight(final double entryHeight) {
        this.entryHeight = entryHeight;
    }
    
    public void setScrollY(final double scrollY) {
        this.scrollY = scrollY;
    }
    
    public void setBarLength(final double barLength) {
        this.barLength = barLength;
    }
    
    public void setBackLength(final double backLength) {
        this.backLength = backLength;
    }
    
    public void setPosTop(final int posTop) {
        this.posTop = posTop;
    }
    
    public void setPosBottom(final int posBottom) {
        this.posBottom = posBottom;
    }
    
    public void setLeft(final double left) {
        this.left = left;
    }
    
    public void setTop(final double top) {
        this.top = top;
    }
    
    public void setRight(final double right) {
        this.right = right;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public void setClickY(final double clickY) {
        this.clickY = clickY;
    }
    
    public void setHold(final boolean hold) {
        this.hold = hold;
    }
    
    public void setRequestBottom(final boolean requestBottom) {
        this.requestBottom = requestBottom;
    }
    
    public void setSpaceBelow(final int spaceBelow) {
        this.spaceBelow = spaceBelow;
    }
    
    public enum EnumMouseAction
    {
        CLICKED("CLICKED", 0), 
        RELEASED("RELEASED", 1), 
        DRAGGING("DRAGGING", 2);
        
        private EnumMouseAction(final String s, final int n) {
        }
    }
}
