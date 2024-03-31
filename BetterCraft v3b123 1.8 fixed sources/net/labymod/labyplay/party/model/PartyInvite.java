// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party.model;

import java.beans.ConstructorProperties;
import java.util.UUID;

public class PartyInvite
{
    private String username;
    private UUID partyUUID;
    private String partyName;
    
    public String getUsername() {
        return this.username;
    }
    
    public UUID getPartyUUID() {
        return this.partyUUID;
    }
    
    public String getPartyName() {
        return this.partyName;
    }
    
    @ConstructorProperties({ "username", "partyUUID", "partyName" })
    public PartyInvite(final String username, final UUID partyUUID, final String partyName) {
        this.username = username;
        this.partyUUID = partyUUID;
        this.partyName = partyName;
    }
}
