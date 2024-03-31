// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.server;

import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;

public class DebugLoggingPrintStream extends LoggingPrintStream
{
    public DebugLoggingPrintStream(final String p_i47315_1_, final OutputStream p_i47315_2_) {
        super(p_i47315_1_, p_i47315_2_);
    }
    
    @Override
    protected void logString(final String string) {
        final StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        final StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
        DebugLoggingPrintStream.LOGGER.info("[{}]@.({}:{}): {}", this.domain, stacktraceelement.getFileName(), stacktraceelement.getLineNumber(), string);
    }
}
