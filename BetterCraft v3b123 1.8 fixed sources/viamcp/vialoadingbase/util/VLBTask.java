// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.util;

import com.viaversion.viaversion.api.scheduler.TaskStatus;
import com.viaversion.viaversion.api.scheduler.Task;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class VLBTask implements PlatformTask<Task>
{
    private final Task object;
    
    public VLBTask(final Task object) {
        this.object = object;
    }
    
    @Override
    public Task getObject() {
        return this.object;
    }
    
    @Override
    public void cancel() {
        this.object.cancel();
    }
    
    public TaskStatus getStatus() {
        return this.getObject().status();
    }
}
