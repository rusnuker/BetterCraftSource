// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ChannelDeleteRequest extends Request
{
    public ChannelDeleteRequest(final int channelId, final boolean force) {
        super("channeldelete", new Parameter[] { Request.value("cid", channelId), Request.value("force", force) });
    }
}
