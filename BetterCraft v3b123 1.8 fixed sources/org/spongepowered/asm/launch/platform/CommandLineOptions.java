// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public final class CommandLineOptions
{
    private List<String> configs;
    
    private CommandLineOptions() {
        this.configs = new ArrayList<String>();
    }
    
    public List<String> getConfigs() {
        return Collections.unmodifiableList((List<? extends String>)this.configs);
    }
    
    private void parseArgs(final List<String> args) {
        boolean captureNext = false;
        for (final String arg : args) {
            if (captureNext) {
                this.configs.add(arg);
            }
            captureNext = ("--mixin".equals(arg) || "--mixin.config".equals(arg));
        }
    }
    
    public static CommandLineOptions defaultArgs() {
        return ofArgs(null);
    }
    
    public static CommandLineOptions ofArgs(List<String> args) {
        final CommandLineOptions options = new CommandLineOptions();
        if (args == null) {
            final String argv = System.getProperty("sun.java.command");
            if (argv != null) {
                args = Arrays.asList(argv.split(" "));
            }
        }
        if (args != null) {
            options.parseArgs(args);
        }
        return options;
    }
    
    public static CommandLineOptions of(final List<String> configs) {
        final CommandLineOptions options = new CommandLineOptions();
        options.configs.addAll(configs);
        return options;
    }
}
