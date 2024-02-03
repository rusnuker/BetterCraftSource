// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import java.util.Iterator;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.main.DefinedSettings;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinProfileSettings extends WindowElement<GuiFriendsLayout>
{
    private Scrollbar scrollbar;
    
    public WinProfileSettings(final GuiFriendsLayout chatLayout) {
        super(chatLayout);
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        (this.scrollbar = new Scrollbar(0)).init();
        this.scrollbar.setSpeed(15);
        this.scrollbar.setPosition(this.right - 4, this.top + 2, this.right, this.bottom - 2);
        this.updateGuiElements();
    }
    
    private void updateGuiElements() {
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        try {
            final List<SettingsElement> allElements = DefinedSettings.getChatSetingsCategory().getSettings().getElements();
            double totalEntryHeight = 0.0;
            for (int zLevel = 0; zLevel < 2; ++zLevel) {
                double posY = this.top + 3 + this.scrollbar.getScrollY();
                final int maxX = this.scrollbar.isHidden() ? (this.right - 1) : (this.right - 6);
                totalEntryHeight = 0.0;
                for (final SettingsElement element : allElements) {
                    if (((!(element instanceof DropDownElement) || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 0) || ((element instanceof DropDownElement || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 1)) {
                        if (element instanceof DropDownElement) {
                            ((DropDownElement)element).getDropDownMenu().setMaxY(this.bottom);
                        }
                        element.draw(this.left + 1, (int)posY, maxX, (int)(posY + element.getEntryHeight()), mouseX, mouseY);
                    }
                    posY += element.getEntryHeight() + 1;
                    totalEntryHeight += element.getEntryHeight() + 1;
                    if (zLevel == 1 && element.isMouseOver() && mouseY > this.top && mouseY < this.bottom) {
                        element.drawDescription(mouseX, mouseY, LabyMod.getInstance().getDrawUtils().getWidth());
                    }
                }
            }
            this.scrollbar.setEntryHeight(totalEntryHeight / allElements.size());
            this.scrollbar.update(allElements.size());
            this.scrollbar.draw();
        }
        catch (final Exception ex) {}
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
    }
    
    public void handleMouseInput() {
        if (this.isMouseOver()) {
            this.scrollbar.mouseInput();
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final List<SettingsElement> allElements = DefinedSettings.getChatSetingsCategory().getSettings().getElements();
        for (final SettingsElement element : allElements) {
            if (element instanceof DropDownElement && ((DropDownElement)element).onClickDropDown(mouseX, mouseY, mouseButton)) {
                return false;
            }
        }
        for (final SettingsElement element : allElements) {
            element.unfocus(mouseX, mouseY, mouseButton);
            if (element.isMouseOver()) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        return false;
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        for (final SettingsElement settingElement : DefinedSettings.getChatSetingsCategory().getSettings().getElements()) {
            settingElement.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void mouseInput() {
    }
    
    public boolean isScrolledToTop() {
        return this.scrollbar.getScrollY() == 0.0;
    }
    
    @Override
    public void updateScreen() {
    }
}
