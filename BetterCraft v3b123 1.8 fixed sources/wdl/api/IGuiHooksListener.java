// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.client.multiplayer.WorldClient;

public interface IGuiHooksListener extends IWDLMod
{
    boolean onBlockGuiClosed(final WorldClient p0, final BlockPos p1, final Container p2);
    
    boolean onEntityGuiClosed(final WorldClient p0, final Entity p1, final Container p2);
}
