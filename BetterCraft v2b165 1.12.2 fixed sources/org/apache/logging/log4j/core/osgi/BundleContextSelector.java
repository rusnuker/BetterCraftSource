// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.osgi;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Objects;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.apache.logging.log4j.util.ReflectionUtil;
import org.osgi.framework.BundleReference;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.LoggerContext;
import java.net.URI;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;

public class BundleContextSelector extends ClassLoaderContextSelector
{
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
            if (loader instanceof BundleReference) {
                return locateContext(((BundleReference)loader).getBundle(), configLocation);
            }
            final Class<?> callerClass = ReflectionUtil.getCallerClass(fqcn);
            if (callerClass != null) {
                return locateContext(FrameworkUtil.getBundle((Class)callerClass), configLocation);
            }
            final LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
            return (lc == null) ? this.getDefault() : lc;
        }
    }
    
    private static LoggerContext locateContext(final Bundle bundle, final URI configLocation) {
        final String name = Objects.requireNonNull(bundle, "No Bundle provided").getSymbolicName();
        final AtomicReference<WeakReference<LoggerContext>> ref = BundleContextSelector.CONTEXT_MAP.get(name);
        if (ref == null) {
            final LoggerContext context = new LoggerContext(name, bundle, configLocation);
            BundleContextSelector.CONTEXT_MAP.putIfAbsent(name, new AtomicReference<WeakReference<LoggerContext>>(new WeakReference<LoggerContext>(context)));
            return BundleContextSelector.CONTEXT_MAP.get(name).get().get();
        }
        final WeakReference<LoggerContext> r = ref.get();
        final LoggerContext ctx = r.get();
        if (ctx == null) {
            final LoggerContext context2 = new LoggerContext(name, bundle, configLocation);
            ref.compareAndSet(r, new WeakReference<LoggerContext>(context2));
            return ref.get().get();
        }
        final URI oldConfigLocation = ctx.getConfigLocation();
        if (oldConfigLocation == null && configLocation != null) {
            BundleContextSelector.LOGGER.debug("Setting bundle ({}) configuration to {}", name, configLocation);
            ctx.setConfigLocation(configLocation);
        }
        else if (oldConfigLocation != null && configLocation != null && !configLocation.equals(oldConfigLocation)) {
            BundleContextSelector.LOGGER.warn("locateContext called with URI [{}], but existing LoggerContext has URI [{}]", configLocation, oldConfigLocation);
        }
        return ctx;
    }
}
