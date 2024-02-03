// 
// Decompiled by Procyon v0.6.0
// 

package tv.twitch.chat;

import java.util.HashSet;

public class ChatTokenizedMessage
{
    public String displayName;
    public HashSet<ChatUserMode> modes;
    public HashSet<ChatUserSubscription> subscriptions;
    public int nameColorARGB;
    public ChatMessageToken[] tokenList;
    public boolean action;
    
    public ChatTokenizedMessage() {
        this.modes = new HashSet<ChatUserMode>();
        this.subscriptions = new HashSet<ChatUserSubscription>();
    }
}
