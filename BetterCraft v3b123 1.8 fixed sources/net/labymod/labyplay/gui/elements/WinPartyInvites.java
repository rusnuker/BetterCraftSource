// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.gui.elements;

import java.util.Iterator;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.labyplay.party.model.PartyInvite;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import java.util.UUID;
import net.labymod.labyplay.gui.GuiPlayLayout;
import net.labymod.gui.layout.WindowElement;

public class WinPartyInvites extends WindowElement<GuiPlayLayout>
{
    private UUID hoverParty;
    private boolean hoverAccept;
    private boolean hoverDeny;
    
    public WinPartyInvites(final GuiPlayLayout layout) {
        super(layout);
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        final int padding = 5;
        final int posX = this.left + 5;
        int posY = this.top + 5 + 15;
        this.draw.drawString("Party invites", this.left + 5, this.top + 5);
        this.hoverParty = null;
        this.hoverAccept = false;
        this.hoverDeny = false;
        for (final PartyInvite party : ((GuiPlayLayout)this.layout).getPartySystem().getPartyInvites()) {
            LabyMod.getInstance().getDrawUtils().drawPlayerHead(party.getUsername(), posX, posY, 18);
            this.draw.drawString(String.valueOf(ModColor.cl('a')) + party.getUsername(), posX + 18 + 2, posY + 1);
            this.draw.drawString(String.valueOf(ModColor.cl('e')) + "invites you", posX + 18 + 2, posY + 10, 0.7);
            final int iconSize = 13;
            final boolean hoverDeny = mouseX > this.right - 20 && mouseX < this.right - 20 + 13 && mouseY > posY + 1 && mouseY < posY + 1 + 13;
            final boolean hoverAccept = mouseX > this.right - 40 && mouseX < this.right - 40 + 13 && mouseY > posY + 1 && mouseY < posY + 1 + 13;
            this.hoverDeny = hoverDeny;
            this.hoverAccept = hoverAccept;
            this.hoverParty = party.getPartyUUID();
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_DENY);
            this.draw.drawTexture(this.right - 20 - (hoverDeny ? 1 : 0), posY + 1 - (hoverDeny ? 1 : 0), 255.0, 255.0, 13 + (hoverDeny ? 2 : 0), 13 + (hoverDeny ? 2 : 0), 1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_ACCEPT);
            this.draw.drawTexture(this.right - 40 - (hoverAccept ? 1 : 0), posY + 1 - (hoverAccept ? 1 : 0), 255.0, 255.0, 13 + (hoverAccept ? 2 : 0), 13 + (hoverAccept ? 2 : 0), 1.0f);
            posY += 20;
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.hoverAccept && this.hoverParty != null) {
            ((GuiPlayLayout)this.layout).getPartySystem().sendInvitePlayerResponse(this.hoverParty, true);
        }
        if (this.hoverDeny && this.hoverParty != null) {
            ((GuiPlayLayout)this.layout).getPartySystem().sendInvitePlayerResponse(this.hoverParty, false);
        }
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
    public void actionPerformed(final GuiButton button) {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void updateScreen() {
    }
}
