// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.Attributes;

final class ASMContentHandler$MaxRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$MaxRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        this.getCodeVisitor().visitMaxs(Integer.parseInt(attributes.getValue("maxStack")), Integer.parseInt(attributes.getValue("maxLocals")));
    }
}
