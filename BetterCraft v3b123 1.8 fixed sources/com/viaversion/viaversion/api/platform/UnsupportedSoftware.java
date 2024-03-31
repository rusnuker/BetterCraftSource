// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.platform;

public interface UnsupportedSoftware
{
    String getName();
    
    String getReason();
    
    String match();
    
    default boolean findMatch() {
        return this.match() != null;
    }
}
