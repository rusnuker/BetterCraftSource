// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

public interface ThreadProperties
{
    Thread.State state();
    
    int priority();
    
    boolean isInterrupted();
    
    boolean isDaemon();
    
    String name();
    
    long id();
    
    StackTraceElement[] stackTrace();
    
    boolean isAlive();
}
