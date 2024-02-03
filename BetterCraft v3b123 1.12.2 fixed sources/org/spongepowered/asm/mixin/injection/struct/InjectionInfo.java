// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.reflect.InvocationTargetException;
import org.spongepowered.asm.mixin.throwables.MixinException;
import java.lang.reflect.Constructor;
import com.google.common.collect.ImmutableSet;
import javax.tools.Diagnostic;
import org.spongepowered.asm.util.logging.MessageRouter;
import org.spongepowered.asm.mixin.throwables.MixinError;
import org.spongepowered.asm.util.asm.MethodNodeEx;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.objectweb.asm.Type;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.selectors.throwables.SelectorConstraintException;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.mixin.injection.selectors.ElementNode;
import com.google.common.base.Joiner;
import org.spongepowered.asm.util.asm.ASM;
import org.spongepowered.asm.mixin.injection.code.MethodSlice;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.selectors.throwables.SelectorException;
import org.spongepowered.asm.mixin.injection.code.InjectorTarget;
import java.util.Collection;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.selectors.InvalidSelectorException;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.code.MethodSlices;
import java.util.List;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import java.util.Set;
import java.lang.annotation.Annotation;
import java.util.Map;
import org.spongepowered.asm.mixin.injection.code.ISliceContext;
import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;

public abstract class InjectionInfo extends SpecialMethodInfo implements ISliceContext
{
    public static final String DEFAULT_PREFIX = "handler";
    private static Map<String, InjectorEntry> registry;
    private static Class<? extends Annotation>[] registeredAnnotations;
    protected final boolean isStatic;
    protected final Set<ITargetSelector> selectors;
    protected final List<SelectedTarget> targets;
    protected final MethodSlices slices;
    protected final String atKey;
    protected final List<InjectionPoint> injectionPoints;
    protected final Map<Target, List<InjectionNodes.InjectionNode>> targetNodes;
    protected int targetCount;
    protected Injector injector;
    protected InjectorGroupInfo group;
    private final List<MethodNode> injectedMethods;
    private int expectedCallbackCount;
    private int requiredCallbackCount;
    private int maxCallbackCount;
    private int injectedCallbackCount;
    private List<String> messages;
    
    protected InjectionInfo(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation) {
        this(mixin, method, annotation, "at");
    }
    
    protected InjectionInfo(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation, final String atKey) {
        super(mixin, method, annotation);
        this.selectors = new LinkedHashSet<ITargetSelector>();
        this.targets = new ArrayList<SelectedTarget>();
        this.injectionPoints = new ArrayList<InjectionPoint>();
        this.targetNodes = new LinkedHashMap<Target, List<InjectionNodes.InjectionNode>>();
        this.targetCount = 0;
        this.injectedMethods = new ArrayList<MethodNode>(0);
        this.expectedCallbackCount = 1;
        this.requiredCallbackCount = 0;
        this.maxCallbackCount = Integer.MAX_VALUE;
        this.injectedCallbackCount = 0;
        this.isStatic = Bytecode.isStatic(method);
        this.slices = MethodSlices.parse(this);
        this.atKey = atKey;
        this.readAnnotation();
    }
    
    protected void readAnnotation() {
        if (this.annotation == null) {
            return;
        }
        final List<AnnotationNode> injectionPoints = this.readInjectionPoints();
        this.parseRequirements();
        this.parseSelectors();
        this.findTargets();
        this.parseInjectionPoints(injectionPoints);
        this.injector = this.parseInjector(this.annotation);
    }
    
    protected void parseSelectors() {
        final Set<ITargetSelector> selectors = new LinkedHashSet<ITargetSelector>();
        TargetSelector.parse(Annotations.getValue(this.annotation, "method", false), this, selectors);
        TargetSelector.parse(Annotations.getValue(this.annotation, "target", false), this, selectors);
        if (selectors.size() == 0) {
            throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing 'method' or 'target' to specify targets", this.annotationType, this.methodName));
        }
        for (final ITargetSelector selector : selectors) {
            try {
                this.selectors.add(selector.validate().attach(this));
            }
            catch (final InvalidMemberDescriptorException ex) {
                throw new InvalidInjectionException(this, String.format("%s annotation on %s, has invalid target descriptor: %s. %s", this.annotationType, this.methodName, ex.getMessage(), this.mixin.getReferenceMapper().getStatus()));
            }
            catch (final TargetNotSupportedException ex2) {
                throw new InvalidInjectionException(this, String.format("%s annotation on %s specifies a target class '%s', which is not supported", this.annotationType, this.methodName, ex2.getMessage()));
            }
            catch (final InvalidSelectorException ex3) {
                throw new InvalidInjectionException(this, String.format("%s annotation on %s is decorated with an invalid selector: %s", this.annotationType, this.methodName, ex3.getMessage()));
            }
        }
    }
    
    protected List<AnnotationNode> readInjectionPoints() {
        final List<AnnotationNode> ats = Annotations.getValue(this.annotation, this.atKey, false);
        if (ats == null) {
            throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing '%s' value(s)", this.annotationType, this.methodName, this.atKey));
        }
        return ats;
    }
    
    protected void parseInjectionPoints(final List<AnnotationNode> ats) {
        this.injectionPoints.addAll(InjectionPoint.parse(this, ats));
    }
    
    protected void parseRequirements() {
        this.group = this.mixin.getInjectorGroups().parseGroup(this.method, this.mixin.getDefaultInjectorGroup()).add(this);
        final Integer expect = Annotations.getValue(this.annotation, "expect");
        if (expect != null) {
            this.expectedCallbackCount = expect;
        }
        final Integer require = Annotations.getValue(this.annotation, "require");
        if (require != null && require > -1) {
            this.requiredCallbackCount = require;
        }
        else if (this.group.isDefault()) {
            this.requiredCallbackCount = this.mixin.getDefaultRequiredInjections();
        }
        final Integer allow = Annotations.getValue(this.annotation, "allow");
        if (allow != null) {
            this.maxCallbackCount = Math.max(Math.max(this.requiredCallbackCount, 1), allow);
        }
    }
    
    protected abstract Injector parseInjector(final AnnotationNode p0);
    
    public boolean isValid() {
        return this.targets.size() > 0 && this.injectionPoints.size() > 0;
    }
    
    public void prepare() {
        this.targetNodes.clear();
        for (final SelectedTarget targetMethod : this.targets) {
            final Target target = this.mixin.getTargetMethod(targetMethod.method);
            final InjectorTarget injectorTarget = new InjectorTarget(this, target, targetMethod.selector);
            try {
                this.targetNodes.put(target, this.injector.find(injectorTarget, this.injectionPoints));
            }
            catch (final SelectorException ex) {
                throw new InvalidInjectionException(this, String.format("Injection validation failed: %s on %s: %s. %s%s", this.annotationType, this.methodName, ex.getMessage(), this.mixin.getReferenceMapper().getStatus(), this.getDynamicInfo()));
            }
            finally {
                injectorTarget.dispose();
            }
        }
    }
    
    public void preInject() {
        for (final Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : this.targetNodes.entrySet()) {
            this.injector.preInject(entry.getKey(), entry.getValue());
        }
    }
    
    public void inject() {
        for (final Map.Entry<Target, List<InjectionNodes.InjectionNode>> entry : this.targetNodes.entrySet()) {
            this.injector.inject(entry.getKey(), entry.getValue());
        }
        this.targets.clear();
    }
    
    public void postInject() {
        for (final MethodNode method : this.injectedMethods) {
            this.classNode.methods.add(method);
        }
        final String description = this.getDescription();
        final String refMapStatus = this.mixin.getReferenceMapper().getStatus();
        final String extraInfo = this.getDynamicInfo() + this.getMessages();
        if (this.mixin.getOption(MixinEnvironment.Option.DEBUG_INJECTORS) && this.injectedCallbackCount < this.expectedCallbackCount) {
            throw new InvalidInjectionException(this, String.format("Injection validation failed: %s %s%s in %s expected %d invocation(s) but %d succeeded. Scanned %d target(s). %s%s", description, this.methodName, this.method.desc, this.mixin, this.expectedCallbackCount, this.injectedCallbackCount, this.targetCount, refMapStatus, extraInfo));
        }
        if (this.injectedCallbackCount < this.requiredCallbackCount) {
            throw new InjectionError(String.format("Critical injection failure: %s %s%s in %s failed injection check, (%d/%d) succeeded. Scanned %d target(s). %s%s", description, this.methodName, this.method.desc, this.mixin, this.injectedCallbackCount, this.requiredCallbackCount, this.targetCount, refMapStatus, extraInfo));
        }
        if (this.injectedCallbackCount > this.maxCallbackCount) {
            throw new InjectionError(String.format("Critical injection failure: %s %s%s in %s failed injection check, %d succeeded of %d allowed.%s", description, this.methodName, this.method.desc, this.mixin, this.injectedCallbackCount, this.maxCallbackCount, extraInfo));
        }
    }
    
    public void notifyInjected(final Target target) {
    }
    
    protected String getDescription() {
        return "Callback method";
    }
    
    @Override
    public String toString() {
        return describeInjector(this.mixin, this.annotation, this.method);
    }
    
    public int getTargetCount() {
        return this.targets.size();
    }
    
    @Override
    public MethodSlice getSlice(final String id) {
        return this.slices.get(this.getSliceId(id));
    }
    
    public String getSliceId(final String id) {
        return "";
    }
    
    public int getInjectedCallbackCount() {
        return this.injectedCallbackCount;
    }
    
    public MethodNode addMethod(final int access, final String name, final String desc) {
        final MethodNode method = new MethodNode(ASM.API_VERSION, access | 0x1000, name, desc, null, null);
        this.injectedMethods.add(method);
        return method;
    }
    
    public void addCallbackInvocation(final MethodNode handler) {
        ++this.injectedCallbackCount;
    }
    
    @Override
    public void addMessage(final String format, final Object... args) {
        super.addMessage(format, args);
        if (this.messages == null) {
            this.messages = new ArrayList<String>();
        }
        final String message = String.format(format, args);
        this.messages.add(message);
    }
    
    protected String getMessages() {
        return (this.messages != null) ? (" Messages: { " + Joiner.on(" ").join(this.messages) + "}") : "";
    }
    
    protected void findTargets() {
        this.targets.clear();
        this.findRootTargets();
        this.validateTargets();
    }
    
    private void findRootTargets() {
        final int passes = this.mixin.getOption(MixinEnvironment.Option.REFMAP_REMAP) ? 2 : 1;
        for (ITargetSelector selector : this.selectors) {
            selector = selector.configure(ITargetSelector.Configure.SELECT_MEMBER, new String[0]);
            int matchCount = 0;
            final int maxCount = selector.getMaxMatchCount();
            final ITargetSelector permissiveSelector = selector.configure(ITargetSelector.Configure.PERMISSIVE, new String[0]);
        Label_0289:
            for (int selectorPasses = (permissiveSelector == selector) ? 1 : passes, pass = 0; pass < selectorPasses && matchCount < 1; ++pass) {
                final ITargetSelector passSelector = (pass == 0) ? selector : permissiveSelector;
                for (final MethodNode target : this.classNode.methods) {
                    if (passSelector.match(ElementNode.of(this.classNode, target)).isExactMatch()) {
                        ++matchCount;
                        final boolean isMixinMethod = Annotations.getVisible(target, MixinMerged.class) != null;
                        if (maxCount <= 1 || ((this.isStatic || !Bytecode.isStatic(target)) && target != this.method && !isMixinMethod)) {
                            this.checkTarget(target);
                            this.targets.add(new SelectedTarget(passSelector, target));
                        }
                        if (matchCount >= maxCount) {
                            break Label_0289;
                        }
                        continue;
                    }
                }
            }
            if (matchCount < selector.getMinMatchCount()) {
                throw new InvalidInjectionException(this, new SelectorConstraintException(selector, String.format("Injection validation failed: %s for %s on %s did not match the required number of targets (required=%d, matched=%d). %s%s", selector, this.annotationType, this.methodName, selector.getMinMatchCount(), matchCount, this.mixin.getReferenceMapper().getStatus(), this.getDynamicInfo())));
            }
        }
    }
    
    protected void validateTargets() {
        this.targetCount = this.targets.size();
        if (this.targetCount > 0) {
            return;
        }
        if (this.mixin.getOption(MixinEnvironment.Option.DEBUG_INJECTORS) && this.expectedCallbackCount > 0) {
            throw new InvalidInjectionException(this, String.format("Injection validation failed: %s annotation on %s could not find any targets matching %s in %s. %s%s", this.annotationType, this.methodName, namesOf(this.selectors), this.mixin.getTarget(), this.mixin.getReferenceMapper().getStatus(), this.getDynamicInfo()));
        }
        if (this.requiredCallbackCount > 0) {
            throw new InvalidInjectionException(this, String.format("Critical injection failure: %s annotation on %s could not find any targets matching %s in %s. %s%s", this.annotationType, this.methodName, namesOf(this.selectors), this.mixin.getTarget(), this.mixin.getReferenceMapper().getStatus(), this.getDynamicInfo()));
        }
    }
    
    protected void checkTarget(final MethodNode target) {
        final AnnotationNode merged = Annotations.getVisible(target, MixinMerged.class);
        if (merged == null) {
            return;
        }
        if (Annotations.getVisible(target, Final.class) != null) {
            throw new InvalidInjectionException(this, String.format("%s cannot inject into @Final method %s::%s%s merged by %s", this, this.classNode.name, target.name, target.desc, Annotations.getValue(merged, "mixin")));
        }
    }
    
    protected String getDynamicInfo() {
        final AnnotationNode annotation = Annotations.getInvisible(this.method, Dynamic.class);
        String description = Strings.nullToEmpty(Annotations.getValue(annotation));
        final Type upstream = Annotations.getValue(annotation, "mixin");
        if (upstream != null) {
            description = String.format("{%s} %s", upstream.getClassName(), description).trim();
        }
        return (description.length() > 0) ? String.format(" Method is @Dynamic(%s).", description) : "";
    }
    
    public static InjectionInfo parse(final MixinTargetContext mixin, final MethodNode method) {
        final AnnotationNode annotation = getInjectorAnnotation(mixin.getMixin(), method);
        if (annotation == null) {
            return null;
        }
        for (final InjectorEntry injector : InjectionInfo.registry.values()) {
            if (annotation.desc.equals(injector.annotationDesc)) {
                return injector.create(mixin, method, annotation);
            }
        }
        return null;
    }
    
    public static AnnotationNode getInjectorAnnotation(final IMixinInfo mixin, final MethodNode method) {
        AnnotationNode annotation = null;
        try {
            annotation = Annotations.getSingleVisible(method, InjectionInfo.registeredAnnotations);
        }
        catch (final IllegalArgumentException ex) {
            throw new InvalidMixinException(mixin, String.format("Error parsing annotations on %s in %s: %s", method.name, mixin.getClassName(), ex.getMessage()));
        }
        return annotation;
    }
    
    public static String getInjectorPrefix(final AnnotationNode annotation) {
        if (annotation == null) {
            return "handler";
        }
        for (final InjectorEntry injector : InjectionInfo.registry.values()) {
            if (annotation.desc.endsWith(injector.annotationDesc)) {
                return injector.prefix;
            }
        }
        return "handler";
    }
    
    static String describeInjector(final IMixinContext mixin, final AnnotationNode annotation, final MethodNode method) {
        return String.format("%s->@%s::%s%s", mixin.toString(), Annotations.getSimpleName(annotation), MethodNodeEx.getName(method), method.desc);
    }
    
    private static String namesOf(final Collection<ITargetSelector> selectors) {
        int index = 0;
        final int count = selectors.size();
        final StringBuilder sb = new StringBuilder();
        for (final ITargetSelector selector : selectors) {
            if (index > 0) {
                if (index == count - 1) {
                    sb.append(" or ");
                }
                else {
                    sb.append(", ");
                }
            }
            sb.append('\'').append(selector.toString()).append('\'');
            ++index;
        }
        return sb.toString();
    }
    
    public static void register(final Class<? extends InjectionInfo> type) {
        final AnnotationType annotationType = type.getAnnotation(AnnotationType.class);
        if (annotationType == null) {
            throw new IllegalArgumentException("Injection info class " + type + " is not annotated with @AnnotationType");
        }
        InjectorEntry entry;
        try {
            entry = new InjectorEntry(annotationType.value(), type);
        }
        catch (final NoSuchMethodException ex) {
            throw new MixinError("InjectionInfo class " + type.getName() + " is missing a valid constructor");
        }
        final InjectorEntry existing = InjectionInfo.registry.get(entry.annotationDesc);
        if (existing != null) {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.WARNING, String.format("Overriding InjectionInfo for @%s with %s (previously %s)", annotationType.value().getSimpleName(), type.getName(), existing.injectorType.getName()));
        }
        else {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.OTHER, String.format("Registering new injector for @%s with %s", annotationType.value().getSimpleName(), type.getName()));
        }
        InjectionInfo.registry.put(entry.annotationDesc, entry);
        final ArrayList<Class<? extends Annotation>> annotations = new ArrayList<Class<? extends Annotation>>();
        for (final InjectorEntry injector : InjectionInfo.registry.values()) {
            annotations.add(injector.annotationType);
        }
        InjectionInfo.registeredAnnotations = annotations.toArray(InjectionInfo.registeredAnnotations);
    }
    
    public static Set<Class<? extends Annotation>> getRegisteredAnnotations() {
        return ImmutableSet.copyOf(InjectionInfo.registeredAnnotations);
    }
    
    static {
        InjectionInfo.registry = new LinkedHashMap<String, InjectorEntry>();
        InjectionInfo.registeredAnnotations = new Class[0];
        register(CallbackInjectionInfo.class);
        register(ModifyArgInjectionInfo.class);
        register(ModifyArgsInjectionInfo.class);
        register(RedirectInjectionInfo.class);
        register(ModifyVariableInjectionInfo.class);
        register(ModifyConstantInjectionInfo.class);
    }
    
    static class InjectorEntry
    {
        final Class<? extends Annotation> annotationType;
        final Class<? extends InjectionInfo> injectorType;
        final Constructor<? extends InjectionInfo> ctor;
        final String annotationDesc;
        final String prefix;
        
        InjectorEntry(final Class<? extends Annotation> annotationType, final Class<? extends InjectionInfo> type) throws NoSuchMethodException {
            this.annotationType = annotationType;
            this.injectorType = type;
            this.ctor = type.getDeclaredConstructor(MixinTargetContext.class, MethodNode.class, AnnotationNode.class);
            this.annotationDesc = Type.getDescriptor(annotationType);
            final HandlerPrefix handlerPrefix = type.getAnnotation(HandlerPrefix.class);
            this.prefix = ((handlerPrefix != null) ? handlerPrefix.value() : "handler");
        }
        
        InjectionInfo create(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation) {
            try {
                return (InjectionInfo)this.ctor.newInstance(mixin, method, annotation);
            }
            catch (final InvocationTargetException itex) {
                final Throwable cause = itex.getCause();
                if (cause instanceof MixinException) {
                    throw (MixinException)cause;
                }
                final Throwable ex = (cause != null) ? cause : itex;
                throw new MixinError("Error initialising injector metaclass [" + this.injectorType + "] for annotation " + annotation.desc, ex);
            }
            catch (final ReflectiveOperationException ex2) {
                throw new MixinError("Failed to instantiate injector metaclass [" + this.injectorType + "] for annotation " + annotation.desc, ex2);
            }
        }
    }
    
    static class SelectedTarget
    {
        private final ITargetSelector root;
        final ITargetSelector selector;
        final MethodNode method;
        
        SelectedTarget(final ITargetSelector root, final ITargetSelector selector, final MethodNode method) {
            this.root = root;
            this.selector = selector;
            this.method = method;
        }
        
        SelectedTarget(final ITargetSelector selector, final MethodNode method) {
            this(null, selector, method);
        }
        
        ITargetSelector getRoot() {
            return (this.root != null) ? this.root : this.selector;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target({ ElementType.TYPE })
    public @interface HandlerPrefix {
        String value();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target({ ElementType.TYPE })
    public @interface AnnotationType {
        Class<? extends Annotation> value();
    }
}
