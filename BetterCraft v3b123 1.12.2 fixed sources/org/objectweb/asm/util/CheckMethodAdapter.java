// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import java.util.Iterator;
import org.objectweb.asm.Label;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;

public class CheckMethodAdapter extends MethodVisitor
{
    public int version;
    private int access;
    private boolean startCode;
    private boolean endCode;
    private boolean endMethod;
    private int insnCount;
    private final Map labels;
    private Set usedLabels;
    private int expandedFrames;
    private int compressedFrames;
    private int lastFrame;
    private List handlers;
    private static final int[] TYPE;
    private static Field labelStatusField;
    static /* synthetic */ Class class$org$objectweb$asm$util$CheckMethodAdapter;
    static /* synthetic */ Class class$org$objectweb$asm$Label;
    
    public CheckMethodAdapter(final MethodVisitor methodVisitor) {
        this(methodVisitor, new HashMap());
    }
    
    public CheckMethodAdapter(final MethodVisitor methodVisitor, final Map map) {
        this(327680, methodVisitor, map);
        if (this.getClass() != CheckMethodAdapter.class$org$objectweb$asm$util$CheckMethodAdapter) {
            throw new IllegalStateException();
        }
    }
    
    protected CheckMethodAdapter(final int api, final MethodVisitor methodVisitor, final Map labels) {
        super(api, methodVisitor);
        this.lastFrame = -1;
        this.labels = labels;
        this.usedLabels = new HashSet();
        this.handlers = new ArrayList();
    }
    
    public CheckMethodAdapter(final int access, final String s, final String s2, final MethodVisitor methodVisitor, final Map map) {
        this(new CheckMethodAdapter$1(327680, access, s, s2, null, null, methodVisitor), map);
        this.access = access;
    }
    
    public void visitParameter(final String name, final int access) {
        if (name != null) {
            checkUnqualifiedName(this.version, name, "name");
        }
        CheckClassAdapter.checkAccess(access, 36880);
        super.visitParameter(name, access);
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        this.checkEndMethod();
        checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(descriptor, visible));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        this.checkEndMethod();
        final int n = typeRef >>> 24;
        if (n != 1 && n != 18 && n != 20 && n != 21 && n != 22 && n != 23) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n));
        }
        CheckClassAdapter.checkTypeRefAndPath(typeRef, typePath);
        checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        this.checkEndMethod();
        return new CheckAnnotationAdapter(super.visitAnnotationDefault(), false);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String descriptor, final boolean visible) {
        this.checkEndMethod();
        checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitParameterAnnotation(parameter, descriptor, visible));
    }
    
    public void visitAttribute(final Attribute attribute) {
        this.checkEndMethod();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }
    
    public void visitCode() {
        if ((this.access & 0x400) != 0x0) {
            throw new RuntimeException("Abstract methods cannot have code");
        }
        this.startCode = true;
        super.visitCode();
    }
    
    public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack, final Object[] stack) {
        if (this.insnCount == this.lastFrame) {
            throw new IllegalStateException("At most one frame can be visited at a given code location.");
        }
        this.lastFrame = this.insnCount;
        int n = 0;
        int n2 = 0;
        switch (type) {
            case -1:
            case 0: {
                n = Integer.MAX_VALUE;
                n2 = Integer.MAX_VALUE;
                break;
            }
            case 3: {
                n = 0;
                n2 = 0;
                break;
            }
            case 4: {
                n = 0;
                n2 = 1;
                break;
            }
            case 1:
            case 2: {
                n = 3;
                n2 = 0;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid frame type " + type);
            }
        }
        if (numLocal > n) {
            throw new IllegalArgumentException("Invalid nLocal=" + numLocal + " for frame type " + type);
        }
        if (numStack > n2) {
            throw new IllegalArgumentException("Invalid nStack=" + numStack + " for frame type " + type);
        }
        if (type != 2) {
            if (numLocal > 0 && (local == null || local.length < numLocal)) {
                throw new IllegalArgumentException("Array local[] is shorter than nLocal");
            }
            for (int i = 0; i < numLocal; ++i) {
                this.checkFrameValue(local[i]);
            }
        }
        if (numStack > 0 && (stack == null || stack.length < numStack)) {
            throw new IllegalArgumentException("Array stack[] is shorter than nStack");
        }
        for (int j = 0; j < numStack; ++j) {
            this.checkFrameValue(stack[j]);
        }
        if (type == -1) {
            ++this.expandedFrames;
        }
        else {
            ++this.compressedFrames;
        }
        if (this.expandedFrames > 0 && this.compressedFrames > 0) {
            throw new RuntimeException("Expanded and compressed frames must not be mixed.");
        }
        super.visitFrame(type, numLocal, local, numStack, stack);
    }
    
    public void visitInsn(final int opcode) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 0);
        super.visitInsn(opcode);
        ++this.insnCount;
    }
    
    public void visitIntInsn(final int opcode, final int operand) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 1);
        switch (opcode) {
            case 16: {
                checkSignedByte(operand, "Invalid operand");
                break;
            }
            case 17: {
                checkSignedShort(operand, "Invalid operand");
                break;
            }
            default: {
                if (operand < 4 || operand > 11) {
                    throw new IllegalArgumentException("Invalid operand (must be an array type code T_...): " + operand);
                }
                break;
            }
        }
        super.visitIntInsn(opcode, operand);
        ++this.insnCount;
    }
    
    public void visitVarInsn(final int opcode, final int var) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 2);
        checkUnsignedShort(var, "Invalid variable index");
        super.visitVarInsn(opcode, var);
        ++this.insnCount;
    }
    
    public void visitTypeInsn(final int opcode, final String type) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 3);
        checkInternalName(type, "type");
        if (opcode == 187 && type.charAt(0) == '[') {
            throw new IllegalArgumentException("NEW cannot be used to create arrays: " + type);
        }
        super.visitTypeInsn(opcode, type);
        ++this.insnCount;
    }
    
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 4);
        checkInternalName(owner, "owner");
        checkUnqualifiedName(this.version, name, "name");
        checkDesc(descriptor, false);
        super.visitFieldInsn(opcode, owner, name, descriptor);
        ++this.insnCount;
    }
    
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor) {
        if (this.api >= 327680) {
            super.visitMethodInsn(opcode, owner, name, descriptor);
            return;
        }
        this.doVisitMethodInsn(opcode, owner, name, descriptor, opcode == 185);
    }
    
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        if (this.api < 327680) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            return;
        }
        this.doVisitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
    
    private void doVisitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 5);
        if (opcode != 183 || !"<init>".equals(name)) {
            checkMethodIdentifier(this.version, name, "name");
        }
        checkInternalName(owner, "owner");
        checkMethodDesc(descriptor);
        if (opcode == 182 && isInterface) {
            throw new IllegalArgumentException("INVOKEVIRTUAL can't be used with interfaces");
        }
        if (opcode == 185 && !isInterface) {
            throw new IllegalArgumentException("INVOKEINTERFACE can't be used with classes");
        }
        if (opcode == 183 && isInterface && (this.version & 0xFFFF) < 52) {
            throw new IllegalArgumentException("INVOKESPECIAL can't be used with interfaces prior to Java 8");
        }
        if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
        ++this.insnCount;
    }
    
    public void visitInvokeDynamicInsn(final String name, final String descriptor, final Handle bootstrapMethodHandle, final Object... bootstrapMethodArguments) {
        this.checkStartCode();
        this.checkEndCode();
        checkMethodIdentifier(this.version, name, "name");
        checkMethodDesc(descriptor);
        if (bootstrapMethodHandle.getTag() != 6 && bootstrapMethodHandle.getTag() != 8) {
            throw new IllegalArgumentException("invalid handle tag " + bootstrapMethodHandle.getTag());
        }
        for (int i = 0; i < bootstrapMethodArguments.length; ++i) {
            this.checkLDCConstant(bootstrapMethodArguments[i]);
        }
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        ++this.insnCount;
    }
    
    public void visitJumpInsn(final int opcode, final Label label) {
        this.checkStartCode();
        this.checkEndCode();
        checkOpcode(opcode, 6);
        this.checkLabel(label, false, "label");
        checkNonDebugLabel(label);
        super.visitJumpInsn(opcode, label);
        this.usedLabels.add(label);
        ++this.insnCount;
    }
    
    public void visitLabel(final Label label) {
        this.checkStartCode();
        this.checkEndCode();
        this.checkLabel(label, false, "label");
        if (this.labels.get(label) != null) {
            throw new IllegalArgumentException("Already visited label");
        }
        this.labels.put(label, new Integer(this.insnCount));
        super.visitLabel(label);
    }
    
    public void visitLdcInsn(final Object value) {
        this.checkStartCode();
        this.checkEndCode();
        this.checkLDCConstant(value);
        super.visitLdcInsn(value);
        ++this.insnCount;
    }
    
    public void visitIincInsn(final int var, final int increment) {
        this.checkStartCode();
        this.checkEndCode();
        checkUnsignedShort(var, "Invalid variable index");
        checkSignedShort(increment, "Invalid increment");
        super.visitIincInsn(var, increment);
        ++this.insnCount;
    }
    
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        this.checkStartCode();
        this.checkEndCode();
        if (max < min) {
            throw new IllegalArgumentException("Max = " + max + " must be greater than or equal to min = " + min);
        }
        this.checkLabel(dflt, false, "default label");
        checkNonDebugLabel(dflt);
        if (labels == null || labels.length != max - min + 1) {
            throw new IllegalArgumentException("There must be max - min + 1 labels");
        }
        for (int i = 0; i < labels.length; ++i) {
            this.checkLabel(labels[i], false, "label at index " + i);
            checkNonDebugLabel(labels[i]);
        }
        super.visitTableSwitchInsn(min, max, dflt, labels);
        for (int j = 0; j < labels.length; ++j) {
            this.usedLabels.add(labels[j]);
        }
        ++this.insnCount;
    }
    
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        this.checkEndCode();
        this.checkStartCode();
        this.checkLabel(dflt, false, "default label");
        checkNonDebugLabel(dflt);
        if (keys == null || labels == null || keys.length != labels.length) {
            throw new IllegalArgumentException("There must be the same number of keys and labels");
        }
        for (int i = 0; i < labels.length; ++i) {
            this.checkLabel(labels[i], false, "label at index " + i);
            checkNonDebugLabel(labels[i]);
        }
        super.visitLookupSwitchInsn(dflt, keys, labels);
        this.usedLabels.add(dflt);
        for (int j = 0; j < labels.length; ++j) {
            this.usedLabels.add(labels[j]);
        }
        ++this.insnCount;
    }
    
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        this.checkStartCode();
        this.checkEndCode();
        checkDesc(descriptor, false);
        if (descriptor.charAt(0) != '[') {
            throw new IllegalArgumentException("Invalid descriptor (must be an array type descriptor): " + descriptor);
        }
        if (numDimensions < 1) {
            throw new IllegalArgumentException("Invalid dimensions (must be greater than 0): " + numDimensions);
        }
        if (numDimensions > descriptor.lastIndexOf(91) + 1) {
            throw new IllegalArgumentException("Invalid dimensions (must not be greater than dims(desc)): " + numDimensions);
        }
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
        ++this.insnCount;
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        this.checkStartCode();
        this.checkEndCode();
        final int n = typeRef >>> 24;
        if (n != 67 && n != 68 && n != 69 && n != 70 && n != 71 && n != 72 && n != 73 && n != 74 && n != 75) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n));
        }
        CheckClassAdapter.checkTypeRefAndPath(typeRef, typePath);
        checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitInsnAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        this.checkStartCode();
        this.checkEndCode();
        this.checkLabel(start, false, "start label");
        this.checkLabel(end, false, "end label");
        this.checkLabel(handler, false, "handler label");
        checkNonDebugLabel(start);
        checkNonDebugLabel(end);
        checkNonDebugLabel(handler);
        if (this.labels.get(start) != null || this.labels.get(end) != null || this.labels.get(handler) != null) {
            throw new IllegalStateException("Try catch blocks must be visited before their labels");
        }
        if (type != null) {
            checkInternalName(type, "type");
        }
        super.visitTryCatchBlock(start, end, handler, type);
        this.handlers.add(start);
        this.handlers.add(end);
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        this.checkStartCode();
        this.checkEndCode();
        final int n = typeRef >>> 24;
        if (n != 66) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n));
        }
        CheckClassAdapter.checkTypeRefAndPath(typeRef, typePath);
        checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
        this.checkStartCode();
        this.checkEndCode();
        checkUnqualifiedName(this.version, name, "name");
        checkDesc(descriptor, false);
        this.checkLabel(start, true, "start label");
        this.checkLabel(end, true, "end label");
        checkUnsignedShort(index, "Invalid variable index");
        if (this.labels.get(end) < this.labels.get(start)) {
            throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
        }
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int typeRef, final TypePath typePath, final Label[] start, final Label[] end, final int[] index, final String descriptor, final boolean visible) {
        this.checkStartCode();
        this.checkEndCode();
        final int n = typeRef >>> 24;
        if (n != 64 && n != 65) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n));
        }
        CheckClassAdapter.checkTypeRefAndPath(typeRef, typePath);
        checkDesc(descriptor, false);
        if (start == null || end == null || index == null || end.length != start.length || index.length != start.length) {
            throw new IllegalArgumentException("Invalid start, end and index arrays (must be non null and of identical length");
        }
        for (int i = 0; i < start.length; ++i) {
            this.checkLabel(start[i], true, "start label");
            this.checkLabel(end[i], true, "end label");
            checkUnsignedShort(index[i], "Invalid variable index");
            if ((int)this.labels.get(end[i]) < (int)this.labels.get(start[i])) {
                throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
            }
        }
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }
    
    public void visitLineNumber(final int line, final Label start) {
        this.checkStartCode();
        this.checkEndCode();
        checkUnsignedShort(line, "Invalid line number");
        this.checkLabel(start, true, "start label");
        super.visitLineNumber(line, start);
    }
    
    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.checkStartCode();
        this.checkEndCode();
        this.endCode = true;
        final Iterator iterator = this.usedLabels.iterator();
        while (iterator.hasNext()) {
            if (this.labels.get(iterator.next()) == null) {
                throw new IllegalStateException("Undefined label used");
            }
        }
        int i = 0;
        while (i < this.handlers.size()) {
            final Integer n = this.labels.get(this.handlers.get(i++));
            final Integer n2 = this.labels.get(this.handlers.get(i++));
            if (n == null || n2 == null) {
                throw new IllegalStateException("Undefined try catch block labels");
            }
            if (n2 <= n) {
                throw new IllegalStateException("Emty try catch block handler range");
            }
        }
        checkUnsignedShort(maxStack, "Invalid max stack");
        checkUnsignedShort(maxLocals, "Invalid max locals");
        super.visitMaxs(maxStack, maxLocals);
    }
    
    public void visitEnd() {
        this.checkEndMethod();
        this.endMethod = true;
        super.visitEnd();
    }
    
    void checkStartCode() {
        if (!this.startCode) {
            throw new IllegalStateException("Cannot visit instructions before visitCode has been called.");
        }
    }
    
    void checkEndCode() {
        if (this.endCode) {
            throw new IllegalStateException("Cannot visit instructions after visitMaxs has been called.");
        }
    }
    
    void checkEndMethod() {
        if (this.endMethod) {
            throw new IllegalStateException("Cannot visit elements after visitEnd has been called.");
        }
    }
    
    void checkFrameValue(final Object o) {
        if (o == Opcodes.TOP || o == Opcodes.INTEGER || o == Opcodes.FLOAT || o == Opcodes.LONG || o == Opcodes.DOUBLE || o == Opcodes.NULL || o == Opcodes.UNINITIALIZED_THIS) {
            return;
        }
        if (o instanceof String) {
            checkInternalName((String)o, "Invalid stack frame value");
            return;
        }
        if (!(o instanceof Label)) {
            throw new IllegalArgumentException("Invalid stack frame value: " + o);
        }
        this.usedLabels.add(o);
    }
    
    static void checkOpcode(final int n, final int n2) {
        if (n < 0 || n > 199 || CheckMethodAdapter.TYPE[n] != n2) {
            throw new IllegalArgumentException("Invalid opcode: " + n);
        }
    }
    
    static void checkSignedByte(final int n, final String s) {
        if (n < -128 || n > 127) {
            throw new IllegalArgumentException(s + " (must be a signed byte): " + n);
        }
    }
    
    static void checkSignedShort(final int n, final String s) {
        if (n < -32768 || n > 32767) {
            throw new IllegalArgumentException(s + " (must be a signed short): " + n);
        }
    }
    
    static void checkUnsignedShort(final int n, final String s) {
        if (n < 0 || n > 65535) {
            throw new IllegalArgumentException(s + " (must be an unsigned short): " + n);
        }
    }
    
    static void checkConstant(final Object o) {
        if (!(o instanceof Integer) && !(o instanceof Float) && !(o instanceof Long) && !(o instanceof Double) && !(o instanceof String)) {
            throw new IllegalArgumentException("Invalid constant: " + o);
        }
    }
    
    void checkLDCConstant(final Object o) {
        if (o instanceof Type) {
            final int sort = ((Type)o).getSort();
            if (sort != 10 && sort != 9 && sort != 11) {
                throw new IllegalArgumentException("Illegal LDC constant value");
            }
            if (sort != 11 && (this.version & 0xFFFF) < 49) {
                throw new IllegalArgumentException("ldc of a constant class requires at least version 1.5");
            }
            if (sort == 11 && (this.version & 0xFFFF) < 51) {
                throw new IllegalArgumentException("ldc of a method type requires at least version 1.7");
            }
        }
        else if (o instanceof Handle) {
            if ((this.version & 0xFFFF) < 51) {
                throw new IllegalArgumentException("ldc of a handle requires at least version 1.7");
            }
            final int tag = ((Handle)o).getTag();
            if (tag < 1 || tag > 9) {
                throw new IllegalArgumentException("invalid handle tag " + tag);
            }
        }
        else {
            checkConstant(o);
        }
    }
    
    static void checkUnqualifiedName(final int n, final String s, final String s2) {
        if ((n & 0xFFFF) < 49) {
            checkIdentifier(s, s2);
        }
        else {
            for (int i = 0; i < s.length(); ++i) {
                if (".;[/".indexOf(s.charAt(i)) != -1) {
                    throw new IllegalArgumentException("Invalid " + s2 + " (must be a valid unqualified name): " + s);
                }
            }
        }
    }
    
    static void checkIdentifier(final String s, final String s2) {
        checkIdentifier(s, 0, -1, s2);
    }
    
    static void checkIdentifier(final String s, final int n, final int n2, final String s2) {
        if (s != null) {
            if (n2 == -1) {
                if (s.length() <= n) {
                    throw new IllegalArgumentException("Invalid " + s2 + " (must not be null or empty)");
                }
            }
            else if (n2 <= n) {
                throw new IllegalArgumentException("Invalid " + s2 + " (must not be null or empty)");
            }
            if (!Character.isJavaIdentifierStart(s.charAt(n))) {
                throw new IllegalArgumentException("Invalid " + s2 + " (must be a valid Java identifier): " + s);
            }
            for (int n3 = (n2 == -1) ? s.length() : n2, i = n + 1; i < n3; ++i) {
                if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                    throw new IllegalArgumentException("Invalid " + s2 + " (must be a valid Java identifier): " + s);
                }
            }
            return;
        }
        throw new IllegalArgumentException("Invalid " + s2 + " (must not be null or empty)");
    }
    
    static void checkMethodIdentifier(final int n, final String s, final String s2) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Invalid " + s2 + " (must not be null or empty)");
        }
        if ((n & 0xFFFF) >= 49) {
            for (int i = 0; i < s.length(); ++i) {
                if (".;[/<>".indexOf(s.charAt(i)) != -1) {
                    throw new IllegalArgumentException("Invalid " + s2 + " (must be a valid unqualified name): " + s);
                }
            }
            return;
        }
        if (!Character.isJavaIdentifierStart(s.charAt(0))) {
            throw new IllegalArgumentException("Invalid " + s2 + " (must be a '<init>', '<clinit>' or a valid Java identifier): " + s);
        }
        for (int j = 1; j < s.length(); ++j) {
            if (!Character.isJavaIdentifierPart(s.charAt(j))) {
                throw new IllegalArgumentException("Invalid " + s2 + " (must be '<init>' or '<clinit>' or a valid Java identifier): " + s);
            }
        }
    }
    
    static void checkInternalName(final String s, final String s2) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Invalid " + s2 + " (must not be null or empty)");
        }
        if (s.charAt(0) == '[') {
            checkDesc(s, false);
        }
        else {
            checkInternalName(s, 0, -1, s2);
        }
    }
    
    static void checkInternalName(final String s, final int n, final int n2, final String s2) {
        final int n3 = (n2 == -1) ? s.length() : n2;
        try {
            int n4 = n;
            int i;
            do {
                i = s.indexOf(47, n4 + 1);
                if (i == -1 || i > n3) {
                    i = n3;
                }
                checkIdentifier(s, n4, i, null);
                n4 = i + 1;
            } while (i != n3);
        }
        catch (final IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid " + s2 + " (must be a fully qualified class name in internal form): " + s);
        }
    }
    
    static void checkDesc(final String s, final boolean b) {
        if (checkDesc(s, 0, b) != s.length()) {
            throw new IllegalArgumentException("Invalid descriptor: " + s);
        }
    }
    
    static int checkDesc(final String s, final int n, final boolean b) {
        if (s == null || n >= s.length()) {
            throw new IllegalArgumentException("Invalid type descriptor (must not be null or empty)");
        }
        switch (s.charAt(n)) {
            case 'V': {
                if (b) {
                    return n + 1;
                }
                throw new IllegalArgumentException("Invalid descriptor: " + s);
            }
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z': {
                return n + 1;
            }
            case '[': {
                int n2;
                for (n2 = n + 1; n2 < s.length() && s.charAt(n2) == '['; ++n2) {}
                if (n2 < s.length()) {
                    return checkDesc(s, n2, false);
                }
                throw new IllegalArgumentException("Invalid descriptor: " + s);
            }
            case 'L': {
                final int index = s.indexOf(59, n);
                if (index == -1 || index - n < 2) {
                    throw new IllegalArgumentException("Invalid descriptor: " + s);
                }
                try {
                    checkInternalName(s, n + 1, index, null);
                }
                catch (final IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Invalid descriptor: " + s);
                }
                return index + 1;
            }
            default: {
                throw new IllegalArgumentException("Invalid descriptor: " + s);
            }
        }
    }
    
    static void checkMethodDesc(final String s) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Invalid method descriptor (must not be null or empty)");
        }
        if (s.charAt(0) != '(' || s.length() < 3) {
            throw new IllegalArgumentException("Invalid descriptor: " + s);
        }
        int checkDesc = 1;
        Label_0143: {
            if (s.charAt(checkDesc) != ')') {
                while (s.charAt(checkDesc) != 'V') {
                    checkDesc = checkDesc(s, checkDesc, false);
                    if (checkDesc >= s.length() || s.charAt(checkDesc) == ')') {
                        break Label_0143;
                    }
                }
                throw new IllegalArgumentException("Invalid descriptor: " + s);
            }
        }
        if (checkDesc(s, checkDesc + 1, true) != s.length()) {
            throw new IllegalArgumentException("Invalid descriptor: " + s);
        }
    }
    
    void checkLabel(final Label label, final boolean b, final String s) {
        if (label == null) {
            throw new IllegalArgumentException("Invalid " + s + " (must not be null)");
        }
        if (b && this.labels.get(label) == null) {
            throw new IllegalArgumentException("Invalid " + s + " (must be visited first)");
        }
    }
    
    private static void checkNonDebugLabel(final Label label) {
        final Field labelStatusField = getLabelStatusField();
        int n;
        try {
            n = (int)((labelStatusField == null) ? 0 : labelStatusField.get(label));
        }
        catch (final IllegalAccessException ex) {
            throw new Error("Internal error");
        }
        if ((n & 0x1) != 0x0) {
            throw new IllegalArgumentException("Labels used for debug info cannot be reused for control flow");
        }
    }
    
    private static Field getLabelStatusField() {
        if (CheckMethodAdapter.labelStatusField == null) {
            CheckMethodAdapter.labelStatusField = getLabelField("a");
            if (CheckMethodAdapter.labelStatusField == null) {
                CheckMethodAdapter.labelStatusField = getLabelField("status");
            }
        }
        return CheckMethodAdapter.labelStatusField;
    }
    
    private static Field getLabelField(final String s) {
        try {
            final Field declaredField = CheckMethodAdapter.class$org$objectweb$asm$Label.getDeclaredField(s);
            declaredField.setAccessible(true);
            return declaredField;
        }
        catch (final NoSuchFieldException ex) {
            return null;
        }
    }
    
    static {
        _clinit_();
        final String s = "BBBBBBBBBBBBBBBBCCIAADDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBDDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBJBBBBBBBBBBBBBBBBBBBBHHHHHHHHHHHHHHHHDKLBBBBBBFFFFGGGGAECEBBEEBBAMHHAA";
        TYPE = new int[s.length()];
        for (int i = 0; i < CheckMethodAdapter.TYPE.length; ++i) {
            CheckMethodAdapter.TYPE[i] = s.charAt(i) - 'A' - 1;
        }
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (final ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    private static void _clinit_() {
        CheckMethodAdapter.class$org$objectweb$asm$util$CheckMethodAdapter = class$("org.objectweb.asm.util.CheckMethodAdapter");
        CheckMethodAdapter.class$org$objectweb$asm$Label = class$("org.objectweb.asm.Label");
    }
}
