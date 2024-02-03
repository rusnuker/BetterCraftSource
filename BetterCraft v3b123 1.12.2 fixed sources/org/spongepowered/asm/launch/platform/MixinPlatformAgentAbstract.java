// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.lang.reflect.Method;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.logging.ILogger;

public abstract class MixinPlatformAgentAbstract implements IMixinPlatformAgent
{
    protected static final ILogger logger;
    protected MixinPlatformManager manager;
    protected IContainerHandle handle;
    
    protected MixinPlatformAgentAbstract() {
    }
    
    @Override
    public AcceptResult accept(final MixinPlatformManager manager, final IContainerHandle handle) {
        this.manager = manager;
        this.handle = handle;
        return AcceptResult.ACCEPTED;
    }
    
    @Override
    public String getPhaseProvider() {
        return null;
    }
    
    @Override
    public void prepare() {
    }
    
    @Override
    public void initPrimaryContainer() {
    }
    
    @Override
    public void inject() {
    }
    
    @Override
    public String toString() {
        return String.format("PlatformAgent[%s:%s]", this.getClass().getSimpleName(), this.handle);
    }
    
    protected static String invokeStringMethod(final ClassLoader classLoader, final String className, final String methodName) {
        try {
            final Class<?> clazz = Class.forName(className, false, classLoader);
            final Method method = clazz.getDeclaredMethod(methodName, (Class<?>[])new Class[0]);
            return ((Enum)method.invoke(null, new Object[0])).name();
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    @Deprecated
    public void wire(final MixinEnvironment.Phase phase, final IConsumer<MixinEnvironment.Phase> phaseConsumer) {
    }
    
    @Deprecated
    public void unwire() {
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
