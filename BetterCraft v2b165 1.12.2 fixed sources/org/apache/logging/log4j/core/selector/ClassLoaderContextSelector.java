// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.selector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.ReflectionUtil;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import java.net.URI;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.atomic.AtomicReference;

public class ClassLoaderContextSelector implements ContextSelector
{
    private static final AtomicReference<LoggerContext> DEFAULT_CONTEXT;
    protected static final StatusLogger LOGGER;
    protected static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP;
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        if (currentContext) {
            final LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
            if (ctx != null) {
                return ctx;
            }
            return this.getDefault();
        }
        else {
            if (loader != null) {
                return this.locateContext(loader, configLocation);
            }
            final Class<?> clazz = ReflectionUtil.getCallerClass(fqcn);
            if (clazz != null) {
                return this.locateContext(clazz.getClassLoader(), configLocation);
            }
            final LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
            if (lc != null) {
                return lc;
            }
            return this.getDefault();
        }
    }
    
    @Override
    public void removeContext(final LoggerContext context) {
        for (final Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : ClassLoaderContextSelector.CONTEXT_MAP.entrySet()) {
            final LoggerContext ctx = entry.getValue().get().get();
            if (ctx == context) {
                ClassLoaderContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }
    }
    
    @Override
    public List<LoggerContext> getLoggerContexts() {
        final List<LoggerContext> list = new ArrayList<LoggerContext>();
        final Collection<AtomicReference<WeakReference<LoggerContext>>> coll = ClassLoaderContextSelector.CONTEXT_MAP.values();
        for (final AtomicReference<WeakReference<LoggerContext>> ref : coll) {
            final LoggerContext ctx = ref.get().get();
            if (ctx != null) {
                list.add(ctx);
            }
        }
        return Collections.unmodifiableList((List<? extends LoggerContext>)list);
    }
    
    private LoggerContext locateContext(final ClassLoader loaderOrNull, final URI configLocation) {
        final ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
        final String name = this.toContextMapKey(loader);
        AtomicReference<WeakReference<LoggerContext>> ref = ClassLoaderContextSelector.CONTEXT_MAP.get(name);
        if (ref == null) {
            if (configLocation == null) {
                for (ClassLoader parent = loader.getParent(); parent != null; parent = parent.getParent()) {
                    ref = ClassLoaderContextSelector.CONTEXT_MAP.get(this.toContextMapKey(parent));
                    if (ref != null) {
                        final WeakReference<LoggerContext> r = ref.get();
                        final LoggerContext ctx = r.get();
                        if (ctx != null) {
                            return ctx;
                        }
                    }
                }
            }
            LoggerContext ctx2 = this.createContext(name, configLocation);
            final AtomicReference<WeakReference<LoggerContext>> r2 = new AtomicReference<WeakReference<LoggerContext>>();
            r2.set(new WeakReference<LoggerContext>(ctx2));
            ClassLoaderContextSelector.CONTEXT_MAP.putIfAbsent(name, r2);
            ctx2 = ClassLoaderContextSelector.CONTEXT_MAP.get(name).get().get();
            return ctx2;
        }
        final WeakReference<LoggerContext> weakRef = ref.get();
        LoggerContext ctx3 = weakRef.get();
        if (ctx3 != null) {
            if (ctx3.getConfigLocation() == null && configLocation != null) {
                ClassLoaderContextSelector.LOGGER.debug("Setting configuration to {}", configLocation);
                ctx3.setConfigLocation(configLocation);
            }
            else if (ctx3.getConfigLocation() != null && configLocation != null && !ctx3.getConfigLocation().equals(configLocation)) {
                ClassLoaderContextSelector.LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx3.getConfigLocation());
            }
            return ctx3;
        }
        ctx3 = this.createContext(name, configLocation);
        ref.compareAndSet(weakRef, new WeakReference<LoggerContext>(ctx3));
        return ctx3;
    }
    
    protected LoggerContext createContext(final String name, final URI configLocation) {
        return new LoggerContext(name, null, configLocation);
    }
    
    protected String toContextMapKey(final ClassLoader loader) {
        return Integer.toHexString(System.identityHashCode(loader));
    }
    
    protected LoggerContext getDefault() {
        final LoggerContext ctx = ClassLoaderContextSelector.DEFAULT_CONTEXT.get();
        if (ctx != null) {
            return ctx;
        }
        ClassLoaderContextSelector.DEFAULT_CONTEXT.compareAndSet(null, this.createContext(this.defaultContextName(), null));
        return ClassLoaderContextSelector.DEFAULT_CONTEXT.get();
    }
    
    protected String defaultContextName() {
        return "Default";
    }
    
    static {
        DEFAULT_CONTEXT = new AtomicReference<LoggerContext>();
        LOGGER = StatusLogger.getLogger();
        CONTEXT_MAP = new ConcurrentHashMap<String, AtomicReference<WeakReference<LoggerContext>>>();
    }
}
