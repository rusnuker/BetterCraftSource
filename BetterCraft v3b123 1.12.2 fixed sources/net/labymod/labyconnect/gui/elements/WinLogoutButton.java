// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinLogoutButton extends WindowElement<GuiFriendsLayout>
{
    public WinLogoutButton(final GuiFriendsLayout chatLayout) {
        super(chatLayout);
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        final int paddingWidth = 0;
        final int dragLineWidth = 2;
        buttonlist.add(new GuiButton(4, left + 0, top + (bottom - top - 20) / 2 + 1, right - left - 0 - 2, 20, LanguageManager.translate("button_logout")));
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        return false;
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
        if (button.id == 4) {
            LabyMod.getInstance().getLabyConnect().setForcedLogout(true);
            LabyMod.getInstance().getLabyConnect().getClientConnection().disconnect(false);
            ((GuiFriendsLayout)this.layout).initGui();
        }
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
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void updateScreen() {
    }
}
