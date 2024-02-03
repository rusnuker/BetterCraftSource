// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.platform;

public interface PlatformTask<T>
{
    @Deprecated
    T getObject();
    
    void cancel();
}
