// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.util.Locals;
import org.objectweb.asm.tree.LocalVariableNode;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.LabelNode;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AbstractInsnNode;

public class Target implements Comparable<Target>, Iterable<AbstractInsnNode>
{
    public final ClassNode classNode;
    public final MethodNode method;
    public final InsnList insns;
    public final boolean isStatic;
    public final boolean isCtor;
    public final Type[] arguments;
    public final Type returnType;
    private final int maxStack;
    private final int maxLocals;
    private final InjectionNodes injectionNodes;
    private String callbackInfoClass;
    private String callbackDescriptor;
    private int[] argIndices;
    private List<Integer> argMapVars;
    private LabelNode start;
    private LabelNode end;
    private Bytecode.DelegateInitialiser delegateInitialiser;
    
    public Target(final ClassNode classNode, final MethodNode method) {
        this.injectionNodes = new InjectionNodes();
        this.classNode = classNode;
        this.method = method;
        this.insns = method.instructions;
        this.isStatic = Bytecode.isStatic(method);
        this.isCtor = method.name.equals("<init>");
        this.arguments = Type.getArgumentTypes(method.desc);
        this.returnType = Type.getReturnType(method.desc);
        this.maxStack = method.maxStack;
        this.maxLocals = method.maxLocals;
    }
    
    public InjectionNodes.InjectionNode addInjectionNode(final AbstractInsnNode node) {
        return this.injectionNodes.add(node);
    }
    
    public InjectionNodes.InjectionNode getInjectionNode(final AbstractInsnNode node) {
        return this.injectionNodes.get(node);
    }
    
    public int getMaxLocals() {
        return this.maxLocals;
    }
    
    public int getMaxStack() {
        return this.maxStack;
    }
    
    public int getCurrentMaxLocals() {
        return this.method.maxLocals;
    }
    
    public int getCurrentMaxStack() {
        return this.method.maxStack;
    }
    
    public int allocateLocal() {
        return this.allocateLocals(1);
    }
    
    public int allocateLocals(final int locals) {
        final int nextLocal = this.method.maxLocals;
        final MethodNode method = this.method;
        method.maxLocals += locals;
        return nextLocal;
    }
    
    public Extension extendLocals() {
        return new Extension(true);
    }
    
    public Extension extendStack() {
        return new Extension(false);
    }
    
    void extendLocalsBy(final int locals) {
        this.setMaxLocals(this.maxLocals + locals);
    }
    
    private void setMaxLocals(final int maxLocals) {
        if (maxLocals > this.method.maxLocals) {
            this.method.maxLocals = maxLocals;
        }
    }
    
    void extendStackBy(final int stack) {
        this.setMaxStack(this.maxStack + stack);
    }
    
    private void setMaxStack(final int maxStack) {
        if (maxStack > this.method.maxStack) {
            this.method.maxStack = maxStack;
        }
    }
    
    public int[] generateArgMap(final Type[] args, final int start) {
        if (this.argMapVars == null) {
            this.argMapVars = new ArrayList<Integer>();
        }
        final int[] argMap = new int[args.length];
        int arg = start;
        int index = 0;
        while (arg < args.length) {
            final int size = args[arg].getSize();
            argMap[arg] = this.allocateArgMapLocal(index, size);
            index += size;
            ++arg;
        }
        return argMap;
    }
    
    private int allocateArgMapLocal(final int index, final int size) {
        if (index >= this.argMapVars.size()) {
            final int base = this.allocateLocals(size);
            for (int offset = 0; offset < size; ++offset) {
                this.argMapVars.add(base + offset);
            }
            return base;
        }
        final int local = this.argMapVars.get(index);
        if (size <= 1 || index + size <= this.argMapVars.size()) {
            return local;
        }
        final int nextLocal = this.allocateLocals(1);
        if (nextLocal == local + 1) {
            this.argMapVars.add(nextLocal);
            return local;
        }
        this.argMapVars.set(index, nextLocal);
        this.argMapVars.add(this.allocateLocals(1));
        return nextLocal;
    }
    
    public int[] getArgIndices() {
        if (this.argIndices == null) {
            this.argIndices = this.calcArgIndices(this.isStatic ? 0 : 1);
        }
        return this.argIndices;
    }
    
    private int[] calcArgIndices(int local) {
        final int[] argIndices = new int[this.arguments.length];
        for (int arg = 0; arg < this.arguments.length; ++arg) {
            argIndices[arg] = local;
            local += this.arguments[arg].getSize();
        }
        return argIndices;
    }
    
    public String getCallbackInfoClass() {
        if (this.callbackInfoClass == null) {
            this.callbackInfoClass = CallbackInfo.getCallInfoClassName(this.returnType);
        }
        return this.callbackInfoClass;
    }
    
    public String getSimpleCallbackDescriptor() {
        return String.format("(L%s;)V", this.getCallbackInfoClass());
    }
    
    public String getCallbackDescriptor(final Type[] locals, final Type[] argumentTypes) {
        return this.getCallbackDescriptor(false, locals, argumentTypes, 0, 32767);
    }
    
    public String getCallbackDescriptor(final boolean captureLocals, final Type[] locals, final Type[] argumentTypes, final int startIndex, int extra) {
        if (this.callbackDescriptor == null) {
            this.callbackDescriptor = String.format("(%sL%s;)V", this.method.desc.substring(1, this.method.desc.indexOf(41)), this.getCallbackInfoClass());
        }
        if (!captureLocals || locals == null) {
            return this.callbackDescriptor;
        }
        final StringBuilder descriptor = new StringBuilder(this.callbackDescriptor.substring(0, this.callbackDescriptor.indexOf(41)));
        for (int l = startIndex; l < locals.length && extra > 0; ++l) {
            if (locals[l] != null) {
                descriptor.append(locals[l].getDescriptor());
                --extra;
            }
        }
        return descriptor.append(")V").toString();
    }
    
    @Override
    public String toString() {
        return String.format("%s::%s%s", this.classNode.name, this.method.name, this.method.desc);
    }
    
    @Override
    public int compareTo(final Target o) {
        if (o == null) {
            return Integer.MAX_VALUE;
        }
        return this.toString().compareTo(o.toString());
    }
    
    public int indexOf(final InjectionNodes.InjectionNode node) {
        return this.insns.indexOf(node.getCurrentTarget());
    }
    
    public int indexOf(final AbstractInsnNode insn) {
        return this.insns.indexOf(insn);
    }
    
    public AbstractInsnNode get(final int index) {
        return this.insns.get(index);
    }
    
    @Override
    public Iterator<AbstractInsnNode> iterator() {
        return this.insns.iterator();
    }
    
    public MethodInsnNode findInitNodeFor(final TypeInsnNode newNode) {
        final int start = this.indexOf(newNode);
        final Iterator<AbstractInsnNode> iter = this.insns.iterator(start);
        while (iter.hasNext()) {
            final AbstractInsnNode insn = iter.next();
            if (insn instanceof MethodInsnNode && insn.getOpcode() == 183) {
                final MethodInsnNode methodNode = (MethodInsnNode)insn;
                if ("<init>".equals(methodNode.name) && methodNode.owner.equals(newNode.desc)) {
                    return methodNode;
                }
                continue;
            }
        }
        return null;
    }
    
    public Bytecode.DelegateInitialiser findDelegateInitNode() {
        if (!this.isCtor) {
            return null;
        }
        if (this.delegateInitialiser == null) {
            final String superName = ClassInfo.forName(this.classNode.name).getSuperName();
            this.delegateInitialiser = Bytecode.findDelegateInit(this.method, superName, this.classNode.name);
        }
        return this.delegateInitialiser;
    }
    
    public void insertBefore(final InjectionNodes.InjectionNode location, final InsnList insns) {
        this.insns.insertBefore(location.getCurrentTarget(), insns);
    }
    
    public void insertBefore(final AbstractInsnNode location, final InsnList insns) {
        this.insns.insertBefore(location, insns);
    }
    
    public void replaceNode(final AbstractInsnNode location, final AbstractInsnNode insn) {
        this.insns.insertBefore(location, insn);
        this.insns.remove(location);
        this.injectionNodes.replace(location, insn);
    }
    
    public void replaceNode(final AbstractInsnNode location, final AbstractInsnNode champion, final InsnList insns) {
        this.insns.insertBefore(location, insns);
        this.insns.remove(location);
        this.injectionNodes.replace(location, champion);
    }
    
    public void wrapNode(final AbstractInsnNode location, final AbstractInsnNode champion, final InsnList before, final InsnList after) {
        this.insns.insertBefore(location, before);
        this.insns.insert(location, after);
        this.injectionNodes.replace(location, champion);
    }
    
    public void replaceNode(final AbstractInsnNode location, final InsnList insns) {
        this.insns.insertBefore(location, insns);
        this.removeNode(location);
    }
    
    public void removeNode(final AbstractInsnNode insn) {
        this.insns.remove(insn);
        this.injectionNodes.remove(insn);
    }
    
    public void addLocalVariable(final int index, final String name, final String desc) {
        this.addLocalVariable(index, name, desc, null, null);
    }
    
    public void addLocalVariable(final int index, final String name, final String desc, LabelNode from, LabelNode to) {
        if (from == null) {
            from = this.getStartLabel();
        }
        if (to == null) {
            to = this.getEndLabel();
        }
        if (this.method.localVariables == null) {
            this.method.localVariables = new ArrayList<LocalVariableNode>();
        }
        final Iterator<LocalVariableNode> iter = this.method.localVariables.iterator();
        while (iter.hasNext()) {
            final LocalVariableNode local = iter.next();
            if (local != null && local.index == index && from == local.start && to == local.end) {
                iter.remove();
            }
        }
        this.method.localVariables.add(new Locals.SyntheticLocalVariableNode(name, desc, null, from, to, index));
    }
    
    private LabelNode getStartLabel() {
        if (this.start == null) {
            this.insns.insert(this.start = new LabelNode());
        }
        return this.start;
    }
    
    private LabelNode getEndLabel() {
        if (this.end == null) {
            this.insns.add(this.end = new LabelNode());
        }
        return this.end;
    }
    
    public class Extension
    {
        private final boolean locals;
        private int size;
        
        Extension(final boolean locals) {
            this.locals = locals;
        }
        
        public Extension add() {
            ++this.size;
            return this;
        }
        
        public Extension add(final int size) {
            this.size += size;
            return this;
        }
        
        public Extension add(final Type[] types) {
            return this.add(Bytecode.getArgsSize(types));
        }
        
        public Extension set(final int size) {
            this.size = size;
            return this;
        }
        
        public int get() {
            return this.size;
        }
        
        public void apply() {
            if (this.locals) {
                Target.this.extendLocalsBy(this.size);
            }
            else {
                Target.this.extendStackBy(this.size);
            }
            this.size = 0;
        }
    }
}
