// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.io.Writer;
import org.apache.logging.log4j.core.LogEvent;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "YamlLayout", category = "Core", elementType = "layout", printObject = true)
public final class YamlLayout extends AbstractJacksonLayout
{
    private static final String DEFAULT_FOOTER = "";
    private static final String DEFAULT_HEADER = "";
    static final String CONTENT_TYPE = "application/yaml";
    
    protected YamlLayout(final Configuration config, final boolean locationInfo, final boolean properties, final boolean complete, final boolean compact, final boolean eventEol, final String headerPattern, final String footerPattern, final Charset charset, final boolean includeStacktrace) {
        super(config, new JacksonFactory.YAML(includeStacktrace).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("").build(), PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("").build());
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
        return "application/yaml; charset=" + this.getCharset();
    }
    
    @PluginFactory
    public static AbstractJacksonLayout createLayout(@PluginConfiguration final Configuration config, @PluginAttribute("locationInfo") final boolean locationInfo, @PluginAttribute("properties") final boolean properties, @PluginAttribute(value = "header", defaultString = "") final String headerPattern, @PluginAttribute(value = "footer", defaultString = "") final String footerPattern, @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) final boolean includeStacktrace) {
        return new YamlLayout(config, locationInfo, properties, false, false, true, headerPattern, footerPattern, charset, includeStacktrace);
    }
    
    public static AbstractJacksonLayout createDefaultLayout() {
        return new YamlLayout(new DefaultConfiguration(), false, false, false, false, false, "", "", StandardCharsets.UTF_8, true);
    }
    
    @Override
    public void toSerializable(final LogEvent event, final Writer writer) throws IOException {
        if (this.complete && this.eventCount > 0L) {
            writer.append((CharSequence)", ");
        }
        super.toSerializable(event, writer);
    }
}
