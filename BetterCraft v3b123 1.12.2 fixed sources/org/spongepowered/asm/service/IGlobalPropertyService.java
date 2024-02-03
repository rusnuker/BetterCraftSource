// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

public interface IGlobalPropertyService
{
    IPropertyKey resolveKey(final String p0);
    
     <T> T getProperty(final IPropertyKey p0);
    
    void setProperty(final IPropertyKey p0, final Object p1);
    
     <T> T getProperty(final IPropertyKey p0, final T p1);
    
    String getPropertyString(final IPropertyKey p0, final String p1);
}
