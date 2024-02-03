// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import java.util.Iterator;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.MixinService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.logging.ILogger;

public class MixinConnectorManager
{
    private static final ILogger logger;
    private final Set<String> connectorClasses;
    private final List<IMixinConnector> connectors;
    
    MixinConnectorManager() {
        this.connectorClasses = new LinkedHashSet<String>();
        this.connectors = new ArrayList<IMixinConnector>();
    }
    
    void addConnector(final String connectorClass) {
        this.connectorClasses.add(connectorClass);
    }
    
    void inject() {
        this.loadConnectors();
        this.initConnectors();
    }
    
    void loadConnectors() {
        final IClassProvider classProvider = MixinService.getService().getClassProvider();
        for (final String connectorClassName : this.connectorClasses) {
            Class<IMixinConnector> connectorClass = null;
            try {
                final Class<?> clazz = classProvider.findClass(connectorClassName);
                if (!IMixinConnector.class.isAssignableFrom(clazz)) {
                    MixinConnectorManager.logger.error("Mixin Connector [" + connectorClassName + "] does not implement IMixinConnector", new Object[0]);
                    continue;
                }
                connectorClass = (Class<IMixinConnector>)clazz;
            }
            catch (final ClassNotFoundException ex) {
                MixinConnectorManager.logger.catching(ex);
                continue;
            }
            try {
                final IMixinConnector connector = connectorClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                this.connectors.add(connector);
                MixinConnectorManager.logger.info("Successfully loaded Mixin Connector [{}]", connectorClassName);
            }
            catch (final ReflectiveOperationException ex2) {
                MixinConnectorManager.logger.warn("Error loading Mixin Connector [{}]", connectorClassName, ex2);
            }
        }
        this.connectorClasses.clear();
    }
    
    void initConnectors() {
        for (final IMixinConnector connector : this.connectors) {
            try {
                connector.connect();
            }
            catch (final Exception ex) {
                MixinConnectorManager.logger.warn("Error initialising Mixin Connector [" + connector.getClass().getName() + "]", ex);
            }
        }
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
