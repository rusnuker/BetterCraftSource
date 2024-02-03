// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.util.ArrayList;
import java.util.HashMap;

final class ASMContentHandler$ExceptionsRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$ExceptionsRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public final void end(final String s) {
        final HashMap hashMap = (HashMap)this.this$0.pop();
        final int access = this.getAccess(hashMap.get("access"));
        final String name = hashMap.get("name");
        final String descriptor = hashMap.get("desc");
        final String signature = hashMap.get("signature");
        final ArrayList list = hashMap.get("exceptions");
        this.this$0.push(this.this$0.cv.visitMethod(access, name, descriptor, signature, (String[])list.toArray(new String[list.size()])));
    }
}
