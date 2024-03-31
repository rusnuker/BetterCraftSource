// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote.keys.provider;

import net.labymod.user.emote.keys.PoseAtTime;

public abstract class EmoteProvider
{
    public abstract boolean hasNext(final int p0);
    
    public abstract boolean isWaiting();
    
    public abstract PoseAtTime next(final int p0);
    
    public abstract void clear();
}
