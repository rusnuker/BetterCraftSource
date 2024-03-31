// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import org.spongepowered.asm.logging.ILogger;
import java.io.InputStream;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import java.util.Collection;
import org.spongepowered.asm.util.ReEntranceLock;
import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IMixinService
{
    String getName();
    
    boolean isValid();
    
    void prepare();
    
    MixinEnvironment.Phase getInitialPhase();
    
    void offer(final IMixinInternal p0);
    
    void init();
    
    void beginPhase();
    
    void checkEnv(final Object p0);
    
    ReEntranceLock getReEntranceLock();
    
    IClassProvider getClassProvider();
    
    IClassBytecodeProvider getBytecodeProvider();
    
    ITransformerProvider getTransformerProvider();
    
    IClassTracker getClassTracker();
    
    IMixinAuditTrail getAuditTrail();
    
    Collection<String> getPlatformAgents();
    
    IContainerHandle getPrimaryContainer();
    
    Collection<IContainerHandle> getMixinContainers();
    
    InputStream getResourceAsStream(final String p0);
    
    String getSideName();
    
    MixinEnvironment.CompatibilityLevel getMinCompatibilityLevel();
    
    MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel();
    
    ILogger getLogger(final String p0);
}
