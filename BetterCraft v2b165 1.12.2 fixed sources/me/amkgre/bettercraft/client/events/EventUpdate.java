// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.events;

import com.darkmagician6.eventapi.events.Event;

public class EventUpdate implements Event
{
    public boolean Cancellable;
    
    public boolean isCancellable() {
        return this.Cancellable;
    }
    
    public void setCancellable(final boolean cancellable) {
        this.Cancellable = cancellable;
    }
}
