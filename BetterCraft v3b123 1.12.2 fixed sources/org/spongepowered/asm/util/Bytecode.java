// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.util.asm.ASM;
import java.util.ListIterator;
import org.spongepowered.asm.util.throwables.SyntheticBridgeException;
import com.google.common.primitives.Ints;
import com.google.common.base.Joiner;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import java.lang.reflect.Field;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;
import java.io.PrintWriter;
import java.io.OutputStream;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Iterator;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;

public final class Bytecode
{
    public static final int[] CONSTANTS_INT;
    public static final int[] CONSTANTS_FLOAT;
    public static final int[] CONSTANTS_DOUBLE;
    public static final int[] CONSTANTS_LONG;
    public static final int[] CONSTANTS_ALL;
    private static final Object[] CONSTANTS_VALUES;
    private static final String[] CONSTANTS_TYPES;
    private static final String[] BOXING_TYPES;
    private static final String[] UNBOXING_METHODS;
    
    private Bytecode() {
    }
    
    public static MethodNode findMethod(final ClassNode classNode, final String name, final String desc) {
        for (final MethodNode method : classNode.methods) {
            if (method.name.equals(name) && method.desc.equals(desc)) {
                return method;
            }
        }
        return null;
    }
    
    public static AbstractInsnNode findInsn(final MethodNode method, final int opcode) {
        for (final AbstractInsnNode insn : method.instructions) {
            if (insn.getOpcode() == opcode) {
                return insn;
            }
        }
        return null;
    }
    
    public static DelegateInitialiser findDelegateInit(final MethodNode ctor, final String superName, final String ownerName) {
        if (!"<init>".equals(ctor.name)) {
            return DelegateInitialiser.NONE;
        }
        int news = 0;
        for (final AbstractInsnNode insn : ctor.instructions) {
            if (insn instanceof TypeInsnNode && insn.getOpcode() == 187) {
                ++news;
            }
            else {
                if (!(insn instanceof MethodInsnNode) || insn.getOpcode() != 183) {
                    continue;
                }
                final MethodInsnNode methodNode = (MethodInsnNode)insn;
                if (!"<init>".equals(methodNode.name)) {
                    continue;
                }
                if (news > 0) {
                    --news;
                }
                else {
                    final boolean isSuper = methodNode.owner.equals(superName);
                    if (isSuper || methodNode.owner.equals(ownerName)) {
                        return new DelegateInitialiser(methodNode, isSuper);
                    }
                    continue;
                }
            }
        }
        return DelegateInitialiser.NONE;
    }
    
    public static void textify(final ClassNode classNode, final OutputStream out) {
        classNode.accept(new TraceClassVisitor(new PrintWriter(out)));
    }
    
    public static void textify(final MethodNode methodNode, final OutputStream out) {
        final TraceClassVisitor trace = new TraceClassVisitor(new PrintWriter(out));
        final MethodVisitor mv = trace.visitMethod(methodNode.access, methodNode.name, methodNode.desc, methodNode.signature, methodNode.exceptions.toArray(new String[0]));
        methodNode.accept(mv);
        trace.visitEnd();
    }
    
    public static void dumpClass(final ClassNode classNode) {
        final ClassWriter cw = new ClassWriter(3);
        classNode.accept(cw);
        dumpClass(cw.toByteArray());
    }
    
    public static void dumpClass(final byte[] bytes) {
        final ClassReader cr = new ClassReader(bytes);
        CheckClassAdapter.verify(cr, true, new PrintWriter(System.out));
    }
    
    public static void printMethodWithOpcodeIndices(final MethodNode method) {
        System.err.printf("%s%s\n", method.name, method.desc);
        int i = 0;
        final Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            System.err.printf("[%4d] %s\n", i++, describeNode(iter.next()));
        }
    }
    
    public static void printMethod(final MethodNode method) {
        System.err.printf("%s%s maxStack=%d maxLocals=%d\n", method.name, method.desc, method.maxStack, method.maxLocals);
        int index = 0;
        final Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            System.err.printf("%-4d  ", index++);
            printNode(iter.next());
        }
    }
    
    public static void printNode(final AbstractInsnNode node) {
        System.err.printf("%s\n", describeNode(node));
    }
    
    public static String describeNode(final AbstractInsnNode node) {
        return describeNode(node, true);
    }
    
    public static String describeNode(final AbstractInsnNode node, final boolean listFormat) {
        if (node == null) {
            return listFormat ? String.format("   %-14s ", "null") : "null";
        }
        if (node instanceof LabelNode) {
            return String.format("[%s]", ((LabelNode)node).getLabel());
        }
        String out = String.format(listFormat ? "   %-14s " : "%s ", node.getClass().getSimpleName().replace("Node", ""));
        if (node instanceof JumpInsnNode) {
            out += String.format("[%s] [%s]", getOpcodeName(node), ((JumpInsnNode)node).label.getLabel());
        }
        else if (node instanceof VarInsnNode) {
            out += String.format("[%s] %d", getOpcodeName(node), ((VarInsnNode)node).var);
        }
        else if (node instanceof MethodInsnNode) {
            final MethodInsnNode mth = (MethodInsnNode)node;
            out += String.format("[%s] %s::%s%s", getOpcodeName(node), mth.owner, mth.name, mth.desc);
        }
        else if (node instanceof FieldInsnNode) {
            final FieldInsnNode fld = (FieldInsnNode)node;
            out += String.format("[%s] %s::%s:%s", getOpcodeName(node), fld.owner, fld.name, fld.desc);
        }
        else if (node instanceof InvokeDynamicInsnNode) {
            final InvokeDynamicInsnNode idc = (InvokeDynamicInsnNode)node;
            out += String.format("[%s] %s%s { %s %s::%s%s }", getOpcodeName(node), idc.name, idc.desc, getOpcodeName(idc.bsm.getTag(), "H_GETFIELD", 1), idc.bsm.getOwner(), idc.bsm.getName(), idc.bsm.getDesc());
        }
        else if (node instanceof LineNumberNode) {
            final LineNumberNode ln = (LineNumberNode)node;
            out += String.format("LINE=[%d] LABEL=[%s]", ln.line, ln.start.getLabel());
        }
        else if (node instanceof LdcInsnNode) {
            out += ((LdcInsnNode)node).cst;
        }
        else if (node instanceof IntInsnNode) {
            out += ((IntInsnNode)node).operand;
        }
        else if (node instanceof FrameNode) {
            out += String.format("[%s] ", getOpcodeName(((FrameNode)node).type, "H_INVOKEINTERFACE", -1));
        }
        else if (node instanceof TypeInsnNode) {
            out += String.format("[%s] %s", getOpcodeName(node), ((TypeInsnNode)node).desc);
        }
        else {
            out += String.format("[%s] ", getOpcodeName(node));
        }
        return out;
    }
    
    public static String getOpcodeName(final AbstractInsnNode node) {
        return (node != null) ? getOpcodeName(node.getOpcode()) : "";
    }
    
    public static String getOpcodeName(final int opcode) {
        return getOpcodeName(opcode, "UNINITIALIZED_THIS", 1);
    }
    
    private static String getOpcodeName(final int opcode, final String start, final int min) {
        if (opcode >= min) {
            boolean found = false;
            try {
                for (final Field f : Opcodes.class.getDeclaredFields()) {
                    if (found || f.getName().equals(start)) {
                        found = true;
                        if (f.getType() == Integer.TYPE && f.getInt(null) == opcode) {
                            return f.getName();
                        }
                    }
                }
            }
            catch (final Exception ex) {}
        }
        return (opcode >= 0) ? String.valueOf(opcode) : "UNKNOWN";
    }
    
    public static boolean methodHasLineNumbers(final MethodNode method) {
        final Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            if (iter.next() instanceof LineNumberNode) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isStatic(final MethodNode method) {
        return (method.access & 0x8) == 0x8;
    }
    
    public static boolean isStatic(final FieldNode field) {
        return (field.access & 0x8) == 0x8;
    }
    
    public static int getFirstNonArgLocalIndex(final MethodNode method) {
        return getFirstNonArgLocalIndex(Type.getArgumentTypes(method.desc), !isStatic(method));
    }
    
    public static int getFirstNonArgLocalIndex(final Type[] args, final boolean includeThis) {
        return getArgsSize(args) + (includeThis ? 1 : 0);
    }
    
    public static int getArgsSize(final Type[] args) {
        return getArgsSize(args, 0, args.length);
    }
    
    public static int getArgsSize(final Type[] args, final int startIndex, final int endIndex) {
        int size = 0;
        for (int index = startIndex; index < args.length && index < endIndex; ++index) {
            size += args[index].getSize();
        }
        return size;
    }
    
    public static void loadArgs(final Type[] args, final InsnList insns, final int pos) {
        loadArgs(args, insns, pos, -1);
    }
    
    public static void loadArgs(final Type[] args, final InsnList insns, final int start, final int end) {
        loadArgs(args, insns, start, end, null);
    }
    
    public static void loadArgs(final Type[] args, final InsnList insns, final int start, final int end, final Type[] casts) {
        int pos = start;
        for (int index = 0; index < args.length; ++index) {
            insns.add(new VarInsnNode(args[index].getOpcode(21), pos));
            if (casts != null && index < casts.length && casts[index] != null) {
                insns.add(new TypeInsnNode(192, casts[index].getInternalName()));
            }
            pos += args[index].getSize();
            if (end >= start && pos >= end) {
                return;
            }
        }
    }
    
    public static Type[] getTypes(final Class<?>... classes) {
        final Type[] types = new Type[classes.length];
        for (int index = 0; index < classes.length; ++index) {
            types[index] = Type.getType(classes[index]);
        }
        return types;
    }
    
    public static Map<LabelNode, LabelNode> cloneLabels(final InsnList source) {
        final Map<LabelNode, LabelNode> labels = new HashMap<LabelNode, LabelNode>();
        for (final AbstractInsnNode insn : source) {
            if (insn instanceof LabelNode) {
                labels.put((LabelNode)insn, new LabelNode(((LabelNode)insn).getLabel()));
            }
        }
        return labels;
    }
    
    public static String generateDescriptor(final Type returnType, final Type... args) {
        return generateDescriptor((Object)returnType, (Object[])args);
    }
    
    public static String generateDescriptor(final Object returnType, final Object... args) {
        final StringBuilder sb = new StringBuilder().append('(');
        for (final Object arg : args) {
            sb.append(toDescriptor(arg));
        }
        return sb.append(')').append((returnType != null) ? toDescriptor(returnType) : "V").toString();
    }
    
    private static String toDescriptor(final Object arg) {
        if (arg instanceof String) {
            return (String)arg;
        }
        if (arg instanceof Type) {
            return arg.toString();
        }
        if (arg instanceof Class) {
            return Type.getDescriptor((Class<?>)arg);
        }
        return (arg == null) ? "" : arg.toString();
    }
    
    public static String getDescriptor(final Type... args) {
        return "(" + Joiner.on("").join(args) + ")";
    }
    
    public static String getDescriptor(final Type returnType, final Type... args) {
        return getDescriptor(args) + returnType.toString();
    }
    
    public static String changeDescriptorReturnType(final String desc, final String returnType) {
        if (desc == null || !desc.startsWith("(") || desc.lastIndexOf(41) < 1) {
            return null;
        }
        if (returnType == null) {
            return desc;
        }
        return desc.substring(0, desc.lastIndexOf(41) + 1) + returnType;
    }
    
    public static String getSimpleName(final Type type) {
        return (type.getSort() < 9) ? type.getDescriptor() : getSimpleName(type.getClassName());
    }
    
    public static String getSimpleName(final String desc) {
        final int pos = Math.max(desc.lastIndexOf(47), 0);
        return desc.substring(pos + 1).replace(";", "");
    }
    
    public static boolean isConstant(final AbstractInsnNode insn) {
        return insn != null && Ints.contains(Bytecode.CONSTANTS_ALL, insn.getOpcode());
    }
    
    public static Object getConstant(final AbstractInsnNode insn) {
        if (insn == null) {
            return null;
        }
        if (insn instanceof LdcInsnNode) {
            return ((LdcInsnNode)insn).cst;
        }
        if (insn instanceof IntInsnNode) {
            final int value = ((IntInsnNode)insn).operand;
            if (insn.getOpcode() == 16 || insn.getOpcode() == 17) {
                return value;
            }
            throw new IllegalArgumentException("IntInsnNode with invalid opcode " + insn.getOpcode() + " in getConstant");
        }
        else {
            if (!(insn instanceof TypeInsnNode)) {
                final int index = Ints.indexOf(Bytecode.CONSTANTS_ALL, insn.getOpcode());
                return (index < 0) ? null : Bytecode.CONSTANTS_VALUES[index];
            }
            if (insn.getOpcode() < 192) {
                return null;
            }
            return Type.getObjectType(((TypeInsnNode)insn).desc);
        }
    }
    
    public static Type getConstantType(final AbstractInsnNode insn) {
        if (insn == null) {
            return null;
        }
        if (insn instanceof LdcInsnNode) {
            final Object cst = ((LdcInsnNode)insn).cst;
            if (cst instanceof Integer) {
                return Type.getType("I");
            }
            if (cst instanceof Float) {
                return Type.getType("F");
            }
            if (cst instanceof Long) {
                return Type.getType("J");
            }
            if (cst instanceof Double) {
                return Type.getType("D");
            }
            if (cst instanceof String) {
                return Type.getType("Ljava/lang/String;");
            }
            if (cst instanceof Type) {
                return Type.getType("Ljava/lang/Class;");
            }
            throw new IllegalArgumentException("LdcInsnNode with invalid payload type " + cst.getClass() + " in getConstant");
        }
        else {
            if (!(insn instanceof TypeInsnNode)) {
                final int index = Ints.indexOf(Bytecode.CONSTANTS_ALL, insn.getOpcode());
                return (index < 0) ? null : Type.getType(Bytecode.CONSTANTS_TYPES[index]);
            }
            if (insn.getOpcode() < 192) {
                return null;
            }
            return Type.getType("Ljava/lang/Class;");
        }
    }
    
    public static boolean hasFlag(final ClassNode classNode, final int flag) {
        return (classNode.access & flag) == flag;
    }
    
    public static boolean hasFlag(final MethodNode method, final int flag) {
        return (method.access & flag) == flag;
    }
    
    public static boolean hasFlag(final FieldNode field, final int flag) {
        return (field.access & flag) == flag;
    }
    
    public static boolean compareFlags(final MethodNode m1, final MethodNode m2, final int flag) {
        return hasFlag(m1, flag) == hasFlag(m2, flag);
    }
    
    public static boolean compareFlags(final FieldNode f1, final FieldNode f2, final int flag) {
        return hasFlag(f1, flag) == hasFlag(f2, flag);
    }
    
    public static boolean isVirtual(final MethodNode method) {
        return method != null && !isStatic(method) && getVisibility(method).isAtLeast(Visibility.PROTECTED);
    }
    
    public static Visibility getVisibility(final MethodNode method) {
        return getVisibility(method.access & 0x7);
    }
    
    public static Visibility getVisibility(final FieldNode field) {
        return getVisibility(field.access & 0x7);
    }
    
    private static Visibility getVisibility(final int flags) {
        if ((flags & 0x4) != 0x0) {
            return Visibility.PROTECTED;
        }
        if ((flags & 0x2) != 0x0) {
            return Visibility.PRIVATE;
        }
        if ((flags & 0x1) != 0x0) {
            return Visibility.PUBLIC;
        }
        return Visibility.PACKAGE;
    }
    
    public static void setVisibility(final ClassNode classNode, final Visibility visibility) {
        classNode.access = setVisibility(classNode.access, visibility.access);
    }
    
    public static void setVisibility(final MethodNode method, final Visibility visibility) {
        method.access = setVisibility(method.access, visibility.access);
    }
    
    public static void setVisibility(final FieldNode field, final Visibility visibility) {
        field.access = setVisibility(field.access, visibility.access);
    }
    
    public static void setVisibility(final ClassNode classNode, final int access) {
        classNode.access = setVisibility(classNode.access, access);
    }
    
    public static void setVisibility(final MethodNode method, final int access) {
        method.access = setVisibility(method.access, access);
    }
    
    public static void setVisibility(final FieldNode field, final int access) {
        field.access = setVisibility(field.access, access);
    }
    
    private static int setVisibility(final int oldAccess, final int newAccess) {
        return (oldAccess & 0xFFFFFFF8) | (newAccess & 0x7);
    }
    
    public static int getMaxLineNumber(final ClassNode classNode, final int min, final int pad) {
        int max = 0;
        for (final MethodNode method : classNode.methods) {
            for (final AbstractInsnNode insn : method.instructions) {
                if (insn instanceof LineNumberNode) {
                    max = Math.max(max, ((LineNumberNode)insn).line);
                }
            }
        }
        return Math.max(min, max + pad);
    }
    
    public static String getBoxingType(final Type type) {
        return (type == null) ? null : Bytecode.BOXING_TYPES[type.getSort()];
    }
    
    public static String getUnboxingMethod(final Type type) {
        return (type == null) ? null : Bytecode.UNBOXING_METHODS[type.getSort()];
    }
    
    public static void compareBridgeMethods(final MethodNode a, final MethodNode b) {
        final ListIterator<AbstractInsnNode> ia = a.instructions.iterator();
        final ListIterator<AbstractInsnNode> ib = b.instructions.iterator();
        int index = 0;
        while (ia.hasNext() && ib.hasNext()) {
            final AbstractInsnNode na = ia.next();
            final AbstractInsnNode nb = ib.next();
            if (!(na instanceof LabelNode)) {
                if (na instanceof MethodInsnNode) {
                    final MethodInsnNode ma = (MethodInsnNode)na;
                    final MethodInsnNode mb = (MethodInsnNode)nb;
                    if (!ma.name.equals(mb.name)) {
                        throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_NAME, a.name, a.desc, index, na, nb);
                    }
                    if (!ma.desc.equals(mb.desc)) {
                        throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INVOKE_DESC, a.name, a.desc, index, na, nb);
                    }
                }
                else {
                    if (na.getOpcode() != nb.getOpcode()) {
                        throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_INSN, a.name, a.desc, index, na, nb);
                    }
                    if (na instanceof VarInsnNode) {
                        final VarInsnNode va = (VarInsnNode)na;
                        final VarInsnNode vb = (VarInsnNode)nb;
                        if (va.var != vb.var) {
                            throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LOAD, a.name, a.desc, index, na, nb);
                        }
                    }
                    else if (na instanceof TypeInsnNode) {
                        final TypeInsnNode ta = (TypeInsnNode)na;
                        final TypeInsnNode tb = (TypeInsnNode)nb;
                        if (ta.getOpcode() == 192 && !ta.desc.equals(tb.desc)) {
                            throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_CAST, a.name, a.desc, index, na, nb);
                        }
                    }
                }
            }
            ++index;
        }
        if (ia.hasNext() || ib.hasNext()) {
            throw new SyntheticBridgeException(SyntheticBridgeException.Problem.BAD_LENGTH, a.name, a.desc, index, null, null);
        }
    }
    
    public static void merge(final ClassNode source, final ClassNode dest) {
        if (source == null) {
            return;
        }
        if (dest == null) {
            throw new NullPointerException("Target ClassNode for merge must not be null");
        }
        dest.version = Math.max(source.version, dest.version);
        dest.interfaces = merge(source.interfaces, dest.interfaces);
        dest.invisibleAnnotations = merge(source.invisibleAnnotations, dest.invisibleAnnotations);
        dest.visibleAnnotations = merge(source.visibleAnnotations, dest.visibleAnnotations);
        dest.visibleTypeAnnotations = merge(source.visibleTypeAnnotations, dest.visibleTypeAnnotations);
        dest.invisibleTypeAnnotations = merge(source.invisibleTypeAnnotations, dest.invisibleTypeAnnotations);
        dest.attrs = merge(source.attrs, dest.attrs);
        dest.innerClasses = merge(source.innerClasses, dest.innerClasses);
        dest.fields = merge(source.fields, dest.fields);
        dest.methods = merge(source.methods, dest.methods);
    }
    
    public static void replace(final ClassNode source, final ClassNode dest) {
        if (source == null) {
            return;
        }
        if (dest == null) {
            throw new NullPointerException("Target ClassNode for replace must not be null");
        }
        dest.name = source.name;
        dest.signature = source.signature;
        dest.superName = source.superName;
        dest.version = source.version;
        dest.access = source.access;
        dest.sourceDebug = source.sourceDebug;
        dest.sourceFile = source.sourceFile;
        dest.outerClass = source.outerClass;
        dest.outerMethod = source.outerMethod;
        dest.outerMethodDesc = source.outerMethodDesc;
        clear(dest.interfaces);
        clear(dest.visibleAnnotations);
        clear(dest.invisibleAnnotations);
        clear(dest.visibleTypeAnnotations);
        clear(dest.invisibleTypeAnnotations);
        clear(dest.attrs);
        clear(dest.innerClasses);
        clear(dest.fields);
        clear(dest.methods);
        if (ASM.API_VERSION >= 393216) {
            dest.module = source.module;
        }
        merge(source, dest);
    }
    
    private static <T> void clear(final List<T> list) {
        if (list != null) {
            list.clear();
        }
    }
    
    private static <T> List<T> merge(final List<T> source, final List<T> destination) {
        if (source == null || source.isEmpty()) {
            return destination;
        }
        if (destination == null) {
            return new ArrayList<T>((Collection<? extends T>)source);
        }
        destination.addAll((Collection<? extends T>)source);
        return destination;
    }
    
    static {
        CONSTANTS_INT = new int[] { 2, 3, 4, 5, 6, 7, 8 };
        CONSTANTS_FLOAT = new int[] { 11, 12, 13 };
        CONSTANTS_DOUBLE = new int[] { 14, 15 };
        CONSTANTS_LONG = new int[] { 9, 10 };
        CONSTANTS_ALL = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 192, 193 };
        CONSTANTS_VALUES = new Object[] { Type.VOID_TYPE, -1, 0, 1, 2, 3, 4, 5, 0L, 1L, 0.0f, 1.0f, 2.0f, 0.0, 1.0 };
        CONSTANTS_TYPES = new String[] { "V", "I", "I", "I", "I", "I", "I", "I", "J", "J", "F", "F", "F", "D", "D", "I", "I" };
        BOXING_TYPES = new String[] { null, "java/lang/Boolean", "java/lang/Character", "java/lang/Byte", "java/lang/Short", "java/lang/Integer", "java/lang/Float", "java/lang/Long", "java/lang/Double", null, null, null };
        UNBOXING_METHODS = new String[] { null, "booleanValue", "charValue", "byteValue", "shortValue", "intValue", "floatValue", "longValue", "doubleValue", null, null, null };
    }
    
    public enum Visibility
    {
        PRIVATE(2), 
        PROTECTED(4), 
        PACKAGE(0), 
        PUBLIC(1);
        
        static final int MASK = 7;
        final int access;
        
        private Visibility(final int access) {
            this.access = access;
        }
        
        public boolean isAtLeast(final Visibility other) {
            return other == null || other.ordinal() <= this.ordinal();
        }
        
        public boolean isLessThan(final Visibility other) {
            return other != null && this.ordinal() < other.ordinal();
        }
    }
    
    public static class DelegateInitialiser
    {
        public static final DelegateInitialiser NONE;
        public final MethodInsnNode insn;
        public final boolean isSuper;
        public final boolean isPresent;
        
        DelegateInitialiser(final MethodInsnNode insn, final boolean isSuper) {
            this.insn = insn;
            this.isSuper = isSuper;
            this.isPresent = (insn != null);
        }
        
        @Override
        public String toString() {
            return this.isSuper ? "super" : "this";
        }
        
        static {
            NONE = new DelegateInitialiser(null, false);
        }
    }
}
