// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.renderer;

import java.beans.ConstructorProperties;

public class ChatLine
{
    private String message;
    private boolean secondChat;
    private String room;
    private Object component;
    private int updateCounter;
    private int chatLineId;
    private Integer highlightColor;
    
    public String getRoom() {
        return (this.room == null) ? "Global" : this.room;
    }
    
    @ConstructorProperties({ "message", "secondChat", "room", "component", "updateCounter", "chatLineId", "highlightColor" })
    public ChatLine(final String message, final boolean secondChat, final String room, final Object component, final int updateCounter, final int chatLineId, final Integer highlightColor) {
        this.message = message;
        this.secondChat = secondChat;
        this.room = room;
        this.component = component;
        this.updateCounter = updateCounter;
        this.chatLineId = chatLineId;
        this.highlightColor = highlightColor;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public boolean isSecondChat() {
        return this.secondChat;
    }
    
    public Object getComponent() {
        return this.component;
    }
    
    public int getUpdateCounter() {
        return this.updateCounter;
    }
    
    public int getChatLineId() {
        return this.chatLineId;
    }
    
    public Integer getHighlightColor() {
        return this.highlightColor;
    }
}
