// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.ListIterator;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointAnnotationContext;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.util.Annotations;
import java.util.Deque;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.LinkedList;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.objectweb.asm.tree.MethodNode;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.logging.ILogger;

public final class MethodSlice
{
    private static final ILogger logger;
    private final ISliceContext owner;
    private final String id;
    private final InjectionPoint from;
    private final InjectionPoint to;
    private final String name;
    
    private MethodSlice(final ISliceContext owner, final String id, final InjectionPoint from, final InjectionPoint to) {
        if (from == null && to == null) {
            throw new InvalidSliceException(owner, String.format("%s is redundant. No 'from' or 'to' value specified", this));
        }
        this.owner = owner;
        this.id = Strings.nullToEmpty(id);
        this.from = from;
        this.to = to;
        this.name = getSliceName(id);
    }
    
    public String getId() {
        return this.id;
    }
    
    public InsnListReadOnly getSlice(final MethodNode method) {
        final int max = method.instructions.size() - 1;
        final int start = this.find(method, this.from, 0, 0, this.name + "(from)");
        final int end = this.find(method, this.to, max, start, this.name + "(to)");
        if (start > end) {
            throw new InvalidSliceException(this.owner, String.format("%s is negative size. Range(%d -> %d)", this.describe(), start, end));
        }
        if (start < 0 || end < 0 || start > max || end > max) {
            throw new InjectionError("Unexpected critical error in " + this + ": out of bounds start=" + start + " end=" + end + " lim=" + max);
        }
        if (start == 0 && end == max) {
            return new InsnListReadOnly(method.instructions);
        }
        return new InsnListSlice(method.instructions, start, end);
    }
    
    private int find(final MethodNode method, final InjectionPoint injectionPoint, final int defaultValue, final int failValue, final String description) {
        if (injectionPoint == null) {
            return defaultValue;
        }
        final Deque<AbstractInsnNode> nodes = new LinkedList<AbstractInsnNode>();
        final InsnListReadOnly insns = new InsnListReadOnly(method.instructions);
        final boolean result = injectionPoint.find(method.desc, insns, nodes);
        final InjectionPoint.Selector select = injectionPoint.getSelector();
        if (nodes.size() != 1 && select == InjectionPoint.Selector.ONE) {
            throw new InvalidSliceException(this.owner, String.format("%s requires 1 result but found %d", this.describe(description), nodes.size()));
        }
        if (!result) {
            if (this.owner.getMixin().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                MethodSlice.logger.warn("{} did not match any instructions", this.describe(description));
            }
            return failValue;
        }
        return method.instructions.indexOf((select == InjectionPoint.Selector.FIRST) ? nodes.getFirst() : nodes.getLast());
    }
    
    @Override
    public String toString() {
        return this.describe();
    }
    
    private String describe() {
        return this.describe(this.name);
    }
    
    private String describe(final String description) {
        return describeSlice(description, this.owner);
    }
    
    private static String describeSlice(final String description, final ISliceContext owner) {
        final String annotation = Annotations.getSimpleName(owner.getAnnotationNode());
        final MethodNode method = owner.getMethod();
        return String.format("%s->%s(%s)::%s%s", owner.getMixin(), annotation, description, method.name, method.desc);
    }
    
    private static String getSliceName(final String id) {
        return String.format("@Slice[%s]", Strings.nullToEmpty(id));
    }
    
    public static MethodSlice parse(final ISliceContext owner, final Slice slice) {
        final String id = slice.id();
        final At from = slice.from();
        final At to = slice.to();
        final InjectionPoint fromPoint = (from != null) ? InjectionPoint.parse(owner, from) : null;
        final InjectionPoint toPoint = (to != null) ? InjectionPoint.parse(owner, to) : null;
        return new MethodSlice(owner, id, fromPoint, toPoint);
    }
    
    public static MethodSlice parse(final ISliceContext info, final AnnotationNode node) {
        final String id = Annotations.getValue(node, "id");
        String coord = "slice";
        if (!Strings.isNullOrEmpty(id)) {
            coord = coord + "." + id;
        }
        final InjectionPointAnnotationContext sliceContext = new InjectionPointAnnotationContext(info, node, coord);
        final AnnotationNode from = Annotations.getValue(node, "from");
        final AnnotationNode to = Annotations.getValue(node, "to");
        final InjectionPoint fromPoint = (from != null) ? InjectionPoint.parse(new InjectionPointAnnotationContext(sliceContext, from, "from"), from) : null;
        final InjectionPoint toPoint = (to != null) ? InjectionPoint.parse(new InjectionPointAnnotationContext(sliceContext, to, "to"), to) : null;
        return new MethodSlice(info, id, fromPoint, toPoint);
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
    
    static final class InsnListSlice extends InsnListReadOnly
    {
        private final int start;
        private final int end;
        
        protected InsnListSlice(final InsnList inner, final int start, final int end) {
            super(inner);
            this.start = start;
            this.end = end;
        }
        
        @Override
        public ListIterator<AbstractInsnNode> iterator() {
            return this.iterator(0);
        }
        
        @Override
        public ListIterator<AbstractInsnNode> iterator(final int index) {
            return new SliceIterator(super.iterator(this.start + index), this.start, this.end, this.start + index);
        }
        
        @Override
        public AbstractInsnNode[] toArray() {
            final AbstractInsnNode[] all = super.toArray();
            final AbstractInsnNode[] subset = new AbstractInsnNode[this.size()];
            System.arraycopy(all, this.start, subset, 0, subset.length);
            return subset;
        }
        
        @Override
        public int size() {
            return this.end - this.start + 1;
        }
        
        @Override
        public AbstractInsnNode getFirst() {
            return super.get(this.start);
        }
        
        @Override
        public AbstractInsnNode getLast() {
            return super.get(this.end);
        }
        
        @Override
        public AbstractInsnNode get(final int index) {
            return super.get(this.start + index);
        }
        
        @Override
        public boolean contains(final AbstractInsnNode insn) {
            for (final AbstractInsnNode node : this.toArray()) {
                if (node == insn) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int indexOf(final AbstractInsnNode insn) {
            final int index = super.indexOf(insn);
            return (index >= this.start && index <= this.end) ? (index - this.start) : -1;
        }
        
        public int realIndexOf(final AbstractInsnNode insn) {
            return super.indexOf(insn);
        }
        
        static class SliceIterator implements ListIterator<AbstractInsnNode>
        {
            private final ListIterator<AbstractInsnNode> iter;
            private int start;
            private int end;
            private int index;
            
            public SliceIterator(final ListIterator<AbstractInsnNode> iter, final int start, final int end, final int index) {
                this.iter = iter;
                this.start = start;
                this.end = end;
                this.index = index;
            }
            
            @Override
            public boolean hasNext() {
                return this.index <= this.end && this.iter.hasNext();
            }
            
            @Override
            public AbstractInsnNode next() {
                if (this.index > this.end) {
                    throw new NoSuchElementException();
                }
                ++this.index;
                return this.iter.next();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.index > this.start;
            }
            
            @Override
            public AbstractInsnNode previous() {
                if (this.index <= this.start) {
                    throw new NoSuchElementException();
                }
                --this.index;
                return this.iter.previous();
            }
            
            @Override
            public int nextIndex() {
                return this.index - this.start;
            }
            
            @Override
            public int previousIndex() {
                return this.index - this.start - 1;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Cannot remove insn from slice");
            }
            
            @Override
            public void set(final AbstractInsnNode e) {
                throw new UnsupportedOperationException("Cannot set insn using slice");
            }
            
            @Override
            public void add(final AbstractInsnNode e) {
                throw new UnsupportedOperationException("Cannot add insn using slice");
            }
        }
    }
}
