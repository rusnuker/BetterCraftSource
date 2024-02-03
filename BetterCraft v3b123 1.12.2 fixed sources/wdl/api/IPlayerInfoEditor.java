// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.client.entity.EntityPlayerSP;

public interface IPlayerInfoEditor extends IWDLMod
{
    void editPlayerInfo(final EntityPlayerSP p0, final SaveHandler p1, final NBTTagCompound p2);
}
