// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.validation;

import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.MixinValidator;

public class ParentValidator extends MixinValidator
{
    public ParentValidator(final IMixinAnnotationProcessor ap) {
        super(ap, IMixinValidator.ValidationPass.EARLY);
    }
    
    public boolean validate(final TypeElement mixin, final IAnnotationHandle annotation, final Collection<TypeHandle> targets) {
        if (mixin.getEnclosingElement().getKind() != ElementKind.PACKAGE && !mixin.getModifiers().contains(Modifier.STATIC)) {
            this.messager.printMessage(IMessagerEx.MessageType.PARENT_VALIDATOR, "Inner class mixin must be declared static", mixin);
        }
        return true;
    }
}
