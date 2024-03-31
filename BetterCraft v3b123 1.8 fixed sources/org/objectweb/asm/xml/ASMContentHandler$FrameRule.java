// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;

final class ASMContentHandler$FrameRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$FrameRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final HashMap hashMap = new HashMap();
        hashMap.put("local", new ArrayList());
        hashMap.put("stack", new ArrayList());
        this.this$0.push(attributes.getValue("type"));
        this.this$0.push((attributes.getValue("count") == null) ? "0" : attributes.getValue("count"));
        this.this$0.push(hashMap);
    }
    
    public void end(final String s) {
        final HashMap hashMap = (HashMap)this.this$0.pop();
        final ArrayList list = hashMap.get("local");
        final int size = list.size();
        final Object[] array = list.toArray();
        final ArrayList list2 = hashMap.get("stack");
        final int size2 = list2.size();
        final Object[] array2 = list2.toArray();
        final String s2 = (String)this.this$0.pop();
        final String s3 = (String)this.this$0.pop();
        if ("NEW".equals(s3)) {
            this.getCodeVisitor().visitFrame(-1, size, array, size2, array2);
        }
        else if ("FULL".equals(s3)) {
            this.getCodeVisitor().visitFrame(0, size, array, size2, array2);
        }
        else if ("APPEND".equals(s3)) {
            this.getCodeVisitor().visitFrame(1, size, array, 0, null);
        }
        else if ("CHOP".equals(s3)) {
            this.getCodeVisitor().visitFrame(2, Integer.parseInt(s2), null, 0, null);
        }
        else if ("SAME".equals(s3)) {
            this.getCodeVisitor().visitFrame(3, 0, null, 0, null);
        }
        else if ("SAME1".equals(s3)) {
            this.getCodeVisitor().visitFrame(4, 0, null, size2, array2);
        }
    }
}
