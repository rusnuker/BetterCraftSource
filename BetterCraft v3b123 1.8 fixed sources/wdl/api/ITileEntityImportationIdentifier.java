// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public interface ITileEntityImportationIdentifier extends IWDLMod
{
    boolean shouldImportTileEntity(final String p0, final BlockPos p1, final Block p2, final NBTTagCompound p3, final Chunk p4);
}
