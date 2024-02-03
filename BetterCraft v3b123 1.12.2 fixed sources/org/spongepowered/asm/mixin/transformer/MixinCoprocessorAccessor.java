// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.objectweb.asm.tree.AnnotationNode;
import java.util.Iterator;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinProxy;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.tree.ClassNode;
import java.util.HashMap;
import java.util.Map;

class MixinCoprocessorAccessor extends MixinCoprocessor
{
    protected final String sessionId;
    private final Map<String, MixinInfo> accessorMixins;
    
    MixinCoprocessorAccessor(final String sessionId) {
        this.accessorMixins = new HashMap<String, MixinInfo>();
        this.sessionId = sessionId;
    }
    
    @Override
    String getName() {
        return "accessor";
    }
    
    @Override
    public void onPrepare(final MixinInfo mixin) {
        if (mixin.isAccessor()) {
            this.registerAccessor(mixin);
        }
    }
    
    void registerAccessor(final MixinInfo mixin) {
        this.accessorMixins.put(mixin.getClassName(), mixin);
    }
    
    @Override
    ProcessResult process(final String className, final ClassNode classNode) {
        if (!MixinEnvironment.getCompatibilityLevel().supports(1) || !this.accessorMixins.containsKey(className)) {
            return ProcessResult.NONE;
        }
        final MixinInfo mixin = this.accessorMixins.get(className);
        boolean transformed = false;
        final MixinInfo.MixinClassNode mixinClassNode = mixin.getClassNode(0);
        final ClassInfo targetClass = mixin.getTargets().get(0);
        if (!Bytecode.hasFlag(mixinClassNode, 1)) {
            Bytecode.setVisibility(mixinClassNode, Bytecode.Visibility.PUBLIC);
            transformed = true;
        }
        for (final MixinInfo.MixinMethodNode methodNode : mixinClassNode.mixinMethods) {
            if (!Bytecode.hasFlag(methodNode, 8)) {
                continue;
            }
            final AnnotationNode accessor = methodNode.getVisibleAnnotation(Accessor.class);
            final AnnotationNode invoker = methodNode.getVisibleAnnotation(Invoker.class);
            if (accessor == null && invoker == null) {
                continue;
            }
            final ClassInfo.Method method = this.getAccessorMethod(mixin, methodNode, targetClass);
            createProxy(methodNode, targetClass, method);
            Annotations.setVisible(methodNode, MixinProxy.class, "sessionId", this.sessionId);
            classNode.methods.add(methodNode);
            transformed = true;
        }
        if (!transformed) {
            return ProcessResult.NONE;
        }
        Bytecode.replace(mixinClassNode, classNode);
        return ProcessResult.PASSTHROUGH_TRANSFORMED;
    }
    
    private ClassInfo.Method getAccessorMethod(final MixinInfo mixin, final MethodNode methodNode, final ClassInfo targetClass) throws MixinTransformerError {
        final ClassInfo.Method method = mixin.getClassInfo().findMethod(methodNode, 10);
        if (!method.isConformed()) {
            final String uniqueName = targetClass.getMethodMapper().getUniqueName(methodNode, this.sessionId, true);
            method.conform(uniqueName);
        }
        return method;
    }
    
    private static void createProxy(final MethodNode methodNode, final ClassInfo targetClass, final ClassInfo.Method method) {
        methodNode.access |= 0x1000;
        methodNode.instructions.clear();
        final Type[] args = Type.getArgumentTypes(methodNode.desc);
        final Type returnType = Type.getReturnType(methodNode.desc);
        Bytecode.loadArgs(args, methodNode.instructions, 0);
        methodNode.instructions.add(new MethodInsnNode(184, targetClass.getName(), method.getName(), methodNode.desc, false));
        methodNode.instructions.add(new InsnNode(returnType.getOpcode(172)));
        methodNode.maxStack = Bytecode.getFirstNonArgLocalIndex(args, false);
        methodNode.maxLocals = 0;
    }
}
