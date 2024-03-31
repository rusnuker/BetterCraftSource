// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiListButton extends GuiButton
{
    private boolean value;
    private final String localizationStr;
    private final GuiPageButtonList.GuiResponder guiResponder;
    
    public GuiListButton(final GuiPageButtonList.GuiResponder responder, final int p_i45539_2_, final int p_i45539_3_, final int p_i45539_4_, final String p_i45539_5_, final boolean p_i45539_6_) {
        super(p_i45539_2_, p_i45539_3_, p_i45539_4_, 150, 20, "");
        this.localizationStr = p_i45539_5_;
        this.value = p_i45539_6_;
        this.displayString = this.buildDisplayString();
        this.guiResponder = responder;
    }
    
    private String buildDisplayString() {
        return String.valueOf(I18n.format(this.localizationStr, new Object[0])) + ": " + I18n.format(this.value ? "gui.yes" : "gui.no", new Object[0]);
    }
    
    public void setValue(final boolean p_175212_1_) {
        this.value = p_175212_1_;
        this.displayString = this.buildDisplayString();
        this.guiResponder.setEntryValue(this.id, p_175212_1_);
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.value = !this.value;
            this.displayString = this.buildDisplayString();
            this.guiResponder.setEntryValue(this.id, this.value);
            return true;
        }
        return false;
    }
}
