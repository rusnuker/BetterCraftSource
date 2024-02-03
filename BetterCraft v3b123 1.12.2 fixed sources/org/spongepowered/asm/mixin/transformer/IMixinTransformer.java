// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;
import java.util.List;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IMixinTransformer
{
    void audit(final MixinEnvironment p0);
    
    List<String> reload(final String p0, final ClassNode p1);
    
    boolean computeFramesForClass(final MixinEnvironment p0, final String p1, final ClassNode p2);
    
    byte[] transformClassBytes(final String p0, final String p1, final byte[] p2);
    
    byte[] transformClass(final MixinEnvironment p0, final String p1, final byte[] p2);
    
    boolean transformClass(final MixinEnvironment p0, final String p1, final ClassNode p2);
    
    byte[] generateClass(final MixinEnvironment p0, final String p1);
    
    boolean generateClass(final MixinEnvironment p0, final String p1, final ClassNode p2);
    
    IExtensionRegistry getExtensions();
}
