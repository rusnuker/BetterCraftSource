// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public abstract class ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    static /* synthetic */ Class class$org$objectweb$asm$Type;
    static /* synthetic */ Class class$org$objectweb$asm$Handle;
    
    protected ASMContentHandler$Rule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
    }
    
    public void begin(final String s, final Attributes attributes) throws SAXException {
    }
    
    public void end(final String s) {
    }
    
    protected final Object getValue(final String s, final String typeDescriptor) throws SAXException {
        Object o = null;
        if (typeDescriptor != null) {
            if ("Ljava/lang/String;".equals(s)) {
                o = this.decode(typeDescriptor);
            }
            else if ("Ljava/lang/Integer;".equals(s) || "I".equals(s) || "S".equals(s) || "B".equals(s) || "C".equals(s) || "Z".equals(s)) {
                o = new Integer(typeDescriptor);
            }
            else if ("Ljava/lang/Short;".equals(s)) {
                o = new Short(typeDescriptor);
            }
            else if ("Ljava/lang/Byte;".equals(s)) {
                o = new Byte(typeDescriptor);
            }
            else if ("Ljava/lang/Character;".equals(s)) {
                o = new Character(this.decode(typeDescriptor).charAt(0));
            }
            else if ("Ljava/lang/Boolean;".equals(s)) {
                o = Boolean.valueOf(typeDescriptor);
            }
            else if ("Ljava/lang/Long;".equals(s) || "J".equals(s)) {
                o = new Long(typeDescriptor);
            }
            else if ("Ljava/lang/Float;".equals(s) || "F".equals(s)) {
                o = new Float(typeDescriptor);
            }
            else if ("Ljava/lang/Double;".equals(s) || "D".equals(s)) {
                o = new Double(typeDescriptor);
            }
            else if (Type.getDescriptor(ASMContentHandler$Rule.class$org$objectweb$asm$Type).equals(s)) {
                o = Type.getType(typeDescriptor);
            }
            else {
                if (!Type.getDescriptor(ASMContentHandler$Rule.class$org$objectweb$asm$Handle).equals(s)) {
                    throw new SAXException("Invalid value:" + typeDescriptor + " desc:" + s + " ctx:" + this);
                }
                o = this.decodeHandle(typeDescriptor);
            }
        }
        return o;
    }
    
    Handle decodeHandle(final String s) throws SAXException {
        try {
            final int index = s.indexOf(46);
            final int index2 = s.indexOf(40, index + 1);
            final int lastIndex = s.lastIndexOf(40);
            final int index3 = s.indexOf(32, lastIndex + 1);
            final boolean isInterface = index3 != -1;
            return new Handle(Integer.parseInt(s.substring(lastIndex + 1, isInterface ? (s.length() - 1) : index3)), s.substring(0, index), s.substring(index + 1, index2), s.substring(index2, lastIndex - 1), isInterface);
        }
        catch (final RuntimeException ex) {
            throw new SAXException("Malformed handle " + s, ex);
        }
    }
    
    private final String decode(final String s) throws SAXException {
        final StringBuffer sb = new StringBuffer(s.length());
        try {
            for (int i = 0; i < s.length(); ++i) {
                final char char1 = s.charAt(i);
                if (char1 == '\\') {
                    ++i;
                    if (s.charAt(i) == '\\') {
                        sb.append('\\');
                    }
                    else {
                        ++i;
                        sb.append((char)Integer.parseInt(s.substring(i, i + 4), 16));
                        i += 3;
                    }
                }
                else {
                    sb.append(char1);
                }
            }
        }
        catch (final RuntimeException ex) {
            throw new SAXException(ex);
        }
        return sb.toString();
    }
    
    protected final Label getLabel(final Object o) {
        Label label = this.this$0.labels.get(o);
        if (label == null) {
            label = new Label();
            this.this$0.labels.put(o, label);
        }
        return label;
    }
    
    protected final MethodVisitor getCodeVisitor() {
        return (MethodVisitor)this.this$0.peek();
    }
    
    protected final int getAccess(final String s) {
        int n = 0;
        if (s.indexOf("public") != -1) {
            n |= 0x1;
        }
        if (s.indexOf("private") != -1) {
            n |= 0x2;
        }
        if (s.indexOf("protected") != -1) {
            n |= 0x4;
        }
        if (s.indexOf("static") != -1) {
            n |= 0x8;
        }
        if (s.indexOf("final") != -1) {
            n |= 0x10;
        }
        if (s.indexOf("super") != -1) {
            n |= 0x20;
        }
        if (s.indexOf("synchronized") != -1) {
            n |= 0x20;
        }
        if (s.indexOf("volatile") != -1) {
            n |= 0x40;
        }
        if (s.indexOf("bridge") != -1) {
            n |= 0x40;
        }
        if (s.indexOf("varargs") != -1) {
            n |= 0x80;
        }
        if (s.indexOf("transient") != -1) {
            n |= 0x80;
        }
        if (s.indexOf("native") != -1) {
            n |= 0x100;
        }
        if (s.indexOf("interface") != -1) {
            n |= 0x200;
        }
        if (s.indexOf("abstract") != -1) {
            n |= 0x400;
        }
        if (s.indexOf("strict") != -1) {
            n |= 0x800;
        }
        if (s.indexOf("synthetic") != -1) {
            n |= 0x1000;
        }
        if (s.indexOf("annotation") != -1) {
            n |= 0x2000;
        }
        if (s.indexOf("enum") != -1) {
            n |= 0x4000;
        }
        if (s.indexOf("deprecated") != -1) {
            n |= 0x20000;
        }
        if (s.indexOf("mandated") != -1) {
            n |= 0x8000;
        }
        return n;
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
        ASMContentHandler$Rule.class$org$objectweb$asm$Type = class$("org.objectweb.asm.Type");
        ASMContentHandler$Rule.class$org$objectweb$asm$Handle = class$("org.objectweb.asm.Handle");
    }
}
