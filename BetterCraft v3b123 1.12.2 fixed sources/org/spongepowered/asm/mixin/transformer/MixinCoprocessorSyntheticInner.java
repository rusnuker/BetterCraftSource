// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.ClassNode;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

class MixinCoprocessorSyntheticInner extends MixinCoprocessor
{
    private final Set<String> syntheticInnerClasses;
    
    MixinCoprocessorSyntheticInner() {
        this.syntheticInnerClasses = new HashSet<String>();
    }
    
    @Override
    String getName() {
        return "syntheticinner";
    }
    
    @Override
    public void onInit(final MixinInfo mixin) {
        for (final String innerClass : mixin.getSyntheticInnerClasses()) {
            this.registerSyntheticInner(innerClass.replace('/', '.'));
        }
    }
    
    void registerSyntheticInner(final String className) {
        this.syntheticInnerClasses.add(className);
    }
    
    @Override
    ProcessResult process(final String className, final ClassNode classNode) {
        if (!this.syntheticInnerClasses.contains(className)) {
            return ProcessResult.NONE;
        }
        classNode.access |= 0x1;
        for (final FieldNode field : classNode.fields) {
            if ((field.access & 0x6) == 0x0) {
                final FieldNode fieldNode = field;
                fieldNode.access |= 0x1;
            }
        }
        for (final MethodNode method : classNode.methods) {
            if ((method.access & 0x6) == 0x0) {
                final MethodNode methodNode = method;
                methodNode.access |= 0x1;
            }
        }
        return ProcessResult.PASSTHROUGH_TRANSFORMED;
    }
}
