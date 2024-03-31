// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform.container;

import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class ContainerHandleVirtual implements IContainerHandle
{
    private final String name;
    private final Map<String, String> attributes;
    private final Set<IContainerHandle> nestedContainers;
    
    public ContainerHandleVirtual(final String name) {
        this.attributes = new HashMap<String, String>();
        this.nestedContainers = new LinkedHashSet<IContainerHandle>();
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ContainerHandleVirtual setAttribute(final String key, final String value) {
        this.attributes.put(key, value);
        return this;
    }
    
    public ContainerHandleVirtual add(final IContainerHandle nested) {
        this.nestedContainers.add(nested);
        return this;
    }
    
    @Override
    public String getAttribute(final String name) {
        return this.attributes.get(name);
    }
    
    @Override
    public Collection<IContainerHandle> getNestedContainers() {
        return (Collection<IContainerHandle>)Collections.unmodifiableSet((Set<?>)this.nestedContainers);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof String && obj.toString().equals(this.name);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("ContainerHandleVirtual(%s:%x)", this.name, this.hashCode());
    }
}
