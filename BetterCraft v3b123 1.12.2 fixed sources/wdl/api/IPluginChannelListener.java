// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.client.multiplayer.WorldClient;

public interface IPluginChannelListener extends IWDLMod
{
    void onPluginChannelPacket(final WorldClient p0, final String p1, final byte[] p2);
}
