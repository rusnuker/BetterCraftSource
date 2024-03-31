// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.client.multiplayer.WorldClient;

public interface IChatMessageListener extends IWDLMod
{
    void onChat(final WorldClient p0, final String p1);
}
