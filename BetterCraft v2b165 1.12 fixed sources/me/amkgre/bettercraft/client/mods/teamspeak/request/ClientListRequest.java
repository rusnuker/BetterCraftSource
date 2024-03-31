// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ClientListRequest extends Request
{
    public ClientListRequest() {
        super("clientlist", new Parameter[] { Request.option("uid"), Request.option("away"), Request.option("voice"), Request.option("groups"), Request.option("icon"), Request.option("country") });
    }
}
