// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.invoke;

import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;

public class ModifyArgsInjector extends InvokeInjector
{
    private final ArgsClassGenerator argsClassGenerator;
    
    public ModifyArgsInjector(final InjectionInfo info) {
        super(info, "@ModifyArgs");
        this.argsClassGenerator = info.getMixin().getExtensions().getGenerator(ArgsClassGenerator.class);
    }
    
    @Override
    protected void checkTarget(final Target target) {
        this.checkTargetModifiers(target, false);
    }
    
    @Override
    protected void inject(final Target target, final InjectionNodes.InjectionNode node) {
        this.checkTargetForNode(target, node, InjectionPoint.RestrictTargetLevel.ALLOW_ALL);
        super.inject(target, node);
    }
    
    @Override
    protected void injectAtInvoke(final Target target, final InjectionNodes.InjectionNode node) {
        final MethodInsnNode targetMethod = (MethodInsnNode)node.getCurrentTarget();
        final Type[] args = Type.getArgumentTypes(targetMethod.desc);
        if (args.length == 0) {
            throw new InvalidInjectionException(this.info, "@ModifyArgs injector " + this + " targets a method invocation " + targetMethod.name + targetMethod.desc + " with no arguments!");
        }
        final String clArgs = this.argsClassGenerator.getArgsClass(targetMethod.desc, this.info.getMixin().getMixin()).getName();
        final boolean withArgs = this.verifyTarget(target);
        final InsnList insns = new InsnList();
        final Target.Extension extraStack = target.extendStack().add(1);
        this.packArgs(insns, clArgs, targetMethod);
        if (withArgs) {
            extraStack.add(target.arguments);
            Bytecode.loadArgs(target.arguments, insns, target.isStatic ? 0 : 1);
        }
        this.invokeHandler(insns);
        this.unpackArgs(insns, clArgs, args);
        extraStack.apply();
        target.insns.insertBefore(targetMethod, insns);
    }
    
    private boolean verifyTarget(final Target target) {
        final String shortDesc = String.format("(L%s;)V", ArgsClassGenerator.ARGS_REF);
        if (this.methodNode.desc.equals(shortDesc)) {
            return false;
        }
        final String targetDesc = Bytecode.changeDescriptorReturnType(target.method.desc, "V");
        final String longDesc = String.format("(L%s;%s", ArgsClassGenerator.ARGS_REF, targetDesc.substring(1));
        if (this.methodNode.desc.equals(longDesc)) {
            return true;
        }
        throw new InvalidInjectionException(this.info, "@ModifyArgs injector " + this + " has an invalid signature " + this.methodNode.desc + ", expected " + shortDesc + " or " + longDesc);
    }
    
    private void packArgs(final InsnList insns, final String clArgs, final MethodInsnNode targetMethod) {
        final String factoryDesc = Bytecode.changeDescriptorReturnType(targetMethod.desc, "L" + clArgs + ";");
        insns.add(new MethodInsnNode(184, clArgs, "of", factoryDesc, false));
        insns.add(new InsnNode(89));
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
            insns.add(new InsnNode(95));
        }
    }
    
    private void unpackArgs(final InsnList insns, final String clArgs, final Type[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (i < args.length - 1) {
                insns.add(new InsnNode(89));
            }
            insns.add(new MethodInsnNode(182, clArgs, "$" + i, "()" + args[i].getDescriptor(), false));
            if (i < args.length - 1) {
                if (args[i].getSize() == 1) {
                    insns.add(new InsnNode(95));
                }
                else {
                    insns.add(new InsnNode(93));
                    insns.add(new InsnNode(88));
                }
            }
        }
    }
}
