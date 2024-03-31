// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.user;

import java.beans.ConstructorProperties;
import net.labymod.labyconnect.GameIconHelper;
import com.mojang.authlib.GameProfile;

public class ChatUser
{
    private GameProfile gameProfile;
    private UserStatus status;
    private String statusMessage;
    private ServerInfo currentServerInfo;
    private int unreadMessages;
    private long lastInteraction;
    private long lastTyping;
    private String timeZone;
    private long lastOnline;
    private long firstJoined;
    private int contactAmount;
    private boolean party;
    
    public void increaseUnreadMessages() {
        ++this.unreadMessages;
        GameIconHelper.updateIcon(false, true);
    }
    
    public void setUnreadMessages(final int amount) {
        final boolean changed = this.unreadMessages != amount;
        this.unreadMessages = amount;
        if (changed) {
            GameIconHelper.updateIcon(amount == 0, false);
        }
    }
    
    public boolean isFriendRequest() {
        return this instanceof ChatRequest;
    }
    
    public boolean isOnline() {
        return this.status != UserStatus.OFFLINE;
    }
    
    public boolean equals(final ChatUser chatUser) {
        return (this.party && chatUser.party == this.party) || (!chatUser.party && !this.party && chatUser.getGameProfile().getId().equals(this.gameProfile.getId()));
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public UserStatus getStatus() {
        return this.status;
    }
    
    public String getStatusMessage() {
        return this.statusMessage;
    }
    
    public ServerInfo getCurrentServerInfo() {
        return this.currentServerInfo;
    }
    
    public int getUnreadMessages() {
        return this.unreadMessages;
    }
    
    public long getLastInteraction() {
        return this.lastInteraction;
    }
    
    public long getLastTyping() {
        return this.lastTyping;
    }
    
    public String getTimeZone() {
        return this.timeZone;
    }
    
    public long getLastOnline() {
        return this.lastOnline;
    }
    
    public long getFirstJoined() {
        return this.firstJoined;
    }
    
    public int getContactAmount() {
        return this.contactAmount;
    }
    
    public boolean isParty() {
        return this.party;
    }
    
    @ConstructorProperties({ "gameProfile", "status", "statusMessage", "currentServerInfo", "unreadMessages", "lastInteraction", "lastTyping", "timeZone", "lastOnline", "firstJoined", "contactAmount", "party" })
    public ChatUser(final GameProfile gameProfile, final UserStatus status, final String statusMessage, final ServerInfo currentServerInfo, final int unreadMessages, final long lastInteraction, final long lastTyping, final String timeZone, final long lastOnline, final long firstJoined, final int contactAmount, final boolean party) {
        this.unreadMessages = 0;
        this.lastInteraction = System.currentTimeMillis();
        this.gameProfile = gameProfile;
        this.status = status;
        this.statusMessage = statusMessage;
        this.currentServerInfo = currentServerInfo;
        this.unreadMessages = unreadMessages;
        this.lastInteraction = lastInteraction;
        this.lastTyping = lastTyping;
        this.timeZone = timeZone;
        this.lastOnline = lastOnline;
        this.firstJoined = firstJoined;
        this.contactAmount = contactAmount;
        this.party = party;
    }
    
    public void setGameProfile(final GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    public void setStatus(final UserStatus status) {
        this.status = status;
    }
    
    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    public void setCurrentServerInfo(final ServerInfo currentServerInfo) {
        this.currentServerInfo = currentServerInfo;
    }
    
    public void setLastInteraction(final long lastInteraction) {
        this.lastInteraction = lastInteraction;
    }
    
    public void setLastTyping(final long lastTyping) {
        this.lastTyping = lastTyping;
    }
    
    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }
    
    public void setLastOnline(final long lastOnline) {
        this.lastOnline = lastOnline;
    }
    
    public void setFirstJoined(final long firstJoined) {
        this.firstJoined = firstJoined;
    }
    
    public void setContactAmount(final int contactAmount) {
        this.contactAmount = contactAmount;
    }
    
    public void setParty(final boolean party) {
        this.party = party;
    }
}
