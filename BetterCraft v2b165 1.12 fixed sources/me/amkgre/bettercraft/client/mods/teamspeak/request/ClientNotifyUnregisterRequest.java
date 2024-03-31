// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import me.amkgre.bettercraft.client.mods.teamspeak.event.EventType;

public class ClientNotifyUnregisterRequest extends Request
{
    public ClientNotifyUnregisterRequest(final EventType eventType) {
        this(0, eventType);
    }
    
    public ClientNotifyUnregisterRequest(final int id, final EventType eventType) {
        super("clientnotifyunregister", new Parameter[] { Request.value("schandlerid", id), Request.value("event", eventType.getName()) });
    }
}
