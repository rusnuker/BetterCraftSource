// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ServerGroupDelClientRequest extends Request
{
    public ServerGroupDelClientRequest(final int groupId, final int clientDatabaseId) {
        super("servergroupdelclient", new Parameter[] { Request.value("sgid", groupId), Request.value("cldbid", clientDatabaseId) });
    }
}
