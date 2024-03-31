// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

public interface IClassTracker
{
    void registerInvalidClass(final String p0);
    
    boolean isClassLoaded(final String p0);
    
    String getClassRestrictions(final String p0);
}
