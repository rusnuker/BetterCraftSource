// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

final class ASMContentHandler$RuleSet
{
    private final HashMap rules;
    private final ArrayList lpatterns;
    private final ArrayList rpatterns;
    
    ASMContentHandler$RuleSet() {
        this.rules = new HashMap();
        this.lpatterns = new ArrayList();
        this.rpatterns = new ArrayList();
    }
    
    public void add(final String s, final Object o) {
        String s2 = s;
        if (s.startsWith("*/")) {
            s2 = s.substring(1);
            this.lpatterns.add(s2);
        }
        else if (s.endsWith("/*")) {
            s2 = s.substring(0, s.length() - 1);
            this.rpatterns.add(s2);
        }
        this.rules.put(s2, o);
    }
    
    public Object match(final String s) {
        if (this.rules.containsKey(s)) {
            return this.rules.get(s);
        }
        final int lastIndex = s.lastIndexOf(47);
        final Iterator iterator = this.lpatterns.iterator();
        while (iterator.hasNext()) {
            final String s2 = (String)iterator.next();
            if (s.substring(lastIndex).endsWith(s2)) {
                return this.rules.get(s2);
            }
        }
        final Iterator iterator2 = this.rpatterns.iterator();
        while (iterator2.hasNext()) {
            final String s3 = (String)iterator2.next();
            if (s.startsWith(s3)) {
                return this.rules.get(s3);
            }
        }
        return null;
    }
}
