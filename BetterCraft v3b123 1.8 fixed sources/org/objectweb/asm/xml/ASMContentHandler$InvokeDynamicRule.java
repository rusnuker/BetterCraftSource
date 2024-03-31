// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.Handle;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import org.xml.sax.Attributes;

final class ASMContentHandler$InvokeDynamicRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$InvokeDynamicRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) throws SAXException {
        this.this$0.push(attributes.getValue("name"));
        this.this$0.push(attributes.getValue("desc"));
        this.this$0.push(this.decodeHandle(attributes.getValue("bsm")));
        this.this$0.push(new ArrayList());
    }
    
    public final void end(final String s) {
        this.getCodeVisitor().visitInvokeDynamicInsn((String)this.this$0.pop(), (String)this.this$0.pop(), (Handle)this.this$0.pop(), ((ArrayList)this.this$0.pop()).toArray());
    }
}
