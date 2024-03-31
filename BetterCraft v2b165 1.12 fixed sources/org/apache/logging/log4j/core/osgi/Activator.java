// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.osgi;

import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.BundleEvent;
import org.apache.logging.log4j.LogManager;
import org.osgi.framework.wiring.BundleWiring;
import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleListener;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.osgi.framework.BundleContext;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.BundleActivator;

public final class Activator implements BundleActivator, SynchronousBundleListener
{
    private static final Logger LOGGER;
    private final AtomicReference<BundleContext> contextRef;
    
    public Activator() {
        this.contextRef = new AtomicReference<BundleContext>();
    }
    
    public void start(final BundleContext context) throws Exception {
        if (PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector") == null) {
            System.setProperty("Log4jContextSelector", BundleContextSelector.class.getName());
        }
        if (this.contextRef.compareAndSet(null, context)) {
            context.addBundleListener((BundleListener)this);
            scanInstalledBundlesForPlugins(context);
        }
    }
    
    private static void scanInstalledBundlesForPlugins(final BundleContext context) {
        final Bundle[] arr$;
        final Bundle[] bundles = arr$ = context.getBundles();
        for (final Bundle bundle : arr$) {
            if (bundle.getState() == 32 && bundle.getBundleId() != 0L) {
                scanBundleForPlugins(bundle);
            }
        }
    }
    
    private static void scanBundleForPlugins(final Bundle bundle) {
        Activator.LOGGER.trace("Scanning bundle [{}] for plugins.", bundle.getSymbolicName());
        PluginRegistry.getInstance().loadFromBundle(bundle.getBundleId(), ((BundleWiring)bundle.adapt((Class)BundleWiring.class)).getClassLoader());
    }
    
    private static void stopBundlePlugins(final Bundle bundle) {
        Activator.LOGGER.trace("Stopping bundle [{}] plugins.", bundle.getSymbolicName());
        PluginRegistry.getInstance().clearBundlePlugins(bundle.getBundleId());
    }
    
    public void stop(final BundleContext context) throws Exception {
        this.contextRef.compareAndSet(context, null);
        LogManager.shutdown();
    }
    
    public void bundleChanged(final BundleEvent event) {
        switch (event.getType()) {
            case 2: {
                scanBundleForPlugins(event.getBundle());
                break;
            }
            case 256: {
                stopBundlePlugins(event.getBundle());
                break;
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
