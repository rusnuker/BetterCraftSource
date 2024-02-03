// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.Handle;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.Label;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;

public final class SAXCodeAdapter extends MethodVisitor
{
    static final String[] TYPES;
    SAXAdapter sa;
    int access;
    private final Map labelNames;
    
    public SAXCodeAdapter(final SAXAdapter sa, final int access) {
        super(327680);
        this.sa = sa;
        this.access = access;
        this.labelNames = new HashMap();
    }
    
    public void visitParameter(final String s, final int n) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        if (s != null) {
            attributesImpl.addAttribute("", "name", "name", "", s);
        }
        final StringBuffer sb = new StringBuffer();
        SAXClassAdapter.appendAccess(n, sb);
        attributesImpl.addAttribute("", "access", "access", "", sb.toString());
        this.sa.addElement("parameter", attributesImpl);
    }
    
    public final void visitCode() {
        if ((this.access & 0x700) == 0x0) {
            this.sa.addStart("code", new AttributesImpl());
        }
    }
    
    public void visitFrame(final int n, final int n2, final Object[] array, final int n3, final Object[] array2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        switch (n) {
            case -1:
            case 0: {
                if (n == -1) {
                    attributesImpl.addAttribute("", "type", "type", "", "NEW");
                }
                else {
                    attributesImpl.addAttribute("", "type", "type", "", "FULL");
                }
                this.sa.addStart("frame", attributesImpl);
                this.appendFrameTypes(true, n2, array);
                this.appendFrameTypes(false, n3, array2);
                break;
            }
            case 1: {
                attributesImpl.addAttribute("", "type", "type", "", "APPEND");
                this.sa.addStart("frame", attributesImpl);
                this.appendFrameTypes(true, n2, array);
                break;
            }
            case 2: {
                attributesImpl.addAttribute("", "type", "type", "", "CHOP");
                attributesImpl.addAttribute("", "count", "count", "", Integer.toString(n2));
                this.sa.addStart("frame", attributesImpl);
                break;
            }
            case 3: {
                attributesImpl.addAttribute("", "type", "type", "", "SAME");
                this.sa.addStart("frame", attributesImpl);
                break;
            }
            case 4: {
                attributesImpl.addAttribute("", "type", "type", "", "SAME1");
                this.sa.addStart("frame", attributesImpl);
                this.appendFrameTypes(false, 1, array2);
                break;
            }
        }
        this.sa.addEnd("frame");
    }
    
    private void appendFrameTypes(final boolean b, final int n, final Object[] array) {
        for (final Object o : array) {
            final AttributesImpl attributesImpl = new AttributesImpl();
            if (o instanceof String) {
                attributesImpl.addAttribute("", "type", "type", "", (String)o);
            }
            else if (o instanceof Integer) {
                attributesImpl.addAttribute("", "type", "type", "", SAXCodeAdapter.TYPES[(int)o]);
            }
            else {
                attributesImpl.addAttribute("", "type", "type", "", "uninitialized");
                attributesImpl.addAttribute("", "label", "label", "", this.getLabel((Label)o));
            }
            this.sa.addElement(b ? "local" : "stack", attributesImpl);
        }
    }
    
    public final void visitInsn(final int n) {
        this.sa.addElement(Printer.OPCODES[n], new AttributesImpl());
    }
    
    public final void visitIntInsn(final int n, final int n2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "value", "value", "", Integer.toString(n2));
        this.sa.addElement(Printer.OPCODES[n], attributesImpl);
    }
    
    public final void visitVarInsn(final int n, final int n2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "var", "var", "", Integer.toString(n2));
        this.sa.addElement(Printer.OPCODES[n], attributesImpl);
    }
    
    public final void visitTypeInsn(final int n, final String s) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "desc", "desc", "", s);
        this.sa.addElement(Printer.OPCODES[n], attributesImpl);
    }
    
    public final void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "owner", "owner", "", s);
        attributesImpl.addAttribute("", "name", "name", "", s2);
        attributesImpl.addAttribute("", "desc", "desc", "", s3);
        this.sa.addElement(Printer.OPCODES[n], attributesImpl);
    }
    
    public final void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "owner", "owner", "", s);
        attributesImpl.addAttribute("", "name", "name", "", s2);
        attributesImpl.addAttribute("", "desc", "desc", "", s3);
        attributesImpl.addAttribute("", "itf", "itf", "", b ? "true" : "false");
        this.sa.addElement(Printer.OPCODES[n], attributesImpl);
    }
    
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "name", "name", "", s);
        attributesImpl.addAttribute("", "desc", "desc", "", s2);
        attributesImpl.addAttribute("", "bsm", "bsm", "", SAXClassAdapter.encode(handle.toString()));
        this.sa.addStart("INVOKEDYNAMIC", attributesImpl);
        for (int i = 0; i < array.length; ++i) {
            this.sa.addElement("bsmArg", getConstantAttribute(array[i]));
        }
        this.sa.addEnd("INVOKEDYNAMIC");
    }
    
    public final void visitJumpInsn(final int n, final Label label) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "label", "label", "", this.getLabel(label));
        this.sa.addElement(Printer.OPCODES[n], attributesImpl);
    }
    
    public final void visitLabel(final Label label) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "name", "name", "", this.getLabel(label));
        this.sa.addElement("Label", attributesImpl);
    }
    
    public final void visitLdcInsn(final Object o) {
        this.sa.addElement(Printer.OPCODES[18], getConstantAttribute(o));
    }
    
    private static AttributesImpl getConstantAttribute(final Object o) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "cst", "cst", "", SAXClassAdapter.encode(o.toString()));
        attributesImpl.addAttribute("", "desc", "desc", "", Type.getDescriptor(o.getClass()));
        return attributesImpl;
    }
    
    public final void visitIincInsn(final int n, final int n2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "var", "var", "", Integer.toString(n));
        attributesImpl.addAttribute("", "inc", "inc", "", Integer.toString(n2));
        this.sa.addElement(Printer.OPCODES[132], attributesImpl);
    }
    
    public final void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "min", "min", "", Integer.toString(n));
        attributesImpl.addAttribute("", "max", "max", "", Integer.toString(n2));
        attributesImpl.addAttribute("", "dflt", "dflt", "", this.getLabel(label));
        final String s = Printer.OPCODES[170];
        this.sa.addStart(s, attributesImpl);
        for (int i = 0; i < array.length; ++i) {
            final AttributesImpl attributesImpl2 = new AttributesImpl();
            attributesImpl2.addAttribute("", "name", "name", "", this.getLabel(array[i]));
            this.sa.addElement("label", attributesImpl2);
        }
        this.sa.addEnd(s);
    }
    
    public final void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "dflt", "dflt", "", this.getLabel(label));
        final String s = Printer.OPCODES[171];
        this.sa.addStart(s, attributesImpl);
        for (int i = 0; i < array2.length; ++i) {
            final AttributesImpl attributesImpl2 = new AttributesImpl();
            attributesImpl2.addAttribute("", "name", "name", "", this.getLabel(array2[i]));
            attributesImpl2.addAttribute("", "key", "key", "", Integer.toString(array[i]));
            this.sa.addElement("label", attributesImpl2);
        }
        this.sa.addEnd(s);
    }
    
    public final void visitMultiANewArrayInsn(final String s, final int n) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "desc", "desc", "", s);
        attributesImpl.addAttribute("", "dims", "dims", "", Integer.toString(n));
        this.sa.addElement(Printer.OPCODES[197], attributesImpl);
    }
    
    public final void visitTryCatchBlock(final Label label, final Label label2, final Label label3, final String s) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "start", "start", "", this.getLabel(label));
        attributesImpl.addAttribute("", "end", "end", "", this.getLabel(label2));
        attributesImpl.addAttribute("", "handler", "handler", "", this.getLabel(label3));
        if (s != null) {
            attributesImpl.addAttribute("", "type", "type", "", s);
        }
        this.sa.addElement("TryCatch", attributesImpl);
    }
    
    public final void visitMaxs(final int n, final int n2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "maxStack", "maxStack", "", Integer.toString(n));
        attributesImpl.addAttribute("", "maxLocals", "maxLocals", "", Integer.toString(n2));
        this.sa.addElement("Max", attributesImpl);
        this.sa.addEnd("code");
    }
    
    public void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, final int n) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "name", "name", "", s);
        attributesImpl.addAttribute("", "desc", "desc", "", s2);
        if (s3 != null) {
            attributesImpl.addAttribute("", "signature", "signature", "", SAXClassAdapter.encode(s3));
        }
        attributesImpl.addAttribute("", "start", "start", "", this.getLabel(label));
        attributesImpl.addAttribute("", "end", "end", "", this.getLabel(label2));
        attributesImpl.addAttribute("", "var", "var", "", Integer.toString(n));
        this.sa.addElement("LocalVar", attributesImpl);
    }
    
    public final void visitLineNumber(final int n, final Label label) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "line", "line", "", Integer.toString(n));
        attributesImpl.addAttribute("", "start", "start", "", this.getLabel(label));
        this.sa.addElement("LineNumber", attributesImpl);
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        return new SAXAnnotationAdapter(this.sa, "annotationDefault", 0, null, null);
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "annotation", b ? 1 : -1, null, s);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "typeAnnotation", b ? 1 : -1, null, s, n, typePath);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int n, final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "parameterAnnotation", b ? 1 : -1, n, s);
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "insnAnnotation", b ? 1 : -1, null, s, n, typePath);
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "tryCatchAnnotation", b ? 1 : -1, null, s, n, typePath);
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int n, final TypePath typePath, final Label[] array, final Label[] array2, final int[] array3, final String s, final boolean b) {
        final String[] array4 = new String[array.length];
        final String[] array5 = new String[array2.length];
        for (int i = 0; i < array4.length; ++i) {
            array4[i] = this.getLabel(array[i]);
        }
        for (int j = 0; j < array5.length; ++j) {
            array5[j] = this.getLabel(array2[j]);
        }
        return new SAXAnnotationAdapter(this.sa, "localVariableAnnotation", b ? 1 : -1, null, s, n, typePath, array4, array5, array3);
    }
    
    public void visitEnd() {
        this.sa.addEnd("method");
    }
    
    private final String getLabel(final Label label) {
        String string = this.labelNames.get(label);
        if (string == null) {
            string = Integer.toString(this.labelNames.size());
            this.labelNames.put(label, string);
        }
        return string;
    }
    
    static {
        _clinit_();
        TYPES = new String[] { "top", "int", "float", "double", "long", "null", "uninitializedThis" };
    }
    
    static /* synthetic */ void _clinit_() {
    }
}
