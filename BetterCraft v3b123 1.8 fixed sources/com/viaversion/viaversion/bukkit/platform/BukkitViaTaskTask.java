// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.platform;

import com.viaversion.viaversion.api.scheduler.Task;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class BukkitViaTaskTask implements PlatformTask<Task>
{
    private final Task task;
    
    public BukkitViaTaskTask(final Task task) {
        this.task = task;
    }
    
    @Override
    public Task getObject() {
        return this.task;
    }
    
    @Override
    public void cancel() {
        this.task.cancel();
    }
}
