// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree.analysis;

import org.objectweb.asm.tree.AbstractInsnNode;

public class AnalyzerException extends Exception
{
    public final AbstractInsnNode node;
    
    public AnalyzerException(final AbstractInsnNode node, final String s) {
        super(s);
        this.node = node;
    }
    
    public AnalyzerException(final AbstractInsnNode node, final String s, final Throwable t) {
        super(s, t);
        this.node = node;
    }
    
    public AnalyzerException(final AbstractInsnNode node, final String s, final Object o, final Value value) {
        super(((s == null) ? "Expected " : (s + ": expected ")) + o + ", but found " + value);
        this.node = node;
    }
}
