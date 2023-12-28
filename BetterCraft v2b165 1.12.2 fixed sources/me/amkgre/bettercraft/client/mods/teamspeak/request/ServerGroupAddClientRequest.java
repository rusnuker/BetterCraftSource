// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ServerGroupAddClientRequest extends Request
{
    public ServerGroupAddClientRequest(final int groupId, final int clientDatabaseId) {
        super("servergroupaddclient", new Parameter[] { Request.value("sgid", groupId), Request.value("cldbid", clientDatabaseId) });
    }
}
