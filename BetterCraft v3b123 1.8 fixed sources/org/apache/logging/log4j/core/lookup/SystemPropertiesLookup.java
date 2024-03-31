// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "sys", category = "Lookup")
public class SystemPropertiesLookup implements StrLookup
{
    @Override
    public String lookup(final String key) {
        try {
            return System.getProperty(key);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        try {
            return System.getProperty(key);
        }
        catch (final Exception ex) {
            return null;
        }
    }
}
