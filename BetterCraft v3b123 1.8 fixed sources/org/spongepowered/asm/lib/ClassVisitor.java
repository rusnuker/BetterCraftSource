// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.lib;

import org.spongepowered.asm.mixin.throwables.CompanionPluginError;

public abstract class ClassVisitor
{
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        throw new CompanionPluginError("ClassVisitor.visit");
    }
    
    public void visitSource(final String source, final String debug) {
        throw new CompanionPluginError("ClassVisitor.visitSource");
    }
    
    public void visitOuterClass(final String owner, final String name, final String desc) {
        throw new CompanionPluginError("ClassVisitor.visitOuterClass");
    }
    
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        throw new CompanionPluginError("ClassVisitor.visitAnnotation");
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String desc, final boolean visible) {
        throw new CompanionPluginError("ClassVisitor.visitTypeAnnotation");
    }
    
    public void visitAttribute(final Attribute attr) {
        throw new CompanionPluginError("ClassVisitor.visitAttribute");
    }
    
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        throw new CompanionPluginError("ClassVisitor.visitInnerClass");
    }
    
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        throw new CompanionPluginError("ClassVisitor.visitField");
    }
    
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        throw new CompanionPluginError("ClassVisitor.visitMethod");
    }
    
    public void visitEnd() {
        throw new CompanionPluginError("ClassVisitor.visitEnd");
    }
}
