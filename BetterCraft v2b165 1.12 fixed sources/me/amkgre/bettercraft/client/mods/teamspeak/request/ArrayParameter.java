// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import com.google.common.base.Joiner;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EscapeUtil;

public class ArrayParameter extends Parameter
{
    private String[] parameters;
    
    ArrayParameter(final String... parameters) {
        this.parameters = new String[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            this.parameters[i] = EscapeUtil.escape(String.valueOf(parameters[i]));
        }
    }
    
    @Override
    public String serialize() {
        return Joiner.on(" ").join(this.parameters);
    }
}
