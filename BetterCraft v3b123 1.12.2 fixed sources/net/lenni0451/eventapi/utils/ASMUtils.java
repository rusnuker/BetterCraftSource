// 
// Decompiled by Procyon v0.6.0
// 

package net.lenni0451.eventapi.utils;

import java.lang.reflect.Method;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ASMUtils
{
    public static byte[] toBytes(final ClassNode node) {
        final ClassWriter classWriter = new ClassWriter(1);
        node.accept(classWriter);
        return classWriter.toByteArray();
    }
    
    public static ClassNode fromBytes(final byte[] classBytes) {
        final ClassReader classReader = new ClassReader(classBytes);
        final ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        return classNode;
    }
    
    public static Class<?> defineClass(final ClassLoader classLoader, final ClassNode classNode) throws Throwable {
        final Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
        defineClass.setAccessible(true);
        final byte[] classBytes = toBytes(classNode);
        return (Class)defineClass.invoke(classLoader, classNode.name.replace("/", "."), classBytes, 0, classBytes.length);
    }
}
