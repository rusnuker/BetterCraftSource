// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

public interface RejectedExecutionHandler
{
    void rejected(final Runnable p0, final SingleThreadEventExecutor p1);
}
