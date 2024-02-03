// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.lang.model.element.Element;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Iterator;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.At;
import java.util.HashSet;
import java.util.Set;

public class MixinObfuscationProcessorInjection extends MixinObfuscationProcessor
{
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTypes = new HashSet<String>();
        supportedAnnotationTypes.add(At.class.getName());
        for (final Class<? extends Annotation> annotationType : InjectionInfo.getRegisteredAnnotations()) {
            supportedAnnotationTypes.add(annotationType.getName());
        }
        return supportedAnnotationTypes;
    }
    
    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            this.postProcess(roundEnv);
            return true;
        }
        this.processMixins(roundEnv);
        for (final Class<? extends Annotation> annotationType : InjectionInfo.getRegisteredAnnotations()) {
            this.processInjectors(roundEnv, annotationType);
        }
        this.postProcess(roundEnv);
        return true;
    }
    
    @Override
    protected void postProcess(final RoundEnvironment roundEnv) {
        super.postProcess(roundEnv);
        try {
            this.mixins.writeReferences();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void processInjectors(final RoundEnvironment roundEnv, final Class<? extends Annotation> injectorClass) {
        for (final Element elem : roundEnv.getElementsAnnotatedWith(injectorClass)) {
            final Element parent = elem.getEnclosingElement();
            if (!(parent instanceof TypeElement)) {
                throw new IllegalStateException("@" + injectorClass.getSimpleName() + " element has unexpected parent with type " + TypeUtils.getElementType(parent));
            }
            final AnnotationHandle inject = AnnotationHandle.of(elem, injectorClass);
            if (elem.getKind() == ElementKind.METHOD) {
                this.mixins.registerInjector((TypeElement)parent, (ExecutableElement)elem, inject);
            }
            else {
                this.mixins.printMessage(IMessagerEx.MessageType.INJECTOR_ON_NON_METHOD_ELEMENT, "Found an @" + injectorClass.getSimpleName() + " annotation on an element which is not a method: " + elem.toString());
            }
        }
    }
}
