// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public abstract class Command
{
    private String name;
    private String[] aliases;
    
    protected Command(final String name, final String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }
    
    public abstract void run(final String p0, final String[] p1);
    
    public abstract List<String> autocomplete(final int p0, final String[] p1);
    
    boolean match(final String name) {
        String[] aliases;
        for (int length = (aliases = this.aliases).length, i = 0; i < length; ++i) {
            final String alias = aliases[i];
            if (alias.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return this.name.equalsIgnoreCase(name);
    }
    
    public List<String> getNameAndAliases() {
        final List<String> l = new ArrayList<String>();
        l.add(this.name);
        l.addAll(Arrays.asList(this.aliases));
        return l;
    }
}
