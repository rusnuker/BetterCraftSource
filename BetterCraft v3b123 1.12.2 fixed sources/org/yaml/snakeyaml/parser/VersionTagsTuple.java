// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.parser;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;

class VersionTagsTuple
{
    private final DumperOptions.Version version;
    private final Map<String, String> tags;
    
    public VersionTagsTuple(final DumperOptions.Version version, final Map<String, String> tags) {
        this.version = version;
        this.tags = tags;
    }
    
    public DumperOptions.Version getVersion() {
        return this.version;
    }
    
    public Map<String, String> getTags() {
        return this.tags;
    }
    
    @Override
    public String toString() {
        return String.format("VersionTagsTuple<%s, %s>", this.version, this.tags);
    }
}
