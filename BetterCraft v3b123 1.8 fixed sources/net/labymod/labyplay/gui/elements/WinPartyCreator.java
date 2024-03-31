// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.gui.elements;

import net.labymod.gui.layout.WindowLayout;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.gui.elements.GuiTextboxPrompt;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.labyplay.gui.GuiPlayLayout;
import net.labymod.gui.layout.WindowElement;

public class WinPartyCreator extends WindowElement<GuiPlayLayout>
{
    public WinPartyCreator(final GuiPlayLayout layout) {
        super(layout);
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        final int margin = 0;
        final int padding = 6;
        final String invitePlayer = "Invite player";
        final int invitePlayerWidth = this.draw.getStringWidth("Invite player") + 12;
        buttonlist.add(new GuiButton(1, left + 0, top + 18, invitePlayerWidth, 20, "Invite player"));
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        final int paddingX = 1;
        final int paddingY = 5;
        this.draw.drawString("Create new party", this.left + 1, this.top + 5);
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTextboxPrompt(Minecraft.getMinecraft().currentScreen, "Player to invite:", "Invite", "Cancel", "", new Consumer<String>() {
                @Override
                public void accept(final String username) {
                    if (!username.isEmpty()) {
                        ((GuiPlayLayout)WinPartyCreator.this.layout).getPartySystem().invitePlayer(username);
                    }
                }
            }));
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        return false;
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
