// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.authlib;

public interface GameProfileRepository
{
    void findProfilesByNames(final String[] p0, final Agent p1, final ProfileLookupCallback p2);
}
