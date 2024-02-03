// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.signature.SignatureVisitor;

public class CheckSignatureAdapter extends SignatureVisitor
{
    public static final int CLASS_SIGNATURE = 0;
    public static final int METHOD_SIGNATURE = 1;
    public static final int TYPE_SIGNATURE = 2;
    private final int type;
    private int state;
    private boolean canBeVoid;
    private final SignatureVisitor sv;
    
    public CheckSignatureAdapter(final int n, final SignatureVisitor signatureVisitor) {
        this(327680, n, signatureVisitor);
    }
    
    protected CheckSignatureAdapter(final int api, final int type, final SignatureVisitor sv) {
        super(api);
        this.type = type;
        this.state = 1;
        this.sv = sv;
    }
    
    public void visitFormalTypeParameter(final String name) {
        if (this.type == 2 || (this.state != 1 && this.state != 2 && this.state != 4)) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(name, "formal type parameter");
        this.state = 2;
        if (this.sv != null) {
            this.sv.visitFormalTypeParameter(name);
        }
    }
    
    public SignatureVisitor visitClassBound() {
        if (this.state != 2) {
            throw new IllegalStateException();
        }
        this.state = 4;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitClassBound());
    }
    
    public SignatureVisitor visitInterfaceBound() {
        if (this.state != 2 && this.state != 4) {
            throw new IllegalArgumentException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitInterfaceBound());
    }
    
    public SignatureVisitor visitSuperclass() {
        if (this.type != 0 || (this.state & 0x7) == 0x0) {
            throw new IllegalArgumentException();
        }
        this.state = 8;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitSuperclass());
    }
    
    public SignatureVisitor visitInterface() {
        if (this.state != 8) {
            throw new IllegalStateException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitInterface());
    }
    
    public SignatureVisitor visitParameterType() {
        if (this.type != 1 || (this.state & 0x17) == 0x0) {
            throw new IllegalArgumentException();
        }
        this.state = 16;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitParameterType());
    }
    
    public SignatureVisitor visitReturnType() {
        if (this.type != 1 || (this.state & 0x17) == 0x0) {
            throw new IllegalArgumentException();
        }
        this.state = 32;
        final CheckSignatureAdapter checkSignatureAdapter = new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitReturnType());
        checkSignatureAdapter.canBeVoid = true;
        return checkSignatureAdapter;
    }
    
    public SignatureVisitor visitExceptionType() {
        if (this.state != 32) {
            throw new IllegalStateException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitExceptionType());
    }
    
    public void visitBaseType(final char descriptor) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        if (descriptor == 'V') {
            if (!this.canBeVoid) {
                throw new IllegalArgumentException();
            }
        }
        else if ("ZCBSIFJD".indexOf(descriptor) == -1) {
            throw new IllegalArgumentException();
        }
        this.state = 64;
        if (this.sv != null) {
            this.sv.visitBaseType(descriptor);
        }
    }
    
    public void visitTypeVariable(final String name) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(name, "type variable");
        this.state = 64;
        if (this.sv != null) {
            this.sv.visitTypeVariable(name);
        }
    }
    
    public SignatureVisitor visitArrayType() {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        this.state = 64;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitArrayType());
    }
    
    public void visitClassType(final String name) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkInternalName(name, "class name");
        this.state = 128;
        if (this.sv != null) {
            this.sv.visitClassType(name);
        }
    }
    
    public void visitInnerClassType(final String name) {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(name, "inner class name");
        if (this.sv != null) {
            this.sv.visitInnerClassType(name);
        }
    }
    
    public void visitTypeArgument() {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        if (this.sv != null) {
            this.sv.visitTypeArgument();
        }
    }
    
    public SignatureVisitor visitTypeArgument(final char wildcard) {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        if ("+-=".indexOf(wildcard) == -1) {
            throw new IllegalArgumentException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitTypeArgument(wildcard));
    }
    
    public void visitEnd() {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        this.state = 256;
        if (this.sv != null) {
            this.sv.visitEnd();
        }
    }
}
