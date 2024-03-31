// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.client.multiplayer.WorldClient;

public interface IWorldLoadListener extends IWDLMod
{
    void onWorldLoad(final WorldClient p0, final boolean p1);
}
