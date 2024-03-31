// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import java.util.Set;
import javax.lang.model.SourceVersion;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Mixin;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.AbstractProcessor;

abstract class MixinObfuscationProcessor extends AbstractProcessor
{
    protected AnnotatedMixins mixins;
    
    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mixins = AnnotatedMixins.getMixinsForEnvironment(processingEnv);
    }
    
    protected void processMixins(final RoundEnvironment roundEnv) {
        this.mixins.onPassStarted();
        for (final Element elem : roundEnv.getElementsAnnotatedWith(Mixin.class)) {
            if (elem.getKind() == ElementKind.CLASS || elem.getKind() == ElementKind.INTERFACE) {
                this.mixins.registerMixin((TypeElement)elem);
            }
            else {
                this.mixins.printMessage(IMessagerEx.MessageType.MIXIN_ON_INVALID_TYPE, "Found an @Mixin annotation on an element which is not a class or interface", elem);
            }
        }
    }
    
    protected void postProcess(final RoundEnvironment roundEnv) {
        this.mixins.onPassCompleted(roundEnv);
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    
    @Override
    public Set<String> getSupportedOptions() {
        return SupportedOptions.getAllOptions();
    }
}
