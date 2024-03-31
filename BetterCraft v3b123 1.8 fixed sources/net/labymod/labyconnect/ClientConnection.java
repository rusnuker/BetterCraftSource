// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect;

import io.netty.bootstrap.AbstractBootstrap;
import java.io.IOException;
import io.netty.channel.ChannelHandlerContext;
import net.labymod.main.Updater;
import com.google.gson.JsonParser;
import net.labymod.user.group.LabyGroup;
import net.labymod.user.User;
import net.labymod.user.FamiliarManager;
import net.labymod.labyconnect.packets.PacketUserBadge;
import net.labymod.user.UserManager;
import net.labymod.labyconnect.packets.PacketUpdateCosmetics;
import net.labymod.labyconnect.packets.PacketMojangStatus;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.labymod.labyconnect.packets.PacketEncryptionResponse;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import java.net.Proxy;
import java.math.BigInteger;
import net.labymod.labyconnect.packets.CryptManager;
import net.labymod.labyconnect.packets.PacketEncryptionRequest;
import net.labymod.labyconnect.packets.PacketLoginTime;
import net.labymod.labyconnect.packets.PacketPlayChangeOptions;
import net.labymod.labyconnect.packets.PacketPlayFriendPlayingOn;
import net.labymod.labyconnect.packets.PacketPlayFriendStatus;
import net.labymod.labyconnect.packets.PacketPlayServerStatusUpdate;
import net.labymod.labyconnect.packets.PacketPlayServerStatus;
import net.labymod.labyconnect.packets.PacketPlayFriendRemove;
import net.labymod.labyconnect.packets.PacketPlayRequestRemove;
import net.labymod.labyconnect.gui.GuiFriendsAddFriend;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriendResponse;
import net.labymod.labyconnect.packets.PacketPlayTyping;
import net.labymod.labyconnect.log.MessageChatComponent;
import net.labymod.labyconnect.log.SingleChat;
import net.labymod.labyconnect.packets.PacketMessage;
import net.labymod.labyconnect.packets.PacketServerMessage;
import net.labymod.labyconnect.packets.PacketPong;
import net.labymod.labyconnect.packets.PacketPing;
import net.labymod.labyconnect.packets.PacketBanned;
import net.minecraft.client.Minecraft;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.labyconnect.packets.PacketPlayDenyFriendRequest;
import net.labymod.labyconnect.user.ChatRequest;
import net.labymod.labyconnect.packets.PacketLoginRequest;
import java.util.Collection;
import net.labymod.labyconnect.packets.PacketLoginFriend;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriend;
import java.util.concurrent.TimeUnit;
import net.labymod.labyconnect.packets.PacketKick;
import net.labymod.labyconnect.packets.PacketChatVisibilityChange;
import net.labymod.labyconnect.packets.PacketLoginComplete;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyconnect.packets.PacketPlayPlayerOnline;
import java.util.Iterator;
import com.google.gson.JsonElement;
import net.labymod.api.LabyModAddon;
import net.labymod.addon.AddonLoader;
import com.google.gson.JsonArray;
import net.labymod.labyconnect.packets.PacketLoginVersion;
import net.labymod.main.Source;
import net.labymod.labyconnect.packets.PacketLoginOptions;
import net.labymod.labyconnect.packets.PacketHelloPong;
import net.labymod.labyconnect.packets.PacketLoginData;
import net.labymod.labyconnect.packets.PacketAddonMessage;
import com.google.gson.JsonObject;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.labymod.labyconnect.packets.PacketDisconnect;
import net.labymod.main.lang.LanguageManager;
import java.nio.channels.UnresolvedAddressException;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketHelloPing;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import net.labymod.utils.ModColor;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.labymod.utils.Consumer;
import net.labymod.labyconnect.packets.EnumConnectionState;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.ChannelHandler;
import net.labymod.labyconnect.handling.PacketHandler;

@ChannelHandler.Sharable
public class ClientConnection extends PacketHandler
{
    private NioEventLoopGroup nioEventLoopGroup;
    private ExecutorService executorService;
    private NioSocketChannel nioSocketChannel;
    private Bootstrap bootstrap;
    private EnumConnectionState currentConnectionState;
    private LabyConnect chatClient;
    private String lastKickMessage;
    private String customIp;
    private int customPort;
    private boolean pinAvailable;
    private Consumer<String> pinResponseConsumer;
    
    public ClientConnection(final LabyConnect chatClient) {
        this.nioEventLoopGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Chat#%d").build());
        this.executorService = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("Helper#%d").build());
        this.currentConnectionState = EnumConnectionState.OFFLINE;
        this.lastKickMessage = "Unknown";
        this.customIp = null;
        this.customPort = -1;
        this.pinAvailable = false;
        this.pinResponseConsumer = null;
        this.chatClient = chatClient;
        if (Debug.isActive()) {
            LabyMod.getInstance().getEventManager().register(new MessageSendEvent() {
                @Override
                public boolean onSend(final String message) {
                    if (!message.toLowerCase().startsWith("/labymodchatdebug")) {
                        return false;
                    }
                    if (!message.toLowerCase().contains(" ")) {
                        LabyMod.getInstance().getLabyModAPI().displayMessageInChat(String.valueOf(ModColor.cl("c")) + "/labymodchatdebug ip:port");
                        return false;
                    }
                    try {
                        String ip = message.toLowerCase().split(" ")[1];
                        int port = 25565;
                        if (ip.contains(":")) {
                            final String[] split = ip.split(":");
                            ip = split[0];
                            port = Integer.parseInt(split[1]);
                        }
                        LabyMod.getInstance().getLabyModAPI().displayMessageInChat(String.valueOf(ModColor.cl("a")) + "Connecting to " + ModColor.cl("f") + ip + ModColor.cl("a") + " on port " + ModColor.cl("f") + port);
                        ClientConnection.access$0(ClientConnection.this, ip);
                        ClientConnection.access$1(ClientConnection.this, port);
                        ClientConnection.this.connect();
                    }
                    catch (final Exception error) {
                        LabyMod.getInstance().getLabyModAPI().displayMessageInChat(String.valueOf(ModColor.cl("c")) + "Error: " + error.getMessage());
                    }
                    return true;
                }
            });
        }
    }
    
    public void connect() {
        String defaultIp = "mod.labymod.net";
        int defaultPort = 30336;
        final String customPort = System.getProperty("customChatPort");
        if (customPort != null) {
            defaultPort = Integer.parseInt(customPort);
        }
        if (this.customIp != null) {
            defaultIp = this.customIp;
        }
        if (this.customPort != -1) {
            defaultPort = this.customPort;
        }
        this.connect(defaultIp, defaultPort);
    }
    
    public void connect(final String ip, final int port) {
        if (this.nioSocketChannel != null && this.nioSocketChannel.isOpen()) {
            this.nioSocketChannel.close();
            this.nioSocketChannel = null;
        }
        this.chatClient.setForcedLogout(false);
        this.chatClient.getChatlogManager().loadChatlogs(LabyMod.getInstance().getPlayerUUID());
        this.updateConnectionState(EnumConnectionState.HELLO);
        (this.bootstrap = new Bootstrap()).group(this.nioEventLoopGroup);
        ((AbstractBootstrap<AbstractBootstrap, Channel>)this.bootstrap).option(ChannelOption.TCP_NODELAY, true);
        ((AbstractBootstrap<AbstractBootstrap, Channel>)this.bootstrap).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.handler(new ClientChannelInitializer(this));
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Connecting to " + ip + ":" + port);
                    ClientConnection.this.bootstrap.connect(ip, port).syncUninterruptibly();
                    ClientConnection.this.sendPacket(new PacketHelloPing(System.currentTimeMillis()));
                }
                catch (final UnresolvedAddressException error) {
                    ClientConnection.this.updateConnectionState(EnumConnectionState.OFFLINE);
                    ClientConnection.access$3(ClientConnection.this, (error.getMessage() == null) ? "Unknown error" : error.getMessage());
                    Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "UnresolvedAddressException: " + error.getMessage());
                    error.printStackTrace();
                }
                catch (final Throwable throwable) {
                    ClientConnection.this.updateConnectionState(EnumConnectionState.OFFLINE);
                    ClientConnection.access$3(ClientConnection.this, (throwable.getMessage() == null) ? "Unknown error" : throwable.getMessage());
                    Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Throwable: " + throwable.getMessage());
                    throwable.printStackTrace();
                    if (ClientConnection.this.lastKickMessage.contains("no further information") || throwable.getMessage() == null) {
                        ClientConnection.access$3(ClientConnection.this, LanguageManager.translate("chat_not_reachable"));
                    }
                }
            }
        });
    }
    
    public void disconnect(final boolean kicked) {
        if (this.currentConnectionState == EnumConnectionState.OFFLINE) {
            return;
        }
        this.updateConnectionState(EnumConnectionState.OFFLINE);
        LabyMod.getInstance().getUserManager().getFamiliarManager().clear();
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                ClientConnection.this.chatClient.getChatlogManager().saveChatlogs(LabyMod.getInstance().getPlayerUUID());
                if (ClientConnection.this.nioSocketChannel != null && !kicked) {
                    ClientConnection.this.nioSocketChannel.writeAndFlush(new PacketDisconnect("Logout")).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture arg0) throws Exception {
                            if (ClientConnection.this.nioSocketChannel != null) {
                                ClientConnection.this.nioSocketChannel.close();
                            }
                        }
                    });
                }
            }
        });
    }
    
    public void updateConnectionState(final EnumConnectionState connectionState) {
        this.currentConnectionState = connectionState;
    }
    
    public void requestPin(final Consumer<String> consumer) {
        this.pinResponseConsumer = consumer;
        this.sendPacket(new PacketAddonMessage("auth_pin", new JsonObject().toString()));
    }
    
    @Override
    public void handle(final PacketLoginData packet) {
    }
    
    @Override
    public void handle(final PacketHelloPing packet) {
    }
    
    @Override
    public void handle(final PacketHelloPong packet) {
        this.updateConnectionState(EnumConnectionState.LOGIN);
        this.sendPacket(new PacketLoginData(LabyMod.getInstance().getPlayerUUID(), LabyMod.getInstance().getPlayerName(), LabyMod.getSettings().motd));
        this.sendPacket(new PacketLoginOptions(LabyMod.getSettings().showConnectedIp, this.chatClient.getClientProfile().getUserStatus(), this.chatClient.getClientProfile().getTimeZone()));
        this.sendPacket(new PacketLoginVersion(23, String.valueOf(Source.ABOUT_MC_VERSION) + "_" + "3.6.6"));
        final JsonArray addons = new JsonArray();
        for (final LabyModAddon addonInfo : AddonLoader.getAddons()) {
            if (addonInfo.about != null) {
                if (addonInfo.about.uuid == null) {
                    continue;
                }
                final JsonObject entry = new JsonObject();
                entry.addProperty("uuid", addonInfo.about.uuid.toString());
                entry.addProperty("name", addonInfo.about.name);
                addons.add(entry);
            }
        }
        final JsonObject obj = new JsonObject();
        obj.add("addons", addons);
        this.sendPacket(new PacketAddonMessage("labymod_addons", obj.toString()));
        this.chatClient.getFriends().clear();
        this.chatClient.getRequests().clear();
    }
    
    @Override
    public void handle(final PacketPlayPlayerOnline packet) {
        final ChatUser chatUser = this.chatClient.getChatUserByUUID(packet.getPlayer().getGameProfile().getId());
        chatUser.setStatus(packet.getPlayer().getStatus());
        chatUser.setStatusMessage(packet.getPlayer().getStatusMessage());
        if (LabyMod.getSettings().alertsOnlineStatus) {
            if (packet.getPlayer().isOnline()) {
                LabyMod.getInstance().notifyMessageProfile(packet.getPlayer().getGameProfile(), String.valueOf(ModColor.cl("a")) + LanguageManager.translate("chat_user_now_online"));
            }
            else {
                LabyMod.getInstance().notifyMessageProfile(packet.getPlayer().getGameProfile(), String.valueOf(ModColor.cl("c")) + LanguageManager.translate("chat_user_now_offline"));
            }
        }
        this.chatClient.sortFriendList(LabyMod.getSettings().friendSortType);
    }
    
    @Override
    public void handle(final PacketLoginComplete packet) {
        this.updateConnectionState(EnumConnectionState.PLAY);
        this.pinAvailable = !packet.getDashboardPin().isEmpty();
        LabyMod.getInstance().getUserManager().getFamiliarManager().refresh();
    }
    
    @Override
    public void handle(final PacketChatVisibilityChange packet) {
    }
    
    @Override
    public void handle(final PacketKick packet) {
        this.disconnect(true);
        this.lastKickMessage = ((packet.getReason() == null) ? LanguageManager.translate("chat_unknown_kick_reason") : packet.getReason());
        LabyMod.getInstance().notifyMessageRaw(LanguageManager.translate("chat_disconnected_title"), this.lastKickMessage);
    }
    
    @Override
    public void handle(final PacketDisconnect packet) {
        this.disconnect(true);
        this.lastKickMessage = ((packet.getReason() == null) ? LanguageManager.translate("chat_unknown_disconnect_reason") : packet.getReason());
        LabyMod.getInstance().notifyMessageRaw(LanguageManager.translate("chat_disconnected_title"), this.lastKickMessage);
        Executors.newSingleThreadScheduledExecutor().schedule(() -> this.connect(), 5L, TimeUnit.SECONDS);
    }
    
    @Override
    public void handle(final PacketPlayRequestAddFriend packet) {
    }
    
    @Override
    public void handle(final PacketLoginFriend packet) {
        this.chatClient.getFriends().addAll(packet.getFriends());
        this.chatClient.sortFriendList(LabyMod.getSettings().friendSortType);
    }
    
    @Override
    public void handle(final PacketLoginRequest packet) {
        if (LabyMod.getSettings().ignoreRequests) {
            for (final ChatRequest chatRequest : packet.getRequests()) {
                this.sendPacket(new PacketPlayDenyFriendRequest(chatRequest));
            }
        }
        else {
            this.chatClient.getRequests().addAll(packet.getRequests());
            for (final ChatRequest chatRequest : this.chatClient.getRequests()) {
                LabyMod.getInstance().notifyMessageProfile(chatRequest.getGameProfile(), String.valueOf(ModColor.cl("f")) + LanguageManager.translate("chat_user_friend_request"));
            }
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiFriendsLayout) {
            ((GuiFriendsLayout)Minecraft.getMinecraft().currentScreen).getChatElementMyProfile().updateButtons();
        }
    }
    
    @Override
    public void handle(final PacketBanned packet) {
        this.disconnect(true);
        Minecraft.getMinecraft().shutdown();
        this.lastKickMessage = ((packet.getReason() == null) ? LanguageManager.translate("chat_unknown_ban_reason") : packet.getReason());
        LabyMod.getInstance().notifyMessageRaw(LanguageManager.translate("chat_disconnected_title"), this.lastKickMessage);
    }
    
    @Override
    public void handle(final PacketPing packet) {
        this.sendPacket(new PacketPong());
    }
    
    @Override
    public void handle(final PacketPong packet) {
    }
    
    @Override
    public void handle(final PacketServerMessage packet) {
        LabyMod.getInstance().notifyMessageRaw(LanguageManager.translate("chat_server_message_title"), packet.getMessage());
    }
    
    @Override
    public void handle(final PacketMessage packet) {
        final ChatUser chatUser = this.chatClient.getChatUserByUUID(packet.getSender().getGameProfile().getId());
        if (chatUser != null) {
            chatUser.setLastTyping(0L);
            final SingleChat singleChat = this.chatClient.getChatlogManager().getChat(chatUser);
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    singleChat.addMessage(new MessageChatComponent(packet.getSender().getGameProfile().getName(), System.currentTimeMillis(), packet.getMessage()));
                }
            });
            LabyMod.getInstance().notifyMessageProfile(packet.getSender().getGameProfile(), packet.getMessage());
        }
    }
    
    @Override
    public void handle(final PacketPlayTyping packet) {
        final ChatUser chatUser = this.chatClient.getChatUserByUUID(packet.getPlayer().getGameProfile().getId());
        if (chatUser != null) {
            chatUser.setLastTyping(System.currentTimeMillis());
        }
    }
    
    @Override
    public void handle(final PacketPlayRequestAddFriendResponse packet) {
        GuiFriendsAddFriend.response = (packet.isRequestSent() ? "true" : packet.getReason());
    }
    
    @Override
    public void handle(final PacketPlayRequestRemove packet) {
        final Iterator<ChatRequest> iterator = this.chatClient.getRequests().iterator();
        while (iterator.hasNext()) {
            final ChatRequest next = iterator.next();
            if (next.getGameProfile().getName().equalsIgnoreCase(packet.getPlayerName())) {
                iterator.remove();
            }
        }
    }
    
    @Override
    public void handle(final PacketPlayDenyFriendRequest packet) {
    }
    
    @Override
    public void handle(final PacketPlayFriendRemove packet) {
        final Iterator<ChatUser> iterator = this.chatClient.getFriends().iterator();
        while (iterator.hasNext()) {
            final ChatUser next = iterator.next();
            if (next.equals(packet.getToRemove())) {
                iterator.remove();
                if (GuiFriendsLayout.selectedUser == null) {
                    continue;
                }
                if (!GuiFriendsLayout.selectedUser.equals(next)) {
                    continue;
                }
                GuiFriendsLayout.selectedUser = null;
            }
        }
        this.chatClient.sortFriendList(LabyMod.getSettings().friendSortType);
        GameIconHelper.updateIcon(true, false);
    }
    
    @Override
    public void handle(final PacketLoginOptions packet) {
    }
    
    @Override
    public void handle(final PacketPlayServerStatus packet) {
    }
    
    @Override
    public void handle(final PacketPlayServerStatusUpdate packet) {
    }
    
    @Override
    public void handle(final PacketPlayFriendStatus packet) {
        final ChatUser chatUser = this.chatClient.getChatUser(packet.getPlayer());
        chatUser.setCurrentServerInfo(packet.getPlayerInfo());
    }
    
    @Override
    public void handle(final PacketPlayFriendPlayingOn packet) {
        if (!LabyMod.getSettings().alertsPlayingOn) {
            return;
        }
        if (packet.getGameModeName() == null || packet.getGameModeName().isEmpty()) {
            return;
        }
        String message = null;
        if (packet.getGameModeName().contains(".")) {
            message = LanguageManager.translate("chat_user_now_playing_on", packet.getGameModeName());
        }
        else {
            message = LanguageManager.translate("chat_user_now_playing", packet.getGameModeName());
        }
        LabyMod.getInstance().notifyMessageProfile(packet.getPlayer().getGameProfile(), message);
    }
    
    @Override
    public void handle(final PacketPlayChangeOptions packet) {
    }
    
    @Override
    public void handle(final PacketLoginTime packet) {
        this.chatClient.getClientProfile().setFirstJoined(packet.getDateJoined());
    }
    
    @Override
    public void handle(final PacketLoginVersion packet) {
    }
    
    @Override
    public void handle(final PacketEncryptionRequest encryptionRequest) {
        final SecretKey secretKey = CryptManager.createNewSharedKey();
        final PublicKey publicKey = CryptManager.decodePublicKey(encryptionRequest.getPublicKey());
        final String serverId = encryptionRequest.getServerId();
        final String token = "";
        final String hash = new BigInteger(CryptManager.getServerIdHash(serverId, publicKey, secretKey)).toString(16);
        final UUID uuid = Minecraft.getMinecraft().getSession().getProfile().getId();
        if (uuid == null) {
            this.lastKickMessage = LanguageManager.translate("chat_invalid_session");
            Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, this.lastKickMessage);
            this.disconnect(false);
            return;
        }
        try {
            final MinecraftSessionService minecraftSessionService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()).createMinecraftSessionService();
            minecraftSessionService.joinServer(Minecraft.getMinecraft().getSession().getProfile(), Minecraft.getMinecraft().getSession().getToken(), hash);
            this.sendPacket(new PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.getVerifyToken()));
            return;
        }
        catch (final AuthenticationUnavailableException e1) {
            this.lastKickMessage = LanguageManager.translate("chat_authentication_unavaileable");
        }
        catch (final InvalidCredentialsException e2) {
            this.lastKickMessage = LanguageManager.translate("chat_invalid_session");
        }
        catch (final AuthenticationException e3) {
            this.lastKickMessage = LanguageManager.translate("chat_login_failed");
        }
        Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, this.lastKickMessage);
        this.disconnect(false);
    }
    
    @Override
    public void handle(final PacketEncryptionResponse packet) {
    }
    
    @Override
    public void handle(final PacketMojangStatus packet) {
    }
    
    @Override
    public void handle(final PacketUpdateCosmetics packet) {
        final UUID uuid = LabyMod.getInstance().getPlayerUUID();
        if (uuid == null) {
            return;
        }
        final String json = packet.getJson();
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        if (json == null) {
            userManager.removeCheckedUser(uuid);
            userManager.getUser(uuid).unloadCosmeticTextures();
            return;
        }
        LabyMod.getInstance().getUserManager().broadcastBitUpdate(true);
        userManager.updateUsersJson(uuid, json, new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted) {
                try {
                    Thread.sleep(100L);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                LabyMod.getInstance().getUserManager().broadcastBitUpdate(false);
            }
        });
    }
    
    @Override
    public void handle(final PacketUserBadge packetUserStatus) {
        final FamiliarManager familiarManager = LabyMod.getInstance().getUserManager().getFamiliarManager();
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final UUID[] uuids = packetUserStatus.getUuids();
        final byte[] ranks = packetUserStatus.getRanks();
        final boolean validRanks = uuids.length == ranks.length;
        for (int i = 0; i < packetUserStatus.getUuids().length; ++i) {
            final UUID uuid = uuids[i];
            familiarManager.newFamiliarUser(uuid);
            if (validRanks) {
                final int rank = ranks[i];
                if (rank > 0) {
                    try {
                        final User user = userManager.getUser(uuid);
                        final LabyGroup group = userManager.getGroupManager().getGroupById(ranks[i]);
                        user.setGroup(group);
                        System.out.println("s");
                    }
                    catch (final Exception error) {
                        Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Error on updating user rank of " + uuid.toString() + ": " + ranks[i]);
                    }
                }
            }
        }
    }
    
    @Override
    public void handle(final PacketAddonMessage packet) {
        LabyMod.getInstance().getEventManager().callAddonMessage(packet);
        final String key = packet.getKey();
        if (key.equals("UPDATE")) {
            final Updater updater = LabyMod.getInstance().getUpdater();
            try {
                updater.downloadUpdaterFile();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            updater.addUpdaterHook();
        }
        if (key.equals("auth_pin")) {
            final JsonObject jsonObject = (JsonObject)new JsonParser().parse(packet.getJson());
            if (jsonObject.has("pin")) {
                final String pin = jsonObject.get("pin").getAsString();
                if (this.pinResponseConsumer != null) {
                    this.pinResponseConsumer.accept(pin);
                }
            }
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.disconnect(false);
        if (!(cause instanceof IOException)) {
            cause.printStackTrace();
            ctx.close();
        }
    }
    
    public void sendPacket(final Packet packet) {
        if (this.nioSocketChannel == null) {
            return;
        }
        if (!this.nioSocketChannel.isOpen() || !this.nioSocketChannel.isWritable() || this.currentConnectionState == EnumConnectionState.OFFLINE) {
            return;
        }
        if (this.nioSocketChannel.eventLoop().inEventLoop()) {
            this.nioSocketChannel.writeAndFlush(packet).addListeners((GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[] { ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE });
        }
        else {
            this.nioSocketChannel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    ClientConnection.this.nioSocketChannel.writeAndFlush(packet).addListeners((GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[] { ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE });
                }
            });
        }
    }
    
    public void setNioSocketChannel(final NioSocketChannel nioSocketChannel) {
        this.nioSocketChannel = nioSocketChannel;
    }
    
    public EnumConnectionState getCurrentConnectionState() {
        return this.currentConnectionState;
    }
    
    public String getLastKickMessage() {
        return this.lastKickMessage;
    }
    
    public String getCustomIp() {
        return this.customIp;
    }
    
    public boolean isPinAvailable() {
        return this.pinAvailable;
    }
    
    static /* synthetic */ void access$0(final ClientConnection clientConnection, final String customIp) {
        clientConnection.customIp = customIp;
    }
    
    static /* synthetic */ void access$1(final ClientConnection clientConnection, final int customPort) {
        clientConnection.customPort = customPort;
    }
    
    static /* synthetic */ void access$3(final ClientConnection clientConnection, final String lastKickMessage) {
        clientConnection.lastKickMessage = lastKickMessage;
    }
}
