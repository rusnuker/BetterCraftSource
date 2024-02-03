// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.SAXException;
import java.util.ArrayList;
import org.xml.sax.Attributes;

final class ASMContentHandler$InvokeDynamicBsmArgumentsRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$InvokeDynamicBsmArgumentsRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) throws SAXException {
        ((ArrayList)this.this$0.peek()).add(this.getValue(attributes.getValue("desc"), attributes.getValue("cst")));
    }
}
