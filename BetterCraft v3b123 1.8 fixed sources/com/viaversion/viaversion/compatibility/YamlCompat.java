// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.compatibility;

import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.DumperOptions;

public interface YamlCompat
{
    Representer createRepresenter(final DumperOptions p0);
    
    SafeConstructor createSafeConstructor();
    
    default boolean isVersion1() {
        try {
            SafeConstructor.class.getDeclaredConstructor((Class<?>[])new Class[0]);
            return true;
        }
        catch (final NoSuchMethodException e) {
            return false;
        }
    }
}
