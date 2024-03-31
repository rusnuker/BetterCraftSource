// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class BasicCommandLineArguments
{
    @Parameter(names = { "--help", "-?", "-h" }, help = true, description = "Prints this help.")
    private boolean help;
    
    public static <T extends BasicCommandLineArguments> T parseCommandLine(final String[] mainArgs, final Class<?> clazz, final T args) {
        final JCommander jCommander = new JCommander(args);
        jCommander.setProgramName(clazz.getName());
        jCommander.setCaseSensitiveOptions(false);
        jCommander.parse(mainArgs);
        if (args.isHelp()) {
            jCommander.usage();
        }
        return args;
    }
    
    public boolean isHelp() {
        return this.help;
    }
    
    public void setHelp(final boolean help) {
        this.help = help;
    }
}
