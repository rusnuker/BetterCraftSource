// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface ITileEntityEditor extends IWDLMod
{
    boolean shouldEdit(final BlockPos p0, final NBTTagCompound p1, final TileEntityCreationMode p2);
    
    void editTileEntity(final BlockPos p0, final NBTTagCompound p1, final TileEntityCreationMode p2);
    
    public enum TileEntityCreationMode
    {
        IMPORTED("IMPORTED", 0), 
        EXISTING("EXISTING", 1), 
        NEW("NEW", 2);
        
        private TileEntityCreationMode(final String s, final int n) {
        }
    }
}
