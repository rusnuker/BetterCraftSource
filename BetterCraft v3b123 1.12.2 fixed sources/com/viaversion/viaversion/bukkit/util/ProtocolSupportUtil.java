// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.util;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import java.lang.reflect.Method;

public final class ProtocolSupportUtil
{
    private static final Method PROTOCOL_VERSION_METHOD;
    private static final Method GET_ID_METHOD;
    
    public static int getProtocolVersion(final Player player) {
        if (ProtocolSupportUtil.PROTOCOL_VERSION_METHOD == null) {
            return -1;
        }
        try {
            final Object version = ProtocolSupportUtil.PROTOCOL_VERSION_METHOD.invoke(null, player);
            return (int)ProtocolSupportUtil.GET_ID_METHOD.invoke(version, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    static {
        Method protocolVersionMethod = null;
        Method getIdMethod = null;
        try {
            protocolVersionMethod = Class.forName("protocolsupport.api.ProtocolSupportAPI").getMethod("getProtocolVersion", Player.class);
            getIdMethod = Class.forName("protocolsupport.api.ProtocolVersion").getMethod("getId", (Class<?>[])new Class[0]);
        }
        catch (final ReflectiveOperationException ex) {}
        PROTOCOL_VERSION_METHOD = protocolVersionMethod;
        GET_ID_METHOD = getIdMethod;
    }
}
