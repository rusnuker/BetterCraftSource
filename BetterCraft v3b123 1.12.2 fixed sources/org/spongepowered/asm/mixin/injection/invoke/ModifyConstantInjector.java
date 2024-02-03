// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.logging.Level;
import org.objectweb.asm.tree.LocalVariableNode;
import org.spongepowered.asm.util.Locals;
import org.spongepowered.asm.util.SignaturePrinter;
import org.objectweb.asm.tree.FieldInsnNode;
import org.spongepowered.asm.mixin.injection.invoke.util.InsnFinder;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class ModifyConstantInjector extends RedirectInjector
{
    private static final int OPCODE_OFFSET = 6;
    
    public ModifyConstantInjector(final InjectionInfo info) {
        super(info, "@ModifyConstant");
    }
    
    @Override
    protected void inject(final Target target, final InjectionNodes.InjectionNode node) {
        if (!this.preInject(node)) {
            return;
        }
        if (node.isReplaced()) {
            throw new UnsupportedOperationException("Target failure for " + this.info);
        }
        final AbstractInsnNode targetNode = node.getCurrentTarget();
        if (targetNode instanceof TypeInsnNode) {
            this.checkTargetModifiers(target, false);
            this.injectTypeConstantModifier(target, (TypeInsnNode)targetNode);
            return;
        }
        if (targetNode instanceof JumpInsnNode) {
            this.checkTargetModifiers(target, false);
            this.injectExpandedConstantModifier(target, (JumpInsnNode)targetNode);
            return;
        }
        if (Bytecode.isConstant(targetNode)) {
            this.checkTargetModifiers(target, false);
            this.injectConstantModifier(target, targetNode);
            return;
        }
        throw new InvalidInjectionException(this.info, String.format("%s annotation is targetting an invalid insn in %s in %s", this.annotationType, target, this));
    }
    
    private void injectTypeConstantModifier(final Target target, final TypeInsnNode typeNode) {
        final int opcode = typeNode.getOpcode();
        if (opcode != 193) {
            throw new InvalidInjectionException(this.info, String.format("%s annotation does not support %s insn in %s in %s", this.annotationType, Bytecode.getOpcodeName(opcode), target, this));
        }
        this.injectAtInstanceOf(target, typeNode);
    }
    
    private void injectExpandedConstantModifier(final Target target, final JumpInsnNode jumpNode) {
        final int opcode = jumpNode.getOpcode();
        if (opcode < 155 || opcode > 158) {
            throw new InvalidInjectionException(this.info, String.format("%s annotation selected an invalid opcode %s in %s in %s", this.annotationType, Bytecode.getOpcodeName(opcode), target, this));
        }
        final Target.Extension extraStack = target.extendStack();
        final InsnList insns = new InsnList();
        insns.add(new InsnNode(3));
        final AbstractInsnNode invoke = this.invokeConstantHandler(Type.getType("I"), target, extraStack, insns, insns);
        insns.add(new JumpInsnNode(opcode + 6, jumpNode.label));
        extraStack.add(1).apply();
        target.replaceNode(jumpNode, invoke, insns);
    }
    
    private void injectConstantModifier(final Target target, final AbstractInsnNode constNode) {
        final Type constantType = Bytecode.getConstantType(constNode);
        if (constantType.getSort() <= 5 && this.info.getMixin().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            this.checkNarrowing(target, constNode, constantType);
        }
        final Target.Extension extraStack = target.extendStack();
        final InsnList before = new InsnList();
        final InsnList after = new InsnList();
        final AbstractInsnNode invoke = this.invokeConstantHandler(constantType, target, extraStack, before, after);
        extraStack.apply();
        target.wrapNode(constNode, invoke, before, after);
    }
    
    private AbstractInsnNode invokeConstantHandler(final Type constantType, final Target target, final Target.Extension extraStack, final InsnList before, final InsnList after) {
        final InjectorData handler = new InjectorData(target, "constant modifier");
        this.validateParams(handler, constantType, constantType);
        if (!this.isStatic) {
            before.insert(new VarInsnNode(25, 0));
            extraStack.add();
        }
        if (handler.captureTargetArgs > 0) {
            this.pushArgs(target.arguments, after, target.getArgIndices(), 0, handler.captureTargetArgs, extraStack);
        }
        return this.invokeHandler(after);
    }
    
    private void checkNarrowing(final Target target, final AbstractInsnNode constNode, final Type constantType) {
        final AbstractInsnNode pop = new InsnFinder().findPopInsn(target, constNode);
        if (pop == null) {
            return;
        }
        if (pop instanceof FieldInsnNode) {
            final FieldInsnNode fieldNode = (FieldInsnNode)pop;
            final Type fieldType = Type.getType(fieldNode.desc);
            this.checkNarrowing(target, constNode, constantType, fieldType, target.indexOf(pop), String.format("%s %s %s.%s", Bytecode.getOpcodeName(pop), SignaturePrinter.getTypeName(fieldType, false), fieldNode.owner.replace('/', '.'), fieldNode.name));
        }
        else if (pop.getOpcode() == 172) {
            this.checkNarrowing(target, constNode, constantType, target.returnType, target.indexOf(pop), "RETURN " + SignaturePrinter.getTypeName(target.returnType, false));
        }
        else if (pop.getOpcode() == 54) {
            final int var = ((VarInsnNode)pop).var;
            final LocalVariableNode localVar = Locals.getLocalVariableAt(target.classNode, target.method, pop, var);
            if (localVar != null && localVar.desc != null) {
                final String name = (localVar.name != null) ? localVar.name : "unnamed";
                final Type localType = Type.getType(localVar.desc);
                this.checkNarrowing(target, constNode, constantType, localType, target.indexOf(pop), String.format("ISTORE[var=%d] %s %s", var, SignaturePrinter.getTypeName(localType, false), name));
            }
        }
    }
    
    private void checkNarrowing(final Target target, final AbstractInsnNode constNode, final Type constantType, final Type type, final int index, final String description) {
        final int fromSort = constantType.getSort();
        final int toSort = type.getSort();
        if (toSort < fromSort) {
            final String fromType = SignaturePrinter.getTypeName(constantType, false);
            final String toType = SignaturePrinter.getTypeName(type, false);
            final String message = (toSort == 1) ? ". Implicit conversion to <boolean> can cause nondeterministic (JVM-specific) behaviour!" : "";
            final Level level = (toSort == 1) ? Level.ERROR : Level.WARN;
            Injector.logger.log(level, "Narrowing conversion of <{}> to <{}> in {} target {} at opcode {} ({}){}", fromType, toType, this.info, target, index, description, message);
        }
    }
}
