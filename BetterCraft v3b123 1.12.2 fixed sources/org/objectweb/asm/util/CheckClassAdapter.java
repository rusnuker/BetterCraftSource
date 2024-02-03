// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import java.util.HashMap;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.MethodVisitor;
import java.util.Iterator;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Analyzer;
import java.util.List;
import org.objectweb.asm.tree.analysis.SimpleVerifier;
import org.objectweb.asm.tree.MethodNode;
import java.util.ArrayList;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import java.io.FileInputStream;
import java.util.Map;
import org.objectweb.asm.ClassVisitor;

public class CheckClassAdapter extends ClassVisitor
{
    private int version;
    private boolean start;
    private boolean source;
    private boolean outer;
    private boolean end;
    private Map labels;
    private boolean checkDataFlow;
    static /* synthetic */ Class class$org$objectweb$asm$util$CheckClassAdapter;
    
    public static void main(final String[] array) throws Exception {
        if (array.length != 1) {
            System.err.println("Verifies the given class.");
            System.err.println("Usage: CheckClassAdapter <fully qualified class name or class file name>");
            return;
        }
        ClassReader classReader;
        if (array[0].endsWith(".class")) {
            classReader = new ClassReader(new FileInputStream(array[0]));
        }
        else {
            classReader = new ClassReader(array[0]);
        }
        verify(classReader, false, new PrintWriter(System.err));
    }
    
    public static void verify(final ClassReader classReader, final ClassLoader classLoader, final boolean b, final PrintWriter printWriter) {
        final ClassNode classNode = new ClassNode();
        classReader.accept(new CheckClassAdapter(classNode, false), 2);
        final Type type = (classNode.superName == null) ? null : Type.getObjectType(classNode.superName);
        final List<MethodNode> methods = classNode.methods;
        final ArrayList list = new ArrayList();
        final Iterator<String> iterator = classNode.interfaces.iterator();
        while (iterator.hasNext()) {
            list.add(Type.getObjectType(iterator.next()));
        }
        for (int i = 0; i < methods.size(); ++i) {
            final MethodNode methodNode = methods.get(i);
            final SimpleVerifier simpleVerifier = new SimpleVerifier(Type.getObjectType(classNode.name), type, list, (classNode.access & 0x200) != 0x0);
            final Analyzer analyzer = new Analyzer(simpleVerifier);
            if (classLoader != null) {
                simpleVerifier.setClassLoader(classLoader);
            }
            try {
                analyzer.analyze(classNode.name, methodNode);
                if (!b) {
                    continue;
                }
            }
            catch (final Exception ex) {
                ex.printStackTrace(printWriter);
            }
            printAnalyzerResult(methodNode, analyzer, printWriter);
        }
        printWriter.flush();
    }
    
    public static void verify(final ClassReader classReader, final boolean b, final PrintWriter printWriter) {
        verify(classReader, null, b, printWriter);
    }
    
    static void printAnalyzerResult(final MethodNode methodNode, final Analyzer analyzer, final PrintWriter printWriter) {
        final Frame[] frames = analyzer.getFrames();
        final Textifier textifier = new Textifier();
        final TraceMethodVisitor methodVisitor = new TraceMethodVisitor(textifier);
        printWriter.println(methodNode.name + methodNode.desc);
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            methodNode.instructions.get(i).accept(methodVisitor);
            final StringBuffer sb = new StringBuffer();
            final Frame frame = frames[i];
            if (frame == null) {
                sb.append('?');
            }
            else {
                for (int j = 0; j < frame.getLocals(); ++j) {
                    sb.append(getShortName(((BasicValue)frame.getLocal(j)).toString())).append(' ');
                }
                sb.append(" : ");
                for (int k = 0; k < frame.getStackSize(); ++k) {
                    sb.append(getShortName(((BasicValue)frame.getStack(k)).toString())).append(' ');
                }
            }
            while (sb.length() < methodNode.maxStack + methodNode.maxLocals + 1) {
                sb.append(' ');
            }
            printWriter.print(Integer.toString(i + 100000).substring(1));
            printWriter.print(" " + (Object)sb + " : " + textifier.text.get(textifier.text.size() - 1));
        }
        for (int l = 0; l < methodNode.tryCatchBlocks.size(); ++l) {
            methodNode.tryCatchBlocks.get(l).accept(methodVisitor);
            printWriter.print(" " + textifier.text.get(textifier.text.size() - 1));
        }
        printWriter.println();
    }
    
    private static String getShortName(final String s) {
        final int lastIndex = s.lastIndexOf(47);
        int length = s.length();
        if (s.charAt(length - 1) == ';') {
            --length;
        }
        return (lastIndex == -1) ? s : s.substring(lastIndex + 1, length);
    }
    
    public CheckClassAdapter(final ClassVisitor classVisitor) {
        this(classVisitor, true);
    }
    
    public CheckClassAdapter(final ClassVisitor classVisitor, final boolean b) {
        this(327680, classVisitor, b);
        if (this.getClass() != CheckClassAdapter.class$org$objectweb$asm$util$CheckClassAdapter) {
            throw new IllegalStateException();
        }
    }
    
    protected CheckClassAdapter(final int api, final ClassVisitor classVisitor, final boolean checkDataFlow) {
        super(api, classVisitor);
        this.labels = new HashMap();
        this.checkDataFlow = checkDataFlow;
    }
    
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        if (this.start) {
            throw new IllegalStateException("visit must be called only once");
        }
        this.start = true;
        this.checkState();
        checkAccess(access, 423473);
        if (name == null || !name.endsWith("package-info")) {
            CheckMethodAdapter.checkInternalName(name, "class name");
        }
        if ("java/lang/Object".equals(name)) {
            if (superName != null) {
                throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
            }
        }
        else {
            CheckMethodAdapter.checkInternalName(superName, "super class name");
        }
        if (signature != null) {
            checkClassSignature(signature);
        }
        if ((access & 0x200) != 0x0 && !"java/lang/Object".equals(superName)) {
            throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
        }
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; ++i) {
                CheckMethodAdapter.checkInternalName(interfaces[i], "interface name at index " + i);
            }
        }
        super.visit(this.version = version, access, name, signature, superName, interfaces);
    }
    
    public void visitSource(final String source, final String debug) {
        this.checkState();
        if (this.source) {
            throw new IllegalStateException("visitSource can be called only once.");
        }
        this.source = true;
        super.visitSource(source, debug);
    }
    
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
        this.checkState();
        if (this.outer) {
            throw new IllegalStateException("visitOuterClass can be called only once.");
        }
        this.outer = true;
        if (owner == null) {
            throw new IllegalArgumentException("Illegal outer class owner");
        }
        if (descriptor != null) {
            CheckMethodAdapter.checkMethodDesc(descriptor);
        }
        super.visitOuterClass(owner, name, descriptor);
    }
    
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        this.checkState();
        CheckMethodAdapter.checkInternalName(name, "class name");
        if (outerName != null) {
            CheckMethodAdapter.checkInternalName(outerName, "outer class name");
        }
        if (innerName != null) {
            int n;
            for (n = 0; n < innerName.length() && Character.isDigit(innerName.charAt(n)); ++n) {}
            if (n == 0 || n < innerName.length()) {
                CheckMethodAdapter.checkIdentifier(innerName, n, -1, "inner class name");
            }
        }
        checkAccess(access, 30239);
        super.visitInnerClass(name, outerName, innerName, access);
    }
    
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        this.checkState();
        checkAccess(access, 413919);
        CheckMethodAdapter.checkUnqualifiedName(this.version, name, "field name");
        CheckMethodAdapter.checkDesc(descriptor, false);
        if (signature != null) {
            checkFieldSignature(signature);
        }
        if (value != null) {
            CheckMethodAdapter.checkConstant(value);
        }
        return new CheckFieldAdapter(super.visitField(access, name, descriptor, signature, value));
    }
    
    public MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        this.checkState();
        checkAccess(n, 400895);
        if (!"<init>".equals(s) && !"<clinit>".equals(s)) {
            CheckMethodAdapter.checkMethodIdentifier(this.version, s, "method name");
        }
        CheckMethodAdapter.checkMethodDesc(s2);
        if (s3 != null) {
            checkMethodSignature(s3);
        }
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                CheckMethodAdapter.checkInternalName(array[i], "exception name at index " + i);
            }
        }
        CheckMethodAdapter checkMethodAdapter;
        if (this.checkDataFlow) {
            checkMethodAdapter = new CheckMethodAdapter(n, s, s2, super.visitMethod(n, s, s2, s3, array), this.labels);
        }
        else {
            checkMethodAdapter = new CheckMethodAdapter(super.visitMethod(n, s, s2, s3, array), this.labels);
        }
        checkMethodAdapter.version = this.version;
        return checkMethodAdapter;
    }
    
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        this.checkState();
        CheckMethodAdapter.checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(descriptor, visible));
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        this.checkState();
        final int n = typeRef >>> 24;
        if (n != 0 && n != 17 && n != 16) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n));
        }
        checkTypeRefAndPath(typeRef, typePath);
        CheckMethodAdapter.checkDesc(descriptor, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
    }
    
    public void visitAttribute(final Attribute attribute) {
        this.checkState();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }
    
    public void visitEnd() {
        this.checkState();
        this.end = true;
        super.visitEnd();
    }
    
    private void checkState() {
        if (!this.start) {
            throw new IllegalStateException("Cannot visit member before visit has been called.");
        }
        if (this.end) {
            throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
        }
    }
    
    static void checkAccess(final int n, final int n2) {
        if ((n & ~n2) != 0x0) {
            throw new IllegalArgumentException("Invalid access flags: " + n);
        }
        if (((((n & 0x1) != 0x0) + ((n & 0x2) != 0x0) + ((n & 0x4) != 0x0)) ? 1 : 0) > 1) {
            throw new IllegalArgumentException("public private and protected are mutually exclusive: " + n);
        }
        if (((((n & 0x10) != 0x0) + ((n & 0x400) != 0x0)) ? 1 : 0) > 1) {
            throw new IllegalArgumentException("final and abstract are mutually exclusive: " + n);
        }
    }
    
    public static void checkClassSignature(final String s) {
        int checkFormalTypeParameters = 0;
        if (getChar(s, 0) == '<') {
            checkFormalTypeParameters = checkFormalTypeParameters(s, checkFormalTypeParameters);
        }
        int n;
        for (n = checkClassTypeSignature(s, checkFormalTypeParameters); getChar(s, n) == 'L'; n = checkClassTypeSignature(s, n)) {}
        if (n != s.length()) {
            throw new IllegalArgumentException(s + ": error at index " + n);
        }
    }
    
    public static void checkMethodSignature(final String s) {
        int checkFormalTypeParameters = 0;
        if (getChar(s, 0) == '<') {
            checkFormalTypeParameters = checkFormalTypeParameters(s, checkFormalTypeParameters);
        }
        int n;
        for (n = checkChar('(', s, checkFormalTypeParameters); "ZCBSIFJDL[T".indexOf(getChar(s, n)) != -1; n = checkTypeSignature(s, n)) {}
        int n2 = checkChar(')', s, n);
        if (getChar(s, n2) == 'V') {
            ++n2;
        }
        else {
            n2 = checkTypeSignature(s, n2);
        }
        while (getChar(s, n2) == '^') {
            ++n2;
            if (getChar(s, n2) == 'L') {
                n2 = checkClassTypeSignature(s, n2);
            }
            else {
                n2 = checkTypeVariableSignature(s, n2);
            }
        }
        if (n2 != s.length()) {
            throw new IllegalArgumentException(s + ": error at index " + n2);
        }
    }
    
    public static void checkFieldSignature(final String s) {
        final int checkFieldTypeSignature = checkFieldTypeSignature(s, 0);
        if (checkFieldTypeSignature != s.length()) {
            throw new IllegalArgumentException(s + ": error at index " + checkFieldTypeSignature);
        }
    }
    
    static void checkTypeRefAndPath(final int n, final TypePath typePath) {
        int n2 = 0;
        switch (n >>> 24) {
            case 0:
            case 1:
            case 22: {
                n2 = -65536;
                break;
            }
            case 19:
            case 20:
            case 21:
            case 64:
            case 65:
            case 67:
            case 68:
            case 69:
            case 70: {
                n2 = -16777216;
                break;
            }
            case 16:
            case 17:
            case 18:
            case 23:
            case 66: {
                n2 = -256;
                break;
            }
            case 71:
            case 72:
            case 73:
            case 74:
            case 75: {
                n2 = -16776961;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(n >>> 24));
            }
        }
        if ((n & ~n2) != 0x0) {
            throw new IllegalArgumentException("Invalid type reference 0x" + Integer.toHexString(n));
        }
        if (typePath != null) {
            for (int i = 0; i < typePath.getLength(); ++i) {
                final int step = typePath.getStep(i);
                if (step != 0 && step != 1 && step != 3 && step != 2) {
                    throw new IllegalArgumentException("Invalid type path step " + i + " in " + typePath);
                }
                if (step != 3 && typePath.getStepArgument(i) != 0) {
                    throw new IllegalArgumentException("Invalid type path step argument for step " + i + " in " + typePath);
                }
            }
        }
    }
    
    private static int checkFormalTypeParameters(final String s, int n) {
        for (n = checkChar('<', s, n), n = checkFormalTypeParameter(s, n); getChar(s, n) != '>'; n = checkFormalTypeParameter(s, n)) {}
        return n + 1;
    }
    
    private static int checkFormalTypeParameter(final String s, int n) {
        n = checkIdentifier(s, n);
        n = checkChar(':', s, n);
        if ("L[T".indexOf(getChar(s, n)) != -1) {
            n = checkFieldTypeSignature(s, n);
        }
        while (getChar(s, n) == ':') {
            n = checkFieldTypeSignature(s, n + 1);
        }
        return n;
    }
    
    private static int checkFieldTypeSignature(final String s, final int n) {
        switch (getChar(s, n)) {
            case 'L': {
                return checkClassTypeSignature(s, n);
            }
            case '[': {
                return checkTypeSignature(s, n + 1);
            }
            default: {
                return checkTypeVariableSignature(s, n);
            }
        }
    }
    
    private static int checkClassTypeSignature(final String s, int n) {
        for (n = checkChar('L', s, n), n = checkIdentifier(s, n); getChar(s, n) == '/'; n = checkIdentifier(s, n + 1)) {}
        if (getChar(s, n) == '<') {
            n = checkTypeArguments(s, n);
        }
        while (getChar(s, n) == '.') {
            n = checkIdentifier(s, n + 1);
            if (getChar(s, n) == '<') {
                n = checkTypeArguments(s, n);
            }
        }
        return checkChar(';', s, n);
    }
    
    private static int checkTypeArguments(final String s, int n) {
        for (n = checkChar('<', s, n), n = checkTypeArgument(s, n); getChar(s, n) != '>'; n = checkTypeArgument(s, n)) {}
        return n + 1;
    }
    
    private static int checkTypeArgument(final String s, int n) {
        final char char1 = getChar(s, n);
        if (char1 == '*') {
            return n + 1;
        }
        if (char1 == '+' || char1 == '-') {
            ++n;
        }
        return checkFieldTypeSignature(s, n);
    }
    
    private static int checkTypeVariableSignature(final String s, int n) {
        n = checkChar('T', s, n);
        n = checkIdentifier(s, n);
        return checkChar(';', s, n);
    }
    
    private static int checkTypeSignature(final String s, final int n) {
        switch (getChar(s, n)) {
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
            default: {
                return checkFieldTypeSignature(s, n);
            }
        }
    }
    
    private static int checkIdentifier(final String s, int n) {
        if (!Character.isJavaIdentifierStart(getChar(s, n))) {
            throw new IllegalArgumentException(s + ": identifier expected at index " + n);
        }
        ++n;
        while (Character.isJavaIdentifierPart(getChar(s, n))) {
            ++n;
        }
        return n;
    }
    
    private static int checkChar(final char c, final String s, final int n) {
        if (getChar(s, n) == c) {
            return n + 1;
        }
        throw new IllegalArgumentException(s + ": '" + c + "' expected at index " + n);
    }
    
    private static char getChar(final String s, final int n) {
        return (n < s.length()) ? s.charAt(n) : '\0';
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (final ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    static {
        CheckClassAdapter.class$org$objectweb$asm$util$CheckClassAdapter = class$("org.objectweb.asm.util.CheckClassAdapter");
    }
}
