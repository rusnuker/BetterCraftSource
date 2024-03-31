// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree.analysis;

import org.objectweb.asm.Type;

public class BasicValue implements Value
{
    public static final BasicValue UNINITIALIZED_VALUE;
    public static final BasicValue INT_VALUE;
    public static final BasicValue FLOAT_VALUE;
    public static final BasicValue LONG_VALUE;
    public static final BasicValue DOUBLE_VALUE;
    public static final BasicValue REFERENCE_VALUE;
    public static final BasicValue RETURNADDRESS_VALUE;
    private final Type type;
    
    public BasicValue(final Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public int getSize() {
        return (this.type == Type.LONG_TYPE || this.type == Type.DOUBLE_TYPE) ? 2 : 1;
    }
    
    public boolean isReference() {
        return this.type != null && (this.type.getSort() == 10 || this.type.getSort() == 9);
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BasicValue)) {
            return false;
        }
        if (this.type == null) {
            return ((BasicValue)o).type == null;
        }
        return this.type.equals(((BasicValue)o).type);
    }
    
    public int hashCode() {
        return (this.type == null) ? 0 : this.type.hashCode();
    }
    
    public String toString() {
        if (this == BasicValue.UNINITIALIZED_VALUE) {
            return ".";
        }
        if (this == BasicValue.RETURNADDRESS_VALUE) {
            return "A";
        }
        if (this == BasicValue.REFERENCE_VALUE) {
            return "R";
        }
        return this.type.getDescriptor();
    }
    
    static {
        _clinit_();
        UNINITIALIZED_VALUE = new BasicValue(null);
        INT_VALUE = new BasicValue(Type.INT_TYPE);
        FLOAT_VALUE = new BasicValue(Type.FLOAT_TYPE);
        LONG_VALUE = new BasicValue(Type.LONG_TYPE);
        DOUBLE_VALUE = new BasicValue(Type.DOUBLE_TYPE);
        REFERENCE_VALUE = new BasicValue(Type.getObjectType("java/lang/Object"));
        RETURNADDRESS_VALUE = new BasicValue(Type.VOID_TYPE);
    }
    
    static /* synthetic */ void _clinit_() {
    }
}
