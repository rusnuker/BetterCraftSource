// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import com.google.common.base.Strings;

public class ClientKickRequest extends Request
{
    public ClientKickRequest(final int reasonId, final String reason, final int clientId) {
        super("clientkick", new Parameter[0]);
        this.addParam(Request.value("reasonid", reasonId));
        if (!Strings.isNullOrEmpty(reason)) {
            this.addParam(Request.value("reasonmsg", reason));
        }
        this.addParam(Request.value("clid", clientId));
    }
}
