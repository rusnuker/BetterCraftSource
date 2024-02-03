// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.scheduler;

import com.viaversion.viaversion.api.scheduler.TaskStatus;
import java.util.concurrent.Future;
import com.viaversion.viaversion.api.scheduler.Task;

public final class SubmittedTask implements Task
{
    private final Future<?> future;
    
    public SubmittedTask(final Future<?> future) {
        this.future = future;
    }
    
    @Override
    public TaskStatus status() {
        return this.future.isDone() ? TaskStatus.STOPPED : TaskStatus.RUNNING;
    }
    
    @Override
    public void cancel() {
        this.future.cancel(false);
    }
}
