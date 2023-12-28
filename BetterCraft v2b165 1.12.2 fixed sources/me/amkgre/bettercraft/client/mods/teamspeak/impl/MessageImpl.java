// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.api.Message;

public class MessageImpl implements Message
{
    private final String message;
    private final long time;
    
    public MessageImpl(final String message, final long time) {
        this.message = message;
        this.time = time;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public long getTime() {
        return this.time;
    }
}
