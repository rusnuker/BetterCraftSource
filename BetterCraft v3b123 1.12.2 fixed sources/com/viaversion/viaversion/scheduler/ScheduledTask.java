// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.scheduler;

import java.util.concurrent.TimeUnit;
import com.viaversion.viaversion.api.scheduler.TaskStatus;
import java.util.concurrent.ScheduledFuture;
import com.viaversion.viaversion.api.scheduler.Task;

public final class ScheduledTask implements Task
{
    private final ScheduledFuture<?> future;
    
    public ScheduledTask(final ScheduledFuture<?> future) {
        this.future = future;
    }
    
    @Override
    public TaskStatus status() {
        if (this.future.getDelay(TimeUnit.MILLISECONDS) > 0L) {
            return TaskStatus.SCHEDULED;
        }
        return this.future.isDone() ? TaskStatus.STOPPED : TaskStatus.RUNNING;
    }
    
    @Override
    public void cancel() {
        this.future.cancel(false);
    }
}
