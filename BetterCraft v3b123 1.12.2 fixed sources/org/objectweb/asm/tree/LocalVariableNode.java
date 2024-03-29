// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree;

import org.objectweb.asm.MethodVisitor;

public class LocalVariableNode
{
    public String name;
    public String desc;
    public String signature;
    public LabelNode start;
    public LabelNode end;
    public int index;
    
    public LocalVariableNode(final String name, final String descriptor, final String signature, final LabelNode start, final LabelNode end, final int index) {
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;
        this.start = start;
        this.end = end;
        this.index = index;
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLocalVariable(this.name, this.desc, this.signature, this.start.getLabel(), this.end.getLabel(), this.index);
    }
}
