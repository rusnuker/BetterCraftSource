// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.objectweb.asm.tree.InnerClassNode;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.util.LanguageFeatures;
import org.spongepowered.asm.mixin.Unique;
import org.objectweb.asm.ClassVisitor;
import java.util.HashSet;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.util.asm.ASM;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.Handle;
import org.spongepowered.asm.util.asm.MethodNodeEx;
import java.io.IOException;
import java.util.Set;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.logging.Level;
import com.google.common.collect.Iterables;
import java.util.Iterator;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTargetAlreadyLoadedException;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Collections;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.base.Functions;
import java.util.Collection;
import org.spongepowered.asm.service.IClassTracker;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import java.util.ArrayList;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.IMixinService;
import java.util.List;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

class MixinInfo implements Comparable<MixinInfo>, IMixinInfo
{
    static int mixinOrder;
    private final transient ILogger logger;
    private final transient Profiler profiler;
    private final transient MixinConfig parent;
    private final String name;
    private final String className;
    private final int priority;
    private final boolean virtual;
    private final transient List<DeclaredTarget> declaredTargets;
    private final transient List<ClassInfo> targetClasses;
    private final List<String> targetClassNames;
    private final transient int order;
    private final transient IMixinService service;
    private final transient PluginHandle plugin;
    private final transient MixinEnvironment.Phase phase;
    private final transient ClassInfo info;
    private final transient SubType type;
    private final transient boolean strict;
    private final transient Extensions extensions;
    private transient State pendingState;
    private transient State state;
    
    MixinInfo(final IMixinService service, final MixinConfig parent, final String name, final PluginHandle plugin, final boolean ignorePlugin, final Extensions extensions) {
        this.logger = MixinService.getService().getLogger("mixin");
        this.profiler = Profiler.getProfiler("mixin");
        this.targetClasses = new ArrayList<ClassInfo>();
        this.targetClassNames = new ArrayList<String>();
        this.order = MixinInfo.mixinOrder++;
        this.service = service;
        this.parent = parent;
        this.name = name;
        this.className = parent.getMixinPackage() + name;
        this.plugin = plugin;
        this.phase = parent.getEnvironment().getPhase();
        this.strict = parent.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_TARGETS);
        this.extensions = extensions;
        try {
            final ClassNode mixinClassNode = this.loadMixinClass(this.className);
            this.pendingState = new State(mixinClassNode);
            this.info = this.pendingState.getClassInfo();
            this.type = SubType.getTypeFor(this);
        }
        catch (final InvalidMixinException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, ex2.getMessage(), ex2);
        }
        if (!this.type.isLoadable()) {
            final IClassTracker tracker = this.service.getClassTracker();
            if (tracker != null) {
                tracker.registerInvalidClass(this.className);
            }
        }
        try {
            this.priority = this.readPriority(this.pendingState.getClassNode());
            this.virtual = this.readPseudo(this.pendingState.getValidationClassNode());
            this.declaredTargets = this.readDeclaredTargets(this.pendingState.getValidationClassNode(), ignorePlugin);
        }
        catch (final InvalidMixinException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, ex2);
        }
    }
    
    void parseTargets() {
        try {
            this.targetClasses.addAll(this.readTargetClasses(this.declaredTargets));
            this.targetClassNames.addAll((Collection<? extends String>)Lists.transform(this.targetClasses, (Function<? super ClassInfo, ?>)Functions.toStringFunction()));
        }
        catch (final InvalidMixinException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, ex2);
        }
    }
    
    void validate() {
        if (this.pendingState == null) {
            throw new IllegalStateException("No pending validation state for " + this);
        }
        try {
            this.pendingState.validate(this.type, this.targetClasses);
            this.state = this.pendingState;
        }
        finally {
            this.pendingState = null;
        }
    }
    
    protected List<DeclaredTarget> readDeclaredTargets(final MixinClassNode classNode, final boolean ignorePlugin) {
        if (classNode == null) {
            return Collections.emptyList();
        }
        final AnnotationNode mixin = Annotations.getInvisible(classNode, Mixin.class);
        if (mixin == null) {
            throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", this.className));
        }
        final IClassTracker tracker = this.service.getClassTracker();
        final List<DeclaredTarget> declaredTargets = new ArrayList<DeclaredTarget>();
        for (final Object target : this.readTargets(mixin)) {
            final DeclaredTarget declaredTarget = DeclaredTarget.of(target, this);
            if (declaredTarget == null) {
                continue;
            }
            if (tracker != null && tracker.isClassLoaded(declaredTarget.name) && !this.isReloading()) {
                final String message = String.format("Critical problem: %s target %s was loaded too early.", this, declaredTarget.name);
                if (this.parent.isRequired()) {
                    throw new MixinTargetAlreadyLoadedException(this, message, declaredTarget.name);
                }
                this.logger.error(message, new Object[0]);
            }
            if (!this.shouldApplyMixin(ignorePlugin, declaredTarget.name)) {
                continue;
            }
            declaredTargets.add(declaredTarget);
        }
        return declaredTargets;
    }
    
    private Iterable<Object> readTargets(final AnnotationNode mixin) {
        final Iterable<Object> publicTargets = Annotations.getValue(mixin, "value");
        final Iterable<Object> privateTargets = Annotations.getValue(mixin, "targets");
        if (publicTargets == null && privateTargets == null) {
            return Collections.emptyList();
        }
        if (publicTargets == null) {
            return privateTargets;
        }
        return (privateTargets == null) ? publicTargets : Iterables.concat((Iterable<?>)publicTargets, (Iterable<?>)privateTargets);
    }
    
    private boolean shouldApplyMixin(final boolean ignorePlugin, final String targetName) {
        final Profiler.Section pluginTimer = this.profiler.begin("plugin");
        final boolean result = ignorePlugin || this.plugin.shouldApplyMixin(targetName, this.className);
        pluginTimer.end();
        return result;
    }
    
    List<ClassInfo> readTargetClasses(final MixinClassNode classNode, final boolean ignorePlugin) {
        return this.readTargetClasses(this.readDeclaredTargets(classNode, ignorePlugin));
    }
    
    private List<ClassInfo> readTargetClasses(final List<DeclaredTarget> declaredTargets) throws InvalidMixinException {
        final List<ClassInfo> targetClasses = new ArrayList<ClassInfo>();
        for (final DeclaredTarget target : declaredTargets) {
            final ClassInfo targetClass = this.getTargetClass(target);
            if (targetClass != null) {
                targetClasses.add(targetClass);
                targetClass.addMixin(this);
            }
        }
        return targetClasses;
    }
    
    private ClassInfo getTargetClass(final DeclaredTarget target) throws InvalidMixinException {
        final ClassInfo targetInfo = ClassInfo.forName(target.name);
        if (targetInfo == null) {
            if (this.isVirtual()) {
                this.logger.debug("Skipping virtual target {} for {}", target.name, this);
            }
            else {
                this.handleTargetError(String.format("@Mixin target %s was not found %s", target.name, this), false);
            }
            return null;
        }
        this.type.validateTarget(target.name, targetInfo);
        if (target.isPrivate && targetInfo.isReallyPublic() && !this.isVirtual()) {
            this.handleTargetError(String.format("@Mixin target %s is public in %s and should be specified in value", target.name, this), true);
        }
        return targetInfo;
    }
    
    private void handleTargetError(final String message, final boolean verboseOnly) {
        if (this.strict) {
            this.logger.error(message, new Object[0]);
            throw new InvalidMixinException(this, message);
        }
        this.logger.log((verboseOnly && !this.parent.isVerboseLogging()) ? Level.DEBUG : Level.WARN, message, new Object[0]);
    }
    
    protected int readPriority(final ClassNode classNode) {
        if (classNode == null) {
            return this.parent.getDefaultMixinPriority();
        }
        final AnnotationNode mixin = Annotations.getInvisible(classNode, Mixin.class);
        if (mixin == null) {
            throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", this.className));
        }
        final Integer priority = Annotations.getValue(mixin, "priority");
        return (priority == null) ? this.parent.getDefaultMixinPriority() : priority;
    }
    
    protected boolean readPseudo(final ClassNode classNode) {
        return Annotations.getInvisible(classNode, Pseudo.class) != null;
    }
    
    private boolean isReloading() {
        return this.pendingState instanceof Reloaded;
    }
    
    String remapClassName(final String className) {
        return this.parent.remapClassName(this.getClassRef(), className);
    }
    
    public boolean hasDeclaredTarget(final String targetClass) {
        for (final DeclaredTarget declaredTarget : this.declaredTargets) {
            if (targetClass.equals(declaredTarget.name)) {
                return true;
            }
        }
        return false;
    }
    
    private State getState() {
        return (this.state != null) ? this.state : this.pendingState;
    }
    
    ClassInfo getClassInfo() {
        return this.info;
    }
    
    @Override
    public IMixinConfig getConfig() {
        return this.parent;
    }
    
    MixinConfig getParent() {
        return this.parent;
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getClassName() {
        return this.className;
    }
    
    @Override
    public String getClassRef() {
        return this.getClassInfo().getName();
    }
    
    @Override
    public byte[] getClassBytes() {
        throw new RuntimeException("NO");
    }
    
    @Override
    public boolean isDetachedSuper() {
        return this.getState().isDetachedSuper();
    }
    
    public boolean isUnique() {
        return this.getState().isUnique();
    }
    
    public boolean isVirtual() {
        return this.virtual;
    }
    
    public boolean isAccessor() {
        return this.type instanceof SubType.Accessor;
    }
    
    public boolean isLoadable() {
        return this.type.isLoadable();
    }
    
    public boolean isRequired() {
        return this.parent.isRequired();
    }
    
    public Level getLoggingLevel() {
        return this.parent.getLoggingLevel();
    }
    
    @Override
    public MixinEnvironment.Phase getPhase() {
        return this.phase;
    }
    
    @Override
    public MixinClassNode getClassNode(final int flags) {
        return this.getState().createClassNode(flags);
    }
    
    List<String> getDeclaredTargetClasses() {
        return Collections.unmodifiableList(Lists.transform(this.declaredTargets, (Function<? super DeclaredTarget, ? extends String>)Functions.toStringFunction()));
    }
    
    @Override
    public List<String> getTargetClasses() {
        return Collections.unmodifiableList((List<? extends String>)this.targetClassNames);
    }
    
    List<InterfaceInfo> getSoftImplements() {
        return Collections.unmodifiableList(this.getState().getSoftImplements());
    }
    
    Set<String> getSyntheticInnerClasses() {
        return Collections.unmodifiableSet((Set<? extends String>)this.getState().getSyntheticInnerClasses());
    }
    
    Set<String> getInnerClasses() {
        return Collections.unmodifiableSet((Set<? extends String>)this.getState().getInnerClasses());
    }
    
    List<ClassInfo> getTargets() {
        return Collections.unmodifiableList((List<? extends ClassInfo>)this.targetClasses);
    }
    
    Set<String> getInterfaces() {
        return this.getState().getInterfaces();
    }
    
    Extensions getExtensions() {
        return this.extensions;
    }
    
    MixinTargetContext createContextFor(final TargetClassContext target) {
        final MixinClassNode classNode = this.getClassNode(8);
        final Profiler.Section preTimer = this.profiler.begin("pre");
        final MixinTargetContext context = this.type.createPreProcessor(classNode).prepare(this.extensions).createContextFor(target);
        preTimer.end();
        return context;
    }
    
    private ClassNode loadMixinClass(final String mixinClassName) throws ClassNotFoundException {
        ClassNode classNode = null;
        try {
            final IClassTracker tracker = this.service.getClassTracker();
            if (tracker != null) {
                final String restrictions = tracker.getClassRestrictions(mixinClassName);
                if (restrictions.length() > 0) {
                    this.logger.error("Classloader restrictions [{}] encountered loading {}, name: {}", restrictions, this, mixinClassName);
                }
            }
            classNode = this.service.getBytecodeProvider().getClassNode(mixinClassName, true);
        }
        catch (final ClassNotFoundException ex) {
            throw new ClassNotFoundException(String.format("The specified mixin '%s' was not found", mixinClassName));
        }
        catch (final IOException ex2) {
            this.logger.warn("Failed to load mixin {}, the specified mixin will not be applied", mixinClassName);
            throw new InvalidMixinException(this, "An error was encountered whilst loading the mixin class", ex2);
        }
        return classNode;
    }
    
    void reloadMixin(final ClassNode classNode) {
        if (this.pendingState != null) {
            throw new IllegalStateException("Cannot reload mixin while it is initialising");
        }
        this.pendingState = new Reloaded(this.state, classNode);
        this.validate();
    }
    
    @Override
    public int compareTo(final MixinInfo other) {
        if (other == null) {
            return 0;
        }
        if (other.priority == this.priority) {
            return this.order - other.order;
        }
        return this.priority - other.priority;
    }
    
    public void preApply(final String transformedName, final ClassNode targetClass) throws Exception {
        if (this.plugin.isAvailable()) {
            final Profiler.Section pluginTimer = this.profiler.begin("plugin");
            try {
                this.plugin.preApply(transformedName, targetClass, this.className, this);
            }
            finally {
                pluginTimer.end();
            }
        }
    }
    
    public void postApply(final String transformedName, final ClassNode targetClass) throws Exception {
        if (this.plugin.isAvailable()) {
            final Profiler.Section pluginTimer = this.profiler.begin("plugin");
            try {
                this.plugin.postApply(transformedName, targetClass, this.className, this);
            }
            finally {
                pluginTimer.end();
            }
        }
        this.parent.postApply(transformedName, targetClass);
        this.info.addAppliedMixin(this);
    }
    
    @Override
    public String toString() {
        return String.format("%s:%s", this.parent.getName(), this.name);
    }
    
    static Variant getVariant(final ClassNode classNode) {
        return getVariant(ClassInfo.fromClassNode(classNode));
    }
    
    static Variant getVariant(final ClassInfo classInfo) {
        if (!classInfo.isInterface()) {
            return Variant.STANDARD;
        }
        boolean containsNonAccessorMethod = false;
        for (final ClassInfo.Method method : classInfo.getMethods()) {
            containsNonAccessorMethod |= (!method.isAccessor() && !method.isSynthetic());
        }
        if (containsNonAccessorMethod) {
            return Variant.INTERFACE;
        }
        return Variant.ACCESSOR;
    }
    
    static {
        MixinInfo.mixinOrder = 0;
    }
    
    enum Variant
    {
        STANDARD, 
        INTERFACE, 
        ACCESSOR, 
        PROXY;
    }
    
    class MixinMethodNode extends MethodNodeEx
    {
        public MixinMethodNode(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
            super(access, name, desc, signature, exceptions, MixinInfo.this);
        }
        
        @Override
        public void visitInvokeDynamicInsn(final String name, final String descriptor, final Handle bootstrapMethodHandle, final Object... bootstrapMethodArguments) {
            final Object[] bsmArgs = new Object[bootstrapMethodArguments.length];
            System.arraycopy(bootstrapMethodArguments, 0, bsmArgs, 0, bootstrapMethodArguments.length);
            this.instructions.add(new InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bsmArgs));
        }
        
        public boolean isInjector() {
            return this.getInjectorAnnotation() != null || this.isSurrogate();
        }
        
        public boolean isSurrogate() {
            return this.getVisibleAnnotation(Surrogate.class) != null;
        }
        
        public boolean isSynthetic() {
            return Bytecode.hasFlag(this, 4096);
        }
        
        public AnnotationNode getVisibleAnnotation(final Class<? extends Annotation> annotationClass) {
            return Annotations.getVisible(this, annotationClass);
        }
        
        public AnnotationNode getInjectorAnnotation() {
            return InjectionInfo.getInjectorAnnotation(MixinInfo.this, this);
        }
    }
    
    class MixinClassNode extends ClassNode
    {
        public final List<MixinMethodNode> mixinMethods;
        
        MixinClassNode(final MixinInfo this$0, final MixinInfo mixin) {
            this(this$0, ASM.API_VERSION);
        }
        
        protected MixinClassNode(final int api) {
            super(api);
            this.mixinMethods = (List)this.methods;
        }
        
        public MixinInfo getMixin() {
            return MixinInfo.this;
        }
        
        public List<FieldNode> getFields() {
            return new ArrayList<FieldNode>(this.fields);
        }
        
        @Override
        public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
            final MethodNode method = new MixinMethodNode(access, name, desc, signature, exceptions);
            this.methods.add(method);
            return method;
        }
    }
    
    class State
    {
        private final ClassNode classNode;
        private final ClassInfo classInfo;
        private boolean detachedSuper;
        private boolean unique;
        protected final Set<String> interfaces;
        protected final List<InterfaceInfo> softImplements;
        protected final Set<String> syntheticInnerClasses;
        protected final Set<String> innerClasses;
        protected MixinClassNode validationClassNode;
        
        State(final MixinInfo this$0, final ClassNode classNode) {
            this(this$0, classNode, null);
        }
        
        State(final ClassNode classNode, final ClassInfo classInfo) {
            this.interfaces = new HashSet<String>();
            this.softImplements = new ArrayList<InterfaceInfo>();
            this.syntheticInnerClasses = new HashSet<String>();
            this.innerClasses = new HashSet<String>();
            this.classNode = classNode;
            this.connect();
            this.classInfo = ((classInfo != null) ? classInfo : ClassInfo.fromClassNode(this.getValidationClassNode()));
        }
        
        protected void connect() {
            this.validationClassNode = this.createClassNode(0);
        }
        
        protected void complete() {
            this.validationClassNode = null;
        }
        
        ClassInfo getClassInfo() {
            return this.classInfo;
        }
        
        ClassNode getClassNode() {
            return this.classNode;
        }
        
        MixinClassNode getValidationClassNode() {
            if (this.validationClassNode == null) {
                throw new IllegalStateException("Attempted a validation task after validation is complete on " + this + " in " + MixinInfo.this);
            }
            return this.validationClassNode;
        }
        
        boolean isDetachedSuper() {
            return this.detachedSuper;
        }
        
        boolean isUnique() {
            return this.unique;
        }
        
        List<? extends InterfaceInfo> getSoftImplements() {
            return this.softImplements;
        }
        
        Set<String> getSyntheticInnerClasses() {
            return this.syntheticInnerClasses;
        }
        
        Set<String> getInnerClasses() {
            return this.innerClasses;
        }
        
        Set<String> getInterfaces() {
            return this.interfaces;
        }
        
        MixinClassNode createClassNode(final int flags) {
            final MixinClassNode mixinClassNode = new MixinClassNode(MixinInfo.this);
            this.classNode.accept(mixinClassNode);
            return mixinClassNode;
        }
        
        void validate(final SubType type, final List<ClassInfo> targetClasses) {
            final MixinClassNode classNode = this.getValidationClassNode();
            final MixinPreProcessorStandard preProcessor = type.createPreProcessor(classNode).prepare(MixinInfo.this.getExtensions());
            for (final ClassInfo target : targetClasses) {
                preProcessor.conform(target);
            }
            type.validate(this, targetClasses);
            this.detachedSuper = type.isDetachedSuper();
            this.unique = (Annotations.getVisible(classNode, Unique.class) != null);
            this.validateInner();
            this.validateClassFeatures();
            this.validateRemappables(targetClasses);
            this.readImplementations(type);
            this.readInnerClasses();
            this.validateChanges(type, targetClasses);
            this.complete();
        }
        
        private void validateInner() {
            if (!this.classInfo.isProbablyStatic()) {
                throw new InvalidMixinException(MixinInfo.this, "Inner class mixin must be declared static");
            }
        }
        
        private void validateClassFeatures() {
            final MixinEnvironment.CompatibilityLevel compatibilityLevel = MixinEnvironment.getCompatibilityLevel();
            final int requiredLanguageFeatures = LanguageFeatures.scan(this.validationClassNode);
            if (requiredLanguageFeatures == 0 || compatibilityLevel.supports(requiredLanguageFeatures)) {
                return;
            }
            final int missingFeatures = requiredLanguageFeatures & ~compatibilityLevel.getLanguageFeatures();
            final MixinEnvironment.CompatibilityLevel minRequiredLevel = MixinEnvironment.CompatibilityLevel.requiredFor(requiredLanguageFeatures);
            throw new InvalidMixinException(MixinInfo.this, String.format("Unsupported mixin, %s requires the following unsupported language features: %s, these features require compatibility level %s", MixinInfo.this, LanguageFeatures.format(missingFeatures), (minRequiredLevel != null) ? minRequiredLevel.toString() : "UNKNOWN"));
        }
        
        private void validateRemappables(final List<ClassInfo> targetClasses) {
            if (targetClasses.size() > 1) {
                for (final FieldNode field : this.validationClassNode.fields) {
                    this.validateRemappable(Shadow.class, field.name, Annotations.getVisible(field, Shadow.class));
                }
                for (final MethodNode method : this.validationClassNode.methods) {
                    this.validateRemappable(Shadow.class, method.name, Annotations.getVisible(method, Shadow.class));
                    final AnnotationNode overwrite = Annotations.getVisible(method, Overwrite.class);
                    if (overwrite != null && ((method.access & 0x8) == 0x0 || (method.access & 0x1) == 0x0)) {
                        throw new InvalidMixinException(MixinInfo.this, "Found @Overwrite annotation on " + method.name + " in " + MixinInfo.this);
                    }
                }
            }
        }
        
        private void validateRemappable(final Class<Shadow> annotationClass, final String name, final AnnotationNode annotation) {
            if (annotation != null && Annotations.getValue(annotation, "remap", Boolean.TRUE)) {
                throw new InvalidMixinException(MixinInfo.this, "Found a remappable @" + annotationClass.getSimpleName() + " annotation on " + name + " in " + this);
            }
        }
        
        void readImplementations(final SubType type) {
            this.interfaces.addAll(this.validationClassNode.interfaces);
            this.interfaces.addAll(type.getInterfaces());
            final AnnotationNode implementsAnnotation = Annotations.getInvisible(this.validationClassNode, Implements.class);
            if (implementsAnnotation == null) {
                return;
            }
            final List<AnnotationNode> interfaces = Annotations.getValue(implementsAnnotation);
            if (interfaces == null) {
                return;
            }
            for (final AnnotationNode interfaceNode : interfaces) {
                final InterfaceInfo interfaceInfo = InterfaceInfo.fromAnnotation(MixinInfo.this, interfaceNode);
                this.softImplements.add(interfaceInfo);
                this.interfaces.add(interfaceInfo.getInternalName());
                if (!(this instanceof Reloaded)) {
                    this.classInfo.addInterface(interfaceInfo.getInternalName());
                }
            }
        }
        
        void readInnerClasses() {
            for (final InnerClassNode inner : this.validationClassNode.innerClasses) {
                final ClassInfo innerClass = ClassInfo.forName(inner.name);
                if ((inner.outerName != null && inner.outerName.equals(this.classInfo.getName())) || inner.name.startsWith(this.validationClassNode.name + "$")) {
                    if (innerClass.isProbablyStatic() && innerClass.isSynthetic()) {
                        this.syntheticInnerClasses.add(inner.name);
                    }
                    else {
                        if (innerClass.isMixin()) {
                            continue;
                        }
                        this.innerClasses.add(inner.name);
                    }
                }
            }
        }
        
        protected void validateChanges(final SubType type, final List<ClassInfo> targetClasses) {
            type.createPreProcessor(this.validationClassNode).prepare(MixinInfo.this.getExtensions());
        }
    }
    
    class Reloaded extends State
    {
        private final State previous;
        
        Reloaded(final State previous, final ClassNode classNode) {
            super(classNode, previous.getClassInfo());
            this.previous = previous;
        }
        
        @Override
        protected void validateChanges(final SubType type, final List<ClassInfo> targetClasses) {
            if (!this.syntheticInnerClasses.equals(this.previous.syntheticInnerClasses)) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change inner classes");
            }
            if (!this.interfaces.equals(this.previous.interfaces)) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change interfaces");
            }
            if (!new HashSet(this.softImplements).equals(new HashSet(this.previous.softImplements))) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change soft interfaces");
            }
            final List<ClassInfo> targets = MixinInfo.this.readTargetClasses(this.validationClassNode, true);
            if (!new HashSet(targets).equals(new HashSet(targetClasses))) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change target classes");
            }
            final int priority = MixinInfo.this.readPriority(this.validationClassNode);
            if (priority != MixinInfo.this.getPriority()) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change mixin priority");
            }
        }
    }
    
    abstract static class SubType
    {
        protected final MixinInfo mixin;
        protected final String annotationType;
        protected final boolean targetMustBeInterface;
        protected boolean detached;
        
        SubType(final MixinInfo info, final String annotationType, final boolean targetMustBeInterface) {
            this.mixin = info;
            this.annotationType = annotationType;
            this.targetMustBeInterface = targetMustBeInterface;
        }
        
        Collection<String> getInterfaces() {
            return (Collection<String>)Collections.emptyList();
        }
        
        boolean isDetachedSuper() {
            return this.detached;
        }
        
        boolean isLoadable() {
            return false;
        }
        
        void validateTarget(final String targetName, final ClassInfo targetInfo) {
            final boolean targetIsInterface = targetInfo.isInterface();
            if (targetIsInterface != this.targetMustBeInterface) {
                final String not = targetIsInterface ? "" : "not ";
                throw new InvalidMixinException(this.mixin, this.annotationType + " target type mismatch: " + targetName + " is " + not + "an interface in " + this);
            }
        }
        
        abstract void validate(final State p0, final List<ClassInfo> p1);
        
        abstract MixinPreProcessorStandard createPreProcessor(final MixinClassNode p0);
        
        static SubType getTypeFor(final MixinInfo mixin) {
            final Variant variant = MixinInfo.getVariant(mixin.getClassInfo());
            switch (variant) {
                case STANDARD: {
                    return new Standard(mixin);
                }
                case INTERFACE: {
                    return new Interface(mixin);
                }
                case ACCESSOR: {
                    return new Accessor(mixin);
                }
                default: {
                    throw new IllegalStateException("Unsupported Mixin variant " + variant + " for " + mixin);
                }
            }
        }
        
        static class Standard extends SubType
        {
            Standard(final MixinInfo info) {
                super(info, "@Mixin", false);
            }
            
            @Override
            void validate(final State state, final List<ClassInfo> targetClasses) {
                final ClassNode classNode = state.getValidationClassNode();
                for (final ClassInfo targetClass : targetClasses) {
                    if (classNode.superName.equals(targetClass.getSuperName())) {
                        continue;
                    }
                    if (!targetClass.hasSuperClass(classNode.superName, ClassInfo.Traversal.SUPER)) {
                        final ClassInfo superClass = ClassInfo.forName(classNode.superName);
                        if (superClass.isMixin()) {
                            for (final ClassInfo superTarget : superClass.getTargets()) {
                                if (targetClasses.contains(superTarget)) {
                                    throw new InvalidMixinException(this.mixin, "Illegal hierarchy detected. Derived mixin " + this + " targets the same class " + superTarget.getClassName() + " as its superclass " + superClass.getClassName());
                                }
                            }
                        }
                        throw new InvalidMixinException(this.mixin, "Super class '" + classNode.superName.replace('/', '.') + "' of " + this.mixin.getName() + " was not found in the hierarchy of target class '" + targetClass + "'");
                    }
                    this.detached = true;
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode classNode) {
                return new MixinPreProcessorStandard(this.mixin, classNode);
            }
        }
        
        static class Interface extends SubType
        {
            Interface(final MixinInfo info) {
                super(info, "@Mixin", true);
            }
            
            @Override
            void validate(final State state, final List<ClassInfo> targetClasses) {
                if (!MixinEnvironment.getCompatibilityLevel().supports(1)) {
                    throw new InvalidMixinException(this.mixin, "Interface mixin not supported in current enviromnment");
                }
                final ClassNode classNode = state.getValidationClassNode();
                if (!"java/lang/Object".equals(classNode.superName)) {
                    throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + classNode.superName.replace('/', '.'));
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode classNode) {
                return new MixinPreProcessorInterface(this.mixin, classNode);
            }
        }
        
        static class Accessor extends SubType
        {
            private final Collection<String> interfaces;
            
            Accessor(final MixinInfo info) {
                super(info, "@Mixin", false);
                (this.interfaces = new ArrayList<String>()).add(info.getClassRef());
            }
            
            @Override
            boolean isLoadable() {
                return true;
            }
            
            @Override
            Collection<String> getInterfaces() {
                return this.interfaces;
            }
            
            @Override
            void validateTarget(final String targetName, final ClassInfo targetInfo) {
                final boolean targetIsInterface = targetInfo.isInterface();
                if (targetIsInterface && !MixinEnvironment.getCompatibilityLevel().supports(1)) {
                    throw new InvalidMixinException(this.mixin, "Accessor mixin targetting an interface is not supported in current enviromnment");
                }
            }
            
            @Override
            void validate(final State state, final List<ClassInfo> targetClasses) {
                final ClassNode classNode = state.getValidationClassNode();
                if (!"java/lang/Object".equals(classNode.superName)) {
                    throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + classNode.superName.replace('/', '.'));
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode classNode) {
                return new MixinPreProcessorAccessor(this.mixin, classNode);
            }
        }
    }
    
    static final class DeclaredTarget
    {
        final String name;
        final boolean isPrivate;
        
        private DeclaredTarget(final String name, final boolean isPrivate) {
            this.name = name;
            this.isPrivate = isPrivate;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        static DeclaredTarget of(final Object target, final MixinInfo info) {
            if (target instanceof String) {
                final String remappedName = info.remapClassName((String)target);
                return (remappedName != null) ? new DeclaredTarget(remappedName, true) : null;
            }
            if (target instanceof Type) {
                return new DeclaredTarget(((Type)target).getClassName(), false);
            }
            return null;
        }
    }
}
