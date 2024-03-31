// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.struct.SelectorAnnotationContext;
import com.google.common.base.Strings;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.asm.mixin.Mixin;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Pseudo;
import java.util.Collection;
import java.util.ArrayList;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import java.util.List;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerSuppressible;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.util.asm.IAnnotatedElement;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

class AnnotatedMixin implements IMixinContext, IAnnotatedElement
{
    private final IAnnotationHandle annotation;
    private final IMessagerSuppressible messager;
    private final ITypeHandleProvider typeProvider;
    private final IObfuscationManager obf;
    private final IMappingConsumer mappings;
    private final TypeElement mixin;
    private final List<MethodHandle> methods;
    private final TypeHandle handle;
    private final List<TypeHandle> targets;
    private final TypeHandle primaryTarget;
    private final String classRef;
    private final boolean remap;
    private final boolean virtual;
    private final AnnotatedMixinElementHandlerOverwrite overwrites;
    private final AnnotatedMixinElementHandlerShadow shadows;
    private final AnnotatedMixinElementHandlerInjector injectors;
    private final AnnotatedMixinElementHandlerAccessor accessors;
    private final AnnotatedMixinElementHandlerSoftImplements softImplements;
    private boolean validated;
    
    public AnnotatedMixin(final IMixinAnnotationProcessor ap, final TypeElement type) {
        this.targets = new ArrayList<TypeHandle>();
        this.validated = false;
        this.typeProvider = ap.getTypeProvider();
        this.obf = ap.getObfuscationManager();
        this.mappings = this.obf.createMappingConsumer();
        this.messager = ap;
        this.mixin = type;
        this.handle = new TypeHandle(type);
        this.methods = new ArrayList<MethodHandle>(this.handle.getMethods());
        this.virtual = this.handle.getAnnotation(Pseudo.class).exists();
        this.annotation = this.handle.getAnnotation(Mixin.class);
        this.classRef = TypeUtils.getInternalName(type);
        this.primaryTarget = this.initTargets(ap);
        this.remap = (this.annotation.getBoolean("remap", true) && this.targets.size() > 0);
        this.overwrites = new AnnotatedMixinElementHandlerOverwrite(ap, this);
        this.shadows = new AnnotatedMixinElementHandlerShadow(ap, this);
        this.injectors = new AnnotatedMixinElementHandlerInjector(ap, this);
        this.accessors = new AnnotatedMixinElementHandlerAccessor(ap, this);
        this.softImplements = new AnnotatedMixinElementHandlerSoftImplements(ap, this);
    }
    
    AnnotatedMixin runValidators(final IMixinValidator.ValidationPass pass, final Collection<IMixinValidator> validators) {
        for (final IMixinValidator validator : validators) {
            if (!validator.validate(pass, this.mixin, this.annotation, this.targets)) {
                break;
            }
        }
        if (pass == IMixinValidator.ValidationPass.FINAL && !this.validated) {
            this.validated = true;
            this.runFinalValidation();
        }
        return this;
    }
    
    private TypeHandle initTargets(final IMixinAnnotationProcessor ap) {
        TypeHandle primaryTarget = null;
        try {
            for (final Object target : this.annotation.getList()) {
                final TypeHandle type = this.typeProvider.getTypeHandle(target);
                if (type != null) {
                    if (this.targets.contains(type)) {
                        continue;
                    }
                    this.addTarget(type);
                    if (primaryTarget != null) {
                        continue;
                    }
                    primaryTarget = type;
                }
            }
        }
        catch (final Exception ex) {
            this.printMessage(IMessagerEx.MessageType.WARNING, "Error processing public targets: " + ex.getClass().getName() + ": " + ex.getMessage());
        }
        try {
            for (final String softTarget : this.annotation.getList("targets")) {
                TypeHandle type = this.typeProvider.getTypeHandle(softTarget);
                if (this.targets.contains(type)) {
                    continue;
                }
                if (this.virtual) {
                    type = this.typeProvider.getSimulatedHandle(softTarget, this.mixin.asType());
                }
                else if (type == null) {
                    this.printMessage(IMessagerEx.MessageType.MIXIN_SOFT_TARGET_NOT_FOUND, "Mixin target " + softTarget + " could not be found");
                    if (IMessagerEx.MessageType.MIXIN_SOFT_TARGET_NOT_FOUND.isError()) {
                        return null;
                    }
                    type = this.typeProvider.getSimulatedHandle(softTarget, this.mixin.asType());
                }
                else if (type.isImaginary()) {
                    this.printMessage(IMessagerEx.MessageType.MIXIN_SOFT_TARGET_NOT_RESOLVED, "Mixin target " + softTarget + " could not be fully resolved.", SuppressedBy.UNRESOLVABLE_TARGET);
                    if (IMessagerEx.MessageType.MIXIN_SOFT_TARGET_NOT_RESOLVED.isError()) {
                        return null;
                    }
                }
                else if (type.isPublic()) {
                    final SuppressedBy suppressedBy = type.getPackage().isUnnamed() ? SuppressedBy.DEFAULT_PACKAGE : SuppressedBy.PUBLIC_TARGET;
                    final String must = IMessagerEx.MessageType.MIXIN_SOFT_TARGET_IS_PUBLIC.isError() ? "must" : "should";
                    this.printMessage(IMessagerEx.MessageType.MIXIN_SOFT_TARGET_IS_PUBLIC, "Mixin target " + softTarget + " is public and " + must + " be specified in value", suppressedBy);
                    if (IMessagerEx.MessageType.MIXIN_SOFT_TARGET_IS_PUBLIC.isError()) {
                        return null;
                    }
                }
                this.addSoftTarget(type, softTarget);
                if (primaryTarget != null) {
                    continue;
                }
                primaryTarget = type;
            }
        }
        catch (final Exception ex) {
            this.printMessage(IMessagerEx.MessageType.WARNING, "Error processing private targets: " + ex.getClass().getName() + ": " + ex.getMessage());
        }
        if (primaryTarget == null) {
            this.printMessage(IMessagerEx.MessageType.MIXIN_NO_TARGETS, "Mixin has no targets");
        }
        return primaryTarget;
    }
    
    private void printMessage(final IMessagerEx.MessageType type, final CharSequence msg) {
        this.messager.printMessage(type, msg, this.mixin, AnnotationHandle.asMirror(this.annotation));
    }
    
    private void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final SuppressedBy suppressedBy) {
        this.messager.printMessage(type, msg, this.mixin, AnnotationHandle.asMirror(this.annotation), suppressedBy);
    }
    
    private void addSoftTarget(final TypeHandle type, final String reference) {
        final ObfuscationData<String> obfClassData = this.obf.getDataProvider().getObfClass(type);
        if (!obfClassData.isEmpty()) {
            this.obf.getReferenceManager().addClassMapping(this.classRef, reference, obfClassData);
        }
        this.addTarget(type);
    }
    
    private void addTarget(final TypeHandle type) {
        this.targets.add(type);
    }
    
    @Override
    public String toString() {
        return this.mixin.getSimpleName().toString();
    }
    
    public IAnnotationHandle getAnnotation() {
        return this.annotation;
    }
    
    public TypeElement getMixinElement() {
        return this.mixin;
    }
    
    public TypeHandle getHandle() {
        return this.handle;
    }
    
    @Override
    public String getClassRef() {
        return this.classRef;
    }
    
    public boolean isInterface() {
        return this.mixin.getKind() == ElementKind.INTERFACE;
    }
    
    @Deprecated
    public TypeHandle getPrimaryTarget() {
        return this.primaryTarget;
    }
    
    public List<TypeHandle> getTargets() {
        return this.targets;
    }
    
    public boolean isMultiTarget() {
        return this.targets.size() > 1;
    }
    
    public boolean remap() {
        return this.remap;
    }
    
    public IMappingConsumer getMappings() {
        return this.mappings;
    }
    
    private void runFinalValidation() {
        for (final MethodHandle method : this.methods) {
            this.overwrites.registerMerge(method);
        }
    }
    
    private void removeMethod(final ExecutableElement method) {
        MethodHandle handle = null;
        for (final MethodHandle methodHandle : this.methods) {
            if (methodHandle.getElement() == method) {
                handle = methodHandle;
            }
        }
        if (handle != null) {
            this.methods.remove(handle);
        }
    }
    
    public void registerOverwrite(final ExecutableElement method, final AnnotationHandle overwrite, final boolean shouldRemap) {
        this.removeMethod(method);
        this.overwrites.registerOverwrite(new AnnotatedMixinElementHandlerOverwrite.AnnotatedElementOverwrite(method, overwrite, shouldRemap));
    }
    
    public void registerShadow(final VariableElement field, final AnnotationHandle shadow, final boolean shouldRemap) {
        this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowField(field, shadow, shouldRemap));
    }
    
    public void registerShadow(final ExecutableElement method, final AnnotationHandle shadow, final boolean shouldRemap) {
        this.removeMethod(method);
        this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowMethod(method, shadow, shouldRemap));
    }
    
    public void registerInjector(final ExecutableElement method, final AnnotationHandle inject, final InjectorRemap remap) {
        this.removeMethod(method);
        final AnnotatedMixinElementHandlerInjector.AnnotatedElementInjector injectorElement = new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjector(method, inject, this, remap);
        this.injectors.registerInjector(injectorElement);
        final List<IAnnotationHandle> ats = inject.getAnnotationList("at");
        for (final IAnnotationHandle at : ats) {
            this.registerInjectionPoint(method, inject, "at", (AnnotationHandle)at, remap, "@At(%s)");
        }
        final List<IAnnotationHandle> slices = inject.getAnnotationList("slice");
        for (final IAnnotationHandle slice : slices) {
            final String id = slice.getValue("id", "");
            String coord = "slice";
            if (!Strings.isNullOrEmpty(id)) {
                coord = coord + "." + id;
            }
            final SelectorAnnotationContext sliceContext = new SelectorAnnotationContext(injectorElement, slice, coord);
            final IAnnotationHandle from = slice.getAnnotation("from");
            if (from != null) {
                this.registerSliceInjectionPoint(method, inject, "from", (AnnotationHandle)from, remap, "@Slice[" + id + "](from=@At(%s))", sliceContext);
            }
            final IAnnotationHandle to = slice.getAnnotation("to");
            if (to != null) {
                this.registerSliceInjectionPoint(method, inject, "to", (AnnotationHandle)to, remap, "@Slice[" + id + "](to=@At(%s))", sliceContext);
            }
        }
    }
    
    public void registerInjectionPoint(final ExecutableElement element, final AnnotationHandle inject, final String selectorCoordinate, final AnnotationHandle at, final InjectorRemap remap, final String format) {
        this.injectors.registerInjectionPoint(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjectionPoint(element, inject, this, selectorCoordinate, at, remap), format);
    }
    
    public void registerSliceInjectionPoint(final ExecutableElement element, final AnnotationHandle inject, final String selectorCoordinate, final AnnotationHandle at, final InjectorRemap remap, final String format, final ISelectorContext parentContext) {
        this.injectors.registerInjectionPoint(new AnnotatedMixinElementHandlerInjector.AnnotatedElementSliceInjectionPoint(element, inject, this, selectorCoordinate, at, remap, parentContext), format);
    }
    
    public void registerAccessor(final ExecutableElement element, final AnnotationHandle accessor, final boolean shouldRemap) {
        this.removeMethod(element);
        this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementAccessor(element, accessor, this, shouldRemap));
    }
    
    public void registerInvoker(final ExecutableElement element, final AnnotationHandle invoker, final boolean shouldRemap) {
        this.removeMethod(element);
        this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementInvoker(element, invoker, this, shouldRemap));
    }
    
    public void registerSoftImplements(final AnnotationHandle implementsAnnotation) {
        this.softImplements.process(implementsAnnotation);
    }
    
    @Override
    public ReferenceMapper getReferenceMapper() {
        return null;
    }
    
    @Override
    public String getClassName() {
        return this.getClassRef().replace('/', '.');
    }
    
    @Override
    public String getTargetClassRef() {
        return this.primaryTarget.getName();
    }
    
    @Override
    public IMixinInfo getMixin() {
        throw new UnsupportedOperationException("MixinInfo not available at compile time");
    }
    
    @Override
    public Extensions getExtensions() {
        throw new UnsupportedOperationException("Mixin Extensions not available at compile time");
    }
    
    @Override
    public boolean getOption(final MixinEnvironment.Option option) {
        throw new UnsupportedOperationException("Options not available at compile time");
    }
    
    @Override
    public int getPriority() {
        throw new UnsupportedOperationException("Priority not available at compile time");
    }
    
    @Override
    public IAnnotationHandle getAnnotation(final Class<? extends Annotation> annotationClass) {
        return AnnotationHandle.of(this.mixin, annotationClass);
    }
}
