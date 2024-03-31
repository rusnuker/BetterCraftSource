// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bungee.handlers;

import com.viaversion.viaversion.api.protocol.ProtocolPipeline;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.score.Team;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.function.Function;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import net.md_5.bungee.protocol.packet.PluginMessage;
import java.util.Collection;
import java.util.ArrayList;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import java.util.Iterator;
import com.viaversion.viaversion.api.data.entity.ClientEntityIdChangeListener;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.EntityIdProvider;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import java.util.List;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.bungee.service.ProtocolDetectorService;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.bungee.storage.BungeeStorage;
import com.viaversion.viaversion.api.Via;
import net.md_5.bungee.api.event.ServerConnectEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.md_5.bungee.api.plugin.Listener;

public class BungeeServerHandler implements Listener
{
    private static Method getHandshake;
    private static Method getRelayMessages;
    private static Method setProtocol;
    private static Method getEntityMap;
    private static Method setVersion;
    private static Field entityRewrite;
    private static Field channelWrapper;
    
    @EventHandler(priority = 120)
    public void onServerConnect(final ServerConnectEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final UserConnection user = Via.getManager().getConnectionManager().getConnectedClient(e.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        if (!user.has(BungeeStorage.class)) {
            user.put(new BungeeStorage(e.getPlayer()));
        }
        final int protocolId = ProtocolDetectorService.getProtocolId(e.getTarget().getName());
        final List<ProtocolPathEntry> protocols = Via.getManager().getProtocolManager().getProtocolPath(user.getProtocolInfo().getProtocolVersion(), protocolId);
        try {
            final Object handshake = BungeeServerHandler.getHandshake.invoke(e.getPlayer().getPendingConnection(), new Object[0]);
            BungeeServerHandler.setProtocol.invoke(handshake, (protocols == null) ? user.getProtocolInfo().getProtocolVersion() : protocolId);
        }
        catch (final InvocationTargetException | IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
    
    @EventHandler(priority = -120)
    public void onServerConnected(final ServerConnectedEvent e) {
        try {
            this.checkServerChange(e, Via.getManager().getConnectionManager().getConnectedClient(e.getPlayer().getUniqueId()));
        }
        catch (final Exception e2) {
            e2.printStackTrace();
        }
    }
    
    @EventHandler(priority = -120)
    public void onServerSwitch(final ServerSwitchEvent e) {
        final UserConnection userConnection = Via.getManager().getConnectionManager().getConnectedClient(e.getPlayer().getUniqueId());
        if (userConnection == null) {
            return;
        }
        int playerId;
        try {
            playerId = Via.getManager().getProviders().get(EntityIdProvider.class).getEntityId(userConnection);
        }
        catch (final Exception ex) {
            return;
        }
        for (final EntityTracker tracker : userConnection.getEntityTrackers()) {
            tracker.setClientEntityId(playerId);
        }
        for (final StorableObject object : userConnection.getStoredObjects().values()) {
            if (object instanceof ClientEntityIdChangeListener) {
                ((ClientEntityIdChangeListener)object).setClientEntityId(playerId);
            }
        }
    }
    
    public void checkServerChange(final ServerConnectedEvent e, final UserConnection user) throws Exception {
        if (user == null) {
            return;
        }
        if (user.has(BungeeStorage.class)) {
            final BungeeStorage storage = user.get(BungeeStorage.class);
            final ProxiedPlayer player = storage.getPlayer();
            if (e.getServer() != null && !e.getServer().getInfo().getName().equals(storage.getCurrentServer())) {
                final EntityTracker1_9 oldEntityTracker = user.getEntityTracker(Protocol1_9To1_8.class);
                if (oldEntityTracker != null && oldEntityTracker.isAutoTeam() && oldEntityTracker.isTeamExists()) {
                    oldEntityTracker.sendTeamPacket(false, true);
                }
                final String serverName = e.getServer().getInfo().getName();
                storage.setCurrentServer(serverName);
                int protocolId = ProtocolDetectorService.getProtocolId(serverName);
                if (protocolId <= ProtocolVersion.v1_8.getVersion() && storage.getBossbar() != null) {
                    if (user.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
                        for (final UUID uuid : storage.getBossbar()) {
                            final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.BOSSBAR, null, user);
                            wrapper.write(Type.UUID, uuid);
                            wrapper.write(Type.VAR_INT, 1);
                            wrapper.send(Protocol1_9To1_8.class);
                        }
                    }
                    storage.getBossbar().clear();
                }
                final ProtocolInfo info = user.getProtocolInfo();
                final int previousServerProtocol = info.getServerProtocolVersion();
                final List<ProtocolPathEntry> protocolPath = Via.getManager().getProtocolManager().getProtocolPath(info.getProtocolVersion(), protocolId);
                final ProtocolPipeline pipeline = user.getProtocolInfo().getPipeline();
                user.clearStoredObjects();
                pipeline.cleanPipes();
                if (protocolPath == null) {
                    protocolId = info.getProtocolVersion();
                }
                else {
                    final List<Protocol> protocols = new ArrayList<Protocol>(protocolPath.size());
                    for (final ProtocolPathEntry entry : protocolPath) {
                        protocols.add(entry.getProtocol());
                    }
                    pipeline.add(protocols);
                }
                info.setServerProtocolVersion(protocolId);
                pipeline.add(Via.getManager().getProtocolManager().getBaseProtocol(protocolId));
                final Object relayMessages = BungeeServerHandler.getRelayMessages.invoke(e.getPlayer().getPendingConnection(), new Object[0]);
                for (final Object message : (List)relayMessages) {
                    final PluginMessage plMsg = (PluginMessage)message;
                    String channel = plMsg.getTag();
                    final int id1_13 = ProtocolVersion.v1_13.getVersion();
                    if (previousServerProtocol != -1) {
                        final String oldChannel = channel;
                        if (previousServerProtocol < id1_13 && protocolId >= id1_13) {
                            channel = InventoryPackets.getNewPluginChannelId(channel);
                            if (channel == null) {
                                throw new RuntimeException(oldChannel + " found in relayMessages");
                            }
                            if (channel.equals("minecraft:register")) {
                                plMsg.setData(Arrays.stream(new String(plMsg.getData(), StandardCharsets.UTF_8).split("\u0000")).map((Function<? super String, ?>)InventoryPackets::getNewPluginChannelId).filter(Objects::nonNull).collect((Collector<? super Object, ?, String>)Collectors.joining("\u0000")).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                        else if (previousServerProtocol >= id1_13 && protocolId < id1_13) {
                            channel = InventoryPackets.getOldPluginChannelId(channel);
                            if (channel == null) {
                                throw new RuntimeException(oldChannel + " found in relayMessages");
                            }
                            if (channel.equals("REGISTER")) {
                                plMsg.setData(Arrays.stream(new String(plMsg.getData(), StandardCharsets.UTF_8).split("\u0000")).map((Function<? super String, ?>)InventoryPackets::getOldPluginChannelId).filter(Objects::nonNull).collect((Collector<? super Object, ?, String>)Collectors.joining("\u0000")).getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                    plMsg.setTag(channel);
                }
                user.put(storage);
                user.setActive(protocolPath != null);
                for (final Protocol protocol : pipeline.pipes()) {
                    protocol.init(user);
                }
                final EntityTracker1_9 newTracker = user.getEntityTracker(Protocol1_9To1_8.class);
                if (newTracker != null && Via.getConfig().isAutoTeam()) {
                    String currentTeam = null;
                    for (final Team team : player.getScoreboard().getTeams()) {
                        if (team.getPlayers().contains(info.getUsername())) {
                            currentTeam = team.getName();
                        }
                    }
                    newTracker.setAutoTeam(true);
                    if (currentTeam == null) {
                        newTracker.sendTeamPacket(true, true);
                        newTracker.setCurrentTeam("viaversion");
                    }
                    else {
                        newTracker.setAutoTeam(Via.getConfig().isAutoTeam());
                        newTracker.setCurrentTeam(currentTeam);
                    }
                }
                final Object wrapper2 = BungeeServerHandler.channelWrapper.get(player);
                BungeeServerHandler.setVersion.invoke(wrapper2, protocolId);
                final Object entityMap = BungeeServerHandler.getEntityMap.invoke(null, protocolId);
                BungeeServerHandler.entityRewrite.set(player, entityMap);
            }
        }
    }
    
    static {
        BungeeServerHandler.getEntityMap = null;
        BungeeServerHandler.setVersion = null;
        BungeeServerHandler.entityRewrite = null;
        BungeeServerHandler.channelWrapper = null;
        try {
            BungeeServerHandler.getHandshake = Class.forName("net.md_5.bungee.connection.InitialHandler").getDeclaredMethod("getHandshake", (Class<?>[])new Class[0]);
            BungeeServerHandler.getRelayMessages = Class.forName("net.md_5.bungee.connection.InitialHandler").getDeclaredMethod("getRelayMessages", (Class<?>[])new Class[0]);
            BungeeServerHandler.setProtocol = Class.forName("net.md_5.bungee.protocol.packet.Handshake").getDeclaredMethod("setProtocolVersion", Integer.TYPE);
            BungeeServerHandler.getEntityMap = Class.forName("net.md_5.bungee.entitymap.EntityMap").getDeclaredMethod("getEntityMap", Integer.TYPE);
            BungeeServerHandler.setVersion = Class.forName("net.md_5.bungee.netty.ChannelWrapper").getDeclaredMethod("setVersion", Integer.TYPE);
            (BungeeServerHandler.channelWrapper = Class.forName("net.md_5.bungee.UserConnection").getDeclaredField("ch")).setAccessible(true);
            (BungeeServerHandler.entityRewrite = Class.forName("net.md_5.bungee.UserConnection").getDeclaredField("entityRewrite")).setAccessible(true);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
