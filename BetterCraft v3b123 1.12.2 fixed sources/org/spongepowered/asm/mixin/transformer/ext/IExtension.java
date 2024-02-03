// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.ext;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IExtension
{
    boolean checkActive(final MixinEnvironment p0);
    
    void preApply(final ITargetClassContext p0);
    
    void postApply(final ITargetClassContext p0);
    
    void export(final MixinEnvironment p0, final String p1, final boolean p2, final ClassNode p3);
}
