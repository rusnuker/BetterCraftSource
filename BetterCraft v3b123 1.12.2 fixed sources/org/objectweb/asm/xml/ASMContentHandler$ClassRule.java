// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;

final class ASMContentHandler$ClassRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$ClassRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        final int int1 = Integer.parseInt(attributes.getValue("major"));
        final int int2 = Integer.parseInt(attributes.getValue("minor"));
        final HashMap hashMap = new HashMap();
        hashMap.put("version", new Integer(int2 << 16 | int1));
        hashMap.put("access", attributes.getValue("access"));
        hashMap.put("name", attributes.getValue("name"));
        hashMap.put("parent", attributes.getValue("parent"));
        hashMap.put("source", attributes.getValue("source"));
        hashMap.put("signature", attributes.getValue("signature"));
        hashMap.put("interfaces", new ArrayList());
        this.this$0.push(hashMap);
    }
}
