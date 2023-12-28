// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ChannelListRequest extends Request
{
    public ChannelListRequest() {
        super("channellist", new Parameter[] { Request.option("topic"), Request.option("flags"), Request.option("voice"), Request.option("icon"), Request.option("limits") });
    }
}
