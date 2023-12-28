// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;

public class ClientMovedEvent extends Event
{
    public final Client client;
    public final Channel from;
    public final Channel to;
    public final Reason reason;
    public final Client invoker;
    public final String reasonMessage;
    
    public ClientMovedEvent(final ServerTab tab, final Client client, final Channel from, final Channel to, final Reason reason, final Client invoker, final String reasonMessage) {
        super(tab);
        this.client = client;
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.invoker = invoker;
        this.reasonMessage = reasonMessage;
    }
    
    public enum Reason
    {
        MOVED_SELF("MOVED_SELF", 0, 0), 
        MOVED_BY_OTHER("MOVED_BY_OTHER", 1, 1), 
        KICKED("KICKED", 2, 4);
        
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
            return Reason.MOVED_SELF;
        }
    }
}
