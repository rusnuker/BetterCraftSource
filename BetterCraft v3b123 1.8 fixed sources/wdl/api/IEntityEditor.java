// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.entity.Entity;

public interface IEntityEditor extends IWDLMod
{
    boolean shouldEdit(final Entity p0);
    
    void editEntity(final Entity p0);
}
