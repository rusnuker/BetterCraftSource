// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.MethodNode;

public class MethodHandleASM extends MethodHandle
{
    private final MethodNode method;
    
    public MethodHandleASM(final TypeHandle owner, final MethodNode method) {
        super(owner, method.name, method.desc);
        this.method = method;
    }
    
    @Override
    public String getJavaSignature() {
        return TypeUtils.getJavaSignature(this.method.desc);
    }
    
    @Override
    public Bytecode.Visibility getVisibility() {
        return Bytecode.getVisibility(this.method);
    }
}
