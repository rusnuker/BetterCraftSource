// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import me.amkgre.bettercraft.client.mods.teamspeak.event.EventType;

public class ClientNotifyRegisterRequest extends Request
{
    public ClientNotifyRegisterRequest(final EventType eventType) {
        this(0, eventType);
    }
    
    public ClientNotifyRegisterRequest(final int id, final EventType eventType) {
        super("clientnotifyregister", new Parameter[] { Request.value("schandlerid", id), Request.value("event", eventType.getName()) });
    }
}
