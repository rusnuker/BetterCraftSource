// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import net.labymod.labyconnect.gui.GuiFriendsRequests;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.labyconnect.gui.GuiFriendsAddFriend;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.GameProfile;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.core.LabyModCore;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.gui.elements.CustomGuiButton;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinMyProfile extends WindowElement<GuiFriendsLayout>
{
    private ChatUser clientUser;
    private CustomGuiButton buttonFriendRequests;
    private CustomGuiButton buttonAddFriend;
    private int totalButtonWidth;
    private boolean hoverProfileIcon;
    
    public WinMyProfile(final GuiFriendsLayout chatLayout, final ChatUser clientUser) {
        super(chatLayout);
        this.hoverProfileIcon = false;
        this.clientUser = clientUser;
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        buttonlist.add(this.buttonFriendRequests = new CustomGuiButton(2, 0, top + (bottom - top - 20) / 2, 0, 20, ""));
        buttonlist.add(this.buttonAddFriend = new CustomGuiButton(3, 0, top + (bottom - top - 20) / 2, 22, 20, "+"));
        this.updateButtons();
    }
    
    public void updateButtons() {
        final int headSize = this.bottom - this.top - 3 - 2;
        final int requestCount = LabyMod.getInstance().getLabyConnect().getRequests().size();
        final String buttonString = String.valueOf(LanguageManager.translate("button_requests")) + ((requestCount == 0) ? "" : (" (" + requestCount + ")"));
        final int buttonWidth = LabyMod.getInstance().getDrawUtils().getStringWidth(buttonString) + 8;
        this.buttonFriendRequests.displayString = buttonString;
        this.buttonFriendRequests.enabled = (requestCount != 0);
        this.buttonFriendRequests.setXPosition(this.right - headSize - buttonWidth - 5);
        this.buttonFriendRequests.setWidth(buttonWidth);
        this.buttonAddFriend.setXPosition(this.right - headSize - buttonWidth - 5 - 25);
        this.totalButtonWidth = LabyModCore.getMinecraft().getXPosition(this.buttonAddFriend);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        try {
            if (this.clientUser == null) {
                return;
            }
            final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            final GameProfile gameProfile = this.clientUser.getGameProfile();
            final int headSize = this.bottom - this.top - 3 - 2;
            this.hoverProfileIcon = (mouseX > this.right - headSize - 2 && mouseX < this.right - headSize - 2 + headSize && mouseY > this.top + 2 && mouseY < this.top + 2 + headSize);
            GlStateManager.color(this.hoverProfileIcon ? 0.6f : 1.0f, this.hoverProfileIcon ? 0.6f : 1.0f, this.hoverProfileIcon ? 0.6f : 1.0f);
            GlStateManager.enableAlpha();
            draw.drawPlayerHead(gameProfile, this.right - headSize - 2, this.top + 2, headSize);
            if (this.hoverProfileIcon) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, new String[] { String.valueOf(ModColor.cl("e")) + gameProfile.getName(), String.valueOf(ModColor.cl("7")) + LanguageManager.translate("button_open_profile_settings") });
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                GlStateManager.enableAlpha();
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final GuiFriendsLayout layout = (GuiFriendsLayout)this.layout;
        if (this.hoverProfileIcon && this.clientUser != null) {
            layout.setProfileOpen(!layout.isProfileOpen());
            ((GuiFriendsLayout)this.layout).initGui();
            this.buttonFriendRequests.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        }
        return false;
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
        final GuiFriendsLayout layout = (GuiFriendsLayout)this.layout;
        if (button.id == this.buttonAddFriend.id) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiFriendsAddFriend(this.layout, layout.getChatElementSearchField().getFieldSearch().getText()));
        }
        if (button.id == this.buttonFriendRequests.id) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiFriendsRequests(this.layout));
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
    
    public int getTotalButtonWidth() {
        return this.totalButtonWidth;
    }
}
