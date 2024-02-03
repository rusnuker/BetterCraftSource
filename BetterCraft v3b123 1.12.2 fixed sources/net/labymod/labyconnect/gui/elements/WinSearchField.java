// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.gui.elements.SmallDropDownMenu;
import net.labymod.gui.elements.ModTextField;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinSearchField extends WindowElement<GuiFriendsLayout>
{
    private ModTextField fieldSearch;
    private SmallDropDownMenu buttonSortOptions;
    
    public WinSearchField(final GuiFriendsLayout chatLayout) {
        super(chatLayout);
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        final int paddingHeight = 5;
        final int paddingWidth = 0;
        final int dragLineWidth = 2;
        final int spaceBetween = 5;
        final int buttonWidth = 30;
        (this.fieldSearch = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), left + 30 + 0 + 5, top + 5, right - left - 30 - 0 - 2 - 5, bottom - top - 10)).setBlackBox(false);
        (this.buttonSortOptions = new SmallDropDownMenu(left + 0, top + (bottom - top - 20) / 2, 30, 20)).addDropDownEntry(LanguageManager.translate("chat_sort_all"));
        this.buttonSortOptions.addDropDownEntry(LanguageManager.translate("online"));
        this.buttonSortOptions.addDropDownEntry(LanguageManager.translate("chat_sort_latest"));
        this.buttonSortOptions.setSelectedOptionIndex(LabyMod.getSettings().friendSortType);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        this.fieldSearch.drawTextBox();
        this.buttonSortOptions.renderButton(mouseX, mouseY);
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.fieldSearch.mouseClicked(mouseX, mouseY, mouseButton);
        final int entryCode = this.buttonSortOptions.onClick(mouseX, mouseY);
        if (entryCode >= 0) {
            LabyMod.getSettings().friendSortType = entryCode;
            LabyMod.getInstance().getLabyConnect().sortFriendList(entryCode);
            return true;
        }
        return false;
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        this.fieldSearch.textboxKeyTyped(typedChar, keyCode);
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void mouseInput() {
    }
    
    @Override
    public void updateScreen() {
        this.fieldSearch.updateCursorCounter();
    }
    
    public ModTextField getFieldSearch() {
        return this.fieldSearch;
    }
    
    public SmallDropDownMenu getButtonSortOptions() {
        return this.buttonSortOptions;
    }
}
