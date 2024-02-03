// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui;

import java.io.IOException;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.labymod.main.lang.LanguageManager;
import net.labymod.labyconnect.packets.EnumConnectionState;
import net.labymod.main.LabyMod;
import net.labymod.gui.elements.Tabs;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.layout.WindowLayout;
import net.minecraft.client.gui.GuiScreen;

public class GuiFriendsNotConnected extends GuiScreen
{
    private WindowLayout prevTabSelected;
    private GuiButton buttonLogin;
    
    public GuiFriendsNotConnected(final WindowLayout prevTabSelected) {
        this.prevTabSelected = prevTabSelected;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(this.buttonLogin = new GuiButton(6, GuiFriendsNotConnected.width / 2 - 100, GuiFriendsNotConnected.height / 2 + 10, 200, 20, ""));
        Tabs.initGuiScreen(this.buttonList, this);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.drawDefaultBackground();
        final boolean isOnline = LabyMod.getInstance().getLabyConnect().isOnline();
        final EnumConnectionState currentConnectionState = LabyMod.getInstance().getLabyConnect().getClientConnection().getCurrentConnectionState();
        final boolean isOffline = currentConnectionState == EnumConnectionState.OFFLINE;
        this.buttonLogin.visible = !isOnline;
        this.buttonLogin.enabled = isOffline;
        this.buttonLogin.displayString = (isOffline ? LanguageManager.translate("button_connect_to_chat") : currentConnectionState.getButtonState());
        if (isOnline) {
            Minecraft.getMinecraft().displayGuiScreen(this.prevTabSelected);
            return;
        }
        LabyMod.getInstance().getDrawUtils().drawGradientShadowTop(41.0, 0.0, GuiFriendsNotConnected.width);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowBottom(GuiFriendsNotConnected.height - 50, 0.0, GuiFriendsNotConnected.width);
        final String accountName = LabyMod.getInstance().getPlayerName();
        final EnumConnectionState connectionState = LabyMod.getInstance().getLabyConnect().getClientConnection().getCurrentConnectionState();
        LabyMod.getInstance().getDrawUtils().drawCenteredString(String.valueOf(ModColor.cl("e")) + "LabyMod Chat " + ModColor.cl("7") + "| " + ModColor.cl(connectionState.getDisplayColor()) + connectionState.name(), GuiFriendsNotConnected.width / 2, GuiFriendsNotConnected.height / 2 - 5);
        LabyMod.getInstance().getDrawUtils().drawRightString(accountName, GuiFriendsNotConnected.width - 2, 29.0);
        LabyMod.getInstance().getDrawUtils().drawCenteredString(LabyMod.getInstance().getLabyConnect().getClientConnection().getLastKickMessage(), GuiFriendsNotConnected.width / 2, GuiFriendsNotConnected.height / 2 + 40);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        Tabs.actionPerformedButton(button);
        if (button.id == 6) {
            LabyMod.getInstance().getLabyConnect().getClientConnection().connect();
        }
    }
    
    public WindowLayout getPrevTabSelected() {
        return this.prevTabSelected;
    }
    
    public GuiButton getButtonLogin() {
        return this.buttonLogin;
    }
}
