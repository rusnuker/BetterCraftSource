// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.logging;

import java.util.Date;
import com.google.common.base.Strings;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

public class LoggerAdapterConsole extends LoggerAdapterAbstract
{
    private static final SimpleDateFormat DATE_FORMAT;
    private PrintStream debug;
    
    public LoggerAdapterConsole(final String name) {
        super(Strings.nullToEmpty(name));
    }
    
    @Override
    public String getType() {
        return "Default Console Logger";
    }
    
    public LoggerAdapterConsole setDebugStream(final PrintStream debug) {
        this.debug = debug;
        return this;
    }
    
    @Override
    public void catching(final Level level, final Throwable t) {
        this.log(Level.WARN, "Catching {}: {}", t.getClass().getName(), t.getMessage(), t);
    }
    
    @Override
    public void log(final Level level, final String message, final Object... params) {
        final PrintStream out = this.getOutputStream(level);
        if (out != null) {
            final FormattedMessage formatted = new FormattedMessage(message, params);
            out.println(String.format("[%s] [%s/%s] %s", LoggerAdapterConsole.DATE_FORMAT.format(new Date()), this.getId(), level, formatted));
            if (formatted.hasThrowable()) {
                formatted.getThrowable().printStackTrace(out);
            }
        }
    }
    
    @Override
    public void log(final Level level, final String message, final Throwable t) {
        final PrintStream out = this.getOutputStream(level);
        if (out != null) {
            out.println(String.format("[%s] [%s/%s] %s", LoggerAdapterConsole.DATE_FORMAT.format(new Date()), this.getId(), level, message));
            t.printStackTrace(out);
        }
    }
    
    @Override
    public <T extends Throwable> T throwing(final T t) {
        this.log(Level.WARN, "Throwing {}: {}", t.getClass().getName(), t.getMessage(), t);
        return t;
    }
    
    private PrintStream getOutputStream(final Level level) {
        if (level == Level.FATAL || level == Level.ERROR || level == Level.WARN) {
            return System.err;
        }
        if (level == Level.INFO) {
            return System.out;
        }
        if (level == Level.DEBUG) {
            return this.debug;
        }
        return null;
    }
    
    static {
        DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    }
}
