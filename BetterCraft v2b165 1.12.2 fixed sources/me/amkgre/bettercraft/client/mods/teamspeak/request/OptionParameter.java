// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import me.amkgre.bettercraft.client.mods.teamspeak.util.EscapeUtil;

public class OptionParameter extends Parameter
{
    private String option;
    
    public OptionParameter(final String option) {
        this.option = option;
    }
    
    @Override
    public String serialize() {
        return "-" + EscapeUtil.escape(String.valueOf(this.option));
    }
}
