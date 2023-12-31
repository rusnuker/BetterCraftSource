// 
// Decompiled by Procyon v0.6.0
// 

package com.sun.jna;

import java.lang.reflect.Field;

public class StructureReadContext extends FromNativeContext
{
    private Structure structure;
    private Field field;
    
    StructureReadContext(final Structure struct, final Field field) {
        super(field.getType());
        this.structure = struct;
        this.field = field;
    }
    
    public Structure getStructure() {
        return this.structure;
    }
    
    public Field getField() {
        return this.field;
    }
}
