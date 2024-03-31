// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import java.util.Stack;
import org.objectweb.asm.signature.SignatureVisitor;

public class SignatureRemapper extends SignatureVisitor
{
    private final SignatureVisitor v;
    private final Remapper remapper;
    private Stack classNames;
    
    public SignatureRemapper(final SignatureVisitor signatureVisitor, final Remapper remapper) {
        this(327680, signatureVisitor, remapper);
    }
    
    protected SignatureRemapper(final int api, final SignatureVisitor v, final Remapper remapper) {
        super(api);
        this.classNames = new Stack();
        this.v = v;
        this.remapper = remapper;
    }
    
    public void visitClassType(final String s) {
        this.classNames.push(s);
        this.v.visitClassType(this.remapper.mapType(s));
    }
    
    public void visitInnerClassType(final String s) {
        final String s2 = this.classNames.pop();
        final String string = s2 + '$' + s;
        this.classNames.push(string);
        final String string2 = this.remapper.mapType(s2) + '$';
        final String mapType = this.remapper.mapType(string);
        this.v.visitInnerClassType(mapType.substring(mapType.startsWith(string2) ? string2.length() : (mapType.lastIndexOf(36) + 1)));
    }
    
    public void visitFormalTypeParameter(final String name) {
        this.v.visitFormalTypeParameter(name);
    }
    
    public void visitTypeVariable(final String name) {
        this.v.visitTypeVariable(name);
    }
    
    public SignatureVisitor visitArrayType() {
        this.v.visitArrayType();
        return this;
    }
    
    public void visitBaseType(final char descriptor) {
        this.v.visitBaseType(descriptor);
    }
    
    public SignatureVisitor visitClassBound() {
        this.v.visitClassBound();
        return this;
    }
    
    public SignatureVisitor visitExceptionType() {
        this.v.visitExceptionType();
        return this;
    }
    
    public SignatureVisitor visitInterface() {
        this.v.visitInterface();
        return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        this.v.visitInterfaceBound();
        return this;
    }
    
    public SignatureVisitor visitParameterType() {
        this.v.visitParameterType();
        return this;
    }
    
    public SignatureVisitor visitReturnType() {
        this.v.visitReturnType();
        return this;
    }
    
    public SignatureVisitor visitSuperclass() {
        this.v.visitSuperclass();
        return this;
    }
    
    public void visitTypeArgument() {
        this.v.visitTypeArgument();
    }
    
    public SignatureVisitor visitTypeArgument(final char wildcard) {
        this.v.visitTypeArgument(wildcard);
        return this;
    }
    
    public void visitEnd() {
        this.v.visitEnd();
        this.classNames.pop();
    }
}
