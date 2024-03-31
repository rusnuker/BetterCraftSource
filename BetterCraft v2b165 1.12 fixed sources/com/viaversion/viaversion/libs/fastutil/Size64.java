// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil;

public interface Size64
{
    long size64();
    
    @Deprecated
    default int size() {
        return (int)Math.min(2147483647L, this.size64());
    }
}
