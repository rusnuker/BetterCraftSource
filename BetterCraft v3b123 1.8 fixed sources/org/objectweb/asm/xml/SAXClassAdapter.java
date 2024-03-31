// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.ContentHandler;
import org.objectweb.asm.ClassVisitor;

public final class SAXClassAdapter extends ClassVisitor
{
    SAXAdapter sa;
    private final boolean singleDocument;
    
    public SAXClassAdapter(final ContentHandler contentHandler, final boolean singleDocument) {
        super(327680);
        this.sa = new SAXAdapter(contentHandler);
        if (!(this.singleDocument = singleDocument)) {
            this.sa.addDocumentStart();
        }
    }
    
    public void visitSource(final String s, final String s2) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        if (s != null) {
            attributesImpl.addAttribute("", "file", "file", "", encode(s));
        }
        if (s2 != null) {
            attributesImpl.addAttribute("", "debug", "debug", "", encode(s2));
        }
        this.sa.addElement("source", attributesImpl);
    }
    
    public void visitOuterClass(final String s, final String s2, final String s3) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "owner", "owner", "", s);
        if (s2 != null) {
            attributesImpl.addAttribute("", "name", "name", "", s2);
        }
        if (s3 != null) {
            attributesImpl.addAttribute("", "desc", "desc", "", s3);
        }
        this.sa.addElement("outerclass", attributesImpl);
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "annotation", b ? 1 : -1, null, s);
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        return new SAXAnnotationAdapter(this.sa, "typeAnnotation", b ? 1 : -1, null, s, n, typePath);
    }
    
    public void visit(final int n, final int n2, final String s, final String s2, final String s3, final String[] array) {
        final StringBuffer sb = new StringBuffer();
        appendAccess(n2 | 0x40000, sb);
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "access", "access", "", sb.toString());
        if (s != null) {
            attributesImpl.addAttribute("", "name", "name", "", s);
        }
        if (s2 != null) {
            attributesImpl.addAttribute("", "signature", "signature", "", encode(s2));
        }
        if (s3 != null) {
            attributesImpl.addAttribute("", "parent", "parent", "", s3);
        }
        attributesImpl.addAttribute("", "major", "major", "", Integer.toString(n & 0xFFFF));
        attributesImpl.addAttribute("", "minor", "minor", "", Integer.toString(n >>> 16));
        this.sa.addStart("class", attributesImpl);
        this.sa.addStart("interfaces", new AttributesImpl());
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                final AttributesImpl attributesImpl2 = new AttributesImpl();
                attributesImpl2.addAttribute("", "name", "name", "", array[i]);
                this.sa.addElement("interface", attributesImpl2);
            }
        }
        this.sa.addEnd("interfaces");
    }
    
    public FieldVisitor visitField(final int n, final String s, final String s2, final String s3, final Object o) {
        final StringBuffer sb = new StringBuffer();
        appendAccess(n | 0x80000, sb);
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "access", "access", "", sb.toString());
        attributesImpl.addAttribute("", "name", "name", "", s);
        attributesImpl.addAttribute("", "desc", "desc", "", s2);
        if (s3 != null) {
            attributesImpl.addAttribute("", "signature", "signature", "", encode(s3));
        }
        if (o != null) {
            attributesImpl.addAttribute("", "value", "value", "", encode(o.toString()));
        }
        return new SAXFieldAdapter(this.sa, attributesImpl);
    }
    
    public MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        final StringBuffer sb = new StringBuffer();
        appendAccess(n, sb);
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "access", "access", "", sb.toString());
        attributesImpl.addAttribute("", "name", "name", "", s);
        attributesImpl.addAttribute("", "desc", "desc", "", s2);
        if (s3 != null) {
            attributesImpl.addAttribute("", "signature", "signature", "", s3);
        }
        this.sa.addStart("method", attributesImpl);
        this.sa.addStart("exceptions", new AttributesImpl());
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                final AttributesImpl attributesImpl2 = new AttributesImpl();
                attributesImpl2.addAttribute("", "name", "name", "", array[i]);
                this.sa.addElement("exception", attributesImpl2);
            }
        }
        this.sa.addEnd("exceptions");
        return new SAXCodeAdapter(this.sa, n);
    }
    
    public final void visitInnerClass(final String s, final String s2, final String s3, final int n) {
        final StringBuffer sb = new StringBuffer();
        appendAccess(n | 0x100000, sb);
        final AttributesImpl attributesImpl = new AttributesImpl();
        attributesImpl.addAttribute("", "access", "access", "", sb.toString());
        if (s != null) {
            attributesImpl.addAttribute("", "name", "name", "", s);
        }
        if (s2 != null) {
            attributesImpl.addAttribute("", "outerName", "outerName", "", s2);
        }
        if (s3 != null) {
            attributesImpl.addAttribute("", "innerName", "innerName", "", s3);
        }
        this.sa.addElement("innerclass", attributesImpl);
    }
    
    public final void visitEnd() {
        this.sa.addEnd("class");
        if (!this.singleDocument) {
            this.sa.addDocumentEnd();
        }
    }
    
    static final String encode(final String s) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '\\') {
                sb.append("\\\\");
            }
            else if (char1 < ' ' || char1 > '\u007f') {
                sb.append("\\u");
                if (char1 < '\u0010') {
                    sb.append("000");
                }
                else if (char1 < '\u0100') {
                    sb.append("00");
                }
                else if (char1 < '\u1000') {
                    sb.append('0');
                }
                sb.append(Integer.toString(char1, 16));
            }
            else {
                sb.append(char1);
            }
        }
        return sb.toString();
    }
    
    static void appendAccess(final int n, final StringBuffer sb) {
        if ((n & 0x1) != 0x0) {
            sb.append("public ");
        }
        if ((n & 0x2) != 0x0) {
            sb.append("private ");
        }
        if ((n & 0x4) != 0x0) {
            sb.append("protected ");
        }
        if ((n & 0x10) != 0x0) {
            sb.append("final ");
        }
        if ((n & 0x8) != 0x0) {
            sb.append("static ");
        }
        if ((n & 0x20) != 0x0) {
            if ((n & 0x40000) == 0x0) {
                sb.append("synchronized ");
            }
            else {
                sb.append("super ");
            }
        }
        if ((n & 0x40) != 0x0) {
            if ((n & 0x80000) == 0x0) {
                sb.append("bridge ");
            }
            else {
                sb.append("volatile ");
            }
        }
        if ((n & 0x80) != 0x0) {
            if ((n & 0x80000) == 0x0) {
                sb.append("varargs ");
            }
            else {
                sb.append("transient ");
            }
        }
        if ((n & 0x100) != 0x0) {
            sb.append("native ");
        }
        if ((n & 0x800) != 0x0) {
            sb.append("strict ");
        }
        if ((n & 0x200) != 0x0) {
            sb.append("interface ");
        }
        if ((n & 0x400) != 0x0) {
            sb.append("abstract ");
        }
        if ((n & 0x1000) != 0x0) {
            sb.append("synthetic ");
        }
        if ((n & 0x2000) != 0x0) {
            sb.append("annotation ");
        }
        if ((n & 0x4000) != 0x0) {
            sb.append("enum ");
        }
        if ((n & 0x20000) != 0x0) {
            sb.append("deprecated ");
        }
        if ((n & 0x8000) != 0x0) {
            sb.append("mandated ");
        }
    }
}
