// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.listeners;

import com.viaversion.viaversion.bukkit.util.NMSUtil;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import java.util.logging.Level;
import com.viaversion.viaversion.api.Via;
import org.bukkit.event.player.PlayerJoinEvent;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.event.Listener;

public class JoinListener implements Listener
{
    private static final Method GET_HANDLE;
    private static final Field CONNECTION;
    private static final Field NETWORK_MANAGER;
    private static final Field CHANNEL;
    
    private static Field findField(final Class<?> clazz, final String... types) throws NoSuchFieldException {
        for (final Field field : clazz.getDeclaredFields()) {
            final String fieldTypeName = field.getType().getSimpleName();
            for (final String type : types) {
                if (fieldTypeName.equals(type)) {
                    if (!Modifier.isPublic(field.getModifiers())) {
                        field.setAccessible(true);
                    }
                    return field;
                }
            }
        }
        throw new NoSuchFieldException(types[0]);
    }
    
    private static Field findField(final Class<?> clazz, final Class<?> fieldType) throws NoSuchFieldException {
        for (final Field field : clazz.getDeclaredFields()) {
            if (field.getType() == fieldType) {
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                return field;
            }
        }
        throw new NoSuchFieldException(fieldType.getSimpleName());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent e) {
        if (JoinListener.CHANNEL == null) {
            return;
        }
        final Player player = e.getPlayer();
        Channel channel;
        try {
            channel = this.getChannel(player);
        }
        catch (final Exception ex) {
            Via.getPlatform().getLogger().log(Level.WARNING, ex, () -> "Could not find Channel for logging-in player " + player.getUniqueId());
            return;
        }
        if (!channel.isOpen()) {
            return;
        }
        final UserConnection user = this.getUserConnection(channel);
        if (user == null) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Could not find UserConnection for logging-in player {0}", player.getUniqueId());
            return;
        }
        final ProtocolInfo info = user.getProtocolInfo();
        info.setUuid(player.getUniqueId());
        info.setUsername(player.getName());
        Via.getManager().getConnectionManager().onLoginSuccess(user);
    }
    
    @Nullable
    private UserConnection getUserConnection(final Channel channel) {
        final BukkitEncodeHandler encoder = channel.pipeline().get(BukkitEncodeHandler.class);
        return (encoder != null) ? encoder.connection() : null;
    }
    
    private Channel getChannel(final Player player) throws Exception {
        final Object entityPlayer = JoinListener.GET_HANDLE.invoke(player, new Object[0]);
        final Object pc = JoinListener.CONNECTION.get(entityPlayer);
        final Object nm = JoinListener.NETWORK_MANAGER.get(pc);
        return (Channel)JoinListener.CHANNEL.get(nm);
    }
    
    static {
        Method getHandleMethod = null;
        Field gamePacketListenerField = null;
        Field connectionField = null;
        Field channelField = null;
        try {
            getHandleMethod = NMSUtil.obc("entity.CraftPlayer").getDeclaredMethod("getHandle", (Class<?>[])new Class[0]);
            gamePacketListenerField = findField(getHandleMethod.getReturnType(), "PlayerConnection", "ServerGamePacketListenerImpl");
            connectionField = findField(gamePacketListenerField.getType(), "NetworkManager", "Connection");
            channelField = findField(connectionField.getType(), Class.forName("io.netty.channel.Channel"));
        }
        catch (final NoSuchMethodException | NoSuchFieldException | ClassNotFoundException e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Couldn't find reflection methods/fields to access Channel from player.\nLogin race condition fixer will be disabled.\n Some plugins that use ViaAPI on join event may work incorrectly.", e);
        }
        GET_HANDLE = getHandleMethod;
        CONNECTION = gamePacketListenerField;
        NETWORK_MANAGER = connectionField;
        CHANNEL = channelField;
    }
}
