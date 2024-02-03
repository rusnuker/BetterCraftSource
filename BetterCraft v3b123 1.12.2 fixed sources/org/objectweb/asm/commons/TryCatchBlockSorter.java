// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import org.objectweb.asm.tree.TryCatchBlockNode;
import java.util.Comparator;
import java.util.Collections;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

public class TryCatchBlockSorter extends MethodNode
{
    public TryCatchBlockSorter(final MethodVisitor methodVisitor, final int n, final String s, final String s2, final String s3, final String[] array) {
        this(327680, methodVisitor, n, s, s2, s3, array);
    }
    
    protected TryCatchBlockSorter(final int api, final MethodVisitor mv, final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        super(api, access, name, descriptor, signature, exceptions);
        this.mv = mv;
    }
    
    public void visitEnd() {
        Collections.sort(this.tryCatchBlocks, new TryCatchBlockSorter$1(this));
        for (int i = 0; i < this.tryCatchBlocks.size(); ++i) {
            this.tryCatchBlocks.get(i).updateIndex(i);
        }
        if (this.mv != null) {
            this.accept(this.mv);
        }
    }
}
