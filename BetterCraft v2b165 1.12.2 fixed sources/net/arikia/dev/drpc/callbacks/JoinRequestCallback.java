// 
// Decompiled by Procyon v0.6.0
// 

package net.arikia.dev.drpc.callbacks;

import net.arikia.dev.drpc.DiscordUser;
import com.sun.jna.Callback;

public interface JoinRequestCallback extends Callback
{
    void apply(final DiscordUser p0);
}
