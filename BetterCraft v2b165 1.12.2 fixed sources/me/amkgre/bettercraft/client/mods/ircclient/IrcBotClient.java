// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import me.amkgre.bettercraft.client.Client;
import net.minecraft.util.StringUtils;
import java.io.IOException;
import java.net.InetAddress;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import org.jibble.pircbot.PircBot;

public class IrcBotClient extends PircBot
{
    public String prefix;
    public String freenode;
    public String mibbit;
    public int port;
    public String sender;
    public String name;
    public String currentChannel;
    public boolean showIP;
    public boolean joinLeaveMessages;
    public ConnectionStatus status;
    
    public IrcBotClient() {
        this.prefix = "§8[§5IRC§8] §7";
        this.freenode = "irc.freenode.net";
        this.mibbit = "irc.mibbit.net";
        this.port = 6687;
        this.sender = "§7%sender% §8> §r";
        this.showIP = true;
        this.joinLeaveMessages = true;
        this.currentChannel = "";
        this.status = ConnectionStatus.Offline;
        Minecraft.getMinecraft();
        this.name = Minecraft.getSession().username;
    }
    
    public void changeClientNick() {
        this.changeNick(this.name);
    }
    
    public void connectBotToNormalChannel() {
        this.connectBot(this.freenode, "#freenode");
    }
    
    public void sendChatWithoutPrefix(final String msg) {
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(msg.replace("§", "§")));
    }
    
    public String getIpByHostname(final String ip) {
        try {
            return InetAddress.getByName(ip).getHostAddress();
        }
        catch (final IOException e) {
            return "Failed: ";
        }
    }
    
    public void sendClientMessage(String message) {
        message = StringUtils.stripControlCodes(message);
        if (message.length() > 100) {
            message = message.substring(0, 100);
        }
        if (!message.trim().isEmpty()) {
            if (message.startsWith("/")) {
                Client.getInstance().ircmanager.onCommand(message);
                return;
            }
            if (this.isConnected()) {
                String msg = "";
                final String[] split = message.split(" ");
                for (int i = 0; i != split.length; ++i) {
                    msg = String.valueOf(msg) + split[i] + " ";
                }
                final String tosend = String.valueOf(this.prefix) + Minecraft.session.username + " §8> §r" + msg;
                new IrcLine(0, tosend);
                this.sendMessage(Client.getInstance().ircbot.currentChannel, message);
                this.sendChatWithoutPrefix(tosend);
            }
            else {
                new IrcLine(1, "You are not connected.");
                this.sendChatWithoutPrefix(String.valueOf(this.prefix) + "You are not connected.");
            }
        }
    }
    
    @Override
    protected void onMessage(final String channel, final String sender, final String login, String hostname, String message) {
        hostname = this.getIpByHostname(hostname);
        message = StringUtils.stripControlCodes(message);
        String msg = "";
        for (int i = 0; i != message.split(" ").length; ++i) {
            msg = String.valueOf(msg) + message.split(" ")[i] + " ";
        }
        final String ip = hostname;
        String tosend;
        if (this.showIP) {
            tosend = String.valueOf(this.prefix) + sender + " §8[§d" + this.getIpByHostname(hostname) + "§8] §8> §r" + msg;
        }
        else {
            tosend = String.valueOf(this.prefix) + sender + " §8> §r" + msg;
        }
        this.sendChatWithoutPrefix(tosend);
        new IrcLine(0, tosend);
    }
    
    @Override
    protected void onJoin(final String channel, final String sender, final String login, final String hostname) {
        if (this.joinLeaveMessages) {
            if (this.showIP) {
                new IrcLine(5, String.valueOf(this.prefix) + " §8[§a+§8] §8[§d" + this.getIpByHostname(hostname) + "§8] §7" + sender);
                this.sendChatWithoutPrefix(String.valueOf(this.prefix) + " §8[§a+§8] §8[§d" + this.getIpByHostname(hostname) + "§8] §7" + sender);
            }
            else {
                new IrcLine(5, String.valueOf(this.prefix) + " §8[§a+§8] §7" + sender);
                this.sendChatWithoutPrefix(String.valueOf(this.prefix) + " §8[§a+§8] §7" + sender);
            }
        }
    }
    
    @Override
    protected void onQuit(final String sourceNick, final String sourceLogin, final String sourceHostname, final String reason) {
        if (this.joinLeaveMessages) {
            if (this.showIP) {
                new IrcLine(5, String.valueOf(this.prefix) + " §8[§c-§8] " + "§8[§d" + this.getIpByHostname(sourceHostname) + "§8]" + " §7" + sourceNick);
                this.sendChatWithoutPrefix(String.valueOf(this.prefix) + " §8[§c-§8] " + "§8[§d" + this.getIpByHostname(sourceHostname) + "§8]" + " §7" + sourceNick);
            }
            else {
                new IrcLine(5, String.valueOf(this.prefix) + " §8[§c-§8] §7" + sourceNick);
                this.sendChatWithoutPrefix(String.valueOf(this.prefix) + " §8[§c-§8] §7" + sourceNick);
            }
        }
    }
    
    @Override
    protected void onKick(final String channel, final String kickerNick, final String kickerLogin, final String kickerHostname, final String recipientNick, final String reason) {
        if (kickerNick.equalsIgnoreCase(this.name)) {
            this.status = ConnectionStatus.Offline;
            new IrcLine(1, "You got kicked from the §dIRC.");
            this.sendChatWithoutPrefix(String.valueOf(this.prefix) + " You got kicked from the §dIRC.");
        }
        super.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
    }
    
    @Override
    protected void onConnect() {
    }
    
    public void connectBot(final String server, final String channel) {
        this.currentChannel = channel;
        new Thread(new Runnable() {
            @Override
            public void run() {
                IrcBotClient.this.status = ConnectionStatus.Connecting;
                try {
                    PircBot.this.setName(IrcBotClient.this.name);
                    final IrcBotClient this$0 = IrcBotClient.this;
                    Client.getInstance();
                    this$0.setLogin(Client.clientPlayer);
                    final IrcBotClient this$2 = IrcBotClient.this;
                    Client.getInstance();
                    final StringBuilder append = new StringBuilder(String.valueOf(Client.clientName)).append(" ");
                    Client.getInstance();
                    final StringBuilder append2 = append.append(Client.clientVersion).append(" by ");
                    Client.getInstance();
                    this$2.setVersion(append2.append(Client.clientAuthor).toString());
                    IrcBotClient.this.connect(server);
                    IrcBotClient.this.joinChannel(IrcBotClient.this.currentChannel);
                    IrcBotClient.this.status = ConnectionStatus.Online;
                }
                catch (final NickAlreadyInUseException var2) {
                    IrcBotClient.this.sendChatWithoutPrefix(String.valueOf(IrcBotClient.this.prefix) + "There is already someone with the name! §dUse /nick");
                    IrcBotClient.this.status = ConnectionStatus.Offline;
                }
                catch (final IrcException | IOException var3) {
                    IrcBotClient.this.sendChatWithoutPrefix(String.valueOf(IrcBotClient.this.prefix) + "Failed to connect with the §dIRC Channel.");
                    IrcBotClient.this.status = ConnectionStatus.Offline;
                }
            }
        }).start();
    }
    
    @Override
    protected void onDisconnect() {
        this.status = ConnectionStatus.Offline;
        super.onDisconnect();
    }
    
    public void disconnectBot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IrcBotClient.this.disconnect();
            }
        }).start();
    }
    
    public enum ConnectionStatus
    {
        Connecting("Connecting", 0, "Connecting"), 
        Online("Online", 1, "Online"), 
        Offline("Offline", 2, "Offline");
        
        String s;
        
        private ConnectionStatus(final String s2, final int n, final String s) {
            this.s = s;
        }
        
        public String getText() {
            return this.s;
        }
    }
}
