// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.Attributes;

final class ASMContentHandler$InnerClassRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$InnerClassRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        this.this$0.cv.visitInnerClass(attributes.getValue("name"), attributes.getValue("outerName"), attributes.getValue("innerName"), this.getAccess(attributes.getValue("access")));
    }
}
