// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class SettableFuture<V> extends TrustedFuture<V>
{
    public static <V> SettableFuture<V> create() {
        return new SettableFuture<V>();
    }
    
    @CanIgnoreReturnValue
    public boolean set(@Nullable final V value) {
        return super.set(value);
    }
    
    @CanIgnoreReturnValue
    public boolean setException(final Throwable throwable) {
        return super.setException(throwable);
    }
    
    @Beta
    @CanIgnoreReturnValue
    public boolean setFuture(final ListenableFuture<? extends V> future) {
        return super.setFuture(future);
    }
    
    private SettableFuture() {
    }
}
