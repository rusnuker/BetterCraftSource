// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class AuthRequest extends Request
{
    public AuthRequest(final String apiKey) {
        super("auth", new Parameter[0]);
        this.addParam(Request.value("apikey", apiKey));
    }
}
