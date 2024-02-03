// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.scheduler;

import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.scheduler.Task;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import com.viaversion.viaversion.api.scheduler.Scheduler;

public final class TaskScheduler implements Scheduler
{
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;
    
    public TaskScheduler() {
        this.executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Via Async Task %d").build());
        this.scheduledExecutorService = Executors.newScheduledThreadPool(0, new ThreadFactoryBuilder().setNameFormat("Via Async Scheduler %d").build());
    }
    
    @Override
    public Task execute(final Runnable runnable) {
        return new SubmittedTask(this.executorService.submit(runnable));
    }
    
    @Override
    public Task schedule(final Runnable runnable, final long delay, final TimeUnit timeUnit) {
        return new ScheduledTask(this.scheduledExecutorService.schedule(runnable, delay, timeUnit));
    }
    
    @Override
    public Task scheduleRepeating(final Runnable runnable, final long delay, final long period, final TimeUnit timeUnit) {
        return new ScheduledTask(this.scheduledExecutorService.scheduleAtFixedRate(runnable, delay, period, timeUnit));
    }
    
    @Override
    public void shutdown() {
        this.executorService.shutdown();
        this.scheduledExecutorService.shutdown();
        try {
            this.executorService.awaitTermination(2L, TimeUnit.SECONDS);
            this.scheduledExecutorService.awaitTermination(2L, TimeUnit.SECONDS);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
