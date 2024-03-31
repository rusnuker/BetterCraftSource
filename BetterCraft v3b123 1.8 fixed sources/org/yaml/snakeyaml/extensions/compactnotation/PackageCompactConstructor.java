// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.extensions.compactnotation;

import org.yaml.snakeyaml.LoaderOptions;

public class PackageCompactConstructor extends CompactConstructor
{
    private final String packageName;
    
    public PackageCompactConstructor(final String packageName) {
        super(new LoaderOptions());
        this.packageName = packageName;
    }
    
    @Override
    protected Class<?> getClassForName(final String name) throws ClassNotFoundException {
        if (name.indexOf(46) < 0) {
            try {
                final Class<?> clazz = Class.forName(this.packageName + "." + name);
                return clazz;
            }
            catch (final ClassNotFoundException ex) {}
        }
        return super.getClassForName(name);
    }
}
