// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.LoaderOptions;

public class CustomClassLoaderConstructor extends Constructor
{
    private final ClassLoader loader;
    
    public CustomClassLoaderConstructor(final ClassLoader loader, final LoaderOptions loadingConfig) {
        this(Object.class, loader, loadingConfig);
    }
    
    public CustomClassLoaderConstructor(final Class<?> theRoot, final ClassLoader theLoader, final LoaderOptions loadingConfig) {
        super(theRoot, loadingConfig);
        if (theLoader == null) {
            throw new NullPointerException("Loader must be provided.");
        }
        this.loader = theLoader;
    }
    
    @Override
    protected Class<?> getClassForName(final String name) throws ClassNotFoundException {
        return Class.forName(name, true, this.loader);
    }
}
