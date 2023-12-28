// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform
{
    static boolean isInstanceOfThrowableClass(@Nullable final Throwable t, final Class<? extends Throwable> expectedClass) {
        return expectedClass.isInstance(t);
    }
    
    private Platform() {
    }
}
