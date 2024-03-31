// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient;

import java.io.IOException;
import java.util.Iterator;
import org.jibble.pircbot.User;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.Client;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiIRC extends GuiScreen
{
    int max;
    int w;
    int speed;
    int curren;
    private GuiScreen oldGui;
    private GuiTextField input;
    private GuiButton loginButton;
    private GuiButton joinMsgButton;
    private GuiButton ipButton;
    
    public GuiIRC(final GuiScreen oldGui) {
        this.max = 100;
        this.w = 2;
        this.speed = 10;
        this.curren = 0;
        this.oldGui = oldGui;
    }
    
    @Override
    public void initGui() {
        if (!Client.getInstance().ircbot.isConnected()) {
            IrcLine.lines.clear();
            new IrcLine(1, String.valueOf(Client.getInstance().ircbot.prefix) + " " + "You are currently not connected to a channel!");
            this.sendChatWithoutPrefix(String.valueOf(Client.getInstance().ircbot.prefix) + " " + "You are currently not connected to a channel!");
        }
        this.max = 103;
        this.w = 1;
        this.speed = 10;
        this.curren = 0;
        this.loginButton = new GuiButton(2, 5, GuiIRC.height - 44 - 5 + 4, 100, 20, Client.getInstance().ircbot.isConnected() ? "§cLogout" : "§aLogin");
        this.buttonList.add(this.loginButton);
        this.joinMsgButton = new GuiButton(4, 113, GuiIRC.height - 44 - 5 - 42, 100, 20, Client.getInstance().ircbot.joinLeaveMessages ? "§aJoin Messages" : "§cJoin Messages");
        this.buttonList.add(this.joinMsgButton);
        this.ipButton = new GuiButton(6, 113, GuiIRC.height - 44 - 5 - 88, 100, 20, Client.getInstance().ircbot.showIP ? "§aShow IP's" : "§cShow IP's");
        this.buttonList.add(this.ipButton);
        this.buttonList.add(new GuiButton(5, 113, GuiIRC.height - 44 - 5 - 65, 100, 20, "EaZy IRC"));
        this.buttonList.add(new GuiButton(3, 113, GuiIRC.height - 44 - 5 - 19, 100, 20, "Clear Chat"));
        this.buttonList.add(new GuiButton(1, 113, GuiIRC.height - 44 - 5 + 27, 100, 20, "Back"));
        (this.input = new GuiTextField(0, this.fontRendererObj, 1, GuiIRC.height - 12, GuiIRC.width - 4, 12)).setFocused(true);
        this.input.setCanLoseFocus(false);
        this.input.setMaxStringLength(100);
        super.initGui();
    }
    
    @Override
    public void updateScreen() {
        this.loginButton.setDisplayString(Client.getInstance().ircbot.isConnected() ? "Disconnect" : "Login");
        this.joinMsgButton.setDisplayString(Client.getInstance().ircbot.joinLeaveMessages ? "§aJoinLeaveMsg" : "§cJoinLeaveMsg");
        this.ipButton.setDisplayString(Client.getInstance().ircbot.showIP ? "§aShow IP's" : "§cShow IP's");
        this.input.updateCursorCounter();
        if (IrcLine.lines.size() > 100) {
            IrcLine.lines.remove(0);
        }
        super.updateScreen();
    }
    
    private void sendChatWithoutPrefix(final String msg) {
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(msg.replace("§", "§")));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fr = this.mc.fontRendererObj;
        this.drawDefaultBackground();
        final ScaledResolution res = new ScaledResolution(this.mc);
        final int y2 = 1;
        for (int i = 0; i < IrcLine.lines.size(); ++i) {
            final IrcLine l = IrcLine.lines.get(IrcLine.lines.size() - i - 1);
            switch (l.ausrichtung) {
                case 0: {
                    this.mc.fontRendererObj.drawStringWithShadow(l.message, 2.0f, (float)(GuiIRC.height - 24 - i * 10), -1);
                    break;
                }
                case 1: {
                    this.mc.fontRendererObj.drawStringWithShadow(l.message, 2.0f, (float)(GuiIRC.height - 24 - i * 10), -1);
                    break;
                }
                case 2: {
                    this.mc.fontRendererObj.drawStringWithShadow(l.message, 2.0f, (float)(GuiIRC.height - 24 - i * 10), -1);
                    break;
                }
                case 5: {
                    this.mc.fontRendererObj.drawStringWithShadow(l.message, 2.0f, (float)(GuiIRC.height - 24 - i * 10), -1);
                    break;
                }
            }
        }
        this.input.drawTextBox();
        Gui.drawRect(GuiIRC.width - this.curren - this.w, 0, GuiIRC.width, GuiIRC.height, Integer.MIN_VALUE);
        int y3 = 1;
        User[] users;
        for (int length = (users = Client.getInstance().ircbot.getUsers(Client.getInstance().ircbot.currentChannel)).length, j = 0; j < length; ++j) {
            final User user = users[j];
            this.mc.fontRendererObj.drawStringWithShadow(String.valueOf(user.isOp() ? "§5" : "§d") + user.getNick(), (float)(GuiIRC.width - this.curren - this.w + 2), (float)y3, -1);
            y3 += 10;
        }
        for (final GuiButton b : this.buttonList) {
            if (b.id < 100) {
                b.setxPosition(GuiIRC.width - this.curren + 1);
            }
        }
        if (mouseX >= GuiIRC.width - this.max) {
            if (this.curren < this.max) {
                this.curren += this.speed;
            }
            if (this.curren > this.max) {
                --this.curren;
            }
        }
        else {
            if (this.curren > 0) {
                this.curren -= this.speed;
            }
            if (this.curren < 0) {
                ++this.curren;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.oldGui);
                break;
            }
            case 2: {
                if (Client.getInstance().ircbot.isConnected()) {
                    Client.getInstance().ircbot.disconnectBot();
                    this.mc.displayGuiScreen(this);
                    break;
                }
                Client.getInstance().ircbot.connectBotToNormalChannel();
                this.mc.displayGuiScreen(this);
                break;
            }
            case 3: {
                IrcLine.lines.clear();
                break;
            }
            case 4: {
                Client.getInstance().ircbot.joinLeaveMessages = !Client.getInstance().ircbot.joinLeaveMessages;
                break;
            }
            case 5: {
                IrcLine.lines.clear();
                this.sendMessage("You are currently connected to #eazyrofl.");
                Client.getInstance().ircbot.connectBot(Client.getInstance().ircbot.freenode, "#eazyrofl");
                break;
            }
            case 6: {
                Client.getInstance().ircbot.showIP = !Client.getInstance().ircbot.showIP;
                break;
            }
        }
    }
    
    public void sendMessage(final String msg) {
        new IrcLine(1, String.valueOf(Client.getInstance().ircbot.prefix) + " " + msg);
        this.sendChatWithoutPrefix(String.valueOf(Client.getInstance().ircbot.prefix) + " " + msg);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.oldGui);
        }
        if (keyCode == 28 && !this.input.getText().trim().isEmpty() && this.input.isFocused()) {
            Client.getInstance().ircbot.sendClientMessage(this.input.getText().trim());
            this.input.setText("");
        }
        this.input.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.input.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
