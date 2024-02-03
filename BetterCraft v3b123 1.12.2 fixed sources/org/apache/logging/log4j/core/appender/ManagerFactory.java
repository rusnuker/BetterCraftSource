// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

public interface ManagerFactory<M, T>
{
    M createManager(final String p0, final T p1);
}
