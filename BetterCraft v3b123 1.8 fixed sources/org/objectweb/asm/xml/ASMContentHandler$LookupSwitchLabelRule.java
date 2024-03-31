// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.Label;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;

final class ASMContentHandler$LookupSwitchLabelRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$LookupSwitchLabelRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        final HashMap hashMap = (HashMap)this.this$0.peek();
        ((ArrayList<Label>)hashMap.get("labels")).add(this.getLabel(attributes.getValue("name")));
        ((ArrayList<String>)hashMap.get("keys")).add(attributes.getValue("key"));
    }
}
