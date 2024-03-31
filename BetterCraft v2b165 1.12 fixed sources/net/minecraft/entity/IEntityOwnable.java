// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IEntityOwnable
{
    @Nullable
    UUID getOwnerId();
    
    @Nullable
    Entity getOwner();
}
