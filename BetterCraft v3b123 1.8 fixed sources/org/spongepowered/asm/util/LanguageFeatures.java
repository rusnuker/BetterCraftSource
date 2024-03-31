// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import java.lang.reflect.Field;
import java.util.ListIterator;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.util.asm.ASM;
import java.util.List;
import org.spongepowered.asm.util.asm.ClassNodeAdapter;
import java.util.Iterator;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;

public final class LanguageFeatures
{
    public static final int METHODS_IN_INTERFACES = 1;
    public static final int PRIVATE_SYNTHETIC_METHODS_IN_INTERFACES = 2;
    public static final int PRIVATE_METHODS_IN_INTERFACES = 4;
    public static final int NESTING = 8;
    public static final int DYNAMIC_CONSTANTS = 16;
    public static final int RECORDS = 32;
    public static final int SEALED_CLASSES = 64;
    
    private LanguageFeatures() {
    }
    
    public static int scan(final ClassNode classNode) {
        int features = scanClassFeatures(classNode);
        final boolean isInterface = Bytecode.hasFlag(classNode, 512);
        for (final MethodNode methodNode : classNode.methods) {
            if (isInterface) {
                features |= scanInterfaceFeatures(methodNode);
            }
            else {
                features |= scanMethodFeatures(methodNode);
            }
        }
        return features;
    }
    
    private static int scanClassFeatures(final ClassNode classNode) {
        int features = 0;
        final String nestHostClass = ClassNodeAdapter.getNestHostClass(classNode);
        final List<String> nestMembers = ClassNodeAdapter.getNestMembers(classNode);
        if (nestHostClass != null || (nestMembers != null && nestMembers.size() > 0)) {
            features |= 0x8;
        }
        return features;
    }
    
    private static int scanInterfaceFeatures(final MethodNode methodNode) {
        int features = 0;
        if (!Bytecode.hasFlag(methodNode, 1024)) {
            features |= 0x1;
        }
        if (Bytecode.getVisibility(methodNode).isLessThan(Bytecode.Visibility.PUBLIC)) {
            features |= (Bytecode.hasFlag(methodNode, 4096) ? 2 : 4);
        }
        return features;
    }
    
    private static int scanMethodFeatures(final MethodNode methodNode) {
        if (ASM.isAtLeastVersion(6)) {
            for (final AbstractInsnNode insn : methodNode.instructions) {
                if (insn instanceof LdcInsnNode && ((LdcInsnNode)insn).cst instanceof ConstantDynamic) {
                    return 16;
                }
            }
        }
        return 0;
    }
    
    public static final String format(final int features) {
        final StringBuilder sb = new StringBuilder("[");
        try {
            int count = 0;
            for (final Field field : LanguageFeatures.class.getDeclaredFields()) {
                if ((features & field.getInt(null)) != 0x0) {
                    if (count++ > 0) {
                        sb.append(',');
                    }
                    sb.append(field.getName());
                }
            }
        }
        catch (final ReflectiveOperationException ex) {
            sb.append("ERROR");
        }
        return sb.append(']').toString();
    }
}
