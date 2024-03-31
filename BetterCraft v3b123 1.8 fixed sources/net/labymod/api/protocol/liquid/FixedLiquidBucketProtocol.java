// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.protocol.liquid;

import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.labymod.support.util.Debug;
import net.labymod.api.permissions.Permissions;

public class FixedLiquidBucketProtocol
{
    public static void handleBucketAction(final Action bucketAction, final int x, final int y, final int z) {
        if (!Permissions.isAllowed(Permissions.Permission.IMPROVED_LAVA)) {
            Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "Can't handle LAVA_UPDATE with action " + bucketAction.name() + " - not allowed!");
            return;
        }
        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeInt(bucketAction.ordinal());
        if (bucketAction == Action.FILL_BUCKET || bucketAction == Action.EMPTY_BUCKET) {
            packetBuffer.writeInt(x);
            packetBuffer.writeInt(y);
            packetBuffer.writeInt(z);
            Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "LAVA_UPDATE with action " + bucketAction.name() + " at " + x + " " + y + " " + z);
        }
        else {
            Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "LAVA_UPDATE with action " + bucketAction.name());
        }
        LabyModCore.getMinecraft().sendPluginMessage("LAVA_UPDATE", packetBuffer);
    }
    
    public static void onPermissionUpdate(final boolean allowed) {
        if (!allowed) {
            return;
        }
        if (LabyMod.getSettings().improvedLavaFixedGhostBlocks) {
            handleBucketAction(Action.ENABLE, 0, 0, 0);
        }
    }
    
    public enum Action
    {
        FILL_BUCKET("FILL_BUCKET", 0), 
        EMPTY_BUCKET("EMPTY_BUCKET", 1), 
        ENABLE("ENABLE", 2), 
        DISABLE("DISABLE", 3);
        
        private Action(final String s, final int n) {
        }
    }
}
