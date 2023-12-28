// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.api;

public interface IJSQueryHandler
{
    boolean handleQuery(final IBrowser p0, final long p1, final String p2, final boolean p3, final IJSQueryCallback p4);
    
    void cancelQuery(final IBrowser p0, final long p1);
}
