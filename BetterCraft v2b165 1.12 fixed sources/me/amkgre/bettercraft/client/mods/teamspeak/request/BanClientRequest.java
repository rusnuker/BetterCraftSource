// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import com.google.common.base.Strings;

public class BanClientRequest extends Request
{
    public BanClientRequest(final int clientId, final String reason, final int time) {
        super("banclient", new Parameter[0]);
        this.addParam(Request.value("clid", clientId));
        if (!Strings.isNullOrEmpty(reason)) {
            this.addParam(Request.value("banreason", reason));
        }
        this.addParam(Request.value("time", time));
    }
}
