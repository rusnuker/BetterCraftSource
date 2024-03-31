// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;

public class CheckFieldAdapter extends FieldVisitor
{
    private boolean end;
    static /* synthetic */ Class class$org$objectweb$asm$util$CheckFieldAdapter;
    
    public CheckFieldAdapter(final FieldVisitor fieldVisitor) {
        this(327680, fieldVisitor);
        if (this.getClass() != CheckFieldAdapter.class$org$objectweb$asm$util$CheckFieldAdapter) {
            throw new IllegalStateException();
        }
    }
    
    protected CheckFieldAdapter(final int api, final FieldVisitor fieldVisitor) {
        super(api, fieldVisitor);
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        this.checkEnd();
        CheckMethodAdapter.checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(descriptor, visible));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        this.checkEnd();
        final int n = typeRef >>> 24;
        if (n != 19) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n));
        }
        CheckClassAdapter.checkTypeRefAndPath(typeRef, typePath);
        CheckMethodAdapter.checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitAttribute(final Attribute attribute) {
        this.checkEnd();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }
    
    public void visitEnd() {
        this.checkEnd();
        this.end = true;
        super.visitEnd();
    }
    
    private void checkEnd() {
        if (this.end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (final ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    static {
        CheckFieldAdapter.class$org$objectweb$asm$util$CheckFieldAdapter = class$("org.objectweb.asm.util.CheckFieldAdapter");
    }
}
