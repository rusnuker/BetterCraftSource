// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;

public class StaticInitMerger extends ClassVisitor
{
    private String name;
    private MethodVisitor clinit;
    private final String prefix;
    private int counter;
    
    public StaticInitMerger(final String s, final ClassVisitor classVisitor) {
        this(327680, s, classVisitor);
    }
    
    protected StaticInitMerger(final int api, final String prefix, final ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.prefix = prefix;
    }
    
    public void visit(final int version, final int access, final String s, final String signature, final String superName, final String[] interfaces) {
        this.cv.visit(version, access, s, signature, superName, interfaces);
        this.name = s;
    }
    
    public MethodVisitor visitMethod(final int access, final String s, final String s2, final String s3, final String[] array) {
        MethodVisitor methodVisitor;
        if ("<clinit>".equals(s)) {
            final int n = 10;
            final String string = this.prefix + this.counter++;
            methodVisitor = this.cv.visitMethod(n, string, s2, s3, array);
            if (this.clinit == null) {
                this.clinit = this.cv.visitMethod(n, s, s2, null, null);
            }
            this.clinit.visitMethodInsn(184, this.name, string, s2, false);
        }
        else {
            methodVisitor = this.cv.visitMethod(access, s, s2, s3, array);
        }
        return methodVisitor;
    }
    
    public void visitEnd() {
        if (this.clinit != null) {
            this.clinit.visitInsn(177);
            this.clinit.visitMaxs(0, 0);
        }
        this.cv.visitEnd();
    }
}
