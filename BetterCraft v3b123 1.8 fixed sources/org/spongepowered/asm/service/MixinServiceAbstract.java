// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.logging.LoggerAdapterDefault;
import org.spongepowered.asm.launch.platform.IMixinPlatformAgent;
import java.util.ArrayList;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import java.util.Collection;
import java.util.Iterator;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.HashMap;
import org.spongepowered.asm.launch.platform.IMixinPlatformServiceAgent;
import java.util.List;
import org.spongepowered.asm.util.ReEntranceLock;
import java.util.Map;
import org.spongepowered.asm.logging.ILogger;

public abstract class MixinServiceAbstract implements IMixinService
{
    protected static final String LAUNCH_PACKAGE = "org.spongepowered.asm.launch.";
    protected static final String MIXIN_PACKAGE = "org.spongepowered.asm.mixin.";
    protected static final String SERVICE_PACKAGE = "org.spongepowered.asm.service.";
    private static ILogger logger;
    private static final Map<String, ILogger> loggers;
    protected final ReEntranceLock lock;
    private final Map<Class<IMixinInternal>, IMixinInternal> internals;
    private List<IMixinPlatformServiceAgent> serviceAgents;
    private String sideName;
    
    protected MixinServiceAbstract() {
        this.lock = new ReEntranceLock(1);
        this.internals = new HashMap<Class<IMixinInternal>, IMixinInternal>();
        if (MixinServiceAbstract.logger == null) {
            MixinServiceAbstract.logger = this.getLogger("mixin");
        }
    }
    
    @Override
    public void prepare() {
    }
    
    @Override
    public MixinEnvironment.Phase getInitialPhase() {
        return MixinEnvironment.Phase.PREINIT;
    }
    
    @Override
    public MixinEnvironment.CompatibilityLevel getMinCompatibilityLevel() {
        return null;
    }
    
    @Override
    public MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel() {
        return null;
    }
    
    @Override
    public void offer(final IMixinInternal internal) {
        this.registerInternal(internal, internal.getClass());
    }
    
    private void registerInternal(final IMixinInternal internal, final Class<?> clazz) {
        for (final Class<?> iface : clazz.getInterfaces()) {
            if (iface == IMixinInternal.class) {
                this.internals.put((Class<IMixinInternal>)clazz, internal);
            }
            this.registerInternal(internal, iface);
        }
    }
    
    protected final <T extends IMixinInternal> T getInternal(final Class<T> type) {
        for (final Class<IMixinInternal> internalType : this.internals.keySet()) {
            if (type.isAssignableFrom(internalType)) {
                return (T)this.internals.get(internalType);
            }
        }
        return null;
    }
    
    @Override
    public void init() {
        for (final IMixinPlatformServiceAgent agent : this.getServiceAgents()) {
            agent.init();
        }
    }
    
    @Override
    public void beginPhase() {
    }
    
    @Override
    public void checkEnv(final Object bootSource) {
    }
    
    @Override
    public ReEntranceLock getReEntranceLock() {
        return this.lock;
    }
    
    @Override
    public Collection<IContainerHandle> getMixinContainers() {
        final ImmutableList.Builder<IContainerHandle> list = ImmutableList.builder();
        this.getContainersFromAgents(list);
        return list.build();
    }
    
    protected final void getContainersFromAgents(final ImmutableList.Builder<IContainerHandle> list) {
        for (final IMixinPlatformServiceAgent agent : this.getServiceAgents()) {
            final Collection<IContainerHandle> containers = agent.getMixinContainers();
            if (containers != null) {
                list.addAll(containers);
            }
        }
    }
    
    @Override
    public final String getSideName() {
        if (this.sideName != null) {
            return this.sideName;
        }
        for (final IMixinPlatformServiceAgent agent : this.getServiceAgents()) {
            try {
                final String side = agent.getSideName();
                if (side != null) {
                    return this.sideName = side;
                }
                continue;
            }
            catch (final Exception ex) {
                MixinServiceAbstract.logger.catching(ex);
            }
        }
        return "UNKNOWN";
    }
    
    private List<IMixinPlatformServiceAgent> getServiceAgents() {
        if (this.serviceAgents != null) {
            return this.serviceAgents;
        }
        this.serviceAgents = new ArrayList<IMixinPlatformServiceAgent>();
        for (final String agentClassName : this.getPlatformAgents()) {
            try {
                final Class<IMixinPlatformAgent> agentClass = (Class<IMixinPlatformAgent>)this.getClassProvider().findClass(agentClassName, false);
                final IMixinPlatformAgent agent = agentClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                if (!(agent instanceof IMixinPlatformServiceAgent)) {
                    continue;
                }
                this.serviceAgents.add((IMixinPlatformServiceAgent)agent);
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
        return this.serviceAgents;
    }
    
    @Override
    public synchronized ILogger getLogger(final String name) {
        ILogger logger = MixinServiceAbstract.loggers.get(name);
        if (logger == null) {
            MixinServiceAbstract.loggers.put(name, logger = this.createLogger(name));
        }
        return logger;
    }
    
    protected ILogger createLogger(final String name) {
        return new LoggerAdapterDefault(name);
    }
    
    @Deprecated
    public void wire(final MixinEnvironment.Phase phase, final IConsumer<MixinEnvironment.Phase> phaseConsumer) {
        for (final IMixinPlatformServiceAgent agent : this.getServiceAgents()) {
            agent.wire(phase, phaseConsumer);
        }
    }
    
    @Deprecated
    public void unwire() {
        for (final IMixinPlatformServiceAgent agent : this.getServiceAgents()) {
            agent.unwire();
        }
    }
    
    static {
        loggers = new HashMap<String, ILogger>();
    }
}
