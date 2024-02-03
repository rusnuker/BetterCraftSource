// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.Type;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;

public final class SAXAnnotationAdapter extends AnnotationVisitor
{
    SAXAdapter sa;
    private final String elementName;
    
    public SAXAnnotationAdapter(final SAXAdapter saxAdapter, final String s, final int n, final String s2, final String s3) {
        this(327680, saxAdapter, s, n, s3, s2, -1, -1, null, null, null, null);
    }
    
    public SAXAnnotationAdapter(final SAXAdapter saxAdapter, final String s, final int n, final int n2, final String s2) {
        this(327680, saxAdapter, s, n, s2, null, n2, -1, null, null, null, null);
    }
    
    public SAXAnnotationAdapter(final SAXAdapter saxAdapter, final String s, final int n, final String s2, final String s3, final int n2, final TypePath typePath) {
        this(327680, saxAdapter, s, n, s3, s2, -1, n2, typePath, null, null, null);
    }
    
    public SAXAnnotationAdapter(final SAXAdapter saxAdapter, final String s, final int n, final String s2, final String s3, final int n2, final TypePath typePath, final String[] array, final String[] array2, final int[] array3) {
        this(327680, saxAdapter, s, n, s3, s2, -1, n2, typePath, array, array2, array3);
    }
    
    protected SAXAnnotationAdapter(final int n, final SAXAdapter saxAdapter, final String s, final int n2, final String s2, final String s3, final int n3) {
        this(n, saxAdapter, s, n2, s2, s3, n3, -1, null, null, null, null);
    }
    
    protected SAXAnnotationAdapter(final int api, final SAXAdapter sa, final String elementName, final int n, final String s, final String s2, final int n2, final int n3, final TypePath typePath, final String[] array, final String[] array2, final int[] array3) {
        super(api);
        this.sa = sa;
        this.elementName = elementName;
        final AttributesImpl attributesImpl = new AttributesImpl();
        if (s2 != null) {
            attributesImpl.addAttribute("", "name", "name", "", s2);
        }
        if (n != 0) {
            attributesImpl.addAttribute("", "visible", "visible", "", (n > 0) ? "true" : "false");
        }
        if (n2 != -1) {
            attributesImpl.addAttribute("", "parameter", "parameter", "", Integer.toString(n2));
        }
        if (s != null) {
            attributesImpl.addAttribute("", "desc", "desc", "", s);
        }
        if (n3 != -1) {
            attributesImpl.addAttribute("", "typeRef", "typeRef", "", Integer.toString(n3));
        }
        if (typePath != null) {
            attributesImpl.addAttribute("", "typePath", "typePath", "", typePath.toString());
        }
        if (array != null) {
            final StringBuffer sb = new StringBuffer(array[0]);
            for (int i = 1; i < array.length; ++i) {
                sb.append(" ").append(array[i]);
            }
            attributesImpl.addAttribute("", "start", "start", "", sb.toString());
        }
        if (array2 != null) {
            final StringBuffer sb2 = new StringBuffer(array2[0]);
            for (int j = 1; j < array2.length; ++j) {
                sb2.append(" ").append(array2[j]);
            }
            attributesImpl.addAttribute("", "end", "end", "", sb2.toString());
        }
        if (array3 != null) {
            final StringBuffer sb3 = new StringBuffer();
            sb3.append(array3[0]);
            for (int k = 1; k < array3.length; ++k) {
                sb3.append(" ").append(array3[k]);
            }
            attributesImpl.addAttribute("", "index", "index", "", sb3.toString());
        }
        sa.addStart(elementName, attributesImpl);
    }
    
    public void visit(final String s, final Object o) {
        final Class<?> class1 = o.getClass();
        if (class1.isArray()) {
            final AnnotationVisitor visitArray = this.visitArray(s);
            if (o instanceof byte[]) {
                final byte[] array = (byte[])o;
                for (int i = 0; i < array.length; ++i) {
                    visitArray.visit(null, new Byte(array[i]));
                }
            }
            else if (o instanceof char[]) {
                final char[] array2 = (char[])o;
                for (int j = 0; j < array2.length; ++j) {
                    visitArray.visit(null, new Character(array2[j]));
                }
            }
            else if (o instanceof short[]) {
                final short[] array3 = (short[])o;
                for (int k = 0; k < array3.length; ++k) {
                    visitArray.visit(null, new Short(array3[k]));
                }
            }
            else if (o instanceof boolean[]) {
                final boolean[] array4 = (boolean[])o;
                for (int l = 0; l < array4.length; ++l) {
                    visitArray.visit(null, array4[l]);
                }
            }
            else if (o instanceof int[]) {
                final int[] array5 = (int[])o;
                for (int n = 0; n < array5.length; ++n) {
                    visitArray.visit(null, new Integer(array5[n]));
                }
            }
            else if (o instanceof long[]) {
                final long[] array6 = (long[])o;
                for (int n2 = 0; n2 < array6.length; ++n2) {
                    visitArray.visit(null, new Long(array6[n2]));
                }
            }
            else if (o instanceof float[]) {
                final float[] array7 = (float[])o;
                for (int n3 = 0; n3 < array7.length; ++n3) {
                    visitArray.visit(null, new Float(array7[n3]));
                }
            }
            else if (o instanceof double[]) {
                final double[] array8 = (double[])o;
                for (int n4 = 0; n4 < array8.length; ++n4) {
                    visitArray.visit(null, new Double(array8[n4]));
                }
            }
            visitArray.visitEnd();
        }
        else {
            this.addValueElement("annotationValue", s, Type.getDescriptor(class1), o.toString());
        }
    }
    
    public void visitEnum(final String s, final String s2, final String s3) {
        this.addValueElement("annotationValueEnum", s, s2, s3);
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final String s2) {
        return new SAXAnnotationAdapter(this.sa, "annotationValueAnnotation", 0, s, s2);
    }
    
    public AnnotationVisitor visitArray(final String s) {
        return new SAXAnnotationAdapter(this.sa, "annotationValueArray", 0, s, null);
    }
    
    public void visitEnd() {
        this.sa.addEnd(this.elementName);
    }
    
    private void addValueElement(final String s, final String s2, final String s3, final String s4) {
        final AttributesImpl attributesImpl = new AttributesImpl();
        if (s2 != null) {
            attributesImpl.addAttribute("", "name", "name", "", s2);
        }
        if (s3 != null) {
            attributesImpl.addAttribute("", "desc", "desc", "", s3);
        }
        if (s4 != null) {
            attributesImpl.addAttribute("", "value", "value", "", SAXClassAdapter.encode(s4));
        }
        this.sa.addElement(s, attributesImpl);
    }
}
