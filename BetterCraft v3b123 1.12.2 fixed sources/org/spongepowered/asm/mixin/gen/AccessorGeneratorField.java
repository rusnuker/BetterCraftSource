// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

public abstract class AccessorGeneratorField extends AccessorGenerator
{
    protected final FieldNode targetField;
    protected final Type targetType;
    
    public AccessorGeneratorField(final AccessorInfo info) {
        super(info, Bytecode.isStatic(info.getTargetField()));
        this.targetField = info.getTargetField();
        this.targetType = info.getTargetFieldType();
        this.checkModifiers();
    }
}
