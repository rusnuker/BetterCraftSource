// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import java.net.URL;

public interface IClassProvider
{
    @Deprecated
    URL[] getClassPath();
    
    Class<?> findClass(final String p0) throws ClassNotFoundException;
    
    Class<?> findClass(final String p0, final boolean p1) throws ClassNotFoundException;
    
    Class<?> findAgentClass(final String p0, final boolean p1) throws ClassNotFoundException;
}
