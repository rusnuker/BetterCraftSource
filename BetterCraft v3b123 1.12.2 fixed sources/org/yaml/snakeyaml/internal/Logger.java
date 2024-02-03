// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.internal;

import java.util.logging.Level;

public class Logger
{
    private final java.util.logging.Logger logger;
    
    private Logger(final String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }
    
    public static Logger getLogger(final String name) {
        return new Logger(name);
    }
    
    public boolean isLoggable(final Level level) {
        return this.logger.isLoggable(level.level);
    }
    
    public void warn(final String msg) {
        this.logger.log(Level.WARNING.level, msg);
    }
    
    public enum Level
    {
        WARNING(java.util.logging.Level.FINE);
        
        private final java.util.logging.Level level;
        
        private Level(final java.util.logging.Level level) {
            this.level = level;
        }
    }
}
