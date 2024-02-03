// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service;

import java.io.IOException;
import org.objectweb.asm.tree.ClassNode;

public interface IClassBytecodeProvider
{
    ClassNode getClassNode(final String p0) throws ClassNotFoundException, IOException;
    
    ClassNode getClassNode(final String p0, final boolean p1) throws ClassNotFoundException, IOException;
}
