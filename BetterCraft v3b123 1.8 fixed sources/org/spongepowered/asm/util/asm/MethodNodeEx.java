// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.objectweb.asm.tree.MethodNode;

public class MethodNodeEx extends MethodNode
{
    private final IMixinInfo owner;
    private final String originalName;
    
    public MethodNodeEx(final int access, final String name, final String descriptor, final String signature, final String[] exceptions, final IMixinInfo owner) {
        super(ASM.API_VERSION, access, name, descriptor, signature, exceptions);
        this.originalName = name;
        this.owner = owner;
    }
    
    @Override
    public String toString() {
        return String.format("%s%s", this.originalName, this.desc);
    }
    
    public String getQualifiedName() {
        return String.format("%s::%s", this.owner.getName(), this.originalName);
    }
    
    public String getOriginalName() {
        return this.originalName;
    }
    
    public IMixinInfo getOwner() {
        return this.owner;
    }
    
    public static String getName(final MethodNode method) {
        return (method instanceof MethodNodeEx) ? ((MethodNodeEx)method).getOriginalName() : method.name;
    }
}
