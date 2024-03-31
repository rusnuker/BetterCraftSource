// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import java.io.PrintWriter;
import org.objectweb.asm.ClassVisitor;

public final class TraceClassVisitor extends ClassVisitor
{
    private final PrintWriter pw;
    public final Printer p;
    
    public TraceClassVisitor(final PrintWriter printWriter) {
        this(null, printWriter);
    }
    
    public TraceClassVisitor(final ClassVisitor classVisitor, final PrintWriter printWriter) {
        this(classVisitor, new Textifier(), printWriter);
    }
    
    public TraceClassVisitor(final ClassVisitor classVisitor, final Printer p3, final PrintWriter pw) {
        super(327680, classVisitor);
        this.pw = pw;
        this.p = p3;
    }
    
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        this.p.visit(version, access, name, signature, superName, interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
    }
    
    public void visitSource(final String source, final String debug) {
        this.p.visitSource(source, debug);
        super.visitSource(source, debug);
    }
    
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
        this.p.visitOuterClass(owner, name, descriptor);
        super.visitOuterClass(owner, name, descriptor);
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.cv == null) ? null : this.cv.visitAnnotation(descriptor, visible), this.p.visitClassAnnotation(descriptor, visible));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        return new TraceAnnotationVisitor((this.cv == null) ? null : this.cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible), this.p.visitClassTypeAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitAttribute(final Attribute attribute) {
        this.p.visitClassAttribute(attribute);
        super.visitAttribute(attribute);
    }
    
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        this.p.visitInnerClass(name, outerName, innerName, access);
        super.visitInnerClass(name, outerName, innerName, access);
    }
    
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        return new TraceFieldVisitor((this.cv == null) ? null : this.cv.visitField(access, name, descriptor, signature, value), this.p.visitField(access, name, descriptor, signature, value));
    }
    
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        return new TraceMethodVisitor((this.cv == null) ? null : this.cv.visitMethod(access, name, descriptor, signature, exceptions), this.p.visitMethod(access, name, descriptor, signature, exceptions));
    }
    
    public void visitEnd() {
        this.p.visitClassEnd();
        if (this.pw != null) {
            this.p.print(this.pw);
            this.pw.flush();
        }
        super.visitEnd();
    }
}
