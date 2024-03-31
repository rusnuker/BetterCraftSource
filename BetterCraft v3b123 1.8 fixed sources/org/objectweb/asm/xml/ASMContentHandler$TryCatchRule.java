// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.Attributes;

final class ASMContentHandler$TryCatchRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$TryCatchRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        this.getCodeVisitor().visitTryCatchBlock(this.getLabel(attributes.getValue("start")), this.getLabel(attributes.getValue("end")), this.getLabel(attributes.getValue("handler")), attributes.getValue("type"));
    }
}
