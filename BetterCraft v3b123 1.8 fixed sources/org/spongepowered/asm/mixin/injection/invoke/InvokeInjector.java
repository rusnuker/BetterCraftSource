// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.code.Injector;

public abstract class InvokeInjector extends Injector
{
    public InvokeInjector(final InjectionInfo info, final String annotationType) {
        super(info, annotationType);
    }
    
    @Override
    protected void sanityCheck(final Target target, final List<InjectionPoint> injectionPoints) {
        super.sanityCheck(target, injectionPoints);
        this.checkTarget(target);
    }
    
    protected void checkTarget(final Target target) {
        this.checkTargetModifiers(target, true);
    }
    
    @Override
    protected void inject(final Target target, final InjectionNodes.InjectionNode node) {
        if (!(node.getCurrentTarget() instanceof MethodInsnNode)) {
            throw new InvalidInjectionException(this.info, String.format("%s annotation on is targetting a non-method insn in %s in %s", this.annotationType, target, this));
        }
        this.injectAtInvoke(target, node);
    }
    
    protected abstract void injectAtInvoke(final Target p0, final InjectionNodes.InjectionNode p1);
}
