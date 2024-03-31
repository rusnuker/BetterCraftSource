// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import com.google.common.base.Strings;

public class ClientMoveRequest extends Request
{
    public ClientMoveRequest(final int channelId, final int clientId, final String channelPassword) {
        super("clientmove", new Parameter[0]);
        this.addParam(Request.value("cid", channelId));
        if (!Strings.isNullOrEmpty(channelPassword)) {
            this.addParam(Request.value("cpw", channelPassword));
        }
        this.addParam(Request.value("clid", clientId));
    }
}
