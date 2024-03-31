// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

import org.apache.logging.log4j.LogManager;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakException;

public abstract class Callback<T>
{
    public abstract void onDone(final T p0);
    
    public void exceptionCaught(final TeamSpeakException exception) {
        LogManager.getLogger().error("An unhandled exception occurred", exception);
    }
}
