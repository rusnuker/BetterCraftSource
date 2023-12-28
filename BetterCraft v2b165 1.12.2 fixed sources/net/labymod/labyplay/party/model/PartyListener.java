// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party.model;

import java.util.UUID;

public interface PartyListener
{
    void onInvitedPlayer(final String p0, final UUID p1, final String p2);
    
    void onInviteSuccess(final String p0, final UUID p1);
    
    void onChatMessage(final String p0, final String p1);
    
    void onSystemMessage(final PartyActionTypes.Message p0, final String[] p1);
    
    void onPartyLeft(final UUID p0);
    
    void onMemberList(final UUID p0, final PartyMember[] p1);
}
