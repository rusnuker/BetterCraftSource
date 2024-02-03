// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree;

import java.util.NoSuchElementException;
import java.util.ListIterator;
import org.objectweb.asm.MethodVisitor;

public class InsnList
{
    AbstractInsnNode[] cache;
    private int size;
    private AbstractInsnNode firstInsn;
    private AbstractInsnNode lastInsn;
    
    public int size() {
        return this.size;
    }
    
    public AbstractInsnNode getFirst() {
        return this.firstInsn;
    }
    
    public AbstractInsnNode getLast() {
        return this.lastInsn;
    }
    
    public AbstractInsnNode get(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.cache == null) {
            this.cache = this.toArray();
        }
        return this.cache[index];
    }
    
    public boolean contains(final AbstractInsnNode insnNode) {
        AbstractInsnNode currentInsn;
        for (currentInsn = this.firstInsn; currentInsn != null && currentInsn != insnNode; currentInsn = currentInsn.nextInsn) {}
        return currentInsn != null;
    }
    
    public int indexOf(final AbstractInsnNode insnNode) {
        if (this.cache == null) {
            this.cache = this.toArray();
        }
        return insnNode.index;
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        for (AbstractInsnNode currentInsn = this.firstInsn; currentInsn != null; currentInsn = currentInsn.nextInsn) {
            currentInsn.accept(methodVisitor);
        }
    }
    
    public ListIterator<AbstractInsnNode> iterator() {
        return this.iterator(0);
    }
    
    public ListIterator<AbstractInsnNode> iterator(final int index) {
        return new InsnListIterator(index);
    }
    
    public AbstractInsnNode[] toArray() {
        int currentInsnIndex = 0;
        AbstractInsnNode currentInsn = this.firstInsn;
        final AbstractInsnNode[] insnNodeArray = new AbstractInsnNode[this.size];
        while (currentInsn != null) {
            insnNodeArray[currentInsnIndex] = currentInsn;
            currentInsn.index = currentInsnIndex++;
            currentInsn = currentInsn.nextInsn;
        }
        return insnNodeArray;
    }
    
    public void set(final AbstractInsnNode oldInsnNode, final AbstractInsnNode newInsnNode) {
        final AbstractInsnNode nextInsn = oldInsnNode.nextInsn;
        newInsnNode.nextInsn = nextInsn;
        if (nextInsn != null) {
            nextInsn.previousInsn = newInsnNode;
        }
        else {
            this.lastInsn = newInsnNode;
        }
        final AbstractInsnNode previousInsn = oldInsnNode.previousInsn;
        newInsnNode.previousInsn = previousInsn;
        if (previousInsn != null) {
            previousInsn.nextInsn = newInsnNode;
        }
        else {
            this.firstInsn = newInsnNode;
        }
        if (this.cache != null) {
            final int index = oldInsnNode.index;
            this.cache[index] = newInsnNode;
            newInsnNode.index = index;
        }
        else {
            newInsnNode.index = 0;
        }
        oldInsnNode.index = -1;
        oldInsnNode.previousInsn = null;
        oldInsnNode.nextInsn = null;
    }
    
    public void add(final AbstractInsnNode insnNode) {
        ++this.size;
        if (this.lastInsn == null) {
            this.firstInsn = insnNode;
            this.lastInsn = insnNode;
        }
        else {
            this.lastInsn.nextInsn = insnNode;
            insnNode.previousInsn = this.lastInsn;
        }
        this.lastInsn = insnNode;
        this.cache = null;
        insnNode.index = 0;
    }
    
    public void add(final InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        if (this.lastInsn == null) {
            this.firstInsn = insnList.firstInsn;
            this.lastInsn = insnList.lastInsn;
        }
        else {
            final AbstractInsnNode firstInsnListElement = insnList.firstInsn;
            this.lastInsn.nextInsn = firstInsnListElement;
            firstInsnListElement.previousInsn = this.lastInsn;
            this.lastInsn = insnList.lastInsn;
        }
        this.cache = null;
        insnList.removeAll(false);
    }
    
    public void insert(final AbstractInsnNode insnNode) {
        ++this.size;
        if (this.firstInsn == null) {
            this.firstInsn = insnNode;
            this.lastInsn = insnNode;
        }
        else {
            this.firstInsn.previousInsn = insnNode;
            insnNode.nextInsn = this.firstInsn;
        }
        this.firstInsn = insnNode;
        this.cache = null;
        insnNode.index = 0;
    }
    
    public void insert(final InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        if (this.firstInsn == null) {
            this.firstInsn = insnList.firstInsn;
            this.lastInsn = insnList.lastInsn;
        }
        else {
            final AbstractInsnNode lastInsnListElement = insnList.lastInsn;
            this.firstInsn.previousInsn = lastInsnListElement;
            lastInsnListElement.nextInsn = this.firstInsn;
            this.firstInsn = insnList.firstInsn;
        }
        this.cache = null;
        insnList.removeAll(false);
    }
    
    public void insert(final AbstractInsnNode previousInsn, final AbstractInsnNode insnNode) {
        ++this.size;
        final AbstractInsnNode nextInsn = previousInsn.nextInsn;
        if (nextInsn == null) {
            this.lastInsn = insnNode;
        }
        else {
            nextInsn.previousInsn = insnNode;
        }
        previousInsn.nextInsn = insnNode;
        insnNode.nextInsn = nextInsn;
        insnNode.previousInsn = previousInsn;
        this.cache = null;
        insnNode.index = 0;
    }
    
    public void insert(final AbstractInsnNode previousInsn, final InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        final AbstractInsnNode firstInsnListElement = insnList.firstInsn;
        final AbstractInsnNode lastInsnListElement = insnList.lastInsn;
        final AbstractInsnNode nextInsn = previousInsn.nextInsn;
        if (nextInsn == null) {
            this.lastInsn = lastInsnListElement;
        }
        else {
            nextInsn.previousInsn = lastInsnListElement;
        }
        previousInsn.nextInsn = firstInsnListElement;
        lastInsnListElement.nextInsn = nextInsn;
        firstInsnListElement.previousInsn = previousInsn;
        this.cache = null;
        insnList.removeAll(false);
    }
    
    public void insertBefore(final AbstractInsnNode nextInsn, final AbstractInsnNode insnNode) {
        ++this.size;
        final AbstractInsnNode previousInsn = nextInsn.previousInsn;
        if (previousInsn == null) {
            this.firstInsn = insnNode;
        }
        else {
            previousInsn.nextInsn = insnNode;
        }
        nextInsn.previousInsn = insnNode;
        insnNode.nextInsn = nextInsn;
        insnNode.previousInsn = previousInsn;
        this.cache = null;
        insnNode.index = 0;
    }
    
    public void insertBefore(final AbstractInsnNode nextInsn, final InsnList insnList) {
        if (insnList.size == 0) {
            return;
        }
        this.size += insnList.size;
        final AbstractInsnNode firstInsnListElement = insnList.firstInsn;
        final AbstractInsnNode lastInsnListElement = insnList.lastInsn;
        final AbstractInsnNode previousInsn = nextInsn.previousInsn;
        if (previousInsn == null) {
            this.firstInsn = firstInsnListElement;
        }
        else {
            previousInsn.nextInsn = firstInsnListElement;
        }
        nextInsn.previousInsn = lastInsnListElement;
        lastInsnListElement.nextInsn = nextInsn;
        firstInsnListElement.previousInsn = previousInsn;
        this.cache = null;
        insnList.removeAll(false);
    }
    
    public void remove(final AbstractInsnNode insnNode) {
        --this.size;
        final AbstractInsnNode nextInsn = insnNode.nextInsn;
        final AbstractInsnNode previousInsn = insnNode.previousInsn;
        if (nextInsn == null) {
            if (previousInsn == null) {
                this.firstInsn = null;
                this.lastInsn = null;
            }
            else {
                previousInsn.nextInsn = null;
                this.lastInsn = previousInsn;
            }
        }
        else if (previousInsn == null) {
            this.firstInsn = nextInsn;
            nextInsn.previousInsn = null;
        }
        else {
            previousInsn.nextInsn = nextInsn;
            nextInsn.previousInsn = previousInsn;
        }
        this.cache = null;
        insnNode.index = -1;
        insnNode.previousInsn = null;
        insnNode.nextInsn = null;
    }
    
    void removeAll(final boolean mark) {
        if (mark) {
            AbstractInsnNode next;
            for (AbstractInsnNode currentInsn = this.firstInsn; currentInsn != null; currentInsn = next) {
                next = currentInsn.nextInsn;
                currentInsn.index = -1;
                currentInsn.previousInsn = null;
                currentInsn.nextInsn = null;
            }
        }
        this.size = 0;
        this.firstInsn = null;
        this.lastInsn = null;
        this.cache = null;
    }
    
    public void clear() {
        this.removeAll(false);
    }
    
    public void resetLabels() {
        for (AbstractInsnNode currentInsn = this.firstInsn; currentInsn != null; currentInsn = currentInsn.nextInsn) {
            if (currentInsn instanceof LabelNode) {
                ((LabelNode)currentInsn).resetLabel();
            }
        }
    }
    
    private final class InsnListIterator implements ListIterator
    {
        AbstractInsnNode nextInsn;
        AbstractInsnNode previousInsn;
        AbstractInsnNode remove;
        InsnList this$0;
        
        InsnListIterator(final int index) {
            if (index == this.this$0.size()) {
                this.nextInsn = null;
                this.previousInsn = this.this$0.getLast();
            }
            else {
                this.nextInsn = this.this$0.get(index);
                this.previousInsn = this.nextInsn.previousInsn;
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.nextInsn != null;
        }
        
        @Override
        public Object next() {
            if (this.nextInsn == null) {
                throw new NoSuchElementException();
            }
            final AbstractInsnNode result = this.nextInsn;
            this.previousInsn = result;
            this.nextInsn = result.nextInsn;
            return this.remove = result;
        }
        
        @Override
        public void remove() {
            if (this.remove != null) {
                if (this.remove == this.nextInsn) {
                    this.nextInsn = this.nextInsn.nextInsn;
                }
                else {
                    this.previousInsn = this.previousInsn.previousInsn;
                }
                InsnList.this.remove(this.remove);
                this.remove = null;
                return;
            }
            throw new IllegalStateException();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.previousInsn != null;
        }
        
        @Override
        public Object previous() {
            final AbstractInsnNode result = this.previousInsn;
            this.nextInsn = result;
            this.previousInsn = result.previousInsn;
            return this.remove = result;
        }
        
        @Override
        public int nextIndex() {
            if (this.nextInsn == null) {
                return InsnList.this.size();
            }
            if (InsnList.this.cache == null) {
                InsnList.this.cache = InsnList.this.toArray();
            }
            return this.nextInsn.index;
        }
        
        @Override
        public int previousIndex() {
            if (this.previousInsn == null) {
                return -1;
            }
            if (InsnList.this.cache == null) {
                InsnList.this.cache = InsnList.this.toArray();
            }
            return this.previousInsn.index;
        }
        
        @Override
        public void add(final Object o) {
            if (this.nextInsn != null) {
                InsnList.this.insertBefore(this.nextInsn, (AbstractInsnNode)o);
            }
            else if (this.previousInsn != null) {
                InsnList.this.insert(this.previousInsn, (AbstractInsnNode)o);
            }
            else {
                InsnList.this.add((AbstractInsnNode)o);
            }
            this.previousInsn = (AbstractInsnNode)o;
            this.remove = null;
        }
        
        @Override
        public void set(final Object o) {
            if (this.remove != null) {
                InsnList.this.set(this.remove, (AbstractInsnNode)o);
                if (this.remove == this.previousInsn) {
                    this.previousInsn = (AbstractInsnNode)o;
                }
                else {
                    this.nextInsn = (AbstractInsnNode)o;
                }
                return;
            }
            throw new IllegalStateException();
        }
    }
}
