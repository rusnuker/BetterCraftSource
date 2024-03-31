// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform.container;

import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.service.MixinService;
import java.util.Map;
import java.nio.file.Path;

public class ContainerHandleModLauncher extends ContainerHandleVirtual
{
    public ContainerHandleModLauncher(final String name) {
        super(name);
    }
    
    public void addResource(final String name, final Path path) {
        this.add(new Resource(name, path));
    }
    
    public void addResource(final Map.Entry<String, Path> entry) {
        this.add(new Resource(entry.getKey(), entry.getValue()));
    }
    
    public void addResource(final Object resource) {
        if (resource instanceof Map.Entry) {
            this.addResource((Map.Entry<String, Path>)resource);
        }
        else {
            MixinService.getService().getLogger("mixin").error("Unrecognised resource type {} passed to {}", resource.getClass(), this);
        }
    }
    
    public void addResources(final List<?> resources) {
        for (final Object resource : resources) {
            this.addResource(resource);
        }
    }
    
    @Override
    public String toString() {
        return String.format("ModLauncher Root Container(%s:%x)", this.getName(), this.hashCode());
    }
    
    class Resource extends ContainerHandleURI
    {
        private String name;
        private Path path;
        
        public Resource(final String name, final Path path) {
            super(path.toUri());
            this.name = name;
            this.path = path;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Path getPath() {
            return this.path;
        }
        
        @Override
        public String toString() {
            return String.format("ContainerHandleModLauncher.Resource(%s:%s)", this.name, this.path);
        }
    }
}
