// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.error.Mark;
import java.util.List;

public final class DirectiveToken<T> extends Token
{
    private final String name;
    private final List<T> value;
    
    public DirectiveToken(final String name, final List<T> value, final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
        this.name = name;
        if (value != null && value.size() != 2) {
            throw new YAMLException("Two strings must be provided instead of " + value.size());
        }
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<T> getValue() {
        return this.value;
    }
    
    @Override
    public ID getTokenId() {
        return ID.Directive;
    }
}
