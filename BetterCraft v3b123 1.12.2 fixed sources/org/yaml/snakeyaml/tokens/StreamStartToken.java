// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class StreamStartToken extends Token
{
    public StreamStartToken(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    @Override
    public ID getTokenId() {
        return ID.StreamStart;
    }
}
