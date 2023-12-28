// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.util.List;
import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.FutureTask;
import java.util.Locale;

public class Util
{
    public static EnumOS getOSType() {
        final String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (s.contains("win")) {
            return EnumOS.WINDOWS;
        }
        if (s.contains("mac")) {
            return EnumOS.OSX;
        }
        if (s.contains("solaris")) {
            return EnumOS.SOLARIS;
        }
        if (s.contains("sunos")) {
            return EnumOS.SOLARIS;
        }
        if (s.contains("linux")) {
            return EnumOS.LINUX;
        }
        return s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN;
    }
    
    @Nullable
    public static <V> V runTask(final FutureTask<V> task, final Logger logger) {
        try {
            task.run();
            return task.get();
        }
        catch (final ExecutionException executionexception) {
            logger.fatal("Error executing task", executionexception);
        }
        catch (final InterruptedException interruptedexception) {
            logger.fatal("Error executing task", interruptedexception);
        }
        return null;
    }
    
    public static <T> T getLastElement(final List<T> list) {
        return list.get(list.size() - 1);
    }
    
    public enum EnumOS
    {
        LINUX("LINUX", 0), 
        SOLARIS("SOLARIS", 1), 
        WINDOWS("WINDOWS", 2), 
        OSX("OSX", 3), 
        UNKNOWN("UNKNOWN", 4);
        
        private EnumOS(final String s, final int n) {
        }
    }
}
