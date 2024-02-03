// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import org.objectweb.asm.tree.ClassNode;

public interface ITreeClassTransformer extends ITransformer
{
    boolean transformClassNode(final String p0, final String p1, final ClassNode p2);
}
