// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay;

import net.labymod.labyplay.party.PartySystem;

public class LabyPlay
{
    private PartySystem partySystem;
    
    public LabyPlay() {
        this.partySystem = new PartySystem();
    }
    
    public PartySystem getPartySystem() {
        return this.partySystem;
    }
}
