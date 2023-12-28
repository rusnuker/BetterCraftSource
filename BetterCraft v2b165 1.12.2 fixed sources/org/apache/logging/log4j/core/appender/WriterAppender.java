// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.CloseShieldWriter;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.Writer;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Writer", category = "Core", elementType = "appender", printObject = true)
public final class WriterAppender extends AbstractWriterAppender<WriterManager>
{
    private static WriterManagerFactory factory;
    
    @PluginFactory
    public static WriterAppender createAppender(StringLayout layout, final Filter filter, final Writer target, final String name, final boolean follow, final boolean ignore) {
        if (name == null) {
            WriterAppender.LOGGER.error("No name provided for WriterAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new WriterAppender(name, layout, filter, getManager(target, follow, layout), ignore);
    }
    
    private static WriterManager getManager(final Writer target, final boolean follow, final StringLayout layout) {
        final Writer writer = new CloseShieldWriter(target);
        final String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
        return WriterManager.getManager(managerName, new FactoryData(writer, managerName, layout), WriterAppender.factory);
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private WriterAppender(final String name, final StringLayout layout, final Filter filter, final WriterManager manager, final boolean ignoreExceptions) {
        super(name, layout, filter, ignoreExceptions, true, manager);
    }
    
    static {
        WriterAppender.factory = new WriterManagerFactory();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<WriterAppender>
    {
        private Filter filter;
        private boolean follow;
        private boolean ignoreExceptions;
        private StringLayout layout;
        private String name;
        private Writer target;
        
        public Builder() {
            this.follow = false;
            this.ignoreExceptions = true;
            this.layout = PatternLayout.createDefaultLayout();
        }
        
        @Override
        public WriterAppender build() {
            return new WriterAppender(this.name, this.layout, this.filter, getManager(this.target, this.follow, this.layout), this.ignoreExceptions, null);
        }
        
        public Builder setFilter(final Filter aFilter) {
            this.filter = aFilter;
            return this;
        }
        
        public Builder setFollow(final boolean shouldFollow) {
            this.follow = shouldFollow;
            return this;
        }
        
        public Builder setIgnoreExceptions(final boolean shouldIgnoreExceptions) {
            this.ignoreExceptions = shouldIgnoreExceptions;
            return this;
        }
        
        public Builder setLayout(final StringLayout aLayout) {
            this.layout = aLayout;
            return this;
        }
        
        public Builder setName(final String aName) {
            this.name = aName;
            return this;
        }
        
        public Builder setTarget(final Writer aTarget) {
            this.target = aTarget;
            return this;
        }
    }
    
    private static class FactoryData
    {
        private final StringLayout layout;
        private final String name;
        private final Writer writer;
        
        public FactoryData(final Writer writer, final String type, final StringLayout layout) {
            this.writer = writer;
            this.name = type;
            this.layout = layout;
        }
    }
    
    private static class WriterManagerFactory implements ManagerFactory<WriterManager, FactoryData>
    {
        @Override
        public WriterManager createManager(final String name, final FactoryData data) {
            return new WriterManager(data.writer, data.name, data.layout, true);
        }
    }
}
