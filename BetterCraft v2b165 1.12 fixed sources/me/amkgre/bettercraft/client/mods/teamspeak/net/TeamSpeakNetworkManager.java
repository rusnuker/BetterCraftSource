// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.net;

import io.netty.bootstrap.AbstractBootstrap;
import me.amkgre.bettercraft.client.mods.teamspeak.util.BlockingRunnable;
import java.util.Collections;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.OwnClientImpl;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ClientImpl;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientListRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ChannelImpl;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelListRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ServerVariableRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelGroupListRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ServerGroupListRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.WhoAmIRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerInfoImpl;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ServerConnectInfoRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ServerConnectionHandlerListRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.UseRequest;
import java.util.Iterator;
import java.util.HashMap;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerTabImpl;
import java.util.Map;
import me.amkgre.bettercraft.client.mods.teamspeak.util.PropertyMap;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakEventResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.MessageImpl;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Utils;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientNotifyRegisterRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.event.EventType;
import me.amkgre.bettercraft.client.mods.teamspeak.request.AuthRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakServerConnectionResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.BlockingCallback;
import org.apache.logging.log4j.LogManager;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeak;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakConnectException;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakAuthException;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.bootstrap.Bootstrap;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakException;
import java.util.ArrayList;
import com.google.common.base.Charsets;
import io.netty.channel.nio.NioEventLoopGroup;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.MarkerManager;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.event.EventManager;
import me.amkgre.bettercraft.client.mods.teamspeak.util.KeepAliveThread;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.channel.EventLoopGroup;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakClientImpl;
import org.apache.logging.log4j.Marker;

public class TeamSpeakNetworkManager
{
    public static final Marker logMarkerPackets;
    private TeamSpeakClientImpl teamSpeakClient;
    private String authKey;
    private final Object AUTH_LOCK;
    private boolean authFailed;
    private static final EventLoopGroup EVENT_LOOP_GROUP;
    private final TeamSpeakHandler CHILD_HANDLER;
    private final LineBasedFrameDecoder LINE_FRAMER;
    private final TeamSpeakDecoder DECODER;
    private final TeamSpeakEncoder ENCODER;
    private volatile boolean connected;
    private KeepAliveThread keepAliveThread;
    private final EventManager eventManager;
    private final List<TeamSpeakRequest> SENT_REQUESTS;
    private final AtomicInteger CURRENT_TAB;
    
    static {
        logMarkerPackets = MarkerManager.getMarker("TEAMSPEAK_PACKETS");
        EVENT_LOOP_GROUP = new NioEventLoopGroup(2, new ThreadFactoryBuilder().setNameFormat("TeamSpeak Netty Client").setDaemon(true).build());
    }
    
    public TeamSpeakNetworkManager(final TeamSpeakClientImpl teamSpeakClient, final String authKey) {
        this.AUTH_LOCK = new Object();
        this.authFailed = false;
        this.CHILD_HANDLER = new TeamSpeakHandler(this);
        this.LINE_FRAMER = new LineBasedFrameDecoder(1000000);
        this.DECODER = new TeamSpeakDecoder(Charsets.UTF_8);
        this.ENCODER = new TeamSpeakEncoder(Charsets.UTF_8);
        this.connected = false;
        this.eventManager = new EventManager(this);
        this.SENT_REQUESTS = new ArrayList<TeamSpeakRequest>();
        this.CURRENT_TAB = new AtomicInteger();
        this.teamSpeakClient = teamSpeakClient;
        this.authKey = authKey;
    }
    
    public void connect(final String host, final int port) throws TeamSpeakException, TeamSpeakConnectException {
        if (this.connected) {
            throw new TeamSpeakException("Already connected to ClientQuery!");
        }
        this.connected = true;
        try {
            final Bootstrap bootstrap = new Bootstrap();
            ((AbstractBootstrap<Bootstrap, C>)bootstrap).group(TeamSpeakNetworkManager.EVENT_LOOP_GROUP).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(final SocketChannel socketChannel) throws Exception {
                    final ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(TeamSpeakNetworkManager.this.LINE_FRAMER);
                    pipeline.addLast(TeamSpeakNetworkManager.this.DECODER);
                    pipeline.addLast(TeamSpeakNetworkManager.this.ENCODER);
                    pipeline.addLast(TeamSpeakNetworkManager.this.CHILD_HANDLER);
                }
            });
            bootstrap.connect(host, port).sync();
            (this.keepAliveThread = new KeepAliveThread(this)).start();
            final Object object = this.AUTH_LOCK;
            synchronized (object) {
                this.AUTH_LOCK.wait();
                monitorexit(object);
            }
            if (this.authFailed) {
                throw new TeamSpeakAuthException();
            }
        }
        catch (final Exception e) {
            throw new TeamSpeakConnectException(e);
        }
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public void disconnect(final Throwable cause) {
        if (this.connected) {
            this.connected = false;
            if (this.keepAliveThread != null) {
                this.keepAliveThread.shutdown();
            }
            this.CHILD_HANDLER.closeChannel();
            this.teamSpeakClient.disconnect(cause);
        }
    }
    
    public RequestChain sendRequest(final Request command, final Callback<TeamSpeakCommandResponse> callback) throws TeamSpeakException {
        if (!this.connected) {
            return new RequestChain(this);
        }
        final RequestChain chain = new RequestChain(this);
        final TeamSpeakRequest request = new TeamSpeakRequest(command, callback, chain);
        this.CHILD_HANDLER.send(command, new GenericFutureListener() {
            @Override
            public void operationComplete(final Future future) throws Exception {
                if (TeamSpeak.isDebugMode()) {
                    LogManager.getLogger().info(TeamSpeakNetworkManager.logMarkerPackets, "OUT|" + request.getRequest());
                }
                final List list = TeamSpeakNetworkManager.this.SENT_REQUESTS;
                synchronized (list) {
                    TeamSpeakNetworkManager.this.SENT_REQUESTS.add(request);
                    monitorexit(list);
                }
            }
        });
        if (callback instanceof BlockingCallback) {
            ((BlockingCallback)callback).onStart();
        }
        return chain;
    }
    
    void sendRequest(final TeamSpeakRequest request) throws TeamSpeakException {
        if (!this.connected) {
            throw new TeamSpeakException("Not connected to ClientQuery!");
        }
        this.CHILD_HANDLER.send(request.getRequest(), new GenericFutureListener() {
            @Override
            public void operationComplete(final Future future) throws Exception {
                if (TeamSpeak.isDebugMode()) {
                    LogManager.getLogger().info(TeamSpeakNetworkManager.logMarkerPackets, "OUT|" + request.getRequest());
                }
                final List list = TeamSpeakNetworkManager.this.SENT_REQUESTS;
                synchronized (list) {
                    TeamSpeakNetworkManager.this.SENT_REQUESTS.add(request);
                    monitorexit(list);
                }
            }
        });
    }
    
    void handleResponse(final TeamSpeakResponse response) {
        if (!this.connected) {
            return;
        }
        if (TeamSpeak.isDebugMode()) {
            LogManager.getLogger().info(TeamSpeakNetworkManager.logMarkerPackets, "IN |" + response.getMessage());
        }
        if (response instanceof TeamSpeakServerConnectionResponse) {
            final int serverConnectionHandlerId = ((TeamSpeakServerConnectionResponse)response).getServerConnectionHandlerId();
            TeamSpeakRequest request = null;
            if (this.CURRENT_TAB.get() != 0) {
                final Object object = this.SENT_REQUESTS;
                synchronized (object) {
                    if (!this.SENT_REQUESTS.isEmpty()) {
                        request = this.SENT_REQUESTS.remove(0);
                    }
                    monitorexit(object);
                }
            }
            this.CURRENT_TAB.set(serverConnectionHandlerId);
            if (((TeamSpeakServerConnectionResponse)response).requiresAuth()) {
                if (this.authKey == null) {
                    final Object object = this.AUTH_LOCK;
                    synchronized (object) {
                        this.authFailed = true;
                        this.AUTH_LOCK.notifyAll();
                        monitorexit(object);
                        return;
                    }
                }
                this.sendRequest(new AuthRequest(this.authKey), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        TeamSpeakNetworkManager.this.sendRequest(new ClientNotifyRegisterRequest(EventType.ANY), new Callback<TeamSpeakCommandResponse>() {
                            @Override
                            public void onDone(final TeamSpeakCommandResponse response) {
                                final Object object = TeamSpeakNetworkManager.this.AUTH_LOCK;
                                synchronized (object) {
                                    TeamSpeakNetworkManager.this.AUTH_LOCK.notifyAll();
                                    monitorexit(object);
                                }
                            }
                        });
                    }
                    
                    @Override
                    public void exceptionCaught(final TeamSpeakException exception) {
                        final Object object = TeamSpeakNetworkManager.this.AUTH_LOCK;
                        synchronized (object) {
                            TeamSpeakNetworkManager.access$6(TeamSpeakNetworkManager.this, true);
                            TeamSpeakNetworkManager.this.AUTH_LOCK.notifyAll();
                            monitorexit(object);
                        }
                    }
                });
            }
            else {
                this.sendRequest(new ClientNotifyRegisterRequest(EventType.ANY), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        final Object object = TeamSpeakNetworkManager.this.AUTH_LOCK;
                        synchronized (object) {
                            TeamSpeakNetworkManager.this.AUTH_LOCK.notifyAll();
                            monitorexit(object);
                        }
                    }
                });
                if (request != null) {
                    request.getCallback().onDone(null);
                }
            }
        }
        else {
            if (response instanceof TeamSpeakCommandResponse) {
                final List<TeamSpeakRequest> serverConnectionHandlerId2 = this.SENT_REQUESTS;
                synchronized (serverConnectionHandlerId2) {
                    if (this.SENT_REQUESTS.isEmpty()) {
                        LogManager.getLogger().warn("Got response without having sent a command: " + response.getRawMessage());
                        monitorexit(serverConnectionHandlerId2);
                        return;
                    }
                    final TeamSpeakRequest request = this.SENT_REQUESTS.remove(0);
                    final TeamSpeakCommandResponse commandResponse = (TeamSpeakCommandResponse)response;
                    if (commandResponse.getErrorId() == 0) {
                        try {
                            request.getCallback().onDone(commandResponse);
                        }
                        catch (final Throwable throwable) {
                            request.getCallback().exceptionCaught(new TeamSpeakException("Could not handle command response of " + request.getRequest(), throwable));
                        }
                        request.getChain().sendNextRequest();
                    }
                    else {
                        final ServerTabImpl selectedTab = this.teamSpeakClient.getSelectedTab();
                        if (selectedTab != null) {
                            selectedTab.getServerChat().addMessage(new MessageImpl(String.valueOf(Utils.getChatTimeString()) + "An error occurred: " + commandResponse.getErrorMsg() + " (" + commandResponse.getErrorId() + ")", System.currentTimeMillis()));
                        }
                        request.getCallback().exceptionCaught(new TeamSpeakException(commandResponse));
                    }
                    monitorexit(serverConnectionHandlerId2);
                }
            }
            if (response instanceof TeamSpeakEventResponse) {
                final TeamSpeakEventResponse eventResponse = (TeamSpeakEventResponse)response;
                final ArrayList<PropertyMap> propertyMaps = new ArrayList<PropertyMap>();
                String[] split;
                for (int length = (split = eventResponse.getMessage().split("\\|")).length, i = 0; i < length; ++i) {
                    final String s = split[i];
                    final HashMap<String, String> parse = TeamSpeakResponse.parse(s);
                    if (!propertyMaps.isEmpty()) {
                        final Map<String, String> firstProperties = propertyMaps.get(0).getProperties();
                        for (final String key : firstProperties.keySet()) {
                            if (parse.containsKey(key)) {
                                continue;
                            }
                            parse.put(key, firstProperties.get(key));
                        }
                    }
                    propertyMaps.add(new PropertyMap(parse));
                }
                if (this.teamSpeakClient.isConnected()) {
                    for (final PropertyMap propertyMap : propertyMaps) {
                        try {
                            this.eventManager.createEvent(eventResponse.getType(), propertyMap);
                        }
                        catch (final Throwable throwable2) {
                            LogManager.getLogger().error("Could not handle event " + eventResponse.getType() + "!", throwable2);
                        }
                    }
                }
            }
            else {
                LogManager.getLogger().error("Got unhandled response: " + response);
            }
        }
    }
    
    public void trySelectTab(final int tabId, final Runnable runnable) {
        if (tabId == 0) {
            throw new IllegalArgumentException("Invalid tab id!");
        }
        if (this.CURRENT_TAB.get() != tabId) {
            this.sendRequest(new UseRequest(tabId), new Callback<TeamSpeakCommandResponse>() {
                @Override
                public void onDone(final TeamSpeakCommandResponse response) {
                    final ServerTabImpl selectedTab = TeamSpeakNetworkManager.this.teamSpeakClient.getSelectedTab();
                    if (selectedTab != null) {
                        selectedTab.setSelected(false);
                    }
                    TeamSpeakNetworkManager.this.teamSpeakClient.getServerTab(tabId).setSelected(true);
                    TeamSpeakNetworkManager.this.CURRENT_TAB.set(tabId);
                    runnable.run();
                }
            });
            this.checkNeedsBlocking(runnable);
        }
        else {
            runnable.run();
        }
    }
    
    public int getSelectedTab() {
        return this.CURRENT_TAB.get();
    }
    
    public void getTabs(final Callback<List<Integer>> callback) {
        this.sendRequest(new ServerConnectionHandlerListRequest(), new Callback<TeamSpeakCommandResponse>() {
            @Override
            public void onDone(final TeamSpeakCommandResponse response) {
                final ArrayList<Integer> result = new ArrayList<Integer>();
                String[] split;
                for (int length = (split = response.getMessage().split("\\|")).length, i = 0; i < length; ++i) {
                    final String tab = split[i];
                    result.add(Integer.valueOf(tab.split("=")[1]));
                }
                callback.onDone(result);
            }
        });
    }
    
    public void getTabInfo(final ServerTabImpl serverTab, final Runnable runnable) {
        this.trySelectTab(serverTab.getId(), new Runnable() {
            @Override
            public void run() {
                TeamSpeakNetworkManager.this.sendRequest(new ServerConnectInfoRequest(), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        final HashMap<String, String> parsedResponse = response.getParsedResponse();
                        serverTab.setServerInfo(new ServerInfoImpl(serverTab, parsedResponse.get("ip"), Integer.parseInt(parsedResponse.get("port"))));
                    }
                    
                    @Override
                    public void exceptionCaught(final TeamSpeakException exception) {
                        TeamSpeakNetworkManager.this.clearTabInfo(serverTab);
                        runnable.run();
                    }
                }).sendThen(new WhoAmIRequest(), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        final HashMap<String, String> parsedResponse = response.getParsedResponse();
                        final int clientId = Integer.parseInt(parsedResponse.get("clid"));
                        serverTab.setSelfId(clientId);
                    }
                }).sendThen(new ServerGroupListRequest(), new EmptyCallback<TeamSpeakCommandResponse>()).sendThen(new ChannelGroupListRequest(), new EmptyCallback<TeamSpeakCommandResponse>()).sendThen(new ServerVariableRequest(), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        final ServerInfoImpl serverInfo = serverTab.getServerInfo();
                        final PropertyMap propertyMap = new PropertyMap(response.getParsedResponse());
                        serverInfo.setName(propertyMap.get("virtualserver_name"));
                        serverInfo.setUniqueId(propertyMap.get("virtualserver_unique_identifier"));
                        serverInfo.setPlatform(propertyMap.get("virtualserver_platform"));
                        serverInfo.setVersion(propertyMap.get("virtualserver_version"));
                        serverInfo.setTimeCreated(propertyMap.getLong("virtualserver_created"));
                        serverInfo.setBannerURL(propertyMap.get("virtualserver_hostbanner_url"));
                        serverInfo.setBannerImageURL(propertyMap.get("virtualserver_hostbanner_gfx_url"));
                        serverInfo.setBannerImageInterval(propertyMap.getInt("virtualserver_hostbanner_gfx_interval"));
                        serverInfo.setPrioritySpeakerDimmModificator(propertyMap.getFloat("virtualserver_priority_speaker_dimm_modificator"));
                        serverInfo.setHostButtonTooltip(propertyMap.get("virtualserver_hostbutton_tooltip"));
                        serverInfo.setHostButtonURL(propertyMap.get("virtualserver_hostbutton_url"));
                        serverInfo.setHostButtonImageURL(propertyMap.get("virtualserver_hostbutton_gfx_url"));
                        serverInfo.setPhoneticName(propertyMap.get("virtualserver_name_phonetic"));
                        serverInfo.setIconId(propertyMap.getInt("virtualserver_icon_id"));
                        serverTab.setDefaultServerGroup(serverTab.getServerGroup(propertyMap.getInt("virtualserver_default_server_group")));
                        serverTab.setDefaultChannelGroup(serverTab.getChannelGroup(propertyMap.getInt("virtualserver_default_channel_group")));
                    }
                }).sendThen(new ChannelListRequest(), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        final HashMap<Integer, ChannelImpl> map = new HashMap<Integer, ChannelImpl>();
                        String[] split;
                        for (int length = (split = response.getMessage().split("\\|")).length, i = 0; i < length; ++i) {
                            final String c = split[i];
                            final HashMap<String, String> parse = TeamSpeakResponse.parse(c);
                            final PropertyMap propertyMap = new PropertyMap(parse);
                            final ChannelImpl channel = new ChannelImpl(serverTab, propertyMap);
                            channel.updateProperties(propertyMap);
                            map.put(channel.getId(), channel);
                        }
                        serverTab.setChannels(map);
                    }
                }).sendThen(new ClientListRequest(), new Callback<TeamSpeakCommandResponse>() {
                    @Override
                    public void onDone(final TeamSpeakCommandResponse response) {
                        final ArrayList<ClientImpl> clients = new ArrayList<ClientImpl>();
                        String[] split;
                        for (int length = (split = response.getMessage().split("\\|")).length, i = 0; i < length; ++i) {
                            final String c = split[i];
                            final HashMap<String, String> parse = TeamSpeakResponse.parse(c);
                            final ChannelImpl channel = serverTab.getChannel(Integer.parseInt(parse.get("cid")));
                            final PropertyMap propertyMap = new PropertyMap(parse);
                            final int clientId = propertyMap.getInt("clid");
                            ClientImpl client;
                            if (clientId == serverTab.getSelfId()) {
                                final OwnClientImpl ownClient = new OwnClientImpl(TeamSpeakNetworkManager.this, clientId, propertyMap.getInt("client_database_id"), propertyMap.get("client_unique_identifier"), propertyMap.get("client_nickname"), channel);
                                serverTab.setSelf(ownClient);
                                client = ownClient;
                            }
                            else {
                                client = new ClientImpl(TeamSpeakNetworkManager.this, clientId, propertyMap.getInt("client_database_id"), propertyMap.get("client_unique_identifier"), propertyMap.get("client_nickname"), channel);
                            }
                            client.updateProperties(propertyMap);
                            clients.add(client);
                        }
                        serverTab.setClients(clients);
                        serverTab.setLoaded(true);
                        runnable.run();
                    }
                });
            }
        });
    }
    
    public void clearTabInfo(final ServerTabImpl serverTab) {
        serverTab.setChannels(Collections.emptyMap());
        serverTab.setClients(Collections.emptyList());
        serverTab.setServerInfo(null);
        serverTab.setSelfId(0);
        serverTab.setSelf(null);
        serverTab.clearServerGroups();
        serverTab.clearChannelGroups();
    }
    
    private void checkNeedsBlocking(final Runnable runnable) {
        if (runnable instanceof BlockingRunnable) {
            ((BlockingRunnable)runnable).onStart();
        }
    }
    
    public TeamSpeakClientImpl getTeamSpeakClient() {
        return this.teamSpeakClient;
    }
    
    static /* synthetic */ void access$6(final TeamSpeakNetworkManager teamSpeakNetworkManager, final boolean authFailed) {
        teamSpeakNetworkManager.authFailed = authFailed;
    }
}
