// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class CompactData
{
    private final String prefix;
    private final List<String> arguments;
    private final Map<String, String> properties;
    
    public CompactData(final String prefix) {
        this.arguments = new ArrayList<String>();
        this.properties = new HashMap<String, String>();
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public List<String> getArguments() {
        return this.arguments;
    }
    
    @Override
    public String toString() {
        return "CompactData: " + this.prefix + " " + this.properties;
    }
}
