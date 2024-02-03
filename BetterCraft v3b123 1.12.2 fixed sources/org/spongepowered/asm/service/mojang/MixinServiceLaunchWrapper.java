// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.mojang;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.util.Locale;
import org.spongepowered.asm.util.perf.Profiler;
import java.io.IOException;
import com.google.common.io.Closeables;
import com.google.common.io.ByteStreams;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.launchwrapper.IClassTransformer;
import java.util.ArrayList;
import org.spongepowered.asm.service.ITransformer;
import java.io.InputStream;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.service.IMixinAuditTrail;
import org.spongepowered.asm.service.IClassTracker;
import java.net.URL;
import org.spongepowered.asm.launch.platform.MainAttributes;
import org.spongepowered.asm.util.Files;
import java.net.URI;
import org.spongepowered.asm.launch.platform.container.ContainerHandleVirtual;
import java.net.URISyntaxException;
import org.spongepowered.asm.launch.platform.container.ContainerHandleURI;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.MixinEnvironment;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.IClassNameTransformer;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import java.util.List;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.service.ITransformerProvider;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.MixinServiceAbstract;

public class MixinServiceLaunchWrapper extends MixinServiceAbstract implements IClassProvider, IClassBytecodeProvider, ITransformerProvider
{
    public static final GlobalProperties.Keys BLACKBOARD_KEY_TWEAKCLASSES;
    public static final GlobalProperties.Keys BLACKBOARD_KEY_TWEAKS;
    private static final String MIXIN_TWEAKER_CLASS = "org.spongepowered.asm.launch.MixinTweaker";
    private static final String STATE_TWEAKER = "org.spongepowered.asm.mixin.EnvironmentStateTweaker";
    private static final String TRANSFORMER_PROXY_CLASS = "org.spongepowered.asm.mixin.transformer.Proxy";
    private static final Set<String> excludeTransformers;
    private static final Logger logger;
    private final LaunchClassLoaderUtil classLoaderUtil;
    private List<ILegacyClassTransformer> delegatedTransformers;
    private IClassNameTransformer nameTransformer;
    
    public MixinServiceLaunchWrapper() {
        this.classLoaderUtil = new LaunchClassLoaderUtil(Launch.classLoader);
    }
    
    @Override
    public String getName() {
        return "LaunchWrapper";
    }
    
    @Override
    public boolean isValid() {
        try {
            Launch.classLoader.hashCode();
        }
        catch (final Throwable ex) {
            return false;
        }
        return true;
    }
    
    @Override
    public void prepare() {
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.launch.");
    }
    
    @Override
    public MixinEnvironment.Phase getInitialPhase() {
        final String command = System.getProperty("sun.java.command");
        if (command != null && command.contains("GradleStart")) {
            System.setProperty("mixin.env.remapRefMap", "true");
        }
        if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") > 132) {
            return MixinEnvironment.Phase.DEFAULT;
        }
        return MixinEnvironment.Phase.PREINIT;
    }
    
    @Override
    public MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel() {
        return MixinEnvironment.CompatibilityLevel.JAVA_8;
    }
    
    @Override
    protected ILogger createLogger(final String name) {
        return new LoggerAdapterLog4j2(name);
    }
    
    @Override
    public void init() {
        if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") < 4) {
            MixinServiceLaunchWrapper.logger.error("MixinBootstrap.doInit() called during a tweak constructor!");
        }
        final List<String> tweakClasses = GlobalProperties.get(MixinServiceLaunchWrapper.BLACKBOARD_KEY_TWEAKCLASSES);
        if (tweakClasses != null) {
            tweakClasses.add("org.spongepowered.asm.mixin.EnvironmentStateTweaker");
        }
        super.init();
    }
    
    @Override
    public Collection<String> getPlatformAgents() {
        return ImmutableList.of("org.spongepowered.asm.launch.platform.MixinPlatformAgentFMLLegacy", "org.spongepowered.asm.launch.platform.MixinPlatformAgentLiteLoaderLegacy");
    }
    
    @Override
    public IContainerHandle getPrimaryContainer() {
        URI uri = null;
        try {
            uri = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            if (uri != null) {
                return new ContainerHandleURI(uri);
            }
        }
        catch (final URISyntaxException ex) {
            ex.printStackTrace();
        }
        return new ContainerHandleVirtual(this.getName());
    }
    
    @Override
    public Collection<IContainerHandle> getMixinContainers() {
        final ImmutableList.Builder<IContainerHandle> list = ImmutableList.builder();
        this.getContainersFromClassPath(list);
        this.getContainersFromAgents(list);
        return list.build();
    }
    
    private void getContainersFromClassPath(final ImmutableList.Builder<IContainerHandle> list) {
        final URL[] sources = this.getClassPath();
        if (sources != null) {
            for (final URL url : sources) {
                try {
                    final URI uri = url.toURI();
                    MixinServiceLaunchWrapper.logger.debug("Scanning {} for mixin tweaker", uri);
                    if ("file".equals(uri.getScheme()) && Files.toFile(uri).exists()) {
                        final MainAttributes attributes = MainAttributes.of(uri);
                        final String tweaker = attributes.get("TweakClass");
                        if ("org.spongepowered.asm.launch.MixinTweaker".equals(tweaker)) {
                            list.add(new ContainerHandleURI(uri));
                        }
                    }
                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public IClassProvider getClassProvider() {
        return this;
    }
    
    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return this;
    }
    
    @Override
    public ITransformerProvider getTransformerProvider() {
        return this;
    }
    
    @Override
    public IClassTracker getClassTracker() {
        return this.classLoaderUtil;
    }
    
    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }
    
    @Override
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        return Launch.classLoader.findClass(name);
    }
    
    @Override
    public Class<?> findClass(final String name, final boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Launch.classLoader);
    }
    
    @Override
    public Class<?> findAgentClass(final String name, final boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, Launch.class.getClassLoader());
    }
    
    @Override
    public void beginPhase() {
        Launch.classLoader.registerTransformer("org.spongepowered.asm.mixin.transformer.Proxy");
        this.delegatedTransformers = null;
    }
    
    @Override
    public void checkEnv(final Object bootSource) {
        if (bootSource.getClass().getClassLoader() != Launch.class.getClassLoader()) {
            throw new MixinException("Attempted to init the mixin environment in the wrong classloader");
        }
    }
    
    @Override
    public InputStream getResourceAsStream(final String name) {
        return Launch.classLoader.getResourceAsStream(name);
    }
    
    @Deprecated
    @Override
    public URL[] getClassPath() {
        return Launch.classLoader.getSources().toArray(new URL[0]);
    }
    
    @Override
    public Collection<ITransformer> getTransformers() {
        final List<IClassTransformer> transformers = Launch.classLoader.getTransformers();
        final List<ITransformer> wrapped = new ArrayList<ITransformer>(transformers.size());
        for (final IClassTransformer transformer : transformers) {
            if (transformer instanceof ITransformer) {
                wrapped.add((ITransformer)transformer);
            }
            else {
                wrapped.add(new LegacyTransformerHandle(transformer));
            }
            if (transformer instanceof IClassNameTransformer) {
                MixinServiceLaunchWrapper.logger.debug("Found name transformer: {}", transformer.getClass().getName());
                this.nameTransformer = (IClassNameTransformer)transformer;
            }
        }
        return wrapped;
    }
    
    @Override
    public List<ITransformer> getDelegatedTransformers() {
        return Collections.unmodifiableList((List<? extends ITransformer>)this.getDelegatedLegacyTransformers());
    }
    
    private List<ILegacyClassTransformer> getDelegatedLegacyTransformers() {
        if (this.delegatedTransformers == null) {
            this.buildTransformerDelegationList();
        }
        return this.delegatedTransformers;
    }
    
    private void buildTransformerDelegationList() {
        MixinServiceLaunchWrapper.logger.debug("Rebuilding transformer delegation list:");
        this.delegatedTransformers = new ArrayList<ILegacyClassTransformer>();
        for (final ITransformer transformer : this.getTransformers()) {
            if (!(transformer instanceof ILegacyClassTransformer)) {
                continue;
            }
            final ILegacyClassTransformer legacyTransformer = (ILegacyClassTransformer)transformer;
            final String transformerName = legacyTransformer.getName();
            boolean include = true;
            for (final String excludeClass : MixinServiceLaunchWrapper.excludeTransformers) {
                if (transformerName.contains(excludeClass)) {
                    include = false;
                    break;
                }
            }
            if (include && !legacyTransformer.isDelegationExcluded()) {
                MixinServiceLaunchWrapper.logger.debug("  Adding:    {}", transformerName);
                this.delegatedTransformers.add(legacyTransformer);
            }
            else {
                MixinServiceLaunchWrapper.logger.debug("  Excluding: {}", transformerName);
            }
        }
        MixinServiceLaunchWrapper.logger.debug("Transformer delegation list created with {} entries", this.delegatedTransformers.size());
    }
    
    @Override
    public void addTransformerExclusion(final String name) {
        MixinServiceLaunchWrapper.excludeTransformers.add(name);
        this.delegatedTransformers = null;
    }
    
    @Deprecated
    public byte[] getClassBytes(final String name, final String transformedName) throws IOException {
        final byte[] classBytes = Launch.classLoader.getClassBytes(name);
        if (classBytes != null) {
            return classBytes;
        }
        URLClassLoader appClassLoader;
        if (Launch.class.getClassLoader() instanceof URLClassLoader) {
            appClassLoader = (URLClassLoader)Launch.class.getClassLoader();
        }
        else {
            appClassLoader = new URLClassLoader(new URL[0], Launch.class.getClassLoader());
        }
        InputStream classStream = null;
        try {
            final String resourcePath = transformedName.replace('.', '/').concat(".class");
            classStream = appClassLoader.getResourceAsStream(resourcePath);
            return ByteStreams.toByteArray(classStream);
        }
        catch (final Exception ex) {
            return null;
        }
        finally {
            Closeables.closeQuietly(classStream);
        }
    }
    
    @Deprecated
    public byte[] getClassBytes(final String className, final boolean runTransformers) throws ClassNotFoundException, IOException {
        final String transformedName = className.replace('/', '.');
        final String name = this.unmapClassName(transformedName);
        final Profiler profiler = Profiler.getProfiler("mixin");
        final Profiler.Section loadTime = profiler.begin(1, "class.load");
        byte[] classBytes = this.getClassBytes(name, transformedName);
        loadTime.end();
        if (runTransformers) {
            final Profiler.Section transformTime = profiler.begin(1, "class.transform");
            classBytes = this.applyTransformers(name, transformedName, classBytes, profiler);
            transformTime.end();
        }
        if (classBytes == null) {
            throw new ClassNotFoundException(String.format("The specified class '%s' was not found", transformedName));
        }
        return classBytes;
    }
    
    private byte[] applyTransformers(final String name, final String transformedName, byte[] basicClass, final Profiler profiler) {
        if (this.classLoaderUtil.isClassExcluded(name, transformedName)) {
            return basicClass;
        }
        for (final ILegacyClassTransformer transformer : this.getDelegatedLegacyTransformers()) {
            this.lock.clear();
            final int pos = transformer.getName().lastIndexOf(46);
            final String simpleName = transformer.getName().substring(pos + 1);
            final Profiler.Section transformTime = profiler.begin(2, simpleName.toLowerCase(Locale.ROOT));
            transformTime.setInfo(transformer.getName());
            basicClass = transformer.transformClassBytes(name, transformedName, basicClass);
            transformTime.end();
            if (this.lock.isSet()) {
                this.addTransformerExclusion(transformer.getName());
                this.lock.clear();
                MixinServiceLaunchWrapper.logger.info("A re-entrant transformer '{}' was detected and will no longer process meta class data", transformer.getName());
            }
        }
        return basicClass;
    }
    
    private String unmapClassName(final String className) {
        if (this.nameTransformer == null) {
            this.findNameTransformer();
        }
        if (this.nameTransformer != null) {
            return this.nameTransformer.unmapClassName(className);
        }
        return className;
    }
    
    private void findNameTransformer() {
        final List<IClassTransformer> transformers = Launch.classLoader.getTransformers();
        for (final IClassTransformer transformer : transformers) {
            if (transformer instanceof IClassNameTransformer) {
                MixinServiceLaunchWrapper.logger.debug("Found name transformer: {}", transformer.getClass().getName());
                this.nameTransformer = (IClassNameTransformer)transformer;
            }
        }
    }
    
    @Override
    public ClassNode getClassNode(final String className) throws ClassNotFoundException, IOException {
        return this.getClassNode(className, this.getClassBytes(className, true), 8);
    }
    
    @Override
    public ClassNode getClassNode(final String className, final boolean runTransformers) throws ClassNotFoundException, IOException {
        return this.getClassNode(className, this.getClassBytes(className, true), 8);
    }
    
    private ClassNode getClassNode(final String className, final byte[] classBytes, final int flags) {
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new MixinClassReader(classBytes, className);
        classReader.accept(classNode, flags);
        return classNode;
    }
    
    private static int findInStackTrace(final String className, final String methodName) {
        final Thread currentThread = Thread.currentThread();
        if (!"main".equals(currentThread.getName())) {
            return 0;
        }
        final StackTraceElement[] stackTrace2;
        final StackTraceElement[] stackTrace = stackTrace2 = currentThread.getStackTrace();
        for (final StackTraceElement s : stackTrace2) {
            if (className.equals(s.getClassName()) && methodName.equals(s.getMethodName())) {
                return s.getLineNumber();
            }
        }
        return 0;
    }
    
    static {
        BLACKBOARD_KEY_TWEAKCLASSES = GlobalProperties.Keys.of("TweakClasses");
        BLACKBOARD_KEY_TWEAKS = GlobalProperties.Keys.of("Tweaks");
        excludeTransformers = Sets.newHashSet("net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer", "cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer", "net.minecraftforge.fml.common.asm.transformers.TerminalTransformer", "cpw.mods.fml.common.asm.transformers.TerminalTransformer");
        logger = LogManager.getLogger();
    }
}
