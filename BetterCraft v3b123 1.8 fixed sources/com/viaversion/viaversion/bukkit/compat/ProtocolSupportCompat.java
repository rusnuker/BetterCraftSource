// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.compat;

import org.bukkit.event.EventException;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import org.bukkit.event.Event;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.ViaVersionPlugin;

public final class ProtocolSupportCompat
{
    public static void registerPSConnectListener(final ViaVersionPlugin plugin) {
        Via.getPlatform().getLogger().info("Registering ProtocolSupport compat connection listener");
        try {
            final Class<? extends Event> connectionOpenEvent = (Class<? extends Event>)Class.forName("protocolsupport.api.events.ConnectionOpenEvent");
            Bukkit.getPluginManager().registerEvent((Class)connectionOpenEvent, (Listener)new Listener() {}, EventPriority.HIGH, (listener, event) -> {
                try {
                    final Object connection = event.getClass().getMethod("getConnection", (Class<?>[])new Class[0]).invoke(event, new Object[0]);
                    final ProtocolSupportConnectionListener connectListener = new ProtocolSupportConnectionListener(connection);
                    ProtocolSupportConnectionListener.ADD_PACKET_LISTENER_METHOD.invoke(connection, connectListener);
                }
                catch (final ReflectiveOperationException e) {
                    Via.getPlatform().getLogger().log(Level.WARNING, "Error when handling ProtocolSupport event", e);
                }
            }, (Plugin)plugin);
        }
        catch (final ClassNotFoundException e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Unable to register ProtocolSupport listener", e);
        }
    }
    
    public static boolean isMultiplatformPS() {
        try {
            Class.forName("protocolsupport.zplatform.impl.spigot.network.pipeline.SpigotPacketEncoder");
            return true;
        }
        catch (final ClassNotFoundException e) {
            return false;
        }
    }
    
    static HandshakeProtocolType handshakeVersionMethod() {
        Class<?> clazz = null;
        try {
            clazz = NMSUtil.nms("PacketHandshakingInSetProtocol", "net.minecraft.network.protocol.handshake.PacketHandshakingInSetProtocol");
            clazz.getMethod("getProtocolVersion", (Class<?>[])new Class[0]);
            return HandshakeProtocolType.MAPPED;
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (final NoSuchMethodException ex) {
            try {
                if (clazz.getMethod("b", (Class<?>[])new Class[0]).getReturnType() == Integer.TYPE) {
                    return HandshakeProtocolType.OBFUSCATED_B;
                }
                if (clazz.getMethod("c", (Class<?>[])new Class[0]).getReturnType() == Integer.TYPE) {
                    return HandshakeProtocolType.OBFUSCATED_C;
                }
                throw new UnsupportedOperationException("Protocol version method not found in " + clazz.getSimpleName());
            }
            catch (final ReflectiveOperationException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
    
    enum HandshakeProtocolType
    {
        MAPPED("getProtocolVersion"), 
        OBFUSCATED_B("b"), 
        OBFUSCATED_C("c");
        
        private final String methodName;
        
        private HandshakeProtocolType(final String methodName) {
            this.methodName = methodName;
        }
        
        public String methodName() {
            return this.methodName;
        }
    }
}
