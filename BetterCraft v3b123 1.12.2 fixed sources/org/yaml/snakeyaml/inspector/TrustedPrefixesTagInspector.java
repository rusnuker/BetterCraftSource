// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.inspector;

import java.util.Iterator;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.List;

public final class TrustedPrefixesTagInspector implements TagInspector
{
    private final List<String> trustedList;
    
    public TrustedPrefixesTagInspector(final List<String> trustedList) {
        this.trustedList = trustedList;
    }
    
    @Override
    public boolean isGlobalTagAllowed(final Tag tag) {
        for (final String trusted : this.trustedList) {
            if (tag.getClassName().startsWith(trusted)) {
                return true;
            }
        }
        return false;
    }
}
