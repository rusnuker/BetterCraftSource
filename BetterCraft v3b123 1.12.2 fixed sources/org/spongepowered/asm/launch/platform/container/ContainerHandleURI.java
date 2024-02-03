// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform.container;

import java.util.Collections;
import java.util.Collection;
import org.spongepowered.asm.util.Files;
import java.io.File;
import org.spongepowered.asm.launch.platform.MainAttributes;
import java.net.URI;

public class ContainerHandleURI implements IContainerHandle
{
    private final URI uri;
    private final MainAttributes attributes;
    
    public ContainerHandleURI(final URI uri) {
        this.uri = uri;
        this.attributes = MainAttributes.of(uri);
    }
    
    public URI getURI() {
        return this.uri;
    }
    
    @Deprecated
    public File getFile() {
        return (this.uri != null && "file".equals(this.uri.getScheme())) ? Files.toFile(this.uri) : null;
    }
    
    @Override
    public String getAttribute(final String name) {
        return this.attributes.get(name);
    }
    
    @Override
    public Collection<IContainerHandle> getNestedContainers() {
        return (Collection<IContainerHandle>)Collections.emptyList();
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ContainerHandleURI && this.uri.equals(((ContainerHandleURI)other).uri);
    }
    
    @Override
    public int hashCode() {
        return this.uri.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ContainerHandleURI(%s)", this.uri);
    }
}
