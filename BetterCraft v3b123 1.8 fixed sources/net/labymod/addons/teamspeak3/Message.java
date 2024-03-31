// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

public class Message
{
    private TeamSpeakUser user;
    private String message;
    
    public Message(final TeamSpeakUser user, final String message) {
        this.user = user;
        this.message = message;
    }
    
    public TeamSpeakUser getUser() {
        return this.user;
    }
    
    public String getMessage() {
        return this.message;
    }
}
