// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.authlib;

public interface ProfileLookupCallback
{
    void onProfileLookupSucceeded(final GameProfile p0);
    
    void onProfileLookupFailed(final GameProfile p0, final Exception p1);
}
