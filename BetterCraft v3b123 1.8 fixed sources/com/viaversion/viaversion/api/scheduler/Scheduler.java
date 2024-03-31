// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.scheduler;

import java.util.concurrent.TimeUnit;

public interface Scheduler
{
    Task execute(final Runnable p0);
    
    Task schedule(final Runnable p0, final long p1, final TimeUnit p2);
    
    Task scheduleRepeating(final Runnable p0, final long p1, final long p2, final TimeUnit p3);
    
    void shutdown();
}
