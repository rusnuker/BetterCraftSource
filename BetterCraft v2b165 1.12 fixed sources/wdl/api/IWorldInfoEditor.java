// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.client.multiplayer.WorldClient;

public interface IWorldInfoEditor extends IWDLMod
{
    void editWorldInfo(final WorldClient p0, final WorldInfo p1, final SaveHandler p2, final NBTTagCompound p3);
}
