// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.inspector;

import org.yaml.snakeyaml.nodes.Tag;

public final class UnTrustedTagInspector implements TagInspector
{
    @Override
    public boolean isGlobalTagAllowed(final Tag tag) {
        return false;
    }
}
