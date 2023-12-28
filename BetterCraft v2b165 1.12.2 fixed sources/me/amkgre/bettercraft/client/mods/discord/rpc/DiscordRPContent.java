// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

public class DiscordRPContent
{
    private String firstLine;
    private String secondLine;
    private String hoverImageFirstLine;
    private String hoverImageSecondLine;
    
    public DiscordRPContent(final String firstLine, final String secondLine, final String hoverImageFirstLine, final String hoverImageSecondLine) {
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.hoverImageFirstLine = hoverImageFirstLine;
        this.hoverImageSecondLine = hoverImageSecondLine;
    }
    
    public String getFirstLine() {
        return this.firstLine;
    }
    
    public String getSecondLine() {
        return this.secondLine;
    }
    
    public String getHoverImageFirstLine() {
        return this.hoverImageFirstLine;
    }
    
    public String getHoverImageSecondLine() {
        return this.hoverImageSecondLine;
    }
}
