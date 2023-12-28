// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import me.amkgre.bettercraft.client.mods.teamspeak.api.MessageTargetMode;

public class SendTextMessageRequest extends Request
{
    public SendTextMessageRequest(final MessageTargetMode mode, final String message) {
        super("sendtextmessage", new Parameter[] { Request.value("targetmode", mode.getId()), Request.value("msg", message) });
    }
    
    public SendTextMessageRequest(final MessageTargetMode mode, final int clientId, final String message) {
        super("sendtextmessage", new Parameter[] { Request.value("targetmode", mode.getId()), Request.value("target", clientId), Request.value("msg", message) });
    }
}
