// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;

final class ASMContentHandler$MethodRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$MethodRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        this.this$0.labels = new HashMap();
        final HashMap hashMap = new HashMap();
        hashMap.put("access", attributes.getValue("access"));
        hashMap.put("name", attributes.getValue("name"));
        hashMap.put("desc", attributes.getValue("desc"));
        hashMap.put("signature", attributes.getValue("signature"));
        hashMap.put("exceptions", new ArrayList());
        this.this$0.push(hashMap);
    }
    
    public final void end(final String s) {
        ((MethodVisitor)this.this$0.pop()).visitEnd();
        this.this$0.labels = null;
    }
}
