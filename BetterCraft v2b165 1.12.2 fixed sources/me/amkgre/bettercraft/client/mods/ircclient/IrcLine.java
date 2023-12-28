// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ircclient;

import java.util.ArrayList;

public class IrcLine
{
    public static ArrayList<IrcLine> lines;
    public int ausrichtung;
    public String message;
    
    static {
        IrcLine.lines = new ArrayList<IrcLine>();
    }
    
    public IrcLine(final int ausrichtung, final String msg) {
        this.ausrichtung = ausrichtung;
        this.message = msg;
        IrcLine.lines.add(this);
    }
}
