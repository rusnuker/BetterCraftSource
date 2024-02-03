// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import net.minecraft.util.ResourceLocation;
import java.util.Comparator;
import java.net.URISyntaxException;
import java.net.URI;
import net.labymod.utils.ModUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.client.gui.Gui;
import net.labymod.utils.ModColor;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.GuiScreen;

public class GuiTeamSpeak extends GuiScreen
{
    private GuiScreen parent;
    private DrawUtils draw;
    boolean boxEnabled;
    boolean boxIsUser;
    int boxId;
    int boxPosX;
    int boxPosY;
    int boxLengthX;
    int boxLengthY;
    boolean drag;
    boolean drop;
    int dragVisible;
    boolean dropFocus;
    int dropX;
    int dropY;
    boolean dragIsUser;
    int dragId;
    boolean changeNickName;
    GuiTextField nickNameField;
    GuiTextField chatInputField;
    long lastClick;
    boolean allowChatScroll;
    boolean allowChannelScroll;
    int yMouse;
    int doubleClickDelay;
    boolean doubleClickIsUser;
    int doubleClickTarget;
    GuiButton connectButton;
    GuiButton backButton;
    int connect;
    boolean init;
    int clickX;
    int clickY;
    boolean moveX;
    boolean moveY;
    
    public GuiTeamSpeak(final GuiScreen parent) {
        this.boxEnabled = false;
        this.boxIsUser = false;
        this.boxId = 0;
        this.boxPosX = 0;
        this.boxPosY = 0;
        this.boxLengthX = 0;
        this.boxLengthY = 0;
        this.drag = false;
        this.drop = false;
        this.dragVisible = 0;
        this.dropFocus = false;
        this.dropX = 0;
        this.dropY = 0;
        this.dragIsUser = false;
        this.dragId = 0;
        this.changeNickName = false;
        this.lastClick = 0L;
        this.yMouse = 0;
        this.doubleClickDelay = 500;
        this.doubleClickTarget = 0;
        this.connect = 0;
        this.init = false;
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.draw = new DrawUtils();
        TeamSpeak.setDefaultScreen();
        this.changeNickName = false;
        this.drag = false;
        this.closeBox();
        this.connect = 0;
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        (this.chatInputField = new GuiTextField(0, this.mc.fontRendererObj, 5, this.draw.getHeight() - 17, this.draw.getWidth() - 10, 12)).setText(TeamSpeak.inputString);
        this.chatInputField.setMaxStringLength(200);
        final int i = TeamSpeak.xSplit - 26;
        (this.nickNameField = new GuiTextField(0, this.mc.fontRendererObj, 0, 0, i, 9)).setMaxStringLength(20);
        this.backButton = new GuiButton(0, 15, 2, 30, 20, "Back");
        this.connectButton = new GuiButton(1, GuiTeamSpeak.width / 2 - 100, GuiTeamSpeak.height / 2 + 10, "Connect");
        if (TeamSpeakController.getInstance() != null) {
            this.connectButton.visible = !TeamSpeakController.getInstance().isConnectionEstablished();
        }
        this.buttonList.add(this.connectButton);
        this.buttonList.add(this.backButton);
        while (TeamSpeak.xSplit > this.draw.getWidth() / 4 * 3) {
            --TeamSpeak.xSplit;
        }
        while (TeamSpeak.xSplit < 200) {
            ++TeamSpeak.xSplit;
        }
        while (TeamSpeak.ySplit > this.draw.getHeight() - 50) {
            --TeamSpeak.ySplit;
        }
        while (TeamSpeak.ySplit < 50) {
            ++TeamSpeak.ySplit;
        }
        this.boxEnabled = false;
        super.initGui();
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                this.connect = 1;
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (TeamSpeak.overlayWindows != null) {
            TeamSpeak.overlayWindows.KeyTyped(typedChar, keyCode);
            if (!TeamSpeak.overlayWindows.allow()) {
                return;
            }
        }
        if (keyCode == 28) {
            if (this.changeNickName && this.nickNameField != null && this.nickNameField.isFocused()) {
                this.changeNickname();
            }
            else if (!TeamSpeak.inputString.isEmpty() && this.chatInputField != null && this.chatInputField.isFocused()) {
                TeamSpeakBridge.sendTextMessage(TeamSpeak.selectedChat, TeamSpeak.inputString);
                TeamSpeak.inputString = "";
                this.chatInputField.setText("");
            }
        }
        if (this.chatInputField.textboxKeyTyped(typedChar, keyCode)) {
            TeamSpeak.inputString = this.chatInputField.getText();
        }
        if (this.changeNickName) {
            this.nickNameField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.boxAction(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        else {
            this.closeBox();
            if (TeamSpeak.overlayWindows != null) {
                TeamSpeak.overlayWindows.mouseClicked(mouseX, mouseY, mouseButton);
                if (TeamSpeak.overlayWindows.isInScreen(mouseX, mouseY)) {
                    super.mouseClicked(mouseX, mouseY, mouseButton);
                    return;
                }
            }
            if (this.changeNickName) {
                this.changeNickname();
            }
            this.chatInputField.mouseClicked(mouseX, mouseY, mouseButton);
            if (this.changeNickName) {
                this.nickNameField.mouseClicked(mouseX, mouseY, mouseButton);
            }
            this.clickX = mouseX - TeamSpeak.xSplit;
            this.clickY = mouseY - TeamSpeak.ySplit;
            if (mouseX > TeamSpeak.xSplit - 5 && mouseX < TeamSpeak.xSplit + 5) {
                this.moveX = true;
            }
            if (mouseY > TeamSpeak.ySplit - 5 && mouseY < TeamSpeak.ySplit + 5) {
                this.moveY = true;
            }
            if (!this.moveX && !this.moveY) {
                this.switchChat(mouseX, mouseY, mouseButton);
                this.channelAction(mouseX, mouseY, mouseButton);
                this.menuAction(mouseX, mouseY, mouseButton);
                super.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }
            if (i < -1) {
                i = -1;
            }
            if (this.boxEnabled) {
                return;
            }
            if (this.changeNickName) {
                return;
            }
            if (this.yMouse > TeamSpeak.ySplit) {
                if (i > 0) {
                    if (this.allowChatScroll) {
                        TeamSpeak.scrollChat -= 10;
                    }
                }
                else if (TeamSpeak.scrollChat < 0) {
                    TeamSpeak.scrollChat += 10;
                }
            }
            else if (i > 0) {
                if (TeamSpeak.scrollChannel < 0) {
                    TeamSpeak.scrollChannel += 20;
                }
            }
            else if (this.allowChannelScroll) {
                TeamSpeak.scrollChannel -= 20;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.callBackListener(mouseX, mouseY);
        this.connectButton.visible = !TeamSpeakController.getInstance().isConnectionEstablished();
        if (this.connectButton != null) {
            if (this.connect == 2) {
                this.connect = 0;
                TeamSpeakController.getInstance().connect();
            }
            if (this.connect == 1) {
                ++this.connect;
            }
        }
        if (!TeamSpeakController.getInstance().isConnectionEstablished()) {
            final String s = String.valueOf(TeamSpeakController.getInstance().serverIP) + ":" + TeamSpeakController.getInstance().serverPort;
            if (!TeamSpeakController.getInstance().serverIP.isEmpty() && TeamSpeakController.getInstance().serverPort != 0) {
                this.draw.drawCenteredString(String.valueOf(ModColor.cl("a")) + "Connect to " + s, GuiTeamSpeak.width / 2, GuiTeamSpeak.height / 2 - 15);
            }
            else {
                this.draw.drawCenteredString(String.valueOf(ModColor.cl("c")) + "Error please open TeamSpeak and Connect", GuiTeamSpeak.width / 2, GuiTeamSpeak.height / 2 - 15);
                if (this.connectButton != null) {
                    this.connectButton.visible = true;
                    if (!this.connectButton.enabled) {
                        this.connectButton.displayString = "Connecting";
                    }
                    else {
                        this.connectButton.displayString = "Connect";
                    }
                }
            }
            if (this.init) {
                this.init = false;
                this.initGui();
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        else if (!TeamSpeakController.getInstance().serverIP.isEmpty() && TeamSpeakController.getInstance().serverPort != 0 && TeamSpeakUser.amount() != 0 && TeamSpeakChannel.amount() != 0) {
            if (!this.init) {
                this.init = true;
                this.initGui();
            }
            this.chatInputField.drawTextBox();
            this.drawChat();
            this.drawChannel(mouseX, mouseY);
            this.drawClientInfo();
            this.drawMenu(mouseX, mouseY);
            if (this.changeNickName) {
                this.nickNameField.drawTextBox();
            }
            this.yMouse = mouseY;
            if (TeamSpeak.selectedUser != -1 && TeamSpeakController.getInstance().getUser(TeamSpeak.selectedUser) == null) {
                TeamSpeak.selectedUser = -1;
            }
            if (TeamSpeak.selectedChannel != -1 && TeamSpeakController.getInstance().getChannel(TeamSpeak.selectedChannel) == null) {
                TeamSpeak.selectedChannel = -1;
            }
            if (TeamSpeak.selectedChannel == -1 && TeamSpeak.selectedUser == -1 && TeamSpeakController.getInstance().me() != null) {
                TeamSpeak.selectedUser = TeamSpeakController.getInstance().me().getClientId();
            }
            if (TeamSpeak.overlayWindows != null) {
                TeamSpeak.overlayWindows.drawWindow(mouseX, mouseY);
                if (TeamSpeak.overlayWindows.isInScreen(mouseX, mouseY)) {
                    super.drawScreen(mouseX, mouseY, partialTicks);
                    this.drawBox(mouseX, mouseY);
                    return;
                }
            }
            this.drawBox(mouseX, mouseY);
            if (mouseX > TeamSpeak.xSplit - 5 && mouseX < TeamSpeak.xSplit + 5 && mouseY > TeamSpeak.ySplit - 5 && mouseY < TeamSpeak.ySplit + 5) {
                this.draw.drawCenteredString(String.valueOf(ModColor.cl("7")) + "+", mouseX + 1, mouseY - 2);
            }
            else {
                if (mouseX > TeamSpeak.xSplit - 5 && mouseX < TeamSpeak.xSplit + 5 && mouseY < TeamSpeak.ySplit) {
                    this.draw.drawCenteredString(String.valueOf(ModColor.cl("7")) + "...", mouseX + 1, mouseY - 6);
                    this.draw.drawCenteredString(String.valueOf(ModColor.cl("7")) + "...", mouseX + 1, mouseY - 3);
                }
                if (mouseY > TeamSpeak.ySplit - 5 && mouseY < TeamSpeak.ySplit + 5) {
                    this.draw.drawCenteredString(String.valueOf(ModColor.cl("7")) + "||", mouseX + 1, mouseY - 3);
                }
            }
            if (this.drag) {
                this.drawDrag(mouseX, mouseY);
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        else {
            if (this.connectButton != null) {
                this.connectButton.visible = true;
            }
            this.draw.drawCenteredString(String.valueOf(ModColor.cl("c")) + "No server found", GuiTeamSpeak.width / 2, GuiTeamSpeak.height / 2 - 15);
            this.draw.drawCenteredString(String.valueOf(ModColor.cl("7")) + "Try restart", GuiTeamSpeak.width / 2, GuiTeamSpeak.height / 2 - 5);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        if (TeamSpeak.overlayWindows != null) {
            TeamSpeak.overlayWindows.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            if (TeamSpeak.overlayWindows.isInScreen(mouseX, mouseY)) {
                super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
                return;
            }
        }
        if (this.moveX && mouseX - this.clickX < this.draw.getWidth() / 4 * 3 && mouseX - this.clickX > 200) {
            TeamSpeak.xSplit = mouseX - this.clickX;
        }
        if (this.moveY && mouseY - this.clickY < this.draw.getHeight() - 50 && mouseY - this.clickY > 50) {
            TeamSpeak.ySplit = mouseY - this.clickY;
        }
        if (this.drag) {
            ++this.dragVisible;
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (TeamSpeak.overlayWindows != null) {
            TeamSpeak.overlayWindows.mouseReleased(mouseX, mouseY, state);
            if (TeamSpeak.overlayWindows.isInScreen(mouseX, mouseY)) {
                super.mouseReleased(mouseX, mouseY, state);
                return;
            }
        }
        if (this.moveX || this.moveY) {
            this.initGui();
        }
        this.moveX = false;
        this.moveY = false;
        if (this.drag) {
            this.setDrop(mouseX, mouseY);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private void drawChat() {
        DrawUtils drawutils = this.draw;
        Gui.drawRect(5, TeamSpeak.ySplit, this.draw.getWidth() - 5, this.draw.getHeight() - 20, Integer.MIN_VALUE);
        Chat chat = null;
        int i = 0;
        for (final Chat chat2 : TeamSpeak.chats) {
            drawutils = this.draw;
            Gui.drawRect(5 + i, this.draw.getHeight() - 33, 50 + i, this.draw.getHeight() - 20, 835640000);
            final String s = "";
            if (TeamSpeak.selectedChat == chat2.getSlotId()) {
                drawutils = this.draw;
                Gui.drawRect(5 + i + 1, this.draw.getHeight() - 33 + 1, 50 + i - 1, this.draw.getHeight() - 20 - 1, Integer.MAX_VALUE);
                chat = chat2;
            }
            String s2 = StringUtils.capitalize(chat2.getTargetMode().name());
            if (chat2.getTargetMode() == EnumTargetMode.USER) {
                s2 = chat2.getChatOwner().getNickName();
                if (s2.length() > 7) {
                    s2 = s2.substring(0, 7);
                }
            }
            this.draw.drawCenteredString(String.valueOf(s) + s2, 27 + i, this.draw.getHeight() - 30);
            i += 46;
        }
        if (chat != null) {
            int j = 0;
            final List<Message> list = new ArrayList<Message>();
            list.addAll(chat.getLog());
            Collections.reverse(list);
            for (final Message message : list) {
                if (this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat >= TeamSpeak.ySplit && this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat <= this.draw.getHeight() - 45) {
                    if (message.getUser() == null) {
                        this.draw.drawString(message.getMessage(), 8.0, this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat);
                    }
                    else {
                        this.draw.drawString(String.valueOf(ModColor.cl("9")) + message.getUser().getNickName() + ModColor.cl("7") + ": " + message.getMessage(), 8.0, this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat);
                    }
                }
                j += 10;
            }
            this.allowChatScroll = (this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat < TeamSpeak.ySplit - 10);
        }
    }
    
    public void switchChat(final int mouseX, final int mouseY, final int mouseButton) {
        int i = 0;
        Chat chat = null;
        for (final Chat chat2 : TeamSpeak.chats) {
            if (mouseX > 5 + i && mouseX < 50 + i && mouseY > this.draw.getHeight() - 33 && mouseY < this.draw.getHeight() - 20) {
                TeamSpeak.scrollChat = 0;
                if (mouseButton == 0) {
                    TeamSpeak.selectedChat = chat2.getSlotId();
                }
                if (mouseButton == 1 && chat2.getSlotId() >= 0) {
                    TeamSpeak.selectedChat = -1;
                    chat = chat2;
                }
            }
            i += 46;
        }
        if (chat != null) {
            TeamSpeak.chats.remove(chat);
        }
        Chat chat3 = null;
        for (final Chat chat4 : TeamSpeak.chats) {
            if (TeamSpeak.selectedChat == chat4.getSlotId()) {
                chat3 = chat4;
            }
        }
        if (chat3 != null) {
            int j = 0;
            final List<Message> list = new ArrayList<Message>();
            list.addAll(chat3.getLog());
            Collections.reverse(list);
            for (final Message message : list) {
                if (this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat >= TeamSpeak.ySplit && this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat <= this.draw.getHeight() - 45 && message.getUser() != null) {
                    if (mouseX > 8 && mouseX < this.draw.getStringWidth(message.getUser().getNickName()) + 8 && mouseY > this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat && mouseY < this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat + 10) {
                        this.openBox(true, message.getUser().getClientId(), mouseX, mouseY);
                    }
                    if (mouseX > 8 && mouseX > this.draw.getStringWidth(message.getUser().getNickName()) + 8 && mouseY > this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat && mouseY < this.draw.getHeight() - 45 - j - TeamSpeak.scrollChat + 10) {
                        final ArrayList<String> arraylist = ModUtils.extractDomains(message.getMessage());
                        if (!arraylist.isEmpty()) {
                            try {
                                ModUtils.openWebpage(new URI(arraylist.get(0)), false);
                            }
                            catch (final URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                j += 10;
            }
        }
    }
    
    private void drawChannel(final int mouseX, final int mouseY) {
        final DrawUtils drawutils = this.draw;
        Gui.drawRect(5, TeamSpeak.ySplit - 1, TeamSpeak.xSplit, 25, Integer.MIN_VALUE);
        final ArrayList<TeamSpeakChannel> arraylist = new ArrayList<TeamSpeakChannel>();
        int i = 0;
        for (final TeamSpeakChannel teamspeakchannel : TeamSpeakBridge.getChannels()) {
            if (teamspeakchannel.getChannelOrder() == 0) {
                arraylist.add(teamspeakchannel);
            }
            else {
                final TeamSpeakChannel teamspeakchannel2 = TeamSpeak.getFromOrder(i);
                if (teamspeakchannel2 == null) {
                    break;
                }
                i = teamspeakchannel2.getChannelId();
                arraylist.add(teamspeakchannel2);
            }
        }
        boolean flag = false;
        int j = 0;
        int k = 0;
        TeamSpeak.outOfView.clear();
        for (final TeamSpeakChannel teamspeakchannel3 : arraylist) {
            if (mouseX > 5 && mouseX < TeamSpeak.xSplit && mouseY > 30 + k + TeamSpeak.scrollChannel && mouseY < 30 + k + TeamSpeak.scrollChannel + 11 && 30 + k + TeamSpeak.scrollChannel + 10 < TeamSpeak.ySplit && 30 + k + TeamSpeak.scrollChannel > 20) {
                flag = true;
                j = teamspeakchannel3.getChannelId();
                if (this.drag) {
                    Gui.drawRect(5, 30 + k + TeamSpeak.scrollChannel - 1, TeamSpeak.xSplit, 30 + k + TeamSpeak.scrollChannel + 10, 1230000000);
                }
            }
            if (30 + k + TeamSpeak.scrollChannel < 20) {
                TeamSpeak.outOfView.add(teamspeakchannel3.getChannelId());
            }
            if (30 + k + TeamSpeak.scrollChannel + 10 < TeamSpeak.ySplit && 30 + k + TeamSpeak.scrollChannel > 20) {
                final String s = ModColor.cl("7");
                if (TeamSpeak.selectedChannel == teamspeakchannel3.getChannelId()) {
                    Gui.drawRect(5, 30 + k + TeamSpeak.scrollChannel - 1, TeamSpeak.xSplit, 30 + k + TeamSpeak.scrollChannel + 10, 1230000000);
                }
                if (TeamSpeak.isSpacer(teamspeakchannel3.getChannelName())) {
                    this.draw.drawCenteredString(String.valueOf(s) + TeamSpeak.toStarSpacer(teamspeakchannel3.getChannelName(), TeamSpeak.xSplit), TeamSpeak.xSplit / 2, 30 + k + TeamSpeak.scrollChannel);
                }
                else if (TeamSpeak.isStarSpacer(teamspeakchannel3.getChannelName())) {
                    this.draw.drawCenteredString(String.valueOf(s) + TeamSpeak.toStarSpacer(teamspeakchannel3.getChannelName(), TeamSpeak.xSplit), TeamSpeak.xSplit / 2, 30 + k + TeamSpeak.scrollChannel);
                }
                else if (TeamSpeak.isCenterSpacer(teamspeakchannel3.getChannelName())) {
                    this.draw.drawCenteredString(String.valueOf(s) + TeamSpeak.toCenterSpacer(teamspeakchannel3.getChannelName()), TeamSpeak.xSplit / 2, 30 + k + TeamSpeak.scrollChannel);
                }
                else {
                    String s2 = ModColor.cl("2");
                    if (teamspeakchannel3.getIsPassword()) {
                        s2 = ModColor.cl("e");
                    }
                    if (!teamspeakchannel3.getSubscription()) {
                        s2 = ModColor.cl("b");
                    }
                    if (teamspeakchannel3.getTotalClients() >= teamspeakchannel3.getMaxClients() && teamspeakchannel3.getMaxClients() != -1) {
                        s2 = ModColor.cl("4");
                    }
                    this.draw.drawString(String.valueOf(s2) + " \u2b1b " + s + teamspeakchannel3.getChannelName(), 5.0, 30 + k + TeamSpeak.scrollChannel);
                }
            }
            final ArrayList<TeamSpeakUser> arraylist2 = new ArrayList<TeamSpeakUser>();
            arraylist2.addAll(TeamSpeakBridge.getUsers());
            Collections.sort(arraylist2, new Comparator<TeamSpeakUser>() {
                @Override
                public int compare(final TeamSpeakUser o1, final TeamSpeakUser o2) {
                    return (o1 != null && o2 != null) ? ((o1.getTalkPower() < o2.getTalkPower()) ? 1 : ((o1.getTalkPower() > o2.getTalkPower()) ? -1 : 0)) : 0;
                }
            });
            for (final TeamSpeakUser teamspeakuser : arraylist2) {
                if (teamspeakuser != null && teamspeakuser.getChannelId() == teamspeakchannel3.getChannelId()) {
                    k += 10;
                    if (30 + k + TeamSpeak.scrollChannel + 10 >= TeamSpeak.ySplit || 30 + k + TeamSpeak.scrollChannel <= 20) {
                        continue;
                    }
                    String s3 = "";
                    if (TeamSpeak.selectedUser == teamspeakuser.getClientId()) {
                        Gui.drawRect(5, 30 + k + TeamSpeak.scrollChannel - 1, TeamSpeak.xSplit, 30 + k + TeamSpeak.scrollChannel + 10, 1230000000);
                    }
                    if (TeamSpeakController.getInstance().me() != null && teamspeakuser.getClientId() == TeamSpeakController.getInstance().me().getClientId()) {
                        s3 = String.valueOf(s3) + ModColor.cl("c") + ModColor.cl("l") + ModColor.cl("n");
                        this.nickNameField.xPosition = 25;
                        this.nickNameField.yPosition = 30 + k + TeamSpeak.scrollChannel;
                    }
                    String s4 = "";
                    if (TeamSpeak.teamSpeakGroupPrefix) {
                        final ArrayList<TeamSpeakServerGroup> arraylist3 = new ArrayList<TeamSpeakServerGroup>();
                        arraylist3.addAll(TeamSpeakServerGroup.getGroups());
                        for (final TeamSpeakServerGroup teamspeakservergroup : arraylist3) {
                            if (teamspeakservergroup != null && teamspeakuser.getServerGroups().contains(teamspeakservergroup.getSgid()) && teamspeakservergroup.getType() == 1 && teamspeakservergroup.getIconId() != 0) {
                                s4 = String.valueOf(s4) + ModColor.cl("b") + "[" + TeamSpeak.fix(teamspeakservergroup.getGroupName()) + "] ";
                            }
                        }
                    }
                    this.draw.drawString(String.valueOf(TeamSpeak.getTalkColor(teamspeakuser)) + "  \u2b24 " + ModColor.cl("f") + s4 + ModColor.cl("f") + s3 + teamspeakuser.getNickName() + TeamSpeak.getAway(teamspeakuser), 5.0, 30 + k + TeamSpeak.scrollChannel);
                }
            }
            k += 10;
        }
        this.allowChannelScroll = (30 + k + TeamSpeak.scrollChannel > TeamSpeak.ySplit - 10);
        if (30 + k + TeamSpeak.scrollChannel < TeamSpeak.ySplit - 30) {
            TeamSpeak.scrollChannel += 10;
        }
        if (TeamSpeak.scrollChannel > 20) {
            TeamSpeak.scrollChannel -= 10;
        }
        this.drop(j, flag);
    }
    
    private void channelAction(final int mouseX, final int mouseY, final int mouseButton) {
        final ArrayList<TeamSpeakChannel> arraylist = new ArrayList<TeamSpeakChannel>();
        int i = 0;
        for (final TeamSpeakChannel teamspeakchannel : TeamSpeakBridge.getChannels()) {
            final TeamSpeakChannel teamspeakchannel2 = TeamSpeak.getFromOrder(i);
            if (teamspeakchannel.getChannelOrder() == 0) {
                arraylist.add(teamspeakchannel);
            }
            else {
                if (teamspeakchannel2 == null) {
                    break;
                }
                i = teamspeakchannel2.getChannelId();
                arraylist.add(teamspeakchannel2);
            }
        }
        int j = 0;
        for (final TeamSpeakChannel teamspeakchannel3 : arraylist) {
            if (30 + j + TeamSpeak.scrollChannel + 5 < TeamSpeak.ySplit && 30 + j + TeamSpeak.scrollChannel > 20 && mouseX > 5 && mouseX < TeamSpeak.xSplit && mouseY > 30 + j + TeamSpeak.scrollChannel && mouseY < 30 + j + TeamSpeak.scrollChannel + 10) {
                TeamSpeak.selectedChannel = teamspeakchannel3.getChannelId();
                TeamSpeak.selectedUser = -1;
                if (mouseButton == 1) {
                    this.openBox(false, teamspeakchannel3.getChannelId(), mouseX, mouseY);
                    return;
                }
                if (this.lastClick + this.doubleClickDelay > System.currentTimeMillis() && this.doubleClickTarget == teamspeakchannel3.getChannelId() && !this.doubleClickIsUser && TeamSpeakController.getInstance().me() != null && TeamSpeakController.getInstance().me().getChannelId() != teamspeakchannel3.getChannelId()) {
                    if (teamspeakchannel3.getIsPassword()) {
                        TeamSpeak.overlayWindows.openInput(teamspeakchannel3.getChannelId(), "Channel Password Title", "insert password for" + teamspeakchannel3.getChannelName(), new PopUpCallback() {
                            @Override
                            public void ok(final int cid, final String message) {
                                TeamSpeakBridge.moveClient(TeamSpeakController.getInstance().me().getClientId(), cid);
                            }
                            
                            @Override
                            public void ok() {
                            }
                            
                            @Override
                            public void cancel() {
                            }
                            
                            @Override
                            public boolean tick(final int cid) {
                                return TeamSpeakController.getInstance().me() != null && TeamSpeakController.getInstance().me().getChannelId() == cid;
                            }
                        });
                    }
                    TeamSpeakBridge.moveClient(TeamSpeakController.getInstance().me().getClientId(), teamspeakchannel3.getChannelId());
                }
                this.doubleClickTarget = teamspeakchannel3.getChannelId();
                this.doubleClickIsUser = false;
                this.lastClick = System.currentTimeMillis();
            }
            final ArrayList<TeamSpeakUser> arraylist2 = new ArrayList<TeamSpeakUser>();
            arraylist2.addAll(TeamSpeakBridge.getUsers());
            Collections.sort(arraylist2, new Comparator<TeamSpeakUser>() {
                @Override
                public int compare(final TeamSpeakUser o1, final TeamSpeakUser o2) {
                    return (o1 != null && o2 != null) ? ((o1.getTalkPower() < o2.getTalkPower()) ? 1 : ((o1.getTalkPower() > o2.getTalkPower()) ? -1 : 0)) : 0;
                }
            });
            for (final TeamSpeakUser teamspeakuser : arraylist2) {
                if (teamspeakuser.getChannelId() == teamspeakchannel3.getChannelId()) {
                    j += 10;
                    if (30 + j + TeamSpeak.scrollChannel + 5 >= TeamSpeak.ySplit || 30 + j + TeamSpeak.scrollChannel <= 20 || mouseX <= 5 || mouseX >= TeamSpeak.xSplit || mouseY <= 30 + j + TeamSpeak.scrollChannel || mouseY >= 30 + j + TeamSpeak.scrollChannel + 10) {
                        continue;
                    }
                    TeamSpeak.selectedUser = teamspeakuser.getClientId();
                    TeamSpeak.selectedChannel = -1;
                    if (mouseButton == 1) {
                        this.openBox(true, teamspeakuser.getClientId(), mouseX, mouseY);
                        return;
                    }
                    this.drag(true, teamspeakuser.getClientId());
                    if (this.lastClick + this.doubleClickDelay > System.currentTimeMillis() && this.doubleClickTarget == teamspeakuser.getClientId() && this.doubleClickIsUser) {
                        if (TeamSpeakController.getInstance().me() != null && teamspeakuser.getClientId() != TeamSpeakController.getInstance().me().getClientId()) {
                            TeamSpeak.addChat(teamspeakuser, TeamSpeakController.getInstance().me(), null, EnumTargetMode.USER);
                            TeamSpeak.selectedChat = teamspeakuser.getClientId();
                        }
                        else {
                            this.openNickNameBox();
                        }
                    }
                    this.doubleClickTarget = teamspeakuser.getClientId();
                    this.doubleClickIsUser = true;
                    this.lastClick = System.currentTimeMillis();
                }
            }
            j += 10;
        }
    }
    
    private void drawClientInfo() {
        if (TeamSpeak.selectedUser != -1) {
            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(TeamSpeak.selectedUser);
            if (teamspeakuser == null) {
                return;
            }
            int i = 30;
            final int j = TeamSpeak.xSplit + 10;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Nickname" + ModColor.cl("f") + ":", j, i);
            i += 12;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Country" + ModColor.cl("f") + ":", j, i);
            i += 12;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Talkpower" + ModColor.cl("f") + ":", j, i);
            i += 12;
            i = 30;
            this.drawInfo(teamspeakuser.getNickName(), j + 70, i);
            i += 12;
            this.drawInfo(new StringBuilder().append(TeamSpeak.country(teamspeakuser.getCountry())).toString(), j + 70, i);
            i += 12;
            this.drawInfo(new StringBuilder().append(teamspeakuser.getTalkPower()).toString(), j + 70, i);
            i += 12;
            i += 10;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Servergroups" + ModColor.cl("f") + ":", j, i);
            i += 12;
            if (teamspeakuser.getServerGroups() == null) {
                return;
            }
            for (final int k : teamspeakuser.getServerGroups()) {
                final TeamSpeakServerGroup teamspeakservergroup = TeamSpeakController.getInstance().getServerGroup(k);
                if (teamspeakservergroup != null) {
                    this.drawInfo(TeamSpeak.fix(teamspeakservergroup.getGroupName()), j, i);
                    i += 12;
                }
                else {
                    this.drawInfo(new StringBuilder().append(k).toString(), j, i);
                    i += 12;
                }
            }
            i += 10;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Channelgroups" + ModColor.cl("f") + ":", j, i);
            i += 12;
            final TeamSpeakChannelGroup teamspeakchannelgroup = TeamSpeakController.getInstance().getChannelGroup(teamspeakuser.getChannelGroupId());
            if (teamspeakchannelgroup != null) {
                this.drawInfo(TeamSpeak.fix(teamspeakchannelgroup.getGroupName()), j, i);
                i += 12;
            }
            else {
                this.drawInfo(new StringBuilder().append(teamspeakuser.getChannelGroupId()).toString(), j, i);
                i += 12;
            }
            i += 10;
            if (!teamspeakuser.hasClientInputHardware()) {
                this.drawInfo(String.valueOf(ModColor.cl("c")) + "Mic off", j, i);
                i += 12;
            }
            if (teamspeakuser.hasClientInputMuted()) {
                this.drawInfo(String.valueOf(ModColor.cl("c")) + "Mic mute", j, i);
                i += 12;
            }
            if (!teamspeakuser.hasClientOutputHardware()) {
                this.drawInfo(String.valueOf(ModColor.cl("4")) + "Sound off", j, i);
                i += 12;
            }
            if (teamspeakuser.hasClientOutputMuted()) {
                this.drawInfo(String.valueOf(ModColor.cl("4")) + "Sound mute", j, i);
                i += 12;
            }
        }
        else if (TeamSpeak.selectedChannel != -1) {
            final TeamSpeakChannel teamspeakchannel = TeamSpeakController.getInstance().getChannel(TeamSpeak.selectedChannel);
            if (teamspeakchannel == null) {
                return;
            }
            int l = 30;
            int i2 = TeamSpeak.xSplit + 10;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Channel Name" + ModColor.cl("f") + ":", i2, l);
            l += 12;
            if (teamspeakchannel != null && teamspeakchannel.getTopic() != null && !teamspeakchannel.getTopic().isEmpty()) {
                this.drawInfo(String.valueOf(ModColor.cl("7")) + "Channel Topic" + ModColor.cl("f") + ":", i2, l);
                l += 12;
            }
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Codec" + ModColor.cl("f") + ":", i2, l);
            l += 12;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Codec quality" + ModColor.cl("f") + ":", i2, l);
            l += 12;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Type" + ModColor.cl("f") + ":", i2, l);
            l += 12;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Current clients" + ModColor.cl("f") + ":", i2, l);
            l += 12;
            this.drawInfo(String.valueOf(ModColor.cl("7")) + "Sub status" + ModColor.cl("f") + ":", i2, l);
            l += 12;
            l = 30;
            i2 += 110;
            this.drawInfo(teamspeakchannel.getChannelName(), i2, l);
            l += 12;
            if (!teamspeakchannel.getTopic().isEmpty()) {
                this.drawInfo(new StringBuilder().append(teamspeakchannel.getTopic()).toString(), i2, l);
                l += 12;
            }
            this.drawInfo(new StringBuilder().append(teamspeakchannel.getChannelCodecName()).toString(), i2, l);
            l += 12;
            this.drawInfo(new StringBuilder().append(teamspeakchannel.getChannelCodecQuality()).toString(), i2, l);
            l += 12;
            this.drawInfo(TeamSpeak.status(teamspeakchannel.getIsPermanent(), "Perm ", "") + TeamSpeak.status(teamspeakchannel.getIsSemiPermanent(), "Semi perm ", "") + TeamSpeak.status(teamspeakchannel.getIsPassword(), "Password", ""), i2, l);
            l += 12;
            this.drawInfo(teamspeakchannel.getTotalClients() + "/" + new StringBuilder(String.valueOf(teamspeakchannel.getMaxClients())).toString().replace("-1", "Unlimited"), i2, l);
            l += 12;
            this.drawInfo(new StringBuilder().append(TeamSpeak.status(teamspeakchannel.getSubscription(), "Subed", "Not subed")).toString(), i2, l);
            l += 12;
        }
    }
    
    public void drawInfo(final String text, final int x, final int y) {
        if (y < TeamSpeak.ySplit - 10) {
            this.draw.drawString(text, x, y);
        }
    }
    
    public void drawBox(final int mouseX, final int mouseY) {
        if (this.boxEnabled) {
            final int i = this.boxPosX;
            final int j = this.boxPosY;
            final int k = this.boxPosX + this.boxLengthX;
            final int l = this.boxPosY + this.boxLengthY;
            final boolean flag = this.boxLengthY == 0;
            if (this.boxIsUser) {
                if (this.boxId == TeamSpeakController.getInstance().me().getClientId()) {
                    int i2 = 0;
                    this.draw.drawBox(i, j, k, l);
                    this.boxSlot("Change nickname", i, j, k, l, i2, mouseX, mouseY);
                    i2 += 15;
                    this.boxSlot("Channel commander", i, j, k, l, i2, mouseX, mouseY);
                    i2 += 15;
                    this.boxLengthX = 110;
                    this.boxLengthY = i2;
                }
                else {
                    int j2 = 0;
                    this.draw.drawBox(i, j, k, l);
                    this.boxSlot("Open chat", i, j, k, l, j2, mouseX, mouseY);
                    j2 += 15;
                    this.boxSlot("Poke", i, j, k, l, j2, mouseX, mouseY);
                    j2 += 15;
                    this.boxSlot("Move to me", i, j, k, l, j2, mouseX, mouseY);
                    j2 += 15;
                    this.boxLengthX = 145;
                    this.boxLengthY = j2;
                }
            }
            else {
                int k2 = 0;
                this.draw.drawBox(i, j, k, l);
                this.boxSlot("Switch channel", i, j, k, l, k2, mouseX, mouseY);
                k2 += 15;
                this.boxLengthX = 100;
                this.boxLengthY = k2;
            }
            if (flag) {
                final int l2 = i + this.boxLengthX;
                final int i3 = j + this.boxLengthY;
                this.draw.drawBox(i, j, l2, i3);
            }
        }
    }
    
    public void boxSlot(final String text, final int x, final int y, final int lengthX, final int lengthY, final int slot, final int mouseX, final int mouseY) {
        String s = ModColor.cl("7");
        if (mouseX > x && mouseX < x + lengthX && mouseY > y + slot && mouseY < y + slot + 15) {
            s = ModColor.cl("f");
        }
        this.draw.drawString(String.valueOf(s) + text, x + 5, y + 4 + slot);
    }
    
    public void boxSplit(final int x, final int y, final int lengthX, final int lengthY, final int slot, final int mouseX, final int mouseY) {
        final DrawUtils drawutils = this.draw;
        Gui.drawRect(x + 5, y + slot + 3, lengthX - 5, y + slot + 4, 1423232232);
    }
    
    private boolean boxClick(final int x, final int y, final int lengthX, final int lengthY, final int slot, final int mouseX, final int mouseY) {
        return mouseX > x && mouseX < x + lengthX && mouseY > y + slot && mouseY < y + slot + 15;
    }
    
    private boolean boxAction(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.boxEnabled) {
            return false;
        }
        if (mouseX <= this.boxPosX || mouseX >= this.boxPosX + this.boxLengthX || mouseY <= this.boxPosY || mouseY >= this.boxPosY + this.boxLengthY) {
            return false;
        }
        if (mouseButton != 0) {
            return true;
        }
        final int i = this.boxPosX;
        final int j = this.boxPosY;
        final int k = this.boxPosX + this.boxLengthX;
        final int l = this.boxPosY + this.boxLengthY;
        if (this.boxIsUser) {
            if (this.boxId == TeamSpeakController.getInstance().me().getClientId()) {
                int i2 = 0;
                if (this.boxClick(i, j, k, l, i2, mouseX, mouseY)) {
                    this.openNickNameBox();
                }
                i2 += 15;
                if (this.boxClick(i, j, k, l, i2, mouseX, mouseY)) {
                    TeamSpeakBridge.setChannelCommander(!TeamSpeakController.getInstance().me().isChannelCommander());
                }
                i2 += 15;
            }
            else {
                int j2 = 0;
                if (this.boxClick(i, j, k, l, j2, mouseX, mouseY)) {
                    TeamSpeak.addChat(TeamSpeakController.getInstance().getUser(this.boxId), TeamSpeakController.getInstance().me(), null, EnumTargetMode.USER);
                    TeamSpeak.selectedChat = this.boxId;
                }
                j2 += 15;
                if (this.boxClick(i, j, k, l, j2, mouseX, mouseY)) {
                    TeamSpeak.overlayWindows.openInput(TeamSpeak.selectedUser, "Poke Title", "gui_ts_window_poke_content", new PopUpCallback() {
                        @Override
                        public void ok(final int id, final String message) {
                            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(id);
                            if (teamspeakuser == null) {
                                TeamSpeak.error("Poke error offline");
                            }
                            else {
                                TeamSpeakBridge.pokeClient(teamspeakuser, message);
                            }
                        }
                        
                        @Override
                        public void ok() {
                        }
                        
                        @Override
                        public void cancel() {
                        }
                        
                        @Override
                        public boolean tick(final int cid) {
                            return false;
                        }
                    });
                }
                j2 += 15;
                if (this.boxClick(i, j, k, l, j2, mouseX, mouseY)) {
                    TeamSpeakBridge.moveClient(this.boxId, TeamSpeakController.getInstance().me().getChannelId());
                }
                j2 += 15;
            }
        }
        else {
            int k2 = 0;
            if (this.boxClick(i, j, k, l, k2, mouseX, mouseY)) {
                if (TeamSpeakController.getInstance().me() != null) {
                    TeamSpeakBridge.moveClient(TeamSpeakController.getInstance().me().getClientId(), this.boxId);
                }
                this.closeBox();
            }
            k2 += 15;
        }
        this.closeBox();
        return true;
    }
    
    private void openBox(final boolean isUser, final int id, final int x, final int y) {
        this.boxEnabled = true;
        this.boxIsUser = isUser;
        this.boxId = id;
        this.boxPosX = x;
        this.boxPosY = y;
        if (isUser) {
            TeamSpeak.selectedChannel = -1;
            TeamSpeak.selectedUser = id;
        }
        else {
            TeamSpeak.selectedUser = -1;
            TeamSpeak.selectedChannel = id;
        }
    }
    
    private void closeBox() {
        this.boxEnabled = false;
        this.boxIsUser = true;
        this.boxId = 0;
        this.boxPosX = 0;
        this.boxPosY = 0;
        this.boxLengthX = 0;
        this.boxLengthY = 0;
    }
    
    private void changeNickname() {
        this.changeNickName = false;
        if (!this.nickNameField.getText().equals(TeamSpeakController.getInstance().me().getNickName())) {
            TeamSpeakBridge.setNickname(this.nickNameField.getText());
        }
    }
    
    private void openNickNameBox() {
        this.changeNickName = true;
        this.nickNameField.setFocused(true);
        this.nickNameField.setText(TeamSpeakController.getInstance().me().getNickName());
    }
    
    private void drag(final boolean isUser, final int Id) {
        this.resetDrag();
        this.drag = true;
        this.dragIsUser = isUser;
        this.dragId = Id;
    }
    
    private void drop(final int channelId, final boolean isInRegion) {
        if (this.drag) {
            this.dropFocus = isInRegion;
            if (this.drop) {
                if (this.dragIsUser && this.dropFocus) {
                    TeamSpeakBridge.moveClient(this.dragId, channelId);
                }
                this.resetDrag();
            }
        }
    }
    
    private void resetDrag() {
        this.drag = false;
        this.drop = false;
        this.dragIsUser = false;
        this.dragId = 0;
        this.dropX = 0;
        this.dropY = 0;
        this.dragVisible = 0;
        this.dropFocus = false;
    }
    
    public void setDrop(final int x, final int y) {
        this.dropX = x;
        this.dropY = y;
        this.drop = true;
    }
    
    public void drawDrag(final int mouseX, final int mouseY) {
        if (this.drag && this.dragVisible >= 5) {
            String s = "";
            String s2 = "";
            if (this.dragIsUser) {
                final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(this.dragId);
                if (teamspeakuser == null) {
                    return;
                }
                s2 = teamspeakuser.getNickName();
            }
            else {
                final TeamSpeakChannel teamspeakchannel = TeamSpeakController.getInstance().getChannel(this.dragId);
                if (teamspeakchannel == null) {
                    return;
                }
                s2 = teamspeakchannel.getChannelName();
            }
            if (!this.dropFocus) {
                s = ModColor.cl("c");
            }
            this.draw.drawString(String.valueOf(s) + s2, mouseX, mouseY);
        }
    }
    
    public void drawMenu(final int mouseX, final int mouseY) {
        if (TeamSpeakController.getInstance().me() != null) {
            int i = 0;
            final String s = String.valueOf(TeamSpeakController.getInstance().serverIP) + ":" + TeamSpeakController.getInstance().serverPort;
            i += this.draw.getWidth() - 30 - this.draw.getStringWidth(s);
            DrawUtils drawutils = this.draw;
            Gui.drawRect(i, 5, this.draw.getWidth() - 5, 20, Integer.MIN_VALUE);
            this.draw.drawRightString(String.valueOf(ModColor.cl("a")) + s, this.draw.getWidth() - 20, 9.0);
            i -= 4;
            int j = Integer.MIN_VALUE;
            if (TeamSpeakController.getInstance().me().hasClientOutputMuted()) {
                j = 2122022291;
            }
            drawutils = this.draw;
            Gui.drawRect(i, 5, i - 16, 20, j);
            this.draw.drawString(new StringBuilder(String.valueOf(ModColor.cl("f"))).toString(), 0.0, 0.0);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("labymod/textures/teamspeak.png"));
            this.draw.drawTexturedModalRect(i - 16 + 3, 7, 12, 0, 12, 12);
            i -= 20;
            j = Integer.MIN_VALUE;
            if (TeamSpeakController.getInstance().me().hasClientInputMuted()) {
                j = 2122022291;
            }
            drawutils = this.draw;
            Gui.drawRect(i, 5, i - 16, 20, j);
            this.draw.drawString(new StringBuilder(String.valueOf(ModColor.cl("f"))).toString(), 0.0, 0.0);
            this.mc.getTextureManager().bindTexture(new ResourceLocation("labymod/textures/teamspeak.png"));
            this.draw.drawTexturedModalRect(i - 16 + 3, 7, 0, 0, 12, 12);
            i -= 20;
            j = Integer.MIN_VALUE;
            if (TeamSpeak.teamSpeakGroupPrefix) {
                j = 2122022291;
            }
            drawutils = this.draw;
            Gui.drawRect(i, 5, i - 16, 20, j);
            this.draw.drawCenteredString(String.valueOf(ModColor.cl("b")) + "[]", i - 8, 9.0);
        }
    }
    
    public void menuAction(final int mouseX, final int mouseY, final int mouseButton) {
        int i = 0;
        final String s = String.valueOf(TeamSpeakController.getInstance().serverIP) + ":" + TeamSpeakController.getInstance().serverPort;
        i += this.draw.getWidth() - 30 - this.draw.getStringWidth(s);
        i -= 4;
        if (mouseX > i - 16 && mouseX < i && mouseY > 5 && mouseY < 20) {
            TeamSpeakBridge.setOutputMuted(!TeamSpeakController.getInstance().me().hasClientOutputMuted());
        }
        i -= 20;
        if (mouseX > i - 16 && mouseX < i && mouseY > 5 && mouseY < 20) {
            TeamSpeakBridge.setInputMuted(!TeamSpeakController.getInstance().me().hasClientInputMuted());
        }
        i -= 20;
        if (mouseX > i - 16 && mouseX < i && mouseY > 5 && mouseY < 20) {
            TeamSpeak.teamSpeakGroupPrefix = !TeamSpeak.teamSpeakGroupPrefix;
        }
    }
    
    public void callBackListener(final int mouseX, final int mouseY) {
        if (TeamSpeak.callBack) {
            TeamSpeak.callBack = false;
            this.openBox(true, TeamSpeak.callBackClient, mouseX, mouseY);
        }
    }
}
