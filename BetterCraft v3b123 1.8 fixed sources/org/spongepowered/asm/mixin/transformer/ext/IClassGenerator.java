// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.ext;

import org.objectweb.asm.tree.ClassNode;

public interface IClassGenerator
{
    String getName();
    
    boolean generate(final String p0, final ClassNode p1);
}
