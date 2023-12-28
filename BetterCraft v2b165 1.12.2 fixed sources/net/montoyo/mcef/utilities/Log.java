// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Log
{
    public static void info(final String what, final Object... data) {
        LogManager.getLogger("MCEF").log(Level.INFO, String.format(what, data));
    }
    
    public static void warning(final String what, final Object... data) {
        LogManager.getLogger("MCEF").log(Level.WARN, String.format(what, data));
    }
    
    public static void error(final String what, final Object... data) {
        LogManager.getLogger("MCEF").log(Level.ERROR, String.format(what, data));
    }
    
    public static void errorEx(final String what, final Throwable t, final Object... data) {
        LogManager.getLogger("MCEF").log(Level.ERROR, String.format(what, data), t);
    }
}
