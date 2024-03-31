// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class HashPasswordRequest extends Request
{
    public HashPasswordRequest(final String password) {
        super("hashpassword", new Parameter[] { Request.value("password", password) });
    }
}
