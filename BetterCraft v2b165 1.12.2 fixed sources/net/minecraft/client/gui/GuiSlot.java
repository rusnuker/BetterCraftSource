// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.awt.Color;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.BufferBuilder;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;

public abstract class GuiSlot
{
    protected final Minecraft mc;
    protected int width;
    protected int height;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected final int slotHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean centerListVertically;
    protected int initialClickY;
    protected float scrollMultiplier;
    protected float amountScrolled;
    protected int selectedElement;
    protected long lastClicked;
    protected boolean visible;
    protected boolean showSelectionBox;
    protected boolean hasListHeader;
    protected int headerPadding;
    private boolean enabled;
    protected boolean drawScorllbar;
    public boolean renderClicked;
    
    public GuiSlot(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
        this.centerListVertically = true;
        this.initialClickY = -2;
        this.selectedElement = -1;
        this.visible = true;
        this.showSelectionBox = true;
        this.enabled = true;
        this.drawScorllbar = true;
        this.renderClicked = true;
        this.mc = mcIn;
        this.width = width;
        this.height = height;
        this.top = topIn;
        this.bottom = bottomIn;
        this.slotHeight = slotHeightIn;
        this.left = 0;
        this.right = width;
    }
    
    public void setDimensions(final int widthIn, final int heightIn, final int topIn, final int bottomIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.top = topIn;
        this.bottom = bottomIn;
        this.left = 0;
        this.right = widthIn;
    }
    
    public void func_193651_b(final boolean p_193651_1_) {
        this.showSelectionBox = p_193651_1_;
    }
    
    protected void setHasListHeader(final boolean hasListHeaderIn, final int headerPaddingIn) {
        this.hasListHeader = hasListHeaderIn;
        this.headerPadding = headerPaddingIn;
        if (!hasListHeaderIn) {
            this.headerPadding = 0;
        }
    }
    
    protected abstract int getSize();
    
    protected abstract void elementClicked(final int p0, final boolean p1, final int p2, final int p3);
    
    protected abstract boolean isSelected(final int p0);
    
    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }
    
    protected abstract void drawBackground();
    
    protected void func_192639_a(final int p_192639_1_, final int p_192639_2_, final int p_192639_3_, final float p_192639_4_) {
    }
    
    protected abstract void func_192637_a(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final float p6);
    
    protected void drawListHeader(final int insideLeft, final int insideTop, final Tessellator tessellatorIn) {
    }
    
    protected void clickedHeader(final int p_148132_1_, final int p_148132_2_) {
    }
    
    protected void renderDecorations(final int mouseXIn, final int mouseYIn) {
    }
    
    public int getSlotIndexFromScreenCoords(final int posX, final int posY) {
        final int i = this.left + this.width / 2 - this.getListWidth() / 2;
        final int j = this.left + this.width / 2 + this.getListWidth() / 2;
        final int k = posY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
        final int l = k / this.slotHeight;
        return (posX < this.getScrollBarX() && posX >= i && posX <= j && l >= 0 && k >= 0 && l < this.getSize()) ? l : -1;
    }
    
    public void registerScrollButtons(final int scrollUpButtonIDIn, final int scrollDownButtonIDIn) {
        this.scrollUpButtonID = scrollUpButtonIDIn;
        this.scrollDownButtonID = scrollDownButtonIDIn;
    }
    
    protected void bindAmountScrolled() {
        this.amountScrolled = MathHelper.clamp(this.amountScrolled, 0.0f, (float)this.getMaxScroll());
    }
    
    public int getMaxScroll() {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }
    
    public int getAmountScrolled() {
        return (int)this.amountScrolled;
    }
    
    public boolean isMouseYWithinSlotBounds(final int p_148141_1_) {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }
    
    public void scrollBy(final int amount) {
        this.amountScrolled += amount;
        this.bindAmountScrolled();
        this.initialClickY = -2;
    }
    
    public void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpButtonID) {
                this.amountScrolled -= this.slotHeight * 2 / 3;
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
            else if (button.id == this.scrollDownButtonID) {
                this.amountScrolled += this.slotHeight * 2 / 3;
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
        }
    }
    
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float partialTicks) {
        if (this.visible) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            final int i = this.getScrollBarX();
            final int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            final int l = this.top + 4 - (int)this.amountScrolled;
            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }
            this.func_192638_a(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            final int i2 = 4;
            if (!(Minecraft.currentScreen instanceof GuiScreenResourcePacks)) {
                Gui.drawRect(0, 0, this.width, this.top, Integer.MIN_VALUE);
                ColorUtils.drawRainbowRectBorder(0, 0, this.width, this.top, 2);
                Gui.drawRect(0, this.bottom, this.width, this.height, Integer.MIN_VALUE);
                ColorUtils.drawRainbowRectBorder(0, this.bottom, this.width, this.height, 2);
            }
            final int j2 = this.getMaxScroll();
            if (j2 > 0) {
                int k2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k2 = MathHelper.clamp(k2, 32, this.bottom - this.top - 8);
                int l2 = (int)this.amountScrolled * (this.bottom - this.top - k2) / j2 + this.top;
                if (l2 < this.top) {
                    l2 = this.top;
                }
            }
            this.renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }
    
    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                final int i = (this.width - this.getListWidth()) / 2;
                final int j = (this.width + this.getListWidth()) / 2;
                final int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                final int l = k / this.slotHeight;
                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
                    this.elementClicked(l, false, this.mouseX, this.mouseY);
                    this.selectedElement = l;
                }
                else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
                    this.clickedHeader(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY != -1) {
                    if (this.initialClickY >= 0) {
                        this.amountScrolled -= (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                        this.initialClickY = this.mouseY;
                    }
                }
                else {
                    boolean flag1 = true;
                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        final int j2 = (this.width - this.getListWidth()) / 2;
                        final int k2 = (this.width + this.getListWidth()) / 2;
                        final int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        final int i2 = l2 / this.slotHeight;
                        if (i2 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i2 >= 0 && l2 >= 0) {
                            final boolean flag2 = i2 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i2, flag2, this.mouseX, this.mouseY);
                            this.selectedElement = i2;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
                            this.clickedHeader(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag1 = false;
                        }
                        final int i3 = this.getScrollBarX();
                        final int j3 = i3 + 6;
                        if (this.mouseX >= i3 && this.mouseX <= j3) {
                            this.scrollMultiplier = -1.0f;
                            int k3 = this.getMaxScroll();
                            if (k3 < 1) {
                                k3 = 1;
                            }
                            int l3 = (int)((this.bottom - this.top) * (this.bottom - this.top) / (float)this.getContentHeight());
                            l3 = MathHelper.clamp(l3, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (this.bottom - this.top - l3) / (float)k3;
                        }
                        else {
                            this.scrollMultiplier = 1.0f;
                        }
                        if (flag1) {
                            this.initialClickY = this.mouseY;
                        }
                        else {
                            this.initialClickY = -2;
                        }
                    }
                    else {
                        this.initialClickY = -2;
                    }
                }
            }
            else {
                this.initialClickY = -1;
            }
            int i4 = Mouse.getEventDWheel();
            if (i4 != 0) {
                if (i4 > 0) {
                    i4 = -1;
                }
                else if (i4 < 0) {
                    i4 = 1;
                }
                this.amountScrolled += i4 * this.slotHeight / 2;
            }
        }
    }
    
    public void setEnabled(final boolean enabledIn) {
        this.enabled = enabledIn;
    }
    
    public boolean getEnabled() {
        return this.enabled;
    }
    
    public int getListWidth() {
        return 220;
    }
    
    protected void func_192638_a(final int p_192638_1_, final int p_192638_2_, final int p_192638_3_, final int p_192638_4_, final float p_192638_5_) {
        final Color rainbow = ColorUtils.rainbowEffect(0L, 1.0f);
        final int i = this.getSize();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        for (int j = 0; j < i; ++j) {
            final int k = p_192638_2_ + j * this.slotHeight + this.headerPadding;
            final int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.func_192639_a(j, p_192638_1_, k, p_192638_5_);
            }
            if (this.showSelectionBox && this.isSelected(j)) {
                final int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                final int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableTexture2D();
                if (this.renderClicked) {
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                    bufferbuilder.pos(i2, k + l + 2, 0.0).tex(0.0, 1.0).color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), 255).endVertex();
                    bufferbuilder.pos(j2, k + l + 2, 0.0).tex(1.0, 1.0).color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), 255).endVertex();
                    bufferbuilder.pos(j2, k - 2, 0.0).tex(1.0, 0.0).color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), 255).endVertex();
                    bufferbuilder.pos(i2, k - 2, 0.0).tex(0.0, 0.0).color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), 255).endVertex();
                    bufferbuilder.pos(i2 + 1, k + l + 1, 0.0).tex(0.0, 1.0).color(79, 32, 79, 20).endVertex();
                    bufferbuilder.pos(j2 - 1, k + l + 1, 0.0).tex(1.0, 1.0).color(79, 32, 79, 20).endVertex();
                    bufferbuilder.pos(j2 - 1, k - 1, 0.0).tex(1.0, 0.0).color(79, 32, 79, 40).endVertex();
                    bufferbuilder.pos(i2 + 1, k - 1, 0.0).tex(0.0, 0.0).color(79, 32, 79, 40).endVertex();
                    tessellator.draw();
                }
                GlStateManager.enableTexture2D();
            }
            if (k >= this.top - this.slotHeight && k <= this.bottom) {
                this.func_192637_a(j, p_192638_1_, k, l, p_192638_3_, p_192638_4_, p_192638_5_);
            }
        }
    }
    
    protected int getScrollBarX() {
        return this.width / 2 + 124;
    }
    
    protected void overlayBackground(final int startY, final int endY, final int startAlpha, final int endAlpha) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 32.0f;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(this.left, endY, 0.0).tex(0.0, endY / 32.0f).color(64, 64, 64, endAlpha).endVertex();
        bufferbuilder.pos(this.left + this.width, endY, 0.0).tex(this.width / 32.0f, endY / 32.0f).color(64, 64, 64, endAlpha).endVertex();
        bufferbuilder.pos(this.left + this.width, startY, 0.0).tex(this.width / 32.0f, startY / 32.0f).color(64, 64, 64, startAlpha).endVertex();
        bufferbuilder.pos(this.left, startY, 0.0).tex(0.0, startY / 32.0f).color(64, 64, 64, startAlpha).endVertex();
        tessellator.draw();
    }
    
    public void setSlotXBoundsFromLeft(final int leftIn) {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }
    
    public int getSlotHeight() {
        return this.slotHeight;
    }
    
    protected void drawContainerBackground(final Tessellator p_drawContainerBackground_1_) {
        final BufferBuilder bufferbuilder = p_drawContainerBackground_1_.getBuffer();
        this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 32.0f;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(this.left, this.bottom, 0.0).tex(this.left / 32.0f, (this.bottom + (int)this.amountScrolled) / 32.0f).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(this.right, this.bottom, 0.0).tex(this.right / 32.0f, (this.bottom + (int)this.amountScrolled) / 32.0f).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(this.right, this.top, 0.0).tex(this.right / 32.0f, (this.top + (int)this.amountScrolled) / 32.0f).color(32, 32, 32, 255).endVertex();
        bufferbuilder.pos(this.left, this.top, 0.0).tex(this.left / 32.0f, (this.top + (int)this.amountScrolled) / 32.0f).color(32, 32, 32, 255).endVertex();
        p_drawContainerBackground_1_.draw();
    }
}
