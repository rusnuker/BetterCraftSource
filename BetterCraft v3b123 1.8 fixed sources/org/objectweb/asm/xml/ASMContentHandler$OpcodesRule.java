// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

final class ASMContentHandler$OpcodesRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$OpcodesRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) throws SAXException {
        final ASMContentHandler$Opcode asmContentHandler$Opcode = ASMContentHandler.OPCODES.get(s);
        if (asmContentHandler$Opcode == null) {
            throw new SAXException("Invalid element: " + s + " at " + this.this$0.match);
        }
        switch (asmContentHandler$Opcode.type) {
            case 0: {
                this.getCodeVisitor().visitInsn(asmContentHandler$Opcode.opcode);
                break;
            }
            case 4: {
                this.getCodeVisitor().visitFieldInsn(asmContentHandler$Opcode.opcode, attributes.getValue("owner"), attributes.getValue("name"), attributes.getValue("desc"));
                break;
            }
            case 1: {
                this.getCodeVisitor().visitIntInsn(asmContentHandler$Opcode.opcode, Integer.parseInt(attributes.getValue("value")));
                break;
            }
            case 6: {
                this.getCodeVisitor().visitJumpInsn(asmContentHandler$Opcode.opcode, this.getLabel(attributes.getValue("label")));
                break;
            }
            case 5: {
                this.getCodeVisitor().visitMethodInsn(asmContentHandler$Opcode.opcode, attributes.getValue("owner"), attributes.getValue("name"), attributes.getValue("desc"), attributes.getValue("itf").equals("true"));
                break;
            }
            case 3: {
                this.getCodeVisitor().visitTypeInsn(asmContentHandler$Opcode.opcode, attributes.getValue("desc"));
                break;
            }
            case 2: {
                this.getCodeVisitor().visitVarInsn(asmContentHandler$Opcode.opcode, Integer.parseInt(attributes.getValue("var")));
                break;
            }
            case 8: {
                this.getCodeVisitor().visitIincInsn(Integer.parseInt(attributes.getValue("var")), Integer.parseInt(attributes.getValue("inc")));
                break;
            }
            case 7: {
                this.getCodeVisitor().visitLdcInsn(this.getValue(attributes.getValue("desc"), attributes.getValue("cst")));
                break;
            }
            case 9: {
                this.getCodeVisitor().visitMultiANewArrayInsn(attributes.getValue("desc"), Integer.parseInt(attributes.getValue("dims")));
                break;
            }
            default: {
                throw new Error("Internal error");
            }
        }
    }
}
