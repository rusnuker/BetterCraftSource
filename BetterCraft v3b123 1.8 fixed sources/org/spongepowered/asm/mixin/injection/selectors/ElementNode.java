// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

import org.spongepowered.asm.util.Handles;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.ClassNode;
import com.google.common.base.Strings;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class ElementNode<TNode>
{
    public boolean isField() {
        return false;
    }
    
    public abstract NodeType getType();
    
    public MethodNode getMethod() {
        return null;
    }
    
    public FieldNode getField() {
        return null;
    }
    
    public AbstractInsnNode getInsn() {
        return null;
    }
    
    public abstract String getOwner();
    
    public abstract String getName();
    
    public String getSyntheticName() {
        return this.getName();
    }
    
    public abstract String getDesc();
    
    public String getDelegateDesc() {
        return this.getDesc();
    }
    
    public String getImplDesc() {
        return this.getDesc();
    }
    
    public abstract String getSignature();
    
    public abstract TNode get();
    
    @Override
    public String toString() {
        String desc = Strings.nullToEmpty(this.getDesc());
        if (!desc.isEmpty() && this.isField()) {
            desc = ":" + desc;
        }
        String owner = Strings.nullToEmpty(this.getOwner());
        if (!owner.isEmpty()) {
            owner = "L" + owner + ";";
        }
        return String.format("%s%s%s", owner, Strings.nullToEmpty(this.getName()), desc);
    }
    
    public static ElementNode<MethodNode> of(final ClassNode owner, final MethodNode method) {
        return new ElementNodeMethod(owner, method);
    }
    
    public static ElementNode<FieldNode> of(final ClassNode owner, final FieldNode field) {
        return new ElementNodeField(owner, field);
    }
    
    public static <TNode> ElementNode<TNode> of(final ClassNode owner, final TNode node) {
        if (node instanceof ElementNode) {
            return (ElementNode)node;
        }
        if (node instanceof MethodNode) {
            return (ElementNode<TNode>)new ElementNodeMethod(owner, (MethodNode)node);
        }
        if (node instanceof FieldNode) {
            return (ElementNode<TNode>)new ElementNodeField(owner, (FieldNode)node);
        }
        if (node instanceof MethodInsnNode) {
            return (ElementNode<TNode>)new ElementNodeMethodInsn((MethodInsnNode)node);
        }
        if (node instanceof InvokeDynamicInsnNode) {
            return (ElementNode<TNode>)new ElementNodeInvokeDynamicInsn((InvokeDynamicInsnNode)node);
        }
        if (node instanceof FieldInsnNode) {
            return (ElementNode<TNode>)new ElementNodeFieldInsn((FieldInsnNode)node);
        }
        throw new IllegalArgumentException("Could not create ElementNode for unknown node type: " + node.getClass().getName());
    }
    
    public static <TNode extends AbstractInsnNode> ElementNode<TNode> of(final TNode node) {
        if (node instanceof MethodInsnNode) {
            return (ElementNode<TNode>)new ElementNodeMethodInsn((MethodInsnNode)node);
        }
        if (node instanceof InvokeDynamicInsnNode) {
            return (ElementNode<TNode>)new ElementNodeInvokeDynamicInsn((InvokeDynamicInsnNode)node);
        }
        if (node instanceof FieldInsnNode) {
            return (ElementNode<TNode>)new ElementNodeFieldInsn((FieldInsnNode)node);
        }
        return null;
    }
    
    public static <TNode> List<ElementNode<TNode>> listOf(final ClassNode owner, final List<TNode> list) {
        final List<ElementNode<TNode>> nodes = new ArrayList<ElementNode<TNode>>();
        for (final TNode node : list) {
            nodes.add(of(owner, node));
        }
        return nodes;
    }
    
    public static List<ElementNode<FieldNode>> fieldList(final ClassNode owner) {
        final List<ElementNode<FieldNode>> fields = new ArrayList<ElementNode<FieldNode>>();
        for (final FieldNode field : owner.fields) {
            fields.add(new ElementNodeField(owner, field));
        }
        return fields;
    }
    
    public static List<ElementNode<MethodNode>> methodList(final ClassNode owner) {
        final List<ElementNode<MethodNode>> methods = new ArrayList<ElementNode<MethodNode>>();
        for (final MethodNode method : owner.methods) {
            methods.add(new ElementNodeMethod(owner, method));
        }
        return methods;
    }
    
    public static Iterable<ElementNode<AbstractInsnNode>> insnList(final InsnList insns) {
        return new ElementNodeIterable((Iterable<AbstractInsnNode>)insns, false);
    }
    
    public static Iterable<ElementNode<AbstractInsnNode>> dynamicInsnList(final InsnList insns) {
        return new ElementNodeIterable((Iterable<AbstractInsnNode>)insns, true);
    }
    
    public enum NodeType
    {
        UNDEFINED(false, false, false), 
        METHOD(true, false, false), 
        FIELD(false, true, false), 
        METHOD_INSN(false, false, true), 
        FIELD_INSN(false, false, true), 
        INVOKEDYNAMIC_INSN(false, false, true);
        
        public final boolean hasMethod;
        public final boolean hasField;
        public final boolean hasInsn;
        
        private NodeType(final boolean isMethod, final boolean isField, final boolean isInsn) {
            this.hasMethod = isMethod;
            this.hasField = isField;
            this.hasInsn = isInsn;
        }
    }
    
    static class ElementNodeMethod extends ElementNode<MethodNode>
    {
        private final ClassNode owner;
        private final MethodNode method;
        
        ElementNodeMethod(final ClassNode owner, final MethodNode method) {
            this.owner = owner;
            this.method = method;
        }
        
        @Override
        public NodeType getType() {
            return NodeType.METHOD;
        }
        
        @Override
        public MethodNode getMethod() {
            return this.method;
        }
        
        @Override
        public String getOwner() {
            return (this.owner != null) ? this.owner.name : null;
        }
        
        @Override
        public String getName() {
            return this.method.name;
        }
        
        @Override
        public String getDesc() {
            return this.method.desc;
        }
        
        @Override
        public String getSignature() {
            return this.method.signature;
        }
        
        @Override
        public MethodNode get() {
            return this.method;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this.method.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.method.hashCode();
        }
    }
    
    static class ElementNodeField extends ElementNode<FieldNode>
    {
        private final ClassNode owner;
        private final FieldNode field;
        
        ElementNodeField(final ClassNode owner, final FieldNode field) {
            this.owner = owner;
            this.field = field;
        }
        
        @Override
        public NodeType getType() {
            return NodeType.FIELD;
        }
        
        @Override
        public boolean isField() {
            return true;
        }
        
        @Override
        public FieldNode getField() {
            return this.field;
        }
        
        @Override
        public String getOwner() {
            return (this.owner != null) ? this.owner.name : null;
        }
        
        @Override
        public String getName() {
            return this.field.name;
        }
        
        @Override
        public String getDesc() {
            return this.field.desc;
        }
        
        @Override
        public String getSignature() {
            return this.field.signature;
        }
        
        @Override
        public FieldNode get() {
            return this.field;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this.field.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.field.hashCode();
        }
    }
    
    static class ElementNodeMethodInsn extends ElementNode<MethodInsnNode>
    {
        private MethodInsnNode insn;
        
        ElementNodeMethodInsn(final MethodInsnNode method) {
            this.insn = method;
        }
        
        @Override
        public NodeType getType() {
            return NodeType.METHOD_INSN;
        }
        
        @Override
        public AbstractInsnNode getInsn() {
            return this.insn;
        }
        
        @Override
        public String getOwner() {
            return this.insn.owner;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        @Override
        public String getDesc() {
            return this.insn.desc;
        }
        
        @Override
        public String getSignature() {
            return null;
        }
        
        @Override
        public MethodInsnNode get() {
            return this.insn;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this.insn.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.insn.hashCode();
        }
    }
    
    static class ElementNodeInvokeDynamicInsn extends ElementNode<InvokeDynamicInsnNode>
    {
        private InvokeDynamicInsnNode insn;
        private Type samMethodType;
        private Handle implMethod;
        private Type instantiatedMethodType;
        
        ElementNodeInvokeDynamicInsn(final InvokeDynamicInsnNode invokeDynamic) {
            this.insn = invokeDynamic;
            if (invokeDynamic.bsmArgs != null && invokeDynamic.bsmArgs.length > 1) {
                final Object samMethodType = invokeDynamic.bsmArgs[0];
                final Object implMethod = invokeDynamic.bsmArgs[1];
                final Object instantiatedMethodType = invokeDynamic.bsmArgs[2];
                if (samMethodType instanceof Type && implMethod instanceof Handle && instantiatedMethodType instanceof Type) {
                    this.samMethodType = (Type)samMethodType;
                    this.implMethod = (Handle)implMethod;
                    this.instantiatedMethodType = (Type)instantiatedMethodType;
                }
            }
        }
        
        @Override
        public NodeType getType() {
            return NodeType.INVOKEDYNAMIC_INSN;
        }
        
        @Override
        public boolean isField() {
            return this.implMethod != null && Handles.isField(this.implMethod);
        }
        
        @Override
        public AbstractInsnNode getInsn() {
            return this.insn;
        }
        
        @Override
        public String getOwner() {
            return (this.implMethod != null) ? this.implMethod.getOwner() : this.insn.name;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        @Override
        public String getSyntheticName() {
            return (this.implMethod != null) ? this.implMethod.getName() : this.insn.name;
        }
        
        @Override
        public String getDesc() {
            return (this.implMethod != null) ? this.implMethod.getDesc() : this.insn.desc;
        }
        
        @Override
        public String getDelegateDesc() {
            return (this.samMethodType != null) ? this.samMethodType.getDescriptor() : this.getDesc();
        }
        
        @Override
        public String getImplDesc() {
            return (this.instantiatedMethodType != null) ? this.instantiatedMethodType.getDescriptor() : this.getDesc();
        }
        
        @Override
        public String getSignature() {
            return null;
        }
        
        @Override
        public InvokeDynamicInsnNode get() {
            return this.insn;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this.insn.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.insn.hashCode();
        }
    }
    
    static class ElementNodeFieldInsn extends ElementNode<FieldInsnNode>
    {
        private FieldInsnNode insn;
        
        ElementNodeFieldInsn(final FieldInsnNode field) {
            this.insn = field;
        }
        
        @Override
        public NodeType getType() {
            return NodeType.FIELD_INSN;
        }
        
        @Override
        public boolean isField() {
            return true;
        }
        
        @Override
        public AbstractInsnNode getInsn() {
            return this.insn;
        }
        
        @Override
        public String getOwner() {
            return this.insn.owner;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        @Override
        public String getDesc() {
            return this.insn.desc;
        }
        
        @Override
        public String getSignature() {
            return null;
        }
        
        @Override
        public FieldInsnNode get() {
            return this.insn;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this.insn.equals(obj);
        }
        
        @Override
        public int hashCode() {
            return this.insn.hashCode();
        }
    }
    
    static class ElementNodeIterator implements Iterator<ElementNode<AbstractInsnNode>>
    {
        private final Iterator<AbstractInsnNode> iter;
        private final boolean filterDynamic;
        
        ElementNodeIterator(final Iterator<AbstractInsnNode> iter, final boolean filterDynamic) {
            this.iter = iter;
            this.filterDynamic = filterDynamic;
        }
        
        @Override
        public boolean hasNext() {
            return this.iter.hasNext();
        }
        
        @Override
        public ElementNode<AbstractInsnNode> next() {
            final AbstractInsnNode elem = this.iter.next();
            return (!this.filterDynamic || (elem != null && elem.getOpcode() == 186)) ? ElementNode.of(elem) : null;
        }
    }
    
    static class ElementNodeIterable implements Iterable<ElementNode<AbstractInsnNode>>
    {
        private final Iterable<AbstractInsnNode> iterable;
        private final boolean filterDynamic;
        
        public ElementNodeIterable(final Iterable<AbstractInsnNode> iterable, final boolean filterDynamic) {
            this.iterable = iterable;
            this.filterDynamic = filterDynamic;
        }
        
        @Override
        public Iterator<ElementNode<AbstractInsnNode>> iterator() {
            return new ElementNodeIterator(this.iterable.iterator(), this.filterDynamic);
        }
    }
}
