// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.bossbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface BossBarImplementation
{
    @ApiStatus.Internal
    @NotNull
    default <I extends BossBarImplementation> I get(@NotNull final BossBar bar, @NotNull final Class<I> type) {
        return BossBarImpl.ImplementationAccessor.get(bar, type);
    }
    
    @ApiStatus.Internal
    public interface Provider
    {
        @ApiStatus.Internal
        @NotNull
        BossBarImplementation create(@NotNull final BossBar bar);
    }
}
