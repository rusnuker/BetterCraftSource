// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.events;

import com.google.gson.JsonElement;

public interface ServerMessageEvent
{
    void onServerMessage(final String p0, final JsonElement p1);
}
