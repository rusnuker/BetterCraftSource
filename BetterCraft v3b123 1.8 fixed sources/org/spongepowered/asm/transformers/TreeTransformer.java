// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.transformers;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;
import org.spongepowered.asm.service.ILegacyClassTransformer;

public abstract class TreeTransformer implements ILegacyClassTransformer
{
    private ClassReader classReader;
    private ClassNode classNode;
    
    protected final ClassNode readClass(final String className, final byte[] basicClass) {
        return this.readClass(className, basicClass, true);
    }
    
    protected final ClassNode readClass(final String className, final byte[] basicClass, final boolean cacheReader) {
        final ClassReader classReader = new MixinClassReader(basicClass, className);
        if (cacheReader) {
            this.classReader = classReader;
        }
        final ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 8);
        return classNode;
    }
    
    protected final byte[] writeClass(final ClassNode classNode) {
        if (this.classReader != null && this.classNode == classNode) {
            this.classNode = null;
            final ClassWriter writer = new MixinClassWriter(this.classReader, 3);
            this.classReader = null;
            classNode.accept(writer);
            return writer.toByteArray();
        }
        this.classNode = null;
        final ClassWriter writer = new MixinClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
