// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import java.lang.reflect.Method;

public class EventListener implements Comparable<EventListener>
{
    private Object listener;
    private Method listenerMethod;
    private EventHandler.Priority priority;
    
    public EventListener(final Object listener, final Method listenerMethod, final EventHandler.Priority priority) {
        this.listener = listener;
        this.listenerMethod = listenerMethod;
        this.priority = priority;
    }
    
    public Object getListener() {
        return this.listener;
    }
    
    public Method getListenerMethod() {
        return this.listenerMethod;
    }
    
    public EventHandler.Priority getPriority() {
        return this.priority;
    }
    
    @Override
    public int compareTo(final EventListener o) {
        return o.getPriority().ordinal() - this.getPriority().ordinal();
    }
}
