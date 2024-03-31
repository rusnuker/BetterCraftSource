// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import java.io.Serializable;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.core.LogEvent;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import com.fasterxml.jackson.databind.ObjectWriter;

abstract class AbstractJacksonLayout extends AbstractStringLayout
{
    protected static final String DEFAULT_EOL = "\r\n";
    protected static final String COMPACT_EOL = "";
    protected final String eol;
    protected final ObjectWriter objectWriter;
    protected final boolean compact;
    protected final boolean complete;
    
    protected AbstractJacksonLayout(final Configuration config, final ObjectWriter objectWriter, final Charset charset, final boolean compact, final boolean complete, final boolean eventEol, final Serializer headerSerializer, final Serializer footerSerializer) {
        super(config, charset, headerSerializer, footerSerializer);
        this.objectWriter = objectWriter;
        this.compact = compact;
        this.complete = complete;
        this.eol = ((compact && !eventEol) ? "" : "\r\n");
    }
    
    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilderWriter writer = new StringBuilderWriter();
        try {
            this.toSerializable(event, writer);
            return writer.toString();
        }
        catch (final IOException e) {
            AbstractJacksonLayout.LOGGER.error(e);
            return "";
        }
    }
    
    private static LogEvent convertMutableToLog4jEvent(final LogEvent event) {
        return (event instanceof MutableLogEvent) ? ((MutableLogEvent)event).createMemento() : event;
    }
    
    public void toSerializable(final LogEvent event, final Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
        this.objectWriter.writeValue(writer, (Object)convertMutableToLog4jEvent(event));
        writer.write(this.eol);
        this.markEvent();
    }
    
    public abstract static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B>
    {
        @PluginBuilderAttribute
        private boolean eventEol;
        @PluginBuilderAttribute
        private boolean compact;
        @PluginBuilderAttribute
        private boolean complete;
        
        public boolean getEventEol() {
            return this.eventEol;
        }
        
        public boolean isCompact() {
            return this.compact;
        }
        
        public boolean isComplete() {
            return this.complete;
        }
        
        public B setEventEol(final boolean eventEol) {
            this.eventEol = eventEol;
            return this.asBuilder();
        }
        
        public B setCompact(final boolean compact) {
            this.compact = compact;
            return this.asBuilder();
        }
        
        public B setComplete(final boolean complete) {
            this.complete = complete;
            return this.asBuilder();
        }
    }
}
