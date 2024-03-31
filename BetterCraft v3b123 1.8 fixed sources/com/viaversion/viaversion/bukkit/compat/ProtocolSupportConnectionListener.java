// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.compat;

import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.api.Via;
import protocolsupport.api.Connection.PacketListener;
import java.lang.reflect.Method;
import protocolsupport.api.Connection;

final class ProtocolSupportConnectionListener extends Connection.PacketListener
{
    static final Method ADD_PACKET_LISTENER_METHOD;
    private static final Class<?> HANDSHAKE_PACKET_CLASS;
    private static final Method GET_VERSION_METHOD;
    private static final Method SET_VERSION_METHOD;
    private static final Method REMOVE_PACKET_LISTENER_METHOD;
    private static final Method GET_LATEST_METHOD;
    private static final Object PROTOCOL_VERSION_MINECRAFT_FUTURE;
    private static final Object PROTOCOL_TYPE_PC;
    private final Object connection;
    
    ProtocolSupportConnectionListener(final Object connection) {
        this.connection = connection;
    }
    
    public void onPacketReceiving(final PacketListener.PacketEvent event) {
        try {
            if (ProtocolSupportConnectionListener.HANDSHAKE_PACKET_CLASS.isInstance(event.getPacket()) && ProtocolSupportConnectionListener.GET_VERSION_METHOD.invoke(this.connection, new Object[0]) == ProtocolSupportConnectionListener.PROTOCOL_VERSION_MINECRAFT_FUTURE) {
                final Object packet = event.getPacket();
                final int protocolVersion = (int)ProtocolSupportConnectionListener.HANDSHAKE_PACKET_CLASS.getDeclaredMethod(ProtocolSupportCompat.handshakeVersionMethod().methodName(), (Class<?>[])new Class[0]).invoke(packet, new Object[0]);
                if (protocolVersion == Via.getAPI().getServerVersion().lowestSupportedVersion()) {
                    ProtocolSupportConnectionListener.SET_VERSION_METHOD.invoke(this.connection, ProtocolSupportConnectionListener.GET_LATEST_METHOD.invoke(null, ProtocolSupportConnectionListener.PROTOCOL_TYPE_PC));
                }
            }
            ProtocolSupportConnectionListener.REMOVE_PACKET_LISTENER_METHOD.invoke(this.connection, this);
        }
        catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        try {
            HANDSHAKE_PACKET_CLASS = NMSUtil.nms("PacketHandshakingInSetProtocol", "net.minecraft.network.protocol.handshake.PacketHandshakingInSetProtocol");
            final Class<?> connectionImplClass = Class.forName("protocolsupport.protocol.ConnectionImpl");
            final Class<?> connectionClass = Class.forName("protocolsupport.api.Connection");
            final Class<?> packetListenerClass = Class.forName("protocolsupport.api.Connection$PacketListener");
            final Class<?> protocolVersionClass = Class.forName("protocolsupport.api.ProtocolVersion");
            final Class<?> protocolTypeClass = Class.forName("protocolsupport.api.ProtocolType");
            GET_VERSION_METHOD = connectionClass.getDeclaredMethod("getVersion", (Class<?>[])new Class[0]);
            SET_VERSION_METHOD = connectionImplClass.getDeclaredMethod("setVersion", protocolVersionClass);
            PROTOCOL_VERSION_MINECRAFT_FUTURE = protocolVersionClass.getDeclaredField("MINECRAFT_FUTURE").get(null);
            GET_LATEST_METHOD = protocolVersionClass.getDeclaredMethod("getLatest", protocolTypeClass);
            PROTOCOL_TYPE_PC = protocolTypeClass.getDeclaredField("PC").get(null);
            ADD_PACKET_LISTENER_METHOD = connectionClass.getDeclaredMethod("addPacketListener", packetListenerClass);
            REMOVE_PACKET_LISTENER_METHOD = connectionClass.getDeclaredMethod("removePacketListener", packetListenerClass);
        }
        catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
