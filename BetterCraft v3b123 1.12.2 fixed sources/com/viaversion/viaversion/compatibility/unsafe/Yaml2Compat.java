// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.compatibility.unsafe;

import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.DumperOptions;
import com.viaversion.viaversion.compatibility.YamlCompat;

public final class Yaml2Compat implements YamlCompat
{
    @Override
    public Representer createRepresenter(final DumperOptions dumperOptions) {
        return new Representer(dumperOptions);
    }
    
    @Override
    public SafeConstructor createSafeConstructor() {
        return new CustomSafeConstructor();
    }
    
    private static final class CustomSafeConstructor extends SafeConstructor
    {
        public CustomSafeConstructor() {
            super(new LoaderOptions());
            this.yamlClassConstructors.put(NodeId.mapping, new ConstructYamlMap());
            this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
        }
    }
}
