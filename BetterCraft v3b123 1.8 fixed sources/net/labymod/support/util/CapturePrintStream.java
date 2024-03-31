// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support.util;

import java.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class CapturePrintStream extends PrintStream
{
    private static final Logger LOGGER;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public CapturePrintStream(final OutputStream outStream) {
        super(outStream);
    }
    
    @Override
    public void println(final String string) {
        this.logString(string);
    }
    
    @Override
    public void println(final Object obj) {
        if (obj == null) {
            this.logString("null");
            return;
        }
        if (obj instanceof Throwable) {
            CapturePrintStream.LOGGER.catching((Throwable)obj);
        }
        else if (!obj.toString().startsWith("\t")) {
            this.logString(String.valueOf(obj));
        }
    }
    
    private void logString(final String string) {
        CapturePrintStream.LOGGER.info("{}", string);
    }
}
