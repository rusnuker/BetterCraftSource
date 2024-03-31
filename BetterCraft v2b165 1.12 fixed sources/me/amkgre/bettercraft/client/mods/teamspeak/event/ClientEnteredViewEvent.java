// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;

public class ClientEnteredViewEvent extends Event
{
    public final Client client;
    public final Channel from;
    public final Channel to;
    public final Reason reason;
    
    public ClientEnteredViewEvent(final ServerTab tab, final Client client, final Channel from, final Channel to, final Reason reason) {
        super(tab);
        this.client = client;
        this.from = from;
        this.to = to;
        this.reason = reason;
    }
    
    public enum Reason
    {
        NONE("NONE", 0, 0);
        
        private int id;
        
        private Reason(final String s, final int n, final int id) {
            this.id = id;
        }
        
        public static Reason byId(final int id) {
            Reason[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Reason reason = values[i];
                if (reason.id == id) {
                    return reason;
                }
            }
            return Reason.NONE;
        }
    }
}
