// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.logging;

public class LoggerAdapterDefault extends LoggerAdapterAbstract
{
    public LoggerAdapterDefault(final String name) {
        super(name);
    }
    
    @Override
    public String getType() {
        return "Default Logger (No Logging)";
    }
    
    @Override
    public void catching(final Level level, final Throwable t) {
    }
    
    @Override
    public void log(final Level level, final String message, final Object... params) {
    }
    
    @Override
    public void log(final Level level, final String message, final Throwable t) {
    }
    
    @Override
    public <T extends Throwable> T throwing(final T t) {
        return null;
    }
}
