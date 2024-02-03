// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.scheduler;

public interface Task
{
    TaskStatus status();
    
    void cancel();
}
