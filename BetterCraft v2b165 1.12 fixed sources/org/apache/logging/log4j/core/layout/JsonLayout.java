// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.util.Builder;
import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.core.LogEvent;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "JsonLayout", category = "Core", elementType = "layout", printObject = true)
public final class JsonLayout extends AbstractJacksonLayout
{
    private static final String DEFAULT_FOOTER = "]";
    private static final String DEFAULT_HEADER = "[";
    static final String CONTENT_TYPE = "application/json";
    
    protected JsonLayout(final Configuration config, final boolean locationInfo, final boolean properties, final boolean encodeThreadContextAsList, final boolean complete, final boolean compact, final boolean eventEol, final String headerPattern, final String footerPattern, final Charset charset, final boolean includeStacktrace) {
        super(config, new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build());
    }
    
    @Override
    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final String str = this.serializeToString(this.getHeaderSerializer());
        if (str != null) {
            buf.append(str);
        }
        buf.append(this.eol);
        return this.getBytes(buf.toString());
    }
    
    @Override
    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        buf.append(this.eol);
        final String str = this.serializeToString(this.getFooterSerializer());
        if (str != null) {
            buf.append(str);
        }
        buf.append(this.eol);
        return this.getBytes(buf.toString());
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("version", "2.0");
        return result;
    }
    
    @Override
    public String getContentType() {
        return "application/json; charset=" + this.getCharset();
    }
    
    @Deprecated
    public static JsonLayout createLayout(@PluginConfiguration final Configuration config, @PluginAttribute("locationInfo") final boolean locationInfo, @PluginAttribute("properties") final boolean properties, @PluginAttribute("propertiesAsList") final boolean propertiesAsList, @PluginAttribute("complete") final boolean complete, @PluginAttribute("compact") final boolean compact, @PluginAttribute("eventEol") final boolean eventEol, @PluginAttribute(value = "header", defaultString = "[") final String headerPattern, @PluginAttribute(value = "footer", defaultString = "]") final String footerPattern, @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) final boolean includeStacktrace) {
        final boolean encodeThreadContextAsList = properties && propertiesAsList;
        return new JsonLayout(config, locationInfo, properties, encodeThreadContextAsList, complete, compact, eventEol, headerPattern, footerPattern, charset, includeStacktrace);
    }
    
    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }
    
    public static JsonLayout createDefaultLayout() {
        return new JsonLayout(new DefaultConfiguration(), false, false, false, false, false, false, "[", "]", StandardCharsets.UTF_8, true);
    }
    
    @Override
    public void toSerializable(final LogEvent event, final Writer writer) throws IOException {
        if (this.complete && this.eventCount > 0L) {
            writer.append((CharSequence)", ");
        }
        super.toSerializable(event, writer);
    }
    
    public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<JsonLayout>
    {
        @PluginBuilderAttribute
        private boolean locationInfo;
        @PluginBuilderAttribute
        private boolean properties;
        @PluginBuilderAttribute
        private boolean propertiesAsList;
        @PluginBuilderAttribute
        private boolean includeStacktrace;
        
        public Builder() {
            this.includeStacktrace = true;
            this.setCharset(StandardCharsets.UTF_8);
        }
        
        @Override
        public JsonLayout build() {
            final boolean encodeThreadContextAsList = this.properties && this.propertiesAsList;
            final String headerPattern = this.toStringOrNull(this.getHeader());
            final String footerPattern = this.toStringOrNull(this.getFooter());
            return new JsonLayout(this.getConfiguration(), this.locationInfo, this.properties, encodeThreadContextAsList, this.isComplete(), this.isCompact(), this.getEventEol(), headerPattern, footerPattern, this.getCharset(), this.includeStacktrace);
        }
        
        private String toStringOrNull(final byte[] header) {
            return (header == null) ? null : new String(header, Charset.defaultCharset());
        }
        
        public boolean isLocationInfo() {
            return this.locationInfo;
        }
        
        public boolean isProperties() {
            return this.properties;
        }
        
        public boolean isPropertiesAsList() {
            return this.propertiesAsList;
        }
        
        public boolean isIncludeStacktrace() {
            return this.includeStacktrace;
        }
        
        public B setLocationInfo(final boolean locationInfo) {
            this.locationInfo = locationInfo;
            return this.asBuilder();
        }
        
        public B setProperties(final boolean properties) {
            this.properties = properties;
            return this.asBuilder();
        }
        
        public B setPropertiesAsList(final boolean propertiesAsList) {
            this.propertiesAsList = propertiesAsList;
            return this.asBuilder();
        }
        
        public B setIncludeStacktrace(final boolean includeStacktrace) {
            this.includeStacktrace = includeStacktrace;
            return this.asBuilder();
        }
    }
}
