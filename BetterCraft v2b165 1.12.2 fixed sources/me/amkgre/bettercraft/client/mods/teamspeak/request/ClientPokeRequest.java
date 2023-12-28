// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ClientPokeRequest extends Request
{
    public ClientPokeRequest(final int clientId, final String message) {
        super("clientpoke", new Parameter[] { Request.value("msg", message), Request.value("clid", clientId) });
    }
}
