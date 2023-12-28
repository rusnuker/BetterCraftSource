// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect;

import io.netty.bootstrap.AbstractBootstrap;
import net.labymod.labyconnect.packets.PacketPlayServerStatusUpdate;
import java.io.IOException;
import net.labymod.user.User;
import net.labymod.user.FamiliarManager;
import net.labymod.user.util.EnumUserRank;
import net.labymod.labyconnect.packets.PacketUserBadge;
import net.labymod.user.UserManager;
import net.labymod.utils.Consumer;
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
import net.labymod.labyconnect.packets.PacketPlayServerStatus;
import net.labymod.labyconnect.packets.PacketPlayFriendRemove;
import net.labymod.labyconnect.packets.PacketPlayDenyFriendRequest;
import net.labymod.labyconnect.packets.PacketPlayRequestRemove;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriendResponse;
import net.labymod.labyconnect.packets.PacketPlayTyping;
import net.labymod.labyconnect.log.SingleChat;
import net.labymod.labyconnect.log.MessageChatComponent;
import net.labymod.labyconnect.packets.PacketMessage;
import net.labymod.labyconnect.packets.PacketServerMessage;
import net.labymod.labyconnect.packets.PacketPong;
import net.labymod.labyconnect.packets.PacketPing;
import net.labymod.labyconnect.packets.PacketBanned;
import java.util.Iterator;
import net.labymod.labyconnect.user.ChatRequest;
import net.labymod.labyconnect.packets.PacketLoginRequest;
import java.util.Collection;
import net.labymod.labyconnect.packets.PacketLoginFriend;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriend;
import net.labymod.labyconnect.packets.PacketKick;
import net.labymod.labyconnect.packets.PacketChatVisibilityChange;
import net.labymod.labyconnect.packets.PacketLoginComplete;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyconnect.packets.PacketPlayPlayerOnline;
import net.labymod.labyconnect.packets.PacketAddonMessage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import net.labymod.labyconnect.packets.PacketLoginVersion;
import net.labymod.labyconnect.packets.PacketLoginOptions;
import net.labymod.labyconnect.packets.PacketHelloPong;
import net.labymod.labyconnect.packets.PacketLoginData;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.labymod.labyconnect.packets.PacketDisconnect;
import java.nio.channels.UnresolvedAddressException;
import net.labymod.labyconnect.packets.PacketHelloPing;
import me.amkgre.bettercraft.client.Client;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import net.labymod.labyconnect.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.labymod.event.SessionListener;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.packets.EnumConnectionState;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import java.util.concurrent.ExecutorService;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.ChannelHandler;
import net.labymod.labyconnect.handling.PacketHandler;

@ChannelHandler.Sharable
public class ClientConnection extends PacketHandler
{
    private NioEventLoopGroup nioEventLoopGroup;
    private ExecutorService executorService;
    private SocketChannel nioSocketChannel;
    private Bootstrap bootstrap;
    private EnumConnectionState currentConnectionState;
    private LabyConnect chatClient;
    private String lastKickMessage;
    private String customIp;
    private int customPort;
    private LabyMod labyMod;
    private SessionListener eventListener;
    
    public ClientConnection(final LabyMod labyMod, final LabyConnect chatClient) {
        this.nioEventLoopGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Chat#%d").build());
        this.executorService = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("Helper#%d").build());
        this.currentConnectionState = EnumConnectionState.OFFLINE;
        this.lastKickMessage = "Unknown";
        this.customIp = null;
        this.customPort = -1;
        this.labyMod = labyMod;
        this.chatClient = chatClient;
    }
    
    public void setEventListener(final SessionListener eventListener) {
        this.eventListener = eventListener;
    }
    
    public SessionListener getEventListener() {
        return this.eventListener;
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object packet) throws Exception {
        final SessionListener.PacketEvent ev = new SessionListener.PacketEvent((Packet)packet);
        if (this.eventListener != null) {
            this.eventListener.onPacketIn(ev);
        }
        packet = ev.getPacket();
        if (ev.isCancelled()) {
            return;
        }
        super.channelRead0(ctx, packet);
    }
    
    public void connect() {
        if (this.nioSocketChannel != null && this.nioSocketChannel.isOpen()) {
            this.nioSocketChannel.close();
            this.nioSocketChannel = null;
        }
        this.updateConnectionState(EnumConnectionState.HELLO);
        (this.bootstrap = new Bootstrap()).group(this.nioEventLoopGroup);
        ((AbstractBootstrap<AbstractBootstrap, Channel>)this.bootstrap).option(ChannelOption.TCP_NODELAY, true);
        ((AbstractBootstrap<AbstractBootstrap, Channel>)this.bootstrap).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.handler(new ClientChannelInitializer(Client.getInstance().getLabyMod(), this));
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientConnection.this.bootstrap.connect("mod.labymod.net", 30336).syncUninterruptibly();
                    ClientConnection.this.sendPacket(new PacketHelloPing(System.currentTimeMillis()));
                }
                catch (final UnresolvedAddressException unresolvedaddressexception) {
                    ClientConnection.this.updateConnectionState(EnumConnectionState.OFFLINE);
                    ClientConnection.access$1(ClientConnection.this, (unresolvedaddressexception.getMessage() == null) ? "Unknown error" : unresolvedaddressexception.getMessage());
                    unresolvedaddressexception.printStackTrace();
                }
                catch (final Throwable throwable) {
                    ClientConnection.this.updateConnectionState(EnumConnectionState.OFFLINE);
                    ClientConnection.access$1(ClientConnection.this, (throwable.getMessage() == null) ? "Unknown error" : throwable.getMessage());
                    throwable.printStackTrace();
                    if (ClientConnection.this.lastKickMessage.contains("no further information") || throwable.getMessage() == null) {
                        ClientConnection.access$1(ClientConnection.this, "chat_not_reachable");
                    }
                }
            }
        });
    }
    
    public void connect(final String ip, final int port) {
    }
    
    public void disconnect(final boolean kicked) {
        if (this.currentConnectionState != EnumConnectionState.OFFLINE) {
            this.updateConnectionState(EnumConnectionState.OFFLINE);
            this.labyMod.getUserManager().getFamiliarManager().clear();
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ClientConnection.this.chatClient.getChatlogManager().saveChatlogs(ClientConnection.this.labyMod.getPlayerUUID());
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
        if (this.eventListener != null) {
            this.eventListener.onDisconnected(kicked, this.lastKickMessage);
        }
    }
    
    public void updateConnectionState(final EnumConnectionState connectionState) {
        this.currentConnectionState = connectionState;
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
        this.sendPacket(new PacketLoginData(this.labyMod.getPlayerUUID(), this.labyMod.getPlayerName(), this.labyMod.getMotd()));
        this.sendPacket(new PacketLoginOptions(true, this.chatClient.getClientProfile().getUserStatus(), this.chatClient.getClientProfile().getTimeZone()));
        this.sendPacket(new PacketLoginVersion(21, "1.8.9_3.4.1"));
        final JsonArray jsonarray = new JsonArray();
        final JsonObject jsonobject1 = new JsonObject();
        jsonobject1.add("addons", jsonarray);
        this.sendPacket(new PacketAddonMessage("labymod_addons", jsonobject1.toString()));
        this.chatClient.getFriends().clear();
        this.chatClient.getRequests().clear();
    }
    
    @Override
    public void handle(final PacketPlayPlayerOnline packet) {
        final ChatUser chatuser = this.chatClient.getChatUserByUUID(packet.getPlayer().getGameProfile().getId());
        chatuser.setStatus(packet.getPlayer().getStatus());
        chatuser.setStatusMessage(packet.getPlayer().getStatusMessage());
    }
    
    @Override
    public void handle(final PacketLoginComplete packet) {
        this.updateConnectionState(EnumConnectionState.PLAY);
        this.labyMod.getUserManager().getFamiliarManager().refresh();
        if (this.eventListener != null) {
            this.eventListener.onConnected();
        }
    }
    
    @Override
    public void handle(final PacketChatVisibilityChange packet) {
    }
    
    @Override
    public void handle(final PacketKick packet) {
        this.disconnect(true);
        this.lastKickMessage = ((packet.getReason() == null) ? "chat_unknown_kick_reason" : packet.getReason());
        System.out.println(this.lastKickMessage);
    }
    
    @Override
    public void handle(final PacketDisconnect packet) {
        this.disconnect(true);
        this.lastKickMessage = ((packet.getReason() == null) ? "chat_unknown_disconnect_reason" : packet.getReason());
    }
    
    @Override
    public void handle(final PacketPlayRequestAddFriend packet) {
    }
    
    @Override
    public void handle(final PacketLoginFriend packet) {
        this.chatClient.getFriends().addAll(packet.getFriends());
    }
    
    @Override
    public void handle(final PacketLoginRequest packet) {
        this.chatClient.getRequests().addAll(packet.getRequests());
        for (ChatRequest chatRequest : this.chatClient.getRequests()) {}
    }
    
    @Override
    public void handle(final PacketBanned packet) {
        this.disconnect(true);
        this.lastKickMessage = ((packet.getReason() == null) ? "chat_unknown_ban_reason" : packet.getReason());
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
    }
    
    @Override
    public void handle(final PacketMessage packet) {
        final SingleChat singlechat = this.chatClient.getChatlogManager().getChat(packet.getSender());
        singlechat.addMessage(new MessageChatComponent(packet.getSender().getGameProfile().getName(), System.currentTimeMillis(), packet.getMessage()));
    }
    
    @Override
    public void handle(final PacketPlayTyping packet) {
    }
    
    @Override
    public void handle(final PacketPlayRequestAddFriendResponse packet) {
    }
    
    @Override
    public void handle(final PacketPlayRequestRemove packet) {
        final Iterator<ChatRequest> iterator = this.chatClient.getRequests().iterator();
        while (iterator.hasNext()) {
            final ChatRequest chatrequest = iterator.next();
            if (!chatrequest.getGameProfile().getName().equalsIgnoreCase(packet.getPlayerName())) {
                continue;
            }
            iterator.remove();
        }
    }
    
    @Override
    public void handle(final PacketPlayDenyFriendRequest packet) {
    }
    
    @Override
    public void handle(final PacketPlayFriendRemove packet) {
        final Iterator<ChatUser> iterator = this.chatClient.getFriends().iterator();
        while (iterator.hasNext()) {
            final ChatUser chatuser = iterator.next();
            if (!chatuser.equals(packet.getToRemove())) {
                continue;
            }
            iterator.remove();
        }
    }
    
    @Override
    public void handle(final PacketLoginOptions packet) {
    }
    
    @Override
    public void handle(final PacketPlayServerStatus packet) {
    }
    
    @Override
    public void handle(final PacketPlayFriendStatus packet) {
        final ChatUser chatuser = this.chatClient.getChatUser(packet.getPlayer());
        chatuser.setCurrentServerInfo(packet.getPlayerInfo());
    }
    
    @Override
    public void handle(final PacketPlayFriendPlayingOn packet) {
        if (packet.getGameModeName() != null && !packet.getGameModeName().isEmpty()) {
            String s = null;
            s = (packet.getGameModeName().contains(".") ? "chat_user_now_playing_on" : "chat_user_now_playing");
        }
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
        final SecretKey secretkey = CryptManager.createNewSharedKey();
        final PublicKey publickey = CryptManager.decodePublicKey(encryptionRequest.getPublicKey());
        final String s = encryptionRequest.getServerId();
        final String s2 = "";
        final String s3 = new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey)).toString(16);
        final UUID uuid = this.labyMod.getPlayerUUID();
        if (uuid == null) {
            this.lastKickMessage = "chat_invalid_session";
            this.disconnect(false);
        }
        else {
            try {
                final MinecraftSessionService minecraftsessionservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()).createMinecraftSessionService();
                minecraftsessionservice.joinServer(this.labyMod.getGameProfile(), this.labyMod.getSession().getToken(), s3);
                this.sendPacket(new PacketEncryptionResponse(secretkey, publickey, encryptionRequest.getVerifyToken()));
                return;
            }
            catch (final AuthenticationUnavailableException var9) {
                this.lastKickMessage = "chat_authentication_unavaileable";
            }
            catch (final InvalidCredentialsException var10) {
                this.lastKickMessage = "chat_invalid_session";
            }
            catch (final AuthenticationException var11) {
                this.lastKickMessage = "chat_login_failed";
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            this.disconnect(false);
        }
    }
    
    @Override
    public void handle(final PacketEncryptionResponse packet) {
    }
    
    @Override
    public void handle(final PacketMojangStatus packet) {
    }
    
    @Override
    public void handle(final PacketUpdateCosmetics packet) {
        final UUID uuid = this.labyMod.getPlayerUUID();
        if (uuid != null) {
            final String s = packet.getJson();
            final UserManager usermanager = this.labyMod.getUserManager();
            if (s == null) {
                usermanager.removeCheckedUser(uuid);
                usermanager.getUser(uuid).unloadCosmeticTextures();
            }
            else {
                usermanager.updateUsersJson(uuid, s, new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean accepted) {
                        try {
                            Thread.sleep(100L);
                        }
                        catch (final InterruptedException interruptedexception) {
                            interruptedexception.printStackTrace();
                        }
                    }
                });
            }
        }
    }
    
    @Override
    public void handle(final PacketUserBadge packetUserStatus) {
        final FamiliarManager familiarmanager = this.labyMod.getUserManager().getFamiliarManager();
        final UserManager usermanager = this.labyMod.getUserManager();
        final UUID[] auuid = packetUserStatus.getUuids();
        final byte[] abyte;
        final boolean flag = auuid.length == (abyte = packetUserStatus.getRanks()).length;
        for (int i = 0; i < packetUserStatus.getUuids().length; ++i) {
            final UUID uuid = auuid[i];
            familiarmanager.newFamiliarUser(uuid);
            if (flag) {
                final byte j;
                if ((j = abyte[i]) > 0) {
                    try {
                        final User user = usermanager.getUser(uuid);
                        user.setRank(EnumUserRank.getById(abyte[i]));
                    }
                    catch (final Exception var11) {
                        var11.printStackTrace();
                    }
                }
            }
        }
    }
    
    @Override
    public void handle(final PacketAddonMessage packet) {
        if (packet.getKey().equals("UPDATE")) {
            System.out.println("Update Request Kappa!");
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.disconnect(false);
        if (!(cause instanceof IOException)) {
            cause.printStackTrace();
            ctx.close();
        }
        else {
            cause.printStackTrace();
        }
    }
    
    public void sendPacket(Packet packet) {
        if (this.nioSocketChannel != null && this.nioSocketChannel.isOpen() && this.nioSocketChannel.isWritable() && this.currentConnectionState != EnumConnectionState.OFFLINE) {
            final SessionListener.PacketEvent ev = new SessionListener.PacketEvent(packet);
            if (this.eventListener != null) {
                this.eventListener.onPacketOut(ev);
            }
            packet = ev.getPacket();
            if (ev.isCancelled()) {
                return;
            }
            if (this.nioSocketChannel.eventLoop().inEventLoop()) {
                this.nioSocketChannel.writeAndFlush(packet).addListeners((GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[] { ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE });
            }
            else {
                final Packet pa = packet;
                this.nioSocketChannel.eventLoop().execute(new Runnable() {
                    @Override
                    public void run() {
                        ClientConnection.this.nioSocketChannel.writeAndFlush(pa).addListeners((GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[] { ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE });
                    }
                });
            }
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
    
    static void access$1(final ClientConnection clientConnection, final String lastKickMessage) {
        clientConnection.lastKickMessage = lastKickMessage;
    }
    
    @Override
    public void handle(final PacketPlayServerStatusUpdate packetPlayServerStatusUpdate) {
    }
}
