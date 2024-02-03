// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorByName;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;
import org.spongepowered.asm.mixin.injection.selectors.ElementNode;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
import org.spongepowered.asm.util.Bytecode;
import java.util.Iterator;
import java.lang.annotation.Annotation;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

class InvokerInfo extends AccessorInfo
{
    InvokerInfo(final MixinTargetContext mixin, final MethodNode method) {
        super(mixin, method, Invoker.class);
    }
    
    @Override
    protected AccessorType initType() {
        if (this.specifiedName != null) {
            final String mappedReference = this.mixin.getReferenceMapper().remap(this.mixin.getClassRef(), this.specifiedName);
            return this.initType(mappedReference.replace('.', '/'), this.mixin.getTargetClassRef());
        }
        final AccessorName accessorName = AccessorName.of(this.method.name, false);
        if (accessorName != null) {
            for (final String prefix : AccessorType.OBJECT_FACTORY.getExpectedPrefixes()) {
                if (prefix.equals(accessorName.prefix)) {
                    return this.initType(accessorName.name, this.mixin.getTargetClassInfo().getSimpleName());
                }
            }
        }
        return AccessorType.METHOD_PROXY;
    }
    
    private AccessorType initType(final String targetName, final String targetClassName) {
        if (!"<init>".equals(targetName) && !targetClassName.equals(targetName)) {
            return AccessorType.METHOD_PROXY;
        }
        if (!this.returnType.equals(this.mixin.getTargetClassInfo().getType())) {
            throw new InvalidAccessorException(this.mixin, String.format("%s appears to have an invalid return type. %s requires matching return type. Found %s expected %s", this, AccessorType.OBJECT_FACTORY, Bytecode.getSimpleName(this.returnType), this.mixin.getTargetClassInfo().getSimpleName()));
        }
        if (!this.isStatic) {
            throw new InvalidAccessorException(this.mixin, String.format("%s for %s must be static", this, AccessorType.OBJECT_FACTORY, Bytecode.getSimpleName(this.returnType)));
        }
        return AccessorType.OBJECT_FACTORY;
    }
    
    @Override
    protected Type initTargetFieldType() {
        return null;
    }
    
    @Override
    protected ITargetSelector initTarget() {
        if (this.type == AccessorType.OBJECT_FACTORY) {
            return new MemberInfo("<init>", null, Bytecode.changeDescriptorReturnType(this.method.desc, "V"));
        }
        return new MemberInfo(this.getTargetName(this.specifiedName), null, this.method.desc);
    }
    
    @Override
    public void locate() {
        this.targetMethod = this.findTargetMethod();
    }
    
    private MethodNode findTargetMethod() {
        final TargetSelector.Result<MethodNode> result = TargetSelector.run(this.target.configure(ITargetSelector.Configure.ORPHAN, new String[0]), ElementNode.methodList(this.classNode));
        try {
            return result.getSingleResult(true);
        }
        catch (final IllegalStateException ex) {
            final String message = ex.getMessage() + " matching " + this.target + " in " + this.classNode.name + " for " + this;
            if (this.type == AccessorType.METHOD_PROXY && this.specifiedName != null && this.target instanceof ITargetSelectorByName) {
                final String name = ((ITargetSelectorByName)this.target).getName();
                if (name != null && (name.contains(".") || name.contains("/"))) {
                    throw new InvalidAccessorException(this, "Invalid factory invoker failed to match the target class. " + message);
                }
            }
            throw new InvalidAccessorException(this, message);
        }
    }
}
