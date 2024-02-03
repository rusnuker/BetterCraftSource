// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;

public class NullConfiguration extends BaseConfiguration
{
    public static final String NULL_NAME = "Null";
    
    public NullConfiguration() {
        this.setName("Null");
        final LoggerConfig root = this.getRootLogger();
        root.setLevel(Level.OFF);
    }
}
