// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.wiring.BundleWire;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.osgi.framework.BundleContext;
import java.util.Iterator;
import java.util.List;
import java.net.URL;
import org.osgi.framework.AdaptPermission;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import java.security.Permission;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.BundleActivator;

public class Activator implements BundleActivator, SynchronousBundleListener
{
    private static final SecurityManager SECURITY_MANAGER;
    private static final Logger LOGGER;
    private boolean lockingProviderUtil;
    
    private static void checkPermission(final Permission permission) {
        if (Activator.SECURITY_MANAGER != null) {
            Activator.SECURITY_MANAGER.checkPermission(permission);
        }
    }
    
    private void loadProvider(final Bundle bundle) {
        if (bundle.getState() == 1) {
            return;
        }
        try {
            checkPermission((Permission)new AdminPermission(bundle, "resource"));
            checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
            this.loadProvider((BundleWiring)bundle.adapt((Class)BundleWiring.class));
        }
        catch (final SecurityException e) {
            Activator.LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), e);
        }
        catch (final Exception e2) {
            Activator.LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), e2);
        }
    }
    
    private void loadProvider(final BundleWiring provider) {
        final List<URL> urls = provider.findEntries("META-INF", "log4j-provider.properties", 0);
        for (final URL url : urls) {
            ProviderUtil.loadProvider(url, provider.getClassLoader());
        }
    }
    
    public void start(final BundleContext context) throws Exception {
        ProviderUtil.STARTUP_LOCK.lock();
        this.lockingProviderUtil = true;
        final BundleWiring self = (BundleWiring)context.getBundle().adapt((Class)BundleWiring.class);
        final List<BundleWire> required = self.getRequiredWires(LoggerContextFactory.class.getName());
        for (final BundleWire wire : required) {
            this.loadProvider(wire.getProviderWiring());
        }
        context.addBundleListener((BundleListener)this);
        final Bundle[] arr$;
        final Bundle[] bundles = arr$ = context.getBundles();
        for (final Bundle bundle : arr$) {
            this.loadProvider(bundle);
        }
        this.unlockIfReady();
    }
    
    private void unlockIfReady() {
        if (this.lockingProviderUtil && !ProviderUtil.PROVIDERS.isEmpty()) {
            ProviderUtil.STARTUP_LOCK.unlock();
            this.lockingProviderUtil = false;
        }
    }
    
    public void stop(final BundleContext context) throws Exception {
        context.removeBundleListener((BundleListener)this);
        this.unlockIfReady();
    }
    
    public void bundleChanged(final BundleEvent event) {
        switch (event.getType()) {
            case 2: {
                this.loadProvider(event.getBundle());
                this.unlockIfReady();
                break;
            }
        }
    }
    
    static {
        SECURITY_MANAGER = System.getSecurityManager();
        LOGGER = StatusLogger.getLogger();
    }
}
