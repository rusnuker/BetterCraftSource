// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class SetClientChannelGroupRequest extends Request
{
    public SetClientChannelGroupRequest(final int groupId, final int channelId, final int clientDatabaseId) {
        super("setclientchannelgroup", new Parameter[] { Request.value("cgid", groupId), Request.value("cid", channelId), Request.value("cldbid", clientDatabaseId) });
    }
}
