// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.permissions;

import java.util.Iterator;
import net.labymod.support.util.Debug;
import net.labymod.api.protocol.liquid.FixedLiquidBucketProtocol;
import net.labymod.main.LabyMod;
import java.util.Map;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import net.minecraft.network.PacketBuffer;

public class OldPluginMessage
{
    @Deprecated
    public static void handlePluginMessage(final String channel, final PacketBuffer data) {
        try {
            if (data != null && channel != null && channel.equals("LABYMOD")) {
                final int length = data.capacity();
                final byte[] array = new byte[length];
                for (int i = 0; i < length; ++i) {
                    array[i] = data.readByte();
                }
                final ByteArrayInputStream byteIn = new ByteArrayInputStream(array);
                final ObjectInputStream in = new ObjectInputStream(byteIn);
                final Map<String, Boolean> list = (Map<String, Boolean>)in.readObject();
                for (final Map.Entry<String, Boolean> entry : list.entrySet()) {
                    try {
                        final Permissions.Permission permission = Permissions.Permission.valueOf(entry.getKey().toUpperCase());
                        LabyMod.getInstance().getServerManager().getPermissionMap().put(permission, entry.getValue());
                        if (permission != Permissions.Permission.IMPROVED_LAVA) {
                            continue;
                        }
                        FixedLiquidBucketProtocol.onPermissionUpdate(entry.getValue());
                    }
                    catch (final Exception error2) {
                        Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "[OldVersionSupport] Permission " + entry.getKey() + " is not supported!");
                    }
                }
            }
        }
        catch (final Exception error3) {
            error3.printStackTrace();
        }
    }
}
