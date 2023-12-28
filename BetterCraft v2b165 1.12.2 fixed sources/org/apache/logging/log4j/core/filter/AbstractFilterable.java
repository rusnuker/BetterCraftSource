// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LifeCycle2;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public abstract class AbstractFilterable extends AbstractLifeCycle implements Filterable
{
    private volatile Filter filter;
    
    protected AbstractFilterable(final Filter filter) {
        this.filter = filter;
    }
    
    protected AbstractFilterable() {
    }
    
    @Override
    public Filter getFilter() {
        return this.filter;
    }
    
    @Override
    public synchronized void addFilter(final Filter filter) {
        if (filter == null) {
            return;
        }
        if (this.filter == null) {
            this.filter = filter;
        }
        else if (this.filter instanceof CompositeFilter) {
            this.filter = ((CompositeFilter)this.filter).addFilter(filter);
        }
        else {
            final Filter[] filters = { this.filter, filter };
            this.filter = CompositeFilter.createFilters(filters);
        }
    }
    
    @Override
    public synchronized void removeFilter(final Filter filter) {
        if (this.filter == null || filter == null) {
            return;
        }
        if (this.filter == filter || this.filter.equals(filter)) {
            this.filter = null;
        }
        else if (this.filter instanceof CompositeFilter) {
            CompositeFilter composite = (CompositeFilter)this.filter;
            composite = composite.removeFilter(filter);
            if (composite.size() > 1) {
                this.filter = composite;
            }
            else if (composite.size() == 1) {
                final Iterator<Filter> iter = composite.iterator();
                this.filter = iter.next();
            }
            else {
                this.filter = null;
            }
        }
    }
    
    @Override
    public boolean hasFilter() {
        return this.filter != null;
    }
    
    @Override
    public void start() {
        this.setStarting();
        if (this.filter != null) {
            this.filter.start();
        }
        this.setStarted();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        return this.stop(timeout, timeUnit, true);
    }
    
    protected boolean stop(final long timeout, final TimeUnit timeUnit, final boolean changeLifeCycleState) {
        if (changeLifeCycleState) {
            this.setStopping();
        }
        boolean stopped = true;
        if (this.filter != null) {
            if (this.filter instanceof LifeCycle2) {
                stopped = ((LifeCycle2)this.filter).stop(timeout, timeUnit);
            }
            else {
                this.filter.stop();
                stopped = true;
            }
        }
        if (changeLifeCycleState) {
            this.setStopped();
        }
        return stopped;
    }
    
    @Override
    public boolean isFiltered(final LogEvent event) {
        return this.filter != null && this.filter.filter(event) == Filter.Result.DENY;
    }
    
    public abstract static class Builder<B extends Builder<B>>
    {
        @PluginElement("Filter")
        private Filter filter;
        
        public Filter getFilter() {
            return this.filter;
        }
        
        public B asBuilder() {
            return (B)this;
        }
        
        public B withFilter(final Filter filter) {
            this.filter = filter;
            return this.asBuilder();
        }
    }
}
