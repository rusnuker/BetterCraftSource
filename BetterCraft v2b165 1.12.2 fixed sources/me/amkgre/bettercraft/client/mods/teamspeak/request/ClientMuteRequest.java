// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ClientMuteRequest extends Request
{
    public ClientMuteRequest(final int clientId) {
        super("clientmute", new Parameter[] { Request.value("clid", clientId) });
    }
}
