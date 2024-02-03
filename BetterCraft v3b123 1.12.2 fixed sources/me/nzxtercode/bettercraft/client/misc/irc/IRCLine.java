// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.irc;

import java.util.ArrayList;

public class IRCLine
{
    public static ArrayList<IRCLine> lines;
    public int ausrichtung;
    public String message;
    
    static {
        IRCLine.lines = new ArrayList<IRCLine>();
    }
    
    public IRCLine(final int ausrichtung, final String msg) {
        this.ausrichtung = ausrichtung;
        this.message = msg;
        IRCLine.lines.add(this);
    }
}
