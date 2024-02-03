// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorRemappable;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import com.google.common.base.Strings;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerSuppressible;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
import java.util.Iterator;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerAccessor extends AnnotatedMixinElementHandler
{
    public AnnotatedMixinElementHandlerAccessor(final IMixinAnnotationProcessor ap, final AnnotatedMixin mixin) {
        super(ap, mixin);
    }
    
    public void registerAccessor(final AnnotatedElementAccessor elem) {
        if (elem.getAccessorType() == null) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_TYPE_UNSUPPORTED, "Unsupported accessor type");
            return;
        }
        final String targetName = this.getAccessorTargetName(elem);
        if (targetName == null) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_NAME_UNRESOLVED, "Cannot inflect accessor target name");
            return;
        }
        elem.setTargetName(targetName);
        for (final TypeHandle target : this.mixin.getTargets()) {
            try {
                elem.attach(target);
            }
            catch (final Exception ex) {
                elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_ATTACH_ERROR, ex.getMessage());
                continue;
            }
            if (elem.getAccessorType() == AccessorInfo.AccessorType.OBJECT_FACTORY) {
                this.registerFactoryForTarget((AnnotatedElementInvoker)elem, target);
            }
            else if (elem.getAccessorType() == AccessorInfo.AccessorType.METHOD_PROXY) {
                this.registerInvokerForTarget((AnnotatedElementInvoker)elem, target);
            }
            else {
                this.registerAccessorForTarget(elem, target);
            }
        }
    }
    
    private void registerAccessorForTarget(final AnnotatedElementAccessor elem, final TypeHandle target) {
        FieldHandle targetField = target.findField(elem.getTargetName(), elem.getTargetTypeName(), false);
        if (targetField == null) {
            if (!target.isImaginary()) {
                elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_TARGET_NOT_FOUND, "Could not locate @Accessor target " + elem + " in target " + target);
                return;
            }
            targetField = new FieldHandle(target.getName(), elem.getTargetName(), elem.getTargetDesc());
        }
        if (!elem.shouldRemap()) {
            return;
        }
        ObfuscationData<MappingField> obfData = this.obf.getDataProvider().getObfField(targetField.asMapping(false).move(target.getName()));
        if (obfData.isEmpty()) {
            final String info = this.mixin.isMultiTarget() ? (" in target " + target) : "";
            elem.printMessage(this.ap, IMessagerEx.MessageType.NO_OBFDATA_FOR_ACCESSOR, "Unable to locate obfuscation mapping" + info + " for @Accessor target " + elem);
            return;
        }
        obfData = AnnotatedMixinElementHandler.stripOwnerData(obfData);
        try {
            this.obf.getReferenceManager().addFieldMapping(this.mixin.getClassRef(), elem.getTargetName(), elem.getContext(), obfData);
        }
        catch (final ReferenceManager.ReferenceConflictException ex) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_MAPPING_CONFLICT, "Mapping conflict for @Accessor target " + elem + ": " + ex.getNew() + " for target " + target + " conflicts with existing mapping " + ex.getOld());
        }
    }
    
    private void registerInvokerForTarget(final AnnotatedElementInvoker elem, final TypeHandle target) {
        MethodHandle targetMethod = target.findMethod(elem.getTargetName(), elem.getTargetTypeName(), false);
        if (targetMethod == null) {
            if (!target.isImaginary()) {
                elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_TARGET_NOT_FOUND, "Could not locate @Invoker target " + elem + " in target " + target);
                return;
            }
            targetMethod = new MethodHandle(target, elem.getTargetName(), elem.getTargetDesc());
        }
        if (!elem.shouldRemap()) {
            return;
        }
        ObfuscationData<MappingMethod> obfData = this.obf.getDataProvider().getObfMethod(targetMethod.asMapping(false).move(target.getName()));
        if (obfData.isEmpty()) {
            final String info = this.mixin.isMultiTarget() ? (" in target " + target) : "";
            elem.printMessage(this.ap, IMessagerEx.MessageType.NO_OBFDATA_FOR_ACCESSOR, "Unable to locate obfuscation mapping" + info + " for @Accessor target " + elem);
            return;
        }
        obfData = AnnotatedMixinElementHandler.stripOwnerData(obfData);
        try {
            this.obf.getReferenceManager().addMethodMapping(this.mixin.getClassRef(), elem.getTargetName(), elem.getContext(), obfData);
        }
        catch (final ReferenceManager.ReferenceConflictException ex) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.ACCESSOR_MAPPING_CONFLICT, "Mapping conflict for @Invoker target " + elem + ": " + ex.getNew() + " for target " + target + " conflicts with existing mapping " + ex.getOld());
        }
    }
    
    private void registerFactoryForTarget(final AnnotatedElementInvoker elem, final TypeHandle target) {
        final TypeUtils.EquivalencyResult equivalency = TypeUtils.isEquivalentType(this.ap.getProcessingEnvironment(), elem.getReturnType(), target.getTypeMirror());
        if (equivalency.type != TypeUtils.Equivalency.EQUIVALENT) {
            if (equivalency.type == TypeUtils.Equivalency.EQUIVALENT_BUT_RAW && equivalency.rawType == 1) {
                elem.printMessage(this.ap, IMessagerEx.MessageType.INVOKER_RAW_RETURN_TYPE, "Raw return type for Factory @Invoker", SuppressedBy.RAW_TYPES);
            }
            else {
                if (equivalency.type == TypeUtils.Equivalency.BOUNDS_MISMATCH) {
                    elem.printMessage(this.ap, IMessagerEx.MessageType.FACTORY_INVOKER_GENERIC_ARGS, "Invalid Factory @Invoker return type, generic type args of " + target.getTypeMirror() + " are incompatible with " + elem.getReturnType() + ". " + equivalency);
                    return;
                }
                elem.printMessage(this.ap, IMessagerEx.MessageType.FACTORY_INVOKER_RETURN_TYPE, "Invalid Factory @Invoker return type, expected " + target.getTypeMirror() + " but found " + elem.getReturnType());
                return;
            }
        }
        if (!elem.isStatic()) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.FACTORY_INVOKER_NONSTATIC, "Factory @Invoker must be static");
            return;
        }
        if (!elem.shouldRemap()) {
            return;
        }
        final ObfuscationData<String> obfData = this.obf.getDataProvider().getObfClass(elem.getAnnotationValue().replace('.', '/'));
        this.obf.getReferenceManager().addClassMapping(this.mixin.getClassRef(), elem.getAnnotationValue(), obfData);
    }
    
    private String getAccessorTargetName(final AnnotatedElementAccessor elem) {
        final String value = elem.getAnnotationValue();
        if (Strings.isNullOrEmpty(value)) {
            return this.inflectAccessorTarget(elem);
        }
        return value;
    }
    
    private String inflectAccessorTarget(final AnnotatedElementAccessor elem) {
        return AccessorInfo.inflectTarget(elem.getSimpleName(), elem.getAccessorType(), "", elem, false);
    }
    
    static class AnnotatedElementAccessor extends AnnotatedElementExecutable
    {
        protected final boolean shouldRemap;
        protected final TypeMirror returnType;
        protected String targetName;
        
        public AnnotatedElementAccessor(final ExecutableElement element, final AnnotationHandle annotation, final IMixinContext context, final boolean shouldRemap) {
            super(element, annotation, context, "value");
            this.shouldRemap = shouldRemap;
            this.returnType = this.getElement().getReturnType();
        }
        
        public void attach(final TypeHandle target) {
        }
        
        public boolean shouldRemap() {
            return this.shouldRemap;
        }
        
        public String getAnnotationValue() {
            return this.getAnnotation().getValue();
        }
        
        public TypeMirror getTargetType() {
            switch (this.getAccessorType()) {
                case FIELD_GETTER: {
                    return this.returnType;
                }
                case FIELD_SETTER: {
                    return ((VariableElement)this.getElement().getParameters().get(0)).asType();
                }
                default: {
                    return null;
                }
            }
        }
        
        public String getTargetTypeName() {
            return TypeUtils.getTypeName(this.getTargetType());
        }
        
        public String getTargetDesc() {
            return TypeUtils.getInternalName(this.getTargetType());
        }
        
        public ITargetSelectorRemappable getContext() {
            return new MemberInfo(this.getTargetName(), null, this.getTargetDesc());
        }
        
        public AccessorInfo.AccessorType getAccessorType() {
            return (this.returnType.getKind() == TypeKind.VOID) ? AccessorInfo.AccessorType.FIELD_SETTER : AccessorInfo.AccessorType.FIELD_GETTER;
        }
        
        public void setTargetName(final String targetName) {
            this.targetName = targetName;
        }
        
        public String getTargetName() {
            return this.targetName;
        }
        
        public TypeMirror getReturnType() {
            return this.returnType;
        }
        
        public boolean isStatic() {
            return this.element.getModifiers().contains(Modifier.STATIC);
        }
        
        @Override
        public String toString() {
            return (this.targetName != null) ? this.targetName : "<invalid>";
        }
    }
    
    static class AnnotatedElementInvoker extends AnnotatedElementAccessor
    {
        private AccessorInfo.AccessorType type;
        
        public AnnotatedElementInvoker(final ExecutableElement element, final AnnotationHandle annotation, final IMixinContext context, final boolean shouldRemap) {
            super(element, annotation, context, shouldRemap);
            this.type = AccessorInfo.AccessorType.METHOD_PROXY;
        }
        
        @Override
        public void attach(final TypeHandle target) {
            this.type = AccessorInfo.AccessorType.METHOD_PROXY;
            if (this.returnType.getKind() != TypeKind.DECLARED) {
                return;
            }
            final String specifiedName = this.getAnnotationValue();
            if (specifiedName != null) {
                if ("<init>".equals(specifiedName) || target.getName().equals(specifiedName.replace('.', '/'))) {
                    this.type = AccessorInfo.AccessorType.OBJECT_FACTORY;
                }
                return;
            }
            final AccessorInfo.AccessorName accessorName = AccessorInfo.AccessorName.of(this.getSimpleName(), false);
            if (accessorName == null) {
                return;
            }
            for (final String prefix : AccessorInfo.AccessorType.OBJECT_FACTORY.getExpectedPrefixes()) {
                if (prefix.equals(accessorName.prefix) && ("<init>".equals(accessorName.name) || target.getSimpleName().equals(accessorName.name))) {
                    this.type = AccessorInfo.AccessorType.OBJECT_FACTORY;
                }
            }
        }
        
        @Override
        public boolean shouldRemap() {
            return (this.type == AccessorInfo.AccessorType.METHOD_PROXY || this.getAnnotationValue() != null) && super.shouldRemap();
        }
        
        @Override
        public String getTargetDesc() {
            return this.getDesc();
        }
        
        @Override
        public AccessorInfo.AccessorType getAccessorType() {
            return this.type;
        }
        
        @Override
        public String getTargetTypeName() {
            return TypeUtils.getJavaSignature(((AnnotatedElement<Element>)this).getElement());
        }
    }
}
