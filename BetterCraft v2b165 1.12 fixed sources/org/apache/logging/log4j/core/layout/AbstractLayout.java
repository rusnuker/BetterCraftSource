// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.status.StatusLogger;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.LogEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import java.io.Serializable;

public abstract class AbstractLayout<T extends Serializable> implements Layout<T>
{
    protected static final Logger LOGGER;
    protected final Configuration configuration;
    protected long eventCount;
    protected final byte[] footer;
    protected final byte[] header;
    
    @Deprecated
    public AbstractLayout(final byte[] header, final byte[] footer) {
        this(null, header, footer);
    }
    
    public AbstractLayout(final Configuration configuration, final byte[] header, final byte[] footer) {
        this.configuration = configuration;
        this.header = header;
        this.footer = footer;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    @Override
    public byte[] getFooter() {
        return this.footer;
    }
    
    @Override
    public byte[] getHeader() {
        return this.header;
    }
    
    protected void markEvent() {
        ++this.eventCount;
    }
    
    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        final byte[] data = this.toByteArray(event);
        writeTo(data, 0, data.length, destination);
    }
    
    public static void writeTo(final byte[] data, int offset, int length, final ByteBufferDestination destination) {
        int chunk = 0;
        synchronized (destination) {
            ByteBuffer buffer = destination.getByteBuffer();
            do {
                if (length > buffer.remaining()) {
                    buffer = destination.drain(buffer);
                }
                chunk = Math.min(length, buffer.remaining());
                buffer.put(data, offset, chunk);
                offset += chunk;
                length -= chunk;
            } while (length > 0);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public abstract static class Builder<B extends Builder<B>>
    {
        @PluginConfiguration
        private Configuration configuration;
        @PluginBuilderAttribute
        private byte[] footer;
        @PluginBuilderAttribute
        private byte[] header;
        
        public B asBuilder() {
            return (B)this;
        }
        
        public Configuration getConfiguration() {
            return this.configuration;
        }
        
        public byte[] getFooter() {
            return this.footer;
        }
        
        public byte[] getHeader() {
            return this.header;
        }
        
        public B setConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this.asBuilder();
        }
        
        public B setFooter(final byte[] footer) {
            this.footer = footer;
            return this.asBuilder();
        }
        
        public B setHeader(final byte[] header) {
            this.header = header;
            return this.asBuilder();
        }
    }
}
