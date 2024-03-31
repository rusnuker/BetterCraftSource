// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.identity;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

final class NilIdentity implements Identity
{
    static final UUID NIL_UUID;
    static final Identity INSTANCE;
    
    @NotNull
    @Override
    public UUID uuid() {
        return NilIdentity.NIL_UUID;
    }
    
    @Override
    public String toString() {
        return "Identity.nil()";
    }
    
    @Override
    public boolean equals(@Nullable final Object that) {
        return this == that;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    static {
        NIL_UUID = new UUID(0L, 0L);
        INSTANCE = new NilIdentity();
    }
}
