// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree.analysis;

import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Set;

public class SourceValue implements Value
{
    public final int size;
    public final Set insns;
    
    public SourceValue(final int n) {
        this(n, SmallSet.emptySet());
    }
    
    public SourceValue(final int size, final AbstractInsnNode abstractInsnNode) {
        this.size = size;
        this.insns = new SmallSet(abstractInsnNode, null);
    }
    
    public SourceValue(final int size, final Set insns) {
        this.size = size;
        this.insns = insns;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof SourceValue)) {
            return false;
        }
        final SourceValue sourceValue = (SourceValue)o;
        return this.size == sourceValue.size && this.insns.equals(sourceValue.insns);
    }
    
    public int hashCode() {
        return this.insns.hashCode();
    }
}
