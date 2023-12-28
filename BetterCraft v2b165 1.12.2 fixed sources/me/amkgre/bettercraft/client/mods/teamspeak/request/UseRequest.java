// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class UseRequest extends Request
{
    public UseRequest(final int id) {
        super("use", new Parameter[] { Request.value("schandlerid", id) });
    }
}
