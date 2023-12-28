// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.events;

import com.darkmagician6.eventapi.events.Event;

public class ChatMessageSendEvent implements Event
{
    private String message;
    private boolean cancelled;
    
    public ChatMessageSendEvent(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
}
