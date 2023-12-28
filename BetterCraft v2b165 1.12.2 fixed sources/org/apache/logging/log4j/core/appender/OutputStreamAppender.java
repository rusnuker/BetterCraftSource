// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.OutputStream;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "OutputStream", category = "Core", elementType = "appender", printObject = true)
public final class OutputStreamAppender extends AbstractOutputStreamAppender<OutputStreamManager>
{
    private static OutputStreamManagerFactory factory;
    
    @PluginFactory
    public static OutputStreamAppender createAppender(Layout<? extends Serializable> layout, final Filter filter, final OutputStream target, final String name, final boolean follow, final boolean ignore) {
        if (name == null) {
            OutputStreamAppender.LOGGER.error("No name provided for OutputStreamAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new OutputStreamAppender(name, layout, filter, getManager(target, follow, layout), ignore);
    }
    
    private static OutputStreamManager getManager(final OutputStream target, final boolean follow, final Layout<? extends Serializable> layout) {
        final OutputStream os = new CloseShieldOutputStream(target);
        final String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
        return OutputStreamManager.getManager(managerName, new FactoryData(os, managerName, layout), OutputStreamAppender.factory);
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    private OutputStreamAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final OutputStreamManager manager, final boolean ignoreExceptions) {
        super(name, layout, filter, ignoreExceptions, true, manager);
    }
    
    static {
        OutputStreamAppender.factory = new OutputStreamManagerFactory();
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<OutputStreamAppender>
    {
        private Filter filter;
        private boolean follow;
        private boolean ignoreExceptions;
        private Layout<? extends Serializable> layout;
        private String name;
        private OutputStream target;
        
        public Builder() {
            this.follow = false;
            this.ignoreExceptions = true;
            this.layout = PatternLayout.createDefaultLayout();
        }
        
        @Override
        public OutputStreamAppender build() {
            return new OutputStreamAppender(this.name, this.layout, this.filter, getManager(this.target, this.follow, this.layout), this.ignoreExceptions, null);
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
        
        public Builder setLayout(final Layout<? extends Serializable> aLayout) {
            this.layout = aLayout;
            return this;
        }
        
        public Builder setName(final String aName) {
            this.name = aName;
            return this;
        }
        
        public Builder setTarget(final OutputStream aTarget) {
            this.target = aTarget;
            return this;
        }
    }
    
    private static class FactoryData
    {
        private final Layout<? extends Serializable> layout;
        private final String name;
        private final OutputStream os;
        
        public FactoryData(final OutputStream os, final String type, final Layout<? extends Serializable> layout) {
            this.os = os;
            this.name = type;
            this.layout = layout;
        }
    }
    
    private static class OutputStreamManagerFactory implements ManagerFactory<OutputStreamManager, FactoryData>
    {
        @Override
        public OutputStreamManager createManager(final String name, final FactoryData data) {
            return new OutputStreamManager(data.os, data.name, data.layout, true);
        }
    }
}
