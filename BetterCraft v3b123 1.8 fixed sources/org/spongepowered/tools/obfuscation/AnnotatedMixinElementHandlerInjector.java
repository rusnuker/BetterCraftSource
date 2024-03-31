// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import java.util.HashMap;
import java.util.Map;
import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;
import org.spongepowered.asm.mixin.injection.Coerce;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import javax.lang.model.element.AnnotationMirror;
import org.spongepowered.tools.obfuscation.ext.SpecialPackages;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorConstructor;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorRemappable;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorByName;
import org.spongepowered.asm.mixin.injection.selectors.InvalidSelectorException;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import java.util.Iterator;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerInjector extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerInjector(final IMixinAnnotationProcessor ap, final AnnotatedMixin mixin) {
        super(ap, mixin);
    }
    
    public void registerInjector(final AnnotatedElementInjector elem) {
        if (this.mixin.isInterface()) {
            this.ap.printMessage(IMessagerEx.MessageType.INJECTOR_IN_INTERFACE, "Injector in interface is unsupported", ((AnnotatedElement<Element>)elem).getElement());
        }
        for (final String reference : elem.getAnnotation().getList("method")) {
            this.registerInjectorTarget(elem, reference, TargetSelector.parse(reference, elem), elem + ".method=\"" + reference + "\"");
        }
        for (final IAnnotationHandle desc : elem.getAnnotation().getAnnotationList("target")) {
            final String subject = String.format("%s.target=@Desc(id = \"%s\")", elem, desc.getValue("id", ""));
            this.registerInjectorTarget(elem, null, TargetSelector.parse(desc, elem), subject);
        }
    }
    
    private void registerInjectorTarget(final AnnotatedElementInjector elem, final String reference, final ITargetSelector targetSelector, final String subject) {
        try {
            targetSelector.validate();
        }
        catch (final InvalidSelectorException ex) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.TARGET_SELECTOR_VALIDATION, ex.getMessage());
        }
        if (!(targetSelector instanceof ITargetSelectorByName)) {
            return;
        }
        final ITargetSelectorByName targetMember = (ITargetSelectorByName)targetSelector;
        if (targetMember.getName() == null) {
            return;
        }
        if (targetMember.getDesc() != null) {
            this.validateReferencedTarget(elem, reference, targetMember, subject);
        }
        if (targetSelector instanceof ITargetSelectorRemappable && elem.shouldRemap()) {
            for (final TypeHandle target : this.mixin.getTargets()) {
                if (!this.registerInjector(elem, reference, (ITargetSelectorRemappable)targetMember, target)) {
                    break;
                }
            }
        }
    }
    
    private boolean registerInjector(final AnnotatedElementInjector elem, final String reference, final ITargetSelectorRemappable targetMember, final TypeHandle target) {
        final String desc = target.findDescriptor(targetMember);
        if (desc == null) {
            final IMessagerEx.MessageType messageType = this.mixin.isMultiTarget() ? IMessagerEx.MessageType.MISSING_INJECTOR_DESC_MULTITARGET : IMessagerEx.MessageType.MISSING_INJECTOR_DESC_SINGLETARGET;
            if (target.isSimulated()) {
                elem.printMessage(this.ap, IMessagerEx.MessageType.MISSING_INJECTOR_DESC_SIMULATED, elem + " target '" + reference + "' in @Pseudo mixin will not be obfuscated");
            }
            else if (target.isImaginary()) {
                elem.printMessage(this.ap, messageType, elem + " target requires method signature because enclosing type information for " + target + " is unavailable");
            }
            else if (!targetMember.isInitialiser()) {
                elem.printMessage(this.ap, messageType, "Unable to determine descriptor for " + elem + " target method");
            }
            return true;
        }
        final String targetName = elem + " target " + targetMember.getName();
        final MappingMethod targetMethod = target.getMappingMethod(targetMember.getName(), desc);
        ObfuscationData<MappingMethod> obfData = this.obf.getDataProvider().getObfMethod(targetMethod);
        if (obfData.isEmpty()) {
            if (target.isSimulated()) {
                obfData = this.obf.getDataProvider().getRemappedMethod(targetMethod);
            }
            else {
                if (targetMember.isClassInitialiser()) {
                    return true;
                }
                elem.addMessage(targetMember.isConstructor() ? IMessagerEx.MessageType.NO_OBFDATA_FOR_CTOR : IMessagerEx.MessageType.NO_OBFDATA_FOR_TARGET, "Unable to locate obfuscation mapping for " + targetName, ((AnnotatedElement<Element>)elem).getElement(), elem.getAnnotation());
                return false;
            }
        }
        final IReferenceManager refMap = this.obf.getReferenceManager();
        try {
            if ((targetMember.getOwner() == null && this.mixin.isMultiTarget()) || target.isSimulated()) {
                obfData = AnnotatedMixinElementHandler.stripOwnerData(obfData);
            }
            refMap.addMethodMapping(this.classRef, reference, obfData);
        }
        catch (final ReferenceManager.ReferenceConflictException ex) {
            final String conflictType = this.mixin.isMultiTarget() ? "Multi-target" : "Target";
            if (elem.hasCoerceArgument() && targetMember.getOwner() == null && targetMember.getDesc() == null) {
                final ITargetSelector oldMember = TargetSelector.parse(ex.getOld(), elem);
                final ITargetSelector newMember = TargetSelector.parse(ex.getNew(), elem);
                final String oldName = (oldMember instanceof ITargetSelectorByName) ? ((ITargetSelectorByName)oldMember).getName() : oldMember.toString();
                final String newName = (newMember instanceof ITargetSelectorByName) ? ((ITargetSelectorByName)newMember).getName() : newMember.toString();
                if (oldName != null && oldName.equals(newName)) {
                    obfData = AnnotatedMixinElementHandler.stripDescriptors(obfData);
                    refMap.setAllowConflicts(true);
                    refMap.addMethodMapping(this.classRef, reference, obfData);
                    refMap.setAllowConflicts(false);
                    elem.printMessage(this.ap, IMessagerEx.MessageType.BARE_REFERENCE, "Coerced " + conflictType + " reference has conflicting descriptors for " + targetName + ": Storing bare references " + obfData.values() + " in refMap");
                    return true;
                }
            }
            elem.printMessage(this.ap, IMessagerEx.MessageType.INJECTOR_MAPPING_CONFLICT, conflictType + " reference conflict for " + targetName + ": " + reference + " -> " + ex.getNew() + " previously defined as " + ex.getOld());
        }
        return true;
    }
    
    public void registerInjectionPoint(final AnnotatedElementInjectionPoint elem, final String format) {
        if (this.mixin.isInterface()) {
            this.ap.printMessage(IMessagerEx.MessageType.INJECTOR_IN_INTERFACE, "Injector in interface is unsupported", ((AnnotatedElement<Element>)elem).getElement());
        }
        ITargetSelector targetSelector = null;
        final String targetReference = elem.getAt().getValue("target");
        if (targetReference != null) {
            targetSelector = TargetSelector.parse(targetReference, elem);
            try {
                targetSelector.validate();
            }
            catch (final InvalidSelectorException ex) {
                this.ap.printMessage(IMessagerEx.MessageType.TARGET_SELECTOR_VALIDATION, ex.getMessage(), ((AnnotatedElement<Element>)elem).getElement(), elem.getAtErrorElement(this.ap.getCompilerEnvironment()));
            }
        }
        final String type = InjectionPointData.parseType(elem.getAt().getValue("value", ""));
        ITargetSelector classSelector = null;
        final String classReference = elem.getAtArg("class");
        if ("NEW".equals(type) && classReference != null) {
            classSelector = TargetSelector.parse(classReference, elem);
            try {
                classSelector.validate();
            }
            catch (final InvalidSelectorException ex2) {
                this.ap.printMessage(IMessagerEx.MessageType.TARGET_SELECTOR_VALIDATION, ex2.getMessage(), ((AnnotatedElement<Element>)elem).getElement(), elem.getAtErrorElement(this.ap.getCompilerEnvironment()));
            }
        }
        if (elem.shouldRemap()) {
            if ("NEW".equals(type)) {
                this.remapNewTarget(String.format(format, type + ".<target>"), targetReference, targetSelector, elem);
                this.remapNewTarget(String.format(format, type + ".args[class]"), classReference, classSelector, elem);
            }
            else {
                this.remapReference(String.format(format, type + ".<target>"), targetReference, targetSelector, elem);
            }
        }
    }
    
    protected final void remapNewTarget(final String subject, final String reference, final ITargetSelector selector, final AnnotatedElementInjectionPoint elem) {
        if (!(selector instanceof ITargetSelectorConstructor)) {
            return;
        }
        final ITargetSelectorConstructor member = (ITargetSelectorConstructor)selector;
        final String target = member.toCtorType();
        if (target != null) {
            final String desc = member.toCtorDesc();
            final MappingMethod m = new MappingMethod(target, ".", (desc != null) ? desc : "()V");
            final ObfuscationData<MappingMethod> remapped = this.obf.getDataProvider().getRemappedMethod(m);
            if (remapped.isEmpty() && !SpecialPackages.isExcludedPackage(member.toCtorType())) {
                this.ap.printMessage(IMessagerEx.MessageType.NO_OBFDATA_FOR_CLASS, "Unable to locate class mapping for " + subject + " '" + target + "'", ((AnnotatedElement<Element>)elem).getElement(), elem.getAnnotation().asMirror(), SuppressedBy.MAPPING);
                return;
            }
            final ObfuscationData<String> mappings = new ObfuscationData<String>();
            for (final ObfuscationType type : remapped) {
                final MappingMethod mapping = remapped.get(type);
                if (desc == null) {
                    mappings.put(type, mapping.getOwner());
                }
                else {
                    mappings.put(type, mapping.getDesc().replace(")V", ")L" + mapping.getOwner() + ";"));
                }
            }
            this.obf.getReferenceManager().addClassMapping(this.classRef, reference, mappings);
        }
        elem.notifyRemapped();
    }
    
    protected final void remapReference(final String subject, final String reference, final ITargetSelector selector, final AnnotatedElementInjectionPoint elem) {
        if (!(selector instanceof ITargetSelectorRemappable)) {
            return;
        }
        final ITargetSelectorRemappable targetMember = (ITargetSelectorRemappable)selector;
        final AnnotationMirror errorElement = elem.getAtErrorElement(this.ap.getCompilerEnvironment());
        if (!targetMember.isFullyQualified()) {
            final String missing = (targetMember.getOwner() == null) ? ((targetMember.getDesc() == null) ? "owner and descriptor" : "owner") : "descriptor";
            this.ap.printMessage(IMessagerEx.MessageType.INJECTOR_TARGET_NOT_FULLY_QUALIFIED, subject + " is not fully qualified, missing " + missing, ((AnnotatedElement<Element>)elem).getElement(), errorElement);
            return;
        }
        try {
            if (targetMember.isField()) {
                final ObfuscationData<MappingField> obfFieldData = this.obf.getDataProvider().getObfFieldRecursive(targetMember);
                if (obfFieldData.isEmpty()) {
                    if (targetMember.getOwner() == null || !SpecialPackages.isExcludedPackage(targetMember.getOwner())) {
                        this.ap.printMessage(IMessagerEx.MessageType.NO_OBFDATA_FOR_FIELD, "Unable to locate field mapping for " + subject + " '" + reference + "'", ((AnnotatedElement<Element>)elem).getElement(), errorElement, SuppressedBy.MAPPING);
                    }
                    return;
                }
                this.obf.getReferenceManager().addFieldMapping(this.classRef, reference, targetMember, obfFieldData);
            }
            else {
                final ObfuscationData<MappingMethod> obfMethodData = this.obf.getDataProvider().getObfMethodRecursive(targetMember);
                if (obfMethodData.isEmpty()) {
                    if (targetMember.getOwner() == null || !SpecialPackages.isExcludedPackage(targetMember.getOwner())) {
                        this.ap.printMessage(IMessagerEx.MessageType.NO_OBFDATA_FOR_METHOD, "Unable to locate method mapping for " + subject + " '" + reference + "'", ((AnnotatedElement<Element>)elem).getElement(), errorElement, SuppressedBy.MAPPING);
                    }
                    return;
                }
                this.obf.getReferenceManager().addMethodMapping(this.classRef, reference, targetMember, obfMethodData);
            }
        }
        catch (final ReferenceManager.ReferenceConflictException ex) {
            this.ap.printMessage(IMessagerEx.MessageType.INJECTOR_MAPPING_CONFLICT, "Unexpected reference conflict for " + subject + ": " + reference + " -> " + ex.getNew() + " previously defined as " + ex.getOld(), ((AnnotatedElement<Element>)elem).getElement(), errorElement);
            return;
        }
        elem.notifyRemapped();
    }
    
    static class AnnotatedElementInjector extends AnnotatedElementExecutable
    {
        private final InjectorRemap state;
        
        public AnnotatedElementInjector(final ExecutableElement element, final AnnotationHandle annotation, final IMixinContext context, final InjectorRemap shouldRemap) {
            super(element, annotation, context, "method");
            this.state = shouldRemap;
        }
        
        public boolean shouldRemap() {
            return this.state.shouldRemap();
        }
        
        public boolean hasCoerceArgument() {
            if (!this.annotation.toString().equals("@Inject")) {
                return false;
            }
            final Iterator<? extends VariableElement> iterator = ((ExecutableElement)this.element).getParameters().iterator();
            if (iterator.hasNext()) {
                final VariableElement param = (VariableElement)iterator.next();
                return AnnotationHandle.of(param, Coerce.class).exists();
            }
            return false;
        }
        
        public void addMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element, final AnnotationHandle annotation) {
            this.state.addMessage(type, msg, element, annotation);
        }
        
        @Override
        public String toString() {
            return this.getAnnotation().toString();
        }
    }
    
    static class AnnotatedElementInjectionPoint extends AnnotatedElementExecutable
    {
        private final AnnotationHandle at;
        private Map<String, String> args;
        private final InjectorRemap state;
        
        public AnnotatedElementInjectionPoint(final ExecutableElement element, final AnnotationHandle inject, final IMixinContext context, final String selectorCoordinate, final AnnotationHandle at, final InjectorRemap state) {
            super(element, inject, context, selectorCoordinate);
            this.at = at;
            this.state = state;
        }
        
        public boolean shouldRemap() {
            return this.at.getBoolean("remap", this.state.shouldRemap());
        }
        
        public AnnotationHandle getAt() {
            return this.at;
        }
        
        public AnnotationMirror getAtErrorElement(final IMixinAnnotationProcessor.CompilerEnvironment compilerEnvironment) {
            return (compilerEnvironment.isDevelopmentEnvironment() ? this.getAt() : this.getAnnotation()).asMirror();
        }
        
        @Override
        public IAnnotationHandle getSelectorAnnotation() {
            return this.getAt();
        }
        
        public String getAtArg(final String key) {
            if (this.args == null) {
                this.args = new HashMap<String, String>();
                for (final String arg : this.at.getList("args")) {
                    if (arg == null) {
                        continue;
                    }
                    final int eqPos = arg.indexOf(61);
                    if (eqPos > -1) {
                        this.args.put(arg.substring(0, eqPos), arg.substring(eqPos + 1));
                    }
                    else {
                        this.args.put(arg, "");
                    }
                }
            }
            return this.args.get(key);
        }
        
        public void notifyRemapped() {
            this.state.notifyRemapped();
        }
    }
    
    static class AnnotatedElementSliceInjectionPoint extends AnnotatedElementInjectionPoint
    {
        private final ISelectorContext parentContext;
        
        public AnnotatedElementSliceInjectionPoint(final ExecutableElement element, final AnnotationHandle inject, final IMixinContext context, final String selectorCoordinate, final AnnotationHandle at, final InjectorRemap state, final ISelectorContext parentContext) {
            super(element, inject, context, selectorCoordinate, at, state);
            this.parentContext = parentContext;
        }
        
        @Override
        public ISelectorContext getParent() {
            return this.parentContext;
        }
    }
}
