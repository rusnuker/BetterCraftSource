// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.Label;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;

final class ASMContentHandler$TableSwitchRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$TableSwitchRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void begin(final String s, final Attributes attributes) {
        final HashMap hashMap = new HashMap();
        hashMap.put("min", attributes.getValue("min"));
        hashMap.put("max", attributes.getValue("max"));
        hashMap.put("dflt", attributes.getValue("dflt"));
        hashMap.put("labels", new ArrayList());
        this.this$0.push(hashMap);
    }
    
    public final void end(final String s) {
        final HashMap hashMap = (HashMap)this.this$0.pop();
        final int int1 = Integer.parseInt(hashMap.get("min"));
        final int int2 = Integer.parseInt(hashMap.get("max"));
        final Label label = this.getLabel(hashMap.get("dflt"));
        final ArrayList list = hashMap.get("labels");
        this.getCodeVisitor().visitTableSwitchInsn(int1, int2, label, (Label[])list.toArray(new Label[list.size()]));
    }
}
