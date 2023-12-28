// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay;

import net.labymod.main.LabyMod;
import net.labymod.labyplay.party.PartySystem;

public class LabyPlay
{
    private PartySystem partySystem;
    private LabyMod labyMod;
    
    public LabyPlay(final LabyMod labyMod) {
        this.labyMod = labyMod;
        this.partySystem = new PartySystem(labyMod);
    }
    
    public PartySystem getPartySystem() {
        return this.partySystem;
    }
}
