// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.log;

import java.beans.ConstructorProperties;

public class MessageChatComponent
{
    private String sender;
    private long sentTime;
    private String message;
    
    public String getSender() {
        return this.sender;
    }
    
    public long getSentTime() {
        return this.sentTime;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @ConstructorProperties({ "sender", "sentTime", "message" })
    public MessageChatComponent(final String sender, final long sentTime, final String message) {
        this.sender = sender;
        this.sentTime = sentTime;
        this.message = message;
    }
}
