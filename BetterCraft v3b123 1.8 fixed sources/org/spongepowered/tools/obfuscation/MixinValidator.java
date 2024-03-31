// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import javax.lang.model.type.TypeMirror;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerSuppressible;
import javax.annotation.processing.ProcessingEnvironment;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;

public abstract class MixinValidator implements IMixinValidator
{
    protected final ProcessingEnvironment processingEnv;
    protected final IMessagerSuppressible messager;
    protected final IOptionProvider options;
    protected final ValidationPass pass;
    
    public MixinValidator(final IMixinAnnotationProcessor ap, final ValidationPass pass) {
        this.processingEnv = ap.getProcessingEnvironment();
        this.messager = ap;
        this.options = ap;
        this.pass = pass;
    }
    
    @Override
    public final boolean validate(final ValidationPass pass, final TypeElement mixin, final IAnnotationHandle annotation, final Collection<TypeHandle> targets) {
        return pass != this.pass || this.validate(mixin, annotation, targets);
    }
    
    protected abstract boolean validate(final TypeElement p0, final IAnnotationHandle p1, final Collection<TypeHandle> p2);
    
    protected final Collection<TypeMirror> getMixinsTargeting(final TypeMirror targetType) {
        return AnnotatedMixins.getMixinsForEnvironment(this.processingEnv).getMixinsTargeting(targetType);
    }
}
