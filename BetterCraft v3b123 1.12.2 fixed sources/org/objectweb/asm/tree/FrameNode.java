// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import java.util.List;

public class FrameNode extends AbstractInsnNode
{
    public int type;
    public List<Object> local;
    public List<Object> stack;
    
    private FrameNode() {
        super(-1);
    }
    
    public FrameNode(final int type, final int numLocal, final Object[] local, final int numStack, final Object[] stack) {
        super(-1);
        switch (this.type = type) {
            case -1:
            case 0: {
                this.local = Util.asArrayList(numLocal, local);
                this.stack = Util.asArrayList(numStack, stack);
            }
            case 1: {
                this.local = Util.asArrayList(numLocal, local);
            }
            case 2: {
                this.local = Util.asArrayList(numLocal);
            }
            case 3: {
                return;
            }
            case 4: {
                this.stack = Util.asArrayList(1, stack);
                break;
            }
        }
        throw new IllegalArgumentException();
    }
    
    private static Object[] asArray(final List<Object> list) {
        final Object[] array = new Object[list.size()];
        for (int i = 0, n = array.length; i < n; ++i) {
            Object o = list.get(i);
            if (o instanceof LabelNode) {
                o = ((LabelNode)o).getLabel();
            }
            array[i] = o;
        }
        return array;
    }
    
    @Override
    public int getType() {
        return 14;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        switch (this.type) {
            case -1:
            case 0: {
                methodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), this.stack.size(), asArray(this.stack));
                return;
            }
            case 1: {
                methodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), 0, null);
                return;
            }
            case 2: {
                methodVisitor.visitFrame(this.type, this.local.size(), null, 0, null);
                return;
            }
            case 3: {
                methodVisitor.visitFrame(this.type, 0, null, 0, null);
                return;
            }
            case 4: {
                methodVisitor.visitFrame(this.type, 0, null, 1, asArray(this.stack));
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        final FrameNode clone = new FrameNode();
        clone.type = this.type;
        if (this.local != null) {
            clone.local = new ArrayList<Object>();
            for (int i = 0, n = this.local.size(); i < n; ++i) {
                Object localElement = this.local.get(i);
                if (localElement instanceof LabelNode) {
                    localElement = clonedLabels.get(localElement);
                }
                clone.local.add(localElement);
            }
        }
        if (this.stack != null) {
            clone.stack = new ArrayList<Object>();
            for (int i = 0, n = this.stack.size(); i < n; ++i) {
                Object stackElement = this.stack.get(i);
                if (stackElement instanceof LabelNode) {
                    stackElement = clonedLabels.get(stackElement);
                }
                clone.stack.add(stackElement);
            }
        }
        return clone;
    }
}
