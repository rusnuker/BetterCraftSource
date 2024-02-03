// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.logging;

public abstract class LoggerAdapterAbstract implements ILogger
{
    private final String id;
    
    protected LoggerAdapterAbstract(final String id) {
        this.id = id;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void catching(final Throwable t) {
        this.catching(Level.WARN, t);
    }
    
    @Override
    public void debug(final String message, final Object... params) {
        this.log(Level.DEBUG, message, params);
    }
    
    @Override
    public void debug(final String message, final Throwable t) {
        this.log(Level.DEBUG, message, t);
    }
    
    @Override
    public void error(final String message, final Object... params) {
        this.log(Level.ERROR, message, params);
    }
    
    @Override
    public void error(final String message, final Throwable t) {
        this.log(Level.ERROR, message, t);
    }
    
    @Override
    public void fatal(final String message, final Object... params) {
        this.log(Level.FATAL, message, params);
    }
    
    @Override
    public void fatal(final String message, final Throwable t) {
        this.log(Level.FATAL, message, t);
    }
    
    @Override
    public void info(final String message, final Object... params) {
        this.log(Level.INFO, message, params);
    }
    
    @Override
    public void info(final String message, final Throwable t) {
        this.log(Level.INFO, message, t);
    }
    
    @Override
    public void trace(final String message, final Object... params) {
        this.log(Level.TRACE, message, params);
    }
    
    @Override
    public void trace(final String message, final Throwable t) {
        this.log(Level.TRACE, message, t);
    }
    
    @Override
    public void warn(final String message, final Object... params) {
        this.log(Level.WARN, message, params);
    }
    
    @Override
    public void warn(final String message, final Throwable t) {
        this.log(Level.WARN, message, t);
    }
    
    public static class FormattedMessage
    {
        private String message;
        private Throwable t;
        
        public FormattedMessage(final String message, final Object... params) {
            if (params.length == 0) {
                this.message = message;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            int pos;
            int param;
            int delimPos;
            for (pos = 0, param = 0; pos < message.length() && param < params.length; pos = delimPos + 2, ++param) {
                delimPos = message.indexOf("{}", pos);
                if (delimPos < 0) {
                    break;
                }
                sb.append(message.substring(pos, delimPos)).append(params[param]);
            }
            if (pos < message.length()) {
                sb.append(message.substring(pos));
            }
            if (param < params.length && params[params.length - 1] instanceof Throwable) {
                this.t = (Throwable)params[params.length - 1];
            }
            this.message = sb.toString();
        }
        
        @Override
        public String toString() {
            return this.message;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public boolean hasThrowable() {
            return this.t != null;
        }
        
        public Throwable getThrowable() {
            return this.t;
        }
    }
}
