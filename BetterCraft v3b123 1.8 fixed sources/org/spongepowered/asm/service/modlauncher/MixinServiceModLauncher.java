// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.launch.IClassProcessor;
import java.io.InputStream;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.spongepowered.asm.service.IMixinAuditTrail;
import org.spongepowered.asm.service.IClassTracker;
import org.spongepowered.asm.service.ITransformerProvider;
import cpw.mods.modlauncher.Launcher;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory;
import org.spongepowered.asm.service.IMixinInternal;
import java.lang.reflect.Constructor;
import cpw.mods.modlauncher.api.ITransformationService;
import org.spongepowered.asm.launch.platform.container.ContainerHandleModLauncher;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.MixinServiceAbstract;

public class MixinServiceModLauncher extends MixinServiceAbstract
{
    private static final String MODLAUNCHER_4_SPECIFICATION_VERSION = "4.0";
    private static final String MODLAUNCHER_9_SPECIFICATION_VERSION = "8.0";
    private static final String CONTAINER_PACKAGE = "org.spongepowered.asm.launch.platform.container.";
    private static final String MODLAUNCHER_4_ROOT_CONTAINER_CLASS = "org.spongepowered.asm.launch.platform.container.ContainerHandleModLauncher";
    private static final String MODLAUNCHER_9_ROOT_CONTAINER_CLASS = "org.spongepowered.asm.launch.platform.container.ContainerHandleModLauncherEx";
    private IClassProvider classProvider;
    private IClassBytecodeProvider bytecodeProvider;
    private MixinTransformationHandler transformationHandler;
    private ModLauncherClassTracker classTracker;
    private ModLauncherAuditTrail auditTrail;
    private IConsumer<MixinEnvironment.Phase> phaseConsumer;
    private volatile boolean initialised;
    private ContainerHandleModLauncher rootContainer;
    private MixinEnvironment.CompatibilityLevel minCompatibilityLevel;
    
    public MixinServiceModLauncher() {
        this.minCompatibilityLevel = MixinEnvironment.CompatibilityLevel.JAVA_8;
        final Package pkg = ITransformationService.class.getPackage();
        if (pkg.isCompatibleWith("8.0")) {
            this.createRootContainer("org.spongepowered.asm.launch.platform.container.ContainerHandleModLauncherEx");
            this.minCompatibilityLevel = MixinEnvironment.CompatibilityLevel.JAVA_16;
        }
        else {
            this.createRootContainer("org.spongepowered.asm.launch.platform.container.ContainerHandleModLauncher");
        }
    }
    
    public void onInit(final IClassBytecodeProvider bytecodeProvider) {
        if (this.initialised) {
            throw new IllegalStateException("Already initialised");
        }
        this.initialised = true;
        this.bytecodeProvider = bytecodeProvider;
    }
    
    private void createRootContainer(final String rootContainerClassName) {
        try {
            final Class<?> clRootContainer = this.getClassProvider().findClass(rootContainerClassName);
            final Constructor<?> ctor = clRootContainer.getDeclaredConstructor(String.class);
            this.rootContainer = (ContainerHandleModLauncher)ctor.newInstance(this.getName());
        }
        catch (final ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }
    
    public void onStartup() {
        this.phaseConsumer.accept(MixinEnvironment.Phase.DEFAULT);
    }
    
    @Override
    public void offer(final IMixinInternal internal) {
        if (internal instanceof IMixinTransformerFactory) {
            this.getTransformationHandler().offer((IMixinTransformerFactory)internal);
        }
        super.offer(internal);
    }
    
    @Override
    public void wire(final MixinEnvironment.Phase phase, final IConsumer<MixinEnvironment.Phase> phaseConsumer) {
        super.wire(phase, phaseConsumer);
        this.phaseConsumer = phaseConsumer;
    }
    
    @Override
    public String getName() {
        return "ModLauncher";
    }
    
    @Override
    public MixinEnvironment.CompatibilityLevel getMinCompatibilityLevel() {
        return this.minCompatibilityLevel;
    }
    
    @Override
    protected ILogger createLogger(final String name) {
        return new LoggerAdapterLog4j2(name);
    }
    
    @Override
    public boolean isValid() {
        try {
            Launcher.INSTANCE.hashCode();
            final Package pkg = ITransformationService.class.getPackage();
            if (!pkg.isCompatibleWith("4.0")) {
                return false;
            }
        }
        catch (final Throwable th) {
            return false;
        }
        return true;
    }
    
    @Override
    public IClassProvider getClassProvider() {
        if (this.classProvider == null) {
            this.classProvider = new ModLauncherClassProvider();
        }
        return this.classProvider;
    }
    
    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        if (this.bytecodeProvider == null) {
            throw new IllegalStateException("Service initialisation incomplete");
        }
        return this.bytecodeProvider;
    }
    
    @Override
    public ITransformerProvider getTransformerProvider() {
        return null;
    }
    
    @Override
    public IClassTracker getClassTracker() {
        if (this.classTracker == null) {
            this.classTracker = new ModLauncherClassTracker();
        }
        return this.classTracker;
    }
    
    @Override
    public IMixinAuditTrail getAuditTrail() {
        if (this.auditTrail == null) {
            this.auditTrail = new ModLauncherAuditTrail();
        }
        return this.auditTrail;
    }
    
    private MixinTransformationHandler getTransformationHandler() {
        if (this.transformationHandler == null) {
            this.transformationHandler = new MixinTransformationHandler();
        }
        return this.transformationHandler;
    }
    
    @Override
    public Collection<String> getPlatformAgents() {
        return ImmutableList.of("org.spongepowered.asm.launch.platform.MixinPlatformAgentMinecraftForge");
    }
    
    @Override
    public ContainerHandleModLauncher getPrimaryContainer() {
        return this.rootContainer;
    }
    
    @Override
    public InputStream getResourceAsStream(final String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }
    
    public Collection<IClassProcessor> getProcessors() {
        return ImmutableList.of(this.getTransformationHandler(), this.getClassTracker());
    }
}
