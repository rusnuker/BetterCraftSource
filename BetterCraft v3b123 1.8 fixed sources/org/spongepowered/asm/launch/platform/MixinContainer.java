// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform;

import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.GlobalProperties;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ArrayList;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.logging.ILogger;
import java.util.List;

public class MixinContainer
{
    private static final List<String> agentClasses;
    private static final ILogger logger;
    private final IContainerHandle handle;
    private final List<IMixinPlatformAgent> agents;
    
    public MixinContainer(final MixinPlatformManager manager, final IContainerHandle handle) {
        this.agents = new ArrayList<IMixinPlatformAgent>();
        this.handle = handle;
        final Iterator<String> iter = MixinContainer.agentClasses.iterator();
        while (iter.hasNext()) {
            final String agentClass = iter.next();
            try {
                final Class<IMixinPlatformAgent> clazz = (Class<IMixinPlatformAgent>)Class.forName(agentClass);
                final String simpleName = clazz.getSimpleName();
                MixinContainer.logger.debug("Instancing new {} for {}", simpleName, this.handle);
                final IMixinPlatformAgent agent = clazz.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                final IMixinPlatformAgent.AcceptResult acceptAction = agent.accept(manager, this.handle);
                if (acceptAction == IMixinPlatformAgent.AcceptResult.ACCEPTED) {
                    this.agents.add(agent);
                }
                else if (acceptAction == IMixinPlatformAgent.AcceptResult.INVALID) {
                    iter.remove();
                    continue;
                }
                MixinContainer.logger.debug("{} {} container {}", simpleName, acceptAction.name().toLowerCase(Locale.ROOT), this.handle);
            }
            catch (final InstantiationException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
                throw new RuntimeException(cause);
            }
            catch (final ReflectiveOperationException ex2) {
                MixinContainer.logger.catching(ex2);
            }
        }
    }
    
    public IContainerHandle getDescriptor() {
        return this.handle;
    }
    
    public Collection<String> getPhaseProviders() {
        final List<String> phaseProviders = new ArrayList<String>();
        for (final IMixinPlatformAgent agent : this.agents) {
            final String phaseProvider = agent.getPhaseProvider();
            if (phaseProvider != null) {
                phaseProviders.add(phaseProvider);
            }
        }
        return phaseProviders;
    }
    
    public void prepare() {
        for (final IMixinPlatformAgent agent : this.agents) {
            MixinContainer.logger.debug("Processing prepare() for {}", agent);
            agent.prepare();
        }
    }
    
    public void initPrimaryContainer() {
        for (final IMixinPlatformAgent agent : this.agents) {
            MixinContainer.logger.debug("Processing launch tasks for {}", agent);
            agent.initPrimaryContainer();
        }
    }
    
    public void inject() {
        for (final IMixinPlatformAgent agent : this.agents) {
            MixinContainer.logger.debug("Processing inject() for {}", agent);
            agent.inject();
        }
    }
    
    static {
        agentClasses = new ArrayList<String>();
        GlobalProperties.put(GlobalProperties.Keys.AGENTS, MixinContainer.agentClasses);
        for (final String agent : MixinService.getService().getPlatformAgents()) {
            MixinContainer.agentClasses.add(agent);
        }
        MixinContainer.agentClasses.add("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault");
        logger = MixinService.getService().getLogger("mixin");
    }
}
