// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm;

public abstract class ClassVisitor
{
    protected final int api;
    protected ClassVisitor cv;
    
    public ClassVisitor(final int api) {
        this(api, null);
    }
    
    public ClassVisitor(final int api, final ClassVisitor classVisitor) {
        if (api != 393216 && api != 327680 && api != 262144 && api != 458752) {
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.cv = classVisitor;
    }
    
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        if (this.cv != null) {
            this.cv.visit(version, access, name, signature, superName, interfaces);
        }
    }
    
    public void visitSource(final String source, final String debug) {
        if (this.cv != null) {
            this.cv.visitSource(source, debug);
        }
    }
    
    public ModuleVisitor visitModule(final String name, final int access, final String version) {
        if (this.api < 393216) {
            throw new UnsupportedOperationException("This feature requires ASM6");
        }
        if (this.cv != null) {
            return this.cv.visitModule(name, access, version);
        }
        return null;
    }
    
    public void visitNestHost(final String nestHost) {
        if (this.api < 458752) {
            throw new UnsupportedOperationException("This feature requires ASM7");
        }
        if (this.cv != null) {
            this.cv.visitNestHost(nestHost);
        }
    }
    
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
        if (this.cv != null) {
            this.cv.visitOuterClass(owner, name, descriptor);
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (this.cv != null) {
            return this.cv.visitAnnotation(descriptor, visible);
        }
        return null;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        if (this.api < 327680) {
            throw new UnsupportedOperationException("This feature requires ASM5");
        }
        if (this.cv != null) {
            return this.cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
        }
        return null;
    }
    
    public void visitAttribute(final Attribute attribute) {
        if (this.cv != null) {
            this.cv.visitAttribute(attribute);
        }
    }
    
    public void visitNestMember(final String nestMember) {
        if (this.api < 458752) {
            throw new UnsupportedOperationException("This feature requires ASM7");
        }
        if (this.cv != null) {
            this.cv.visitNestMember(nestMember);
        }
    }
    
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        if (this.cv != null) {
            this.cv.visitInnerClass(name, outerName, innerName, access);
        }
    }
    
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        if (this.cv != null) {
            return this.cv.visitField(access, name, descriptor, signature, value);
        }
        return null;
    }
    
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        if (this.cv != null) {
            return this.cv.visitMethod(access, name, descriptor, signature, exceptions);
        }
        return null;
    }
    
    public void visitEnd() {
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }
}
