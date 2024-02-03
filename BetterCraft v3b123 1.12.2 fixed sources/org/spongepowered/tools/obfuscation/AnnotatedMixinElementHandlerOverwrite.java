// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import javax.lang.model.element.Element;
import java.lang.reflect.Method;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import java.util.Iterator;
import java.util.Locale;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerOverwrite extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerOverwrite(final IMixinAnnotationProcessor ap, final AnnotatedMixin mixin) {
        super(ap, mixin);
    }
    
    public void registerMerge(final MethodHandle method) {
        if (!method.isImaginary()) {
            this.validateTargetMethod(method.getElement(), null, new AliasedElementName(method, AnnotationHandle.MISSING), "overwrite", true, true);
        }
    }
    
    public void registerOverwrite(final AnnotatedElementOverwrite elem) {
        final AliasedElementName name = new AliasedElementName(((AnnotatedElement<Element>)elem).getElement(), elem.getAnnotation());
        this.validateTargetMethod(elem.getElement(), elem.getAnnotation(), name, "@Overwrite", true, false);
        this.checkConstraints(elem.getElement(), elem.getAnnotation());
        if (elem.shouldRemap()) {
            for (final TypeHandle target : this.mixin.getTargets()) {
                if (!this.registerOverwriteForTarget(elem, target)) {
                    return;
                }
            }
        }
        if (!"true".equalsIgnoreCase(this.ap.getOption("disableOverwriteChecker"))) {
            final String javadoc = this.ap.getJavadocProvider().getJavadoc(((AnnotatedElement<Element>)elem).getElement());
            if (javadoc == null) {
                this.ap.printMessage(IMessagerEx.MessageType.OVERWRITE_DOCS, "@Overwrite is missing javadoc comment", ((AnnotatedElement<Element>)elem).getElement(), SuppressedBy.OVERWRITE);
                return;
            }
            if (!javadoc.toLowerCase(Locale.ROOT).contains("@author")) {
                this.ap.printMessage(IMessagerEx.MessageType.OVERWRITE_DOCS, "@Overwrite is missing an @author tag", ((AnnotatedElement<Element>)elem).getElement(), SuppressedBy.OVERWRITE);
            }
            if (!javadoc.toLowerCase(Locale.ROOT).contains("@reason")) {
                this.ap.printMessage(IMessagerEx.MessageType.OVERWRITE_DOCS, "@Overwrite is missing an @reason tag", ((AnnotatedElement<Element>)elem).getElement(), SuppressedBy.OVERWRITE);
            }
        }
    }
    
    private boolean registerOverwriteForTarget(final AnnotatedElementOverwrite elem, final TypeHandle target) {
        final MappingMethod targetMethod = target.getMappingMethod(elem.getSimpleName(), elem.getDesc());
        final ObfuscationData<MappingMethod> obfData = this.obf.getDataProvider().getObfMethod(targetMethod);
        if (obfData.isEmpty()) {
            IMessagerEx.MessageType messageType = IMessagerEx.MessageType.NO_OBFDATA_FOR_OVERWRITE;
            try {
                final Method md = elem.getElement().getClass().getMethod("isStatic", (Class<?>[])new Class[0]);
                if (md.invoke(((AnnotatedElement<Object>)elem).getElement(), new Object[0])) {
                    messageType = IMessagerEx.MessageType.NO_OBFDATA_FOR_STATIC_OVERWRITE;
                }
            }
            catch (final Exception ex2) {}
            this.ap.printMessage(messageType, "Unable to locate obfuscation mapping for @Overwrite method", ((AnnotatedElement<Element>)elem).getElement());
            return false;
        }
        try {
            this.addMethodMappings(elem.getSimpleName(), elem.getDesc(), obfData);
        }
        catch (final Mappings.MappingConflictException ex) {
            elem.printMessage(this.ap, IMessagerEx.MessageType.OVERWRITE_MAPPING_CONFLICT, "Mapping conflict for @Overwrite method: " + ex.getNew().getSimpleName() + " for target " + target + " conflicts with existing mapping " + ex.getOld().getSimpleName());
            return false;
        }
        return true;
    }
    
    static class AnnotatedElementOverwrite extends AnnotatedElement<ExecutableElement>
    {
        private final boolean shouldRemap;
        
        public AnnotatedElementOverwrite(final ExecutableElement element, final AnnotationHandle annotation, final boolean shouldRemap) {
            super(element, annotation);
            this.shouldRemap = shouldRemap;
        }
        
        public boolean shouldRemap() {
            return this.shouldRemap;
        }
    }
}
