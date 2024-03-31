// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ClientUnMuteRequest extends Request
{
    public ClientUnMuteRequest(final int clientId) {
        super("clientunmute", new Parameter[] { Request.value("clid", clientId) });
    }
}
