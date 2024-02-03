// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.labymod.utils.ModColor;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriend;
import java.util.Iterator;
import java.util.Collection;
import net.labymod.labyconnect.user.ChatUser;
import java.util.ArrayList;
import java.io.IOException;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.ModTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiFriendsAddFriend extends GuiScreen
{
    public static String response;
    private GuiScreen lastScreen;
    private ModTextField username;
    private GuiButton done;
    private GuiButton cancel;
    private String error;
    private long time;
    private boolean flash;
    private boolean check;
    private boolean canAdd;
    private String defaultQuery;
    
    static {
        GuiFriendsAddFriend.response = null;
    }
    
    public GuiFriendsAddFriend(final GuiScreen lastScreen, final String friendAddQuery) {
        this.error = "";
        this.time = 0L;
        this.flash = false;
        this.check = false;
        this.canAdd = true;
        this.defaultQuery = "";
        this.lastScreen = lastScreen;
        this.defaultQuery = friendAddQuery;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        (this.username = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, GuiFriendsAddFriend.width / 2 - 100, GuiFriendsAddFriend.height / 2 - 20, 200, 20)).setBlacklistWord(" ");
        this.username.setMaxStringLength(16);
        this.username.setText(this.defaultQuery);
        if (this.defaultQuery != null && !this.defaultQuery.isEmpty()) {
            this.username.setCursorPositionEnd();
            this.username.setSelectionPos(0);
        }
        this.done = new GuiButton(0, GuiFriendsAddFriend.width / 2 + 3, GuiFriendsAddFriend.height / 2 + 5, 98, 20, LanguageManager.translate("button_request_user"));
        this.buttonList.add(this.done);
        this.cancel = new GuiButton(1, GuiFriendsAddFriend.width / 2 - 101, GuiFriendsAddFriend.height / 2 + 5, 98, 20, LanguageManager.translate("button_cancel"));
        this.buttonList.add(this.cancel);
        super.initGui();
        this.check();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.check && this.username.textboxKeyTyped(typedChar, keyCode)) {
            this.check();
        }
        if (this.done.enabled && (keyCode == 28 || keyCode == 156)) {
            this.actionPerformed(this.done);
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    private void check() {
        final ArrayList<ChatUser> list = new ArrayList<ChatUser>(LabyMod.getInstance().getLabyConnect().getFriends());
        this.canAdd = true;
        for (final ChatUser p : list) {
            if (p.getGameProfile().getName().equalsIgnoreCase(this.username.getText())) {
                this.canAdd = false;
                break;
            }
        }
        if (this.username.getText().equalsIgnoreCase(LabyMod.getInstance().getPlayerName())) {
            this.canAdd = false;
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            this.check = true;
            LabyMod.getInstance().getLabyConnect().getClientConnection().sendPacket(new PacketPlayRequestAddFriend(this.username.getText()));
            this.username.setText("");
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (GuiFriendsAddFriend.response != null) {
            this.error = GuiFriendsAddFriend.response;
            this.check = false;
            GuiFriendsAddFriend.response = null;
            this.flash = true;
            this.time = System.currentTimeMillis();
        }
        this.done.enabled = (!this.check && !this.username.getText().isEmpty() && this.canAdd);
        LabyMod.getInstance().getDrawUtils().drawString(String.valueOf(LanguageManager.translate("minecraft_name")) + ":", GuiFriendsAddFriend.width / 2 - 100, GuiFriendsAddFriend.height / 2 - 33);
        if (!this.error.isEmpty()) {
            if (this.error.contains("true")) {
                Gui.drawRect(0, 10, GuiFriendsAddFriend.width, 30, ModColor.toRGB(30, 220, 100, 200));
                LabyMod.getInstance().getDrawUtils().drawCenteredString("The request has been sent!", GuiFriendsAddFriend.width / 2, 16.0);
            }
            else {
                final String c = ModColor.cl("f");
                Gui.drawRect(0, 10, GuiFriendsAddFriend.width, 30, Color.RED.getRGB());
                if (this.time + 1000L > System.currentTimeMillis() && this.flash) {
                    LabyMod.getInstance().getDrawUtils().drawCenteredString(String.valueOf(c) + "Error: " + this.error + "test2", GuiFriendsAddFriend.width / 2 - 1, 16.0);
                }
                else {
                    LabyMod.getInstance().getDrawUtils().drawCenteredString(String.valueOf(c) + "Error: " + this.error + "test3", GuiFriendsAddFriend.width / 2, 16.0);
                }
            }
            this.flash = !this.flash;
        }
        if (this.check) {
            Gui.drawRect(0, 10, GuiFriendsAddFriend.width, 30, Color.BLUE.getRGB());
            LabyMod.getInstance().getDrawUtils().drawCenteredString("Loading..", GuiFriendsAddFriend.width / 2 - 1, 16.0);
        }
        this.username.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
