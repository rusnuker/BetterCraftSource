// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ChannelMoveRequest extends Request
{
    public ChannelMoveRequest(final int channelId, final int parentId, final int orderId) {
        super("channelmove", new Parameter[] { Request.value("cid", channelId), Request.value("cpid", parentId), Request.value("order", orderId) });
    }
}
