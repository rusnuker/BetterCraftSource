// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface Interner<E>
{
    @CanIgnoreReturnValue
    E intern(final E p0);
}
