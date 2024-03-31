// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.datafix;

import net.minecraft.nbt.NBTTagCompound;

public interface IFixableData
{
    int getFixVersion();
    
    NBTTagCompound fixTagCompound(final NBTTagCompound p0);
}
