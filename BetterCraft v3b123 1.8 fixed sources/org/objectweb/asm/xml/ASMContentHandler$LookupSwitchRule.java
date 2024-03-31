// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.Label;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;

final class ASMContentHandler$LookupSwitchRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$LookupSwitchRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        final HashMap hashMap = new HashMap();
        hashMap.put("dflt", attributes.getValue("dflt"));
        hashMap.put("labels", new ArrayList());
        hashMap.put("keys", new ArrayList());
        this.this$0.push(hashMap);
    }
    
    public final void end(final String s) {
        final HashMap hashMap = (HashMap)this.this$0.pop();
        final Label label = this.getLabel(hashMap.get("dflt"));
        final ArrayList list = hashMap.get("keys");
        final ArrayList list2 = hashMap.get("labels");
        final Label[] labels = (Label[])list2.toArray(new Label[list2.size()]);
        final int[] keys = new int[list.size()];
        for (int i = 0; i < keys.length; ++i) {
            keys[i] = Integer.parseInt((String)list.get(i));
        }
        this.getCodeVisitor().visitLookupSwitchInsn(label, keys, labels);
    }
}
