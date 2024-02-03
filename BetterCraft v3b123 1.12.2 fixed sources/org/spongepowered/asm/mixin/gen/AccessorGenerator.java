// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import org.objectweb.asm.tree.AnnotationNode;
import java.util.ArrayList;
import org.spongepowered.asm.util.asm.ASM;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;

public abstract class AccessorGenerator
{
    protected final AccessorInfo info;
    protected final boolean targetIsStatic;
    
    public AccessorGenerator(final AccessorInfo info, final boolean isStatic) {
        this.info = info;
        this.targetIsStatic = isStatic;
    }
    
    protected void checkModifiers() {
        if (this.info.isStatic() && !this.targetIsStatic) {
            final IMixinContext context = this.info.getMixin();
            throw new InvalidInjectionException(context, String.format("%s is invalid. Accessor method is%s static but the target is not.", this.info, this.info.isStatic() ? "" : " not"));
        }
    }
    
    protected final MethodNode createMethod(final int maxLocals, final int maxStack) {
        final MethodNode method = this.info.getMethod();
        final MethodNode accessor = new MethodNode(ASM.API_VERSION, (method.access & 0xFFFFFBFF) | 0x1000, method.name, method.desc, null, null);
        (accessor.visibleAnnotations = new ArrayList<AnnotationNode>()).add(this.info.getAnnotationNode());
        accessor.maxLocals = maxLocals;
        accessor.maxStack = maxStack;
        return accessor;
    }
    
    public void validate() {
    }
    
    public abstract MethodNode generate();
}
