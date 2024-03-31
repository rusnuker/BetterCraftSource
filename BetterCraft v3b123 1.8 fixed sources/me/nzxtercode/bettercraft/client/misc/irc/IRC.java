// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.irc;

import com.google.gson.JsonElement;
import org.jibble.pircbot.User;
import com.google.gson.JsonArray;
import java.util.Objects;
import java.util.stream.StreamSupport;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;
import com.google.gson.JsonParser;
import net.minecraft.util.StringUtils;
import java.util.AbstractMap;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import java.io.IOException;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticInstance;
import net.minecraft.client.Minecraft;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import com.google.common.collect.Lists;
import me.nzxtercode.bettercraft.client.BetterCraft;
import java.util.Map;
import java.util.function.Consumer;
import java.util.List;
import org.jibble.pircbot.PircBot;

public class IRC extends PircBot
{
    private static IRC INSTANCE;
    public String prefix;
    public String server;
    public int port;
    public ConnectionStatus status;
    public String currentChannel;
    public String sender;
    private List<Consumer<Map.Entry<String, String>>> messageListeners;
    private List<Map.Entry<String, String>> messageList;
    private int unreadMessages;
    
    static {
        IRC.INSTANCE = new IRC();
    }
    
    public IRC() {
        BetterCraft.getInstance();
        this.prefix = String.valueOf(BetterCraft.clientPrefix) + "§8[§fIRC§8] §7";
        this.server = "irc.libera.chat";
        this.port = 6697;
        this.sender = "§7%sender% §8> §r";
        this.messageListeners = (List<Consumer<Map.Entry<String, String>>>)Lists.newArrayList();
        this.messageList = (List<Map.Entry<String, String>>)Lists.newArrayList();
        this.unreadMessages = 0;
    }
    
    public static final IRC getInstance() {
        return IRC.INSTANCE;
    }
    
    public final boolean isUserConnected(final String nick) {
        return Arrays.asList(getInstance().getUsers(getInstance().currentChannel)).stream().map(user -> user.getNick()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).contains(nick);
    }
    
    public List<Consumer<Map.Entry<String, String>>> getMessageListeners() {
        return this.messageListeners;
    }
    
    public List<Map.Entry<String, String>> getMessageList() {
        return this.messageList;
    }
    
    public int getUnreadMessages() {
        return this.unreadMessages;
    }
    
    public void setUnreadMessages(final int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }
    
    public void init() {
        final String server = this.server;
        final StringBuilder sb = new StringBuilder("#");
        BetterCraft.getInstance();
        this.connectBot(server, sb.append(BetterCraft.clientName).toString());
        this.messageListeners.clear();
        this.messageListeners.add(messageEntry -> {
            this.messageList.add(messageEntry);
            ++this.unreadMessages;
        });
    }
    
    public void connectBot(final String server, final String channel) {
        this.currentChannel = channel;
        new Thread(new Runnable() {
            @Override
            public void run() {
                IRC.this.status = ConnectionStatus.Connecting;
                try {
                    PircBot.this.setLogin("~" + String.valueOf(Minecraft.getMinecraft().getSession().getProfile().getId()));
                    PircBot.this.setName(String.valueOf(Minecraft.getMinecraft().getSession().getUsername()));
                    final IRC this$0 = IRC.this;
                    BetterCraft.getInstance();
                    final StringBuilder append = new StringBuilder(String.valueOf(BetterCraft.clientName)).append(" ");
                    BetterCraft.getInstance();
                    final StringBuilder append2 = append.append(BetterCraft.clientVersion).append(" by ");
                    BetterCraft.getInstance();
                    this$0.setVersion(append2.append(BetterCraft.clientAuthor).append(" Client.getInstance().clientDiscord").toString());
                    IRC.this.connect(server);
                    IRC.this.joinChannel(channel);
                    CosmeticInstance.sendCosmetics();
                    IRC.this.status = ConnectionStatus.Online;
                    System.out.println("Online IRC");
                }
                catch (final NickAlreadyInUseException var2) {
                    IRC.this.status = ConnectionStatus.Offline;
                    System.out.println("Offline Nick");
                }
                catch (final IrcException | IOException var3) {
                    IRC.this.status = ConnectionStatus.Offline;
                    System.out.println("Offline IRC");
                }
            }
        }).start();
    }
    
    public void disconnectBot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IRC.this.disconnect();
            }
        }).start();
    }
    
    @Override
    protected void onDisconnect() {
        this.status = ConnectionStatus.Offline;
        super.onDisconnect();
    }
    
    public void sendChatWithoutPrefix(final String msg) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(msg.replace("§", "§")));
    }
    
    @Override
    protected void onJoin(final String channel, final String sender, final String login, final String hostname) {
        CosmeticInstance.sendCosmetics();
    }
    
    public void sendClientMessage(final String inputMessage) {
        if (this.isConnected()) {
            this.messageListeners.forEach(listener -> listener.accept(new AbstractMap.SimpleEntry(this.getName(), s)));
            final String message = StringUtils.stripControlCodes(inputMessage);
            this.sendMessage(this.currentChannel, message);
        }
    }
    
    @Override
    protected void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        if (message.startsWith("$Cosmetics")) {
            try {
                final JsonArray array = new JsonParser().parse(message.replace("$Cosmetics ", "")).getAsJsonArray();
                if (CosmeticInstance.USER_COSMETICS.containsKey(sender)) {
                    CosmeticInstance.USER_COSMETICS.remove(sender);
                }
                CosmeticInstance.USER_COSMETICS.put(sender, StreamSupport.stream(array.spliterator(), false).map(element -> element.getAsInt()).map(id -> CosmeticInstance.getCosmetics().stream().filter(cosmetic -> Objects.equals(cosmetic.getId(), n)).findFirst().get()).collect((Collector<? super Object, ?, List<CosmeticBase>>)Collectors.toList()));
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
        else {
            this.messageListeners.forEach(listener -> listener.accept(new AbstractMap.SimpleEntry(s, s2)));
        }
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
