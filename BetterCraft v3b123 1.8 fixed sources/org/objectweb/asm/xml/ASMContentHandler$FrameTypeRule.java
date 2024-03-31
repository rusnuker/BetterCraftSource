// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.util.HashMap;
import java.util.ArrayList;
import org.xml.sax.Attributes;

final class ASMContentHandler$FrameTypeRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$FrameTypeRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final ArrayList list = ((HashMap)this.this$0.peek()).get(s);
        final String value = attributes.getValue("type");
        if ("uninitialized".equals(value)) {
            list.add(this.getLabel(attributes.getValue("label")));
        }
        else {
            final Integer n = ASMContentHandler.TYPES.get(value);
            if (n == null) {
                list.add(value);
            }
            else {
                list.add(n);
            }
        }
    }
}
