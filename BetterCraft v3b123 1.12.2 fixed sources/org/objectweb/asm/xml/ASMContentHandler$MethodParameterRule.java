// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.Attributes;

final class ASMContentHandler$MethodParameterRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$MethodParameterRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        this.getCodeVisitor().visitParameter(attributes.getValue("name"), this.getAccess(attributes.getValue("access")));
    }
}
