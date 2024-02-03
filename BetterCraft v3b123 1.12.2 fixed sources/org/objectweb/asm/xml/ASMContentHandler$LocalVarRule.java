// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.Attributes;

final class ASMContentHandler$LocalVarRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$LocalVarRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        this.getCodeVisitor().visitLocalVariable(attributes.getValue("name"), attributes.getValue("desc"), attributes.getValue("signature"), this.getLabel(attributes.getValue("start")), this.getLabel(attributes.getValue("end")), Integer.parseInt(attributes.getValue("var")));
    }
}
