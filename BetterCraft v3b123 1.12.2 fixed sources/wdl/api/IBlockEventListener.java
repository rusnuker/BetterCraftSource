// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.multiplayer.WorldClient;

public interface IBlockEventListener extends IWDLMod
{
    void onBlockEvent(final WorldClient p0, final BlockPos p1, final Block p2, final int p3, final int p4);
}
