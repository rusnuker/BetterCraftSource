// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

public final class SetUtils
{
    private SetUtils() {
    }
    
    public static String[] prefixSet(final Set<String> set, final String prefix) {
        final Set<String> prefixSet = new HashSet<String>();
        for (final String str : set) {
            if (str.startsWith(prefix)) {
                prefixSet.add(str);
            }
        }
        return prefixSet.toArray(new String[prefixSet.size()]);
    }
}
