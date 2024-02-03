// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.ArrayList;

public class Chat
{
    private TeamSpeakUser sender;
    private TeamSpeakUser chatOwner;
    private EnumTargetMode mode;
    private int global;
    private ArrayList<Message> log;
    
    public Chat(final TeamSpeakUser chatOwner, final TeamSpeakUser sender, final EnumTargetMode targetMode, final String message) {
        this.log = new ArrayList<Message>();
        this.sender = sender;
        this.mode = targetMode;
        this.chatOwner = chatOwner;
        this.log.add(new Message(sender, message));
    }
    
    public Chat(final TeamSpeakUser chatOwner, final TeamSpeakUser sender, final EnumTargetMode targetMode) {
        this.log = new ArrayList<Message>();
        this.sender = sender;
        this.mode = targetMode;
        this.chatOwner = chatOwner;
    }
    
    public Chat(final int id, final EnumTargetMode targetMode) {
        this.log = new ArrayList<Message>();
        this.sender = null;
        this.mode = targetMode;
        this.global = id;
    }
    
    public TeamSpeakUser getSender() {
        return this.sender;
    }
    
    public TeamSpeakUser getChatOwner() {
        return this.chatOwner;
    }
    
    public ArrayList<Message> getLog() {
        return this.log;
    }
    
    public void addMessage(final TeamSpeakUser sender, final String msg) {
        this.log.add(new Message(sender, msg));
    }
    
    public EnumTargetMode getTargetMode() {
        return this.mode;
    }
    
    public int getSlotId() {
        return (this.chatOwner == null) ? this.global : this.chatOwner.getClientId();
    }
}
