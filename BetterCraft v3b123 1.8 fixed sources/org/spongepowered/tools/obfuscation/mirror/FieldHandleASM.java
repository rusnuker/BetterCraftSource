// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.FieldNode;

public class FieldHandleASM extends FieldHandle
{
    private final FieldNode field;
    
    public FieldHandleASM(final TypeHandle owner, final FieldNode field) {
        super(owner, field.name, field.desc);
        this.field = field;
    }
    
    @Override
    public Bytecode.Visibility getVisibility() {
        return Bytecode.getVisibility(this.field);
    }
}
