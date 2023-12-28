// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;

public class GuiClickableScrolledSelectionListProxy extends GuiSlot
{
    private final RealmsClickableScrolledSelectionList proxy;
    
    public GuiClickableScrolledSelectionListProxy(final RealmsClickableScrolledSelectionList selectionList, final int p_i45526_2_, final int p_i45526_3_, final int p_i45526_4_, final int p_i45526_5_, final int p_i45526_6_) {
        super(Minecraft.getMinecraft(), p_i45526_2_, p_i45526_3_, p_i45526_4_, p_i45526_5_, p_i45526_6_);
        this.proxy = selectionList;
    }
    
    @Override
    protected int getSize() {
        return this.proxy.getItemCount();
    }
    
    @Override
    protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        this.proxy.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return this.proxy.isSelectedItem(slotIndex);
    }
    
    @Override
    protected void drawBackground() {
        this.proxy.renderBackground();
    }
    
    @Override
    protected void func_192637_a(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
        this.proxy.renderItem(p_192637_1_, p_192637_2_, p_192637_3_, p_192637_4_, p_192637_5_, p_192637_6_);
    }
    
    public int width() {
        return this.width;
    }
    
    public int mouseY() {
        return this.mouseY;
    }
    
    public int mouseX() {
        return this.mouseX;
    }
    
    @Override
    protected int getContentHeight() {
        return this.proxy.getMaxPosition();
    }
    
    @Override
    protected int getScrollBarX() {
        return this.proxy.getScrollbarPosition();
    }
    
    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (this.scrollMultiplier > 0.0f && Mouse.getEventButtonState()) {
            this.proxy.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
        }
    }
    
    public void renderSelected(final int p_178043_1_, final int p_178043_2_, final int p_178043_3_, final Tezzelator p_178043_4_) {
        this.proxy.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
    }
    
    @Override
    protected void func_192638_a(final int p_192638_1_, final int p_192638_2_, final int p_192638_3_, final int p_192638_4_, final float p_192638_5_) {
        for (int i = this.getSize(), j = 0; j < i; ++j) {
            final int k = p_192638_2_ + j * this.slotHeight + this.headerPadding;
            final int l = this.slotHeight - 4;
            if (k > this.bottom || k + l < this.top) {
                this.func_192639_a(j, p_192638_1_, k, p_192638_5_);
            }
            if (this.showSelectionBox && this.isSelected(j)) {
                this.renderSelected(this.width, k, l, Tezzelator.instance);
            }
            this.func_192637_a(j, p_192638_1_, k, l, p_192638_3_, p_192638_4_, p_192638_5_);
        }
    }
}
