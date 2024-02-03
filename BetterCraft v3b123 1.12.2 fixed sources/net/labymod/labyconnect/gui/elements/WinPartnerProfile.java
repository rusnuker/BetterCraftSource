// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import net.labymod.labyconnect.user.ChatUser;
import net.minecraft.client.gui.GuiYesNo;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayFriendRemove;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.labyconnect.gui.GuiFriendsAbout;
import net.labymod.user.User;
import java.util.UUID;
import net.labymod.user.UserManager;
import net.labymod.labyconnect.user.ServerInfo;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModUtils;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.labyconnect.user.UserStatus;
import net.labymod.utils.ModColor;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.gui.elements.SmallDropDownMenu;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinPartnerProfile extends WindowElement<GuiFriendsLayout>
{
    private SmallDropDownMenu smallDropDownMenu;
    
    public WinPartnerProfile(final GuiFriendsLayout chatLayout) {
        super(chatLayout);
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        this.initDropDown();
    }
    
    private void initDropDown() {
        (this.smallDropDownMenu = new SmallDropDownMenu(0, this.bottom - 14, 30, 10)).setRenderCustomSelected(LanguageManager.translate("chat_user_options"));
        if (GuiFriendsLayout.selectedUser != null && GuiFriendsLayout.selectedUser.getCurrentServerInfo() != null && GuiFriendsLayout.selectedUser.getCurrentServerInfo().isServerAvailable()) {
            this.smallDropDownMenu.addDropDownEntry(LanguageManager.translate("chat_user_join_server"));
        }
        this.smallDropDownMenu.addDropDownEntry(LanguageManager.translate("chat_user_about"));
        this.smallDropDownMenu.addDropDownEntry(LanguageManager.translate("chat_user_remove_friend"));
        this.smallDropDownMenu.setChangeable(false);
        this.smallDropDownMenu.setMinecraftStyle(false);
        this.smallDropDownMenu.setColor(Integer.MIN_VALUE);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        if (GuiFriendsLayout.selectedUser == null) {
            return;
        }
        final DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();
        drawUtils.drawOverlayBackground(this.left, this.top, this.right, this.top);
        if (!GuiFriendsLayout.selectedUser.isOnline()) {
            GlStateManager.color(0.2f, 0.2f, 0.2f, 1.0f);
        }
        else {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        final int headPadding = 2;
        final int headSize = this.bottom - this.top - 4;
        if (GuiFriendsLayout.selectedUser.isParty()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_PARTY);
            drawUtils.drawTexture(this.left + 1 + 2, this.top + 2, 255.0, 255.0, headSize, headSize);
        }
        else {
            drawUtils.drawPlayerHead(GuiFriendsLayout.selectedUser.getGameProfile(), this.left + 1 + 2, this.top + 2, headSize);
        }
        final ServerInfo serverInfo = GuiFriendsLayout.selectedUser.getCurrentServerInfo();
        final String displayOnlineStatus = String.valueOf(ModColor.cl(GuiFriendsLayout.selectedUser.getStatus().getChatColor())) + GuiFriendsLayout.selectedUser.getStatus().getName();
        final boolean statusAvailable = serverInfo != null && serverInfo.isServerAvailable() && GuiFriendsLayout.selectedUser.getStatus() != UserStatus.OFFLINE;
        String displayServerStatus = statusAvailable ? (String.valueOf(ModColor.cl("f")) + " " + LanguageManager.translate("chat_user_online_on") + " " + ModColor.cl("a") + serverInfo.getDisplayAddress()) : "";
        if (statusAvailable && serverInfo.getSpecifiedServerName() != null && !serverInfo.getSpecifiedServerName().isEmpty()) {
            displayServerStatus = String.valueOf(displayServerStatus) + ModColor.cl("f") + ", " + ModColor.cl("e") + serverInfo.getSpecifiedServerName();
        }
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final UUID uuid = GuiFriendsLayout.selectedUser.getGameProfile().getId();
        String username = GuiFriendsLayout.selectedUser.getGameProfile().getName();
        if (GuiFriendsLayout.selectedUser.isOnline() && userManager.isWhitelisted(uuid)) {
            final User user = userManager.getUser(uuid);
            final char hexColor = user.getGroup().getColorMinecraft();
            username = String.valueOf(ModColor.cl(hexColor)) + username;
            if (mouseX > this.left + 2 + headSize + 5 && mouseX < this.left + 2 + headSize + 5 + this.draw.getStringWidth(username) && mouseY > this.top + 4 && mouseY < this.top + 4 + 10) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, String.valueOf(ModColor.cl(hexColor)) + user.getGroup().getDisplayName());
            }
        }
        final String partyUsername = GuiFriendsLayout.selectedUser.isParty() ? "Party" : username;
        String finalNameLine = String.valueOf(partyUsername) + ModColor.cl("7") + " (" + displayOnlineStatus + displayServerStatus + ModColor.cl("7") + ")";
        final GuiFriendsLayout layout = (GuiFriendsLayout)this.layout;
        finalNameLine = LabyMod.getInstance().getDrawUtils().trimStringToWidth(finalNameLine, layout.getChatElementMyProfile().getTotalButtonWidth() - this.left - 2 - headSize - 10);
        drawUtils.drawString(finalNameLine, this.left + 2 + headSize + 5, this.top + 4);
        final boolean isTyping = GuiFriendsLayout.selectedUser.getLastTyping() + 1500L > System.currentTimeMillis();
        if (isTyping) {
            drawUtils.drawString(String.valueOf(ModColor.cl('7')) + LanguageManager.translate("chat_is_typing"), this.left + 2 + headSize + 5 + this.smallDropDownMenu.getWidthIn() + 5, this.top + 4 + 15, 0.7);
        }
        if (!GuiFriendsLayout.selectedUser.isOnline()) {
            drawUtils.drawString(ModUtils.getTimeDiff(GuiFriendsLayout.selectedUser.getLastOnline()), this.left + 2 + headSize + 5 + drawUtils.getStringWidth(finalNameLine) + 5, this.top + 4, 0.7);
        }
        if (!GuiFriendsLayout.selectedUser.isParty()) {
            this.smallDropDownMenu.setX(this.left + 2 + headSize + 5);
            this.smallDropDownMenu.renderButton(mouseX, mouseY);
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        int entryId = this.smallDropDownMenu.onClick(mouseX, mouseY);
        if (entryId >= 0) {
            if (this.smallDropDownMenu.getDropDownEntrys().size() != 3) {
                ++entryId;
            }
            switch (entryId) {
                case 0: {
                    final ServerInfo serverInfo = GuiFriendsLayout.selectedUser.getCurrentServerInfo();
                    if (serverInfo != null && serverInfo.isServerAvailable()) {
                        LabyMod.getInstance().switchServer(String.valueOf(serverInfo.getServerIp()) + ":" + serverInfo.getServerPort(), true);
                        break;
                    }
                    break;
                }
                case 1: {
                    if (GuiFriendsLayout.selectedUser != null) {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiFriendsAbout(Minecraft.getMinecraft().currentScreen, GuiFriendsLayout.selectedUser));
                        break;
                    }
                    break;
                }
                case 2: {
                    final GuiScreen lastScreen = Minecraft.getMinecraft().currentScreen;
                    if (GuiFriendsLayout.selectedUser != null) {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                            @Override
                            public void confirmClicked(final boolean result, final int id) {
                                if (GuiFriendsLayout.selectedUser != null && result) {
                                    LabyMod.getInstance().getLabyConnect().getClientConnection().sendPacket(new PacketPlayFriendRemove(GuiFriendsLayout.selectedUser));
                                    Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                                    GuiFriendsLayout.selectedUser = null;
                                }
                                else {
                                    Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                                }
                            }
                        }, LanguageManager.translate("chat_user_remove_friend_popup"), String.valueOf(ModColor.cl("c")) + GuiFriendsLayout.selectedUser.getGameProfile().getName(), 0));
                        break;
                    }
                    break;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
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
    
    public void setPartner(final ChatUser partner) {
        GuiFriendsLayout.selectedUser = partner;
        if (partner != null) {
            partner.setUnreadMessages(0);
        }
        this.initDropDown();
    }
    
    @Override
    public void updateScreen() {
    }
}
