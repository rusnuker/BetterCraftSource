// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.gui.elements;

import net.labymod.gui.layout.WindowLayout;
import java.util.UUID;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.gui.elements.GuiTextboxPrompt;
import net.labymod.utils.Consumer;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.Gui;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.gui.elements.SmallDropDownMenu;
import net.labymod.labyplay.party.model.PartyMember;
import net.labymod.labyplay.gui.GuiPlayLayout;
import net.labymod.gui.layout.WindowElement;

public class WinCurrentParty extends WindowElement<GuiPlayLayout>
{
    private PartyMember hoverMember;
    private PartyMember dropDownMember;
    private SmallDropDownMenu dropDown;
    
    public WinCurrentParty(final GuiPlayLayout layout) {
        super(layout);
        this.hoverMember = null;
        this.dropDownMember = null;
        this.dropDown = null;
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        final int padding = 6;
        final int space = 2;
        final String invitePlayer = "Invite player";
        final int invitePlayerWidth = this.draw.getStringWidth("Invite player") + 6;
        buttonlist.add(new GuiButton(1, left, bottom - 20, invitePlayerWidth, 20, "Invite player"));
        final String leave = "Leave";
        final int leaveWidth = this.draw.getStringWidth("Leave") + 6;
        buttonlist.add(new GuiButton(2, left + invitePlayerWidth + 2, bottom - 20, leaveWidth, 20, "Leave"));
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        final int paddingX = 2;
        final int paddingY = 4;
        int posX = this.left + 2;
        final int posY = this.top + 4 + 14;
        final int headSize = 18;
        final int space = 3;
        this.draw.drawString("Party", this.left + 2, this.top + 4);
        this.hoverMember = null;
        PartyMember[] members;
        for (int length = (members = ((GuiPlayLayout)this.layout).getPartySystem().getMembers()).length, i = 0; i < length; ++i) {
            final PartyMember member = members[i];
            if (this.drawMember(member, posX, posY, mouseX, mouseY, 18)) {
                posX += 21;
            }
        }
        if (this.dropDown != null) {
            this.dropDown.renderButton(mouseX, mouseY);
        }
    }
    
    private boolean drawMember(final PartyMember member, final int posX, final int posY, final int mouseX, final int mouseY, final int headSize) {
        final boolean hover = mouseX > posX && mouseX < posX + headSize && mouseY > posY && mouseY < posY + headSize;
        LabyMod.getInstance().getDrawUtils().drawPlayerHead(member.getUuid(), posX, posY, headSize);
        if (!member.isMember()) {
            final long inviteAliveDuration = System.currentTimeMillis() - member.getTimestamp();
            if (inviteAliveDuration > 60000L) {
                return false;
            }
            final double progress = headSize / 60000.0 * (60000L - inviteAliveDuration);
            Gui.drawRect(posX, posY, posX + headSize, posY + headSize, ModColor.toRGB(0, 0, 0, 200));
            Gui.drawRect(posX, posY + headSize - 1, posX + headSize, posY + headSize, ModColor.toRGB(0, 0, 0, 200));
            DrawUtils.drawRect(posX, posY + headSize - 1, posX + progress, posY + headSize, ModColor.toRGB(100, 200, 100, 200));
        }
        if (member.isOwner()) {
            final int crownSize = 10;
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_CROWN);
            this.draw.drawTexture(posX, posY - 5 - 1, 255.0, 255.0, 10.0, 10.0);
        }
        if (hover && this.dropDown == null) {
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, member.getName());
            this.hoverMember = member;
        }
        return true;
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTextboxPrompt(Minecraft.getMinecraft().currentScreen, "Player to invite:", "Invite", "Cancel", "", new Consumer<String>() {
                @Override
                public void accept(final String username) {
                    if (!username.isEmpty()) {
                        ((GuiPlayLayout)WinCurrentParty.this.layout).getPartySystem().invitePlayer(username);
                    }
                }
            }));
        }
        if (button.id == 2) {
            ((GuiPlayLayout)this.layout).getPartySystem().leaveParty();
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.dropDown != null && this.dropDownMember != null) {
            final int result = this.dropDown.onClick(mouseX, mouseY);
            if (((GuiPlayLayout)this.layout).getPartySystem().hasParty()) {
                final UUID targetUUID = this.dropDownMember.getUuid();
                switch (result) {
                    case 0: {
                        ((GuiPlayLayout)this.layout).getPartySystem().kickPlayer(targetUUID);
                        break;
                    }
                    case 1: {
                        ((GuiPlayLayout)this.layout).getPartySystem().changeOwner(targetUUID);
                        break;
                    }
                }
            }
            this.dropDown = null;
            this.dropDownMember = null;
        }
        if (this.hoverMember != null && this.dropDown == null && mouseButton == 1) {
            final PartyMember clientMember = ((GuiPlayLayout)this.layout).getPartySystem().getClientMember();
            final boolean isPartyOwner = clientMember.isOwner();
            final boolean isClientMember = this.hoverMember.getUuid().equals(clientMember.getUuid());
            if (isPartyOwner && !isClientMember) {
                (this.dropDown = new SmallDropDownMenu(mouseX, mouseY, 0, 0)).addDropDownEntry(ModColor.RED + "Kick " + this.hoverMember.getName());
                this.dropDown.addDropDownEntry(ModColor.AQUA + "Make party leader");
                this.dropDown.setMinecraftStyle(false);
                this.dropDown.setOpen(true);
                this.dropDown.setDropDownX(mouseX);
                this.dropDown.setDropDownY(mouseY - this.dropDown.getMaxY());
                this.dropDownMember = this.hoverMember;
            }
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
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void updateScreen() {
    }
}
