// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch.platform.container;

import java.nio.file.Path;
import cpw.mods.jarhandling.SecureJar;

public class ContainerHandleModLauncherEx extends ContainerHandleModLauncher
{
    public ContainerHandleModLauncherEx(final String name) {
        super(name);
    }
    
    @Override
    public void addResource(final Object resource) {
        if (resource instanceof final SecureJar secureJar) {
            this.add(new SecureJarResource(secureJar));
        }
        else {
            super.addResource(resource);
        }
    }
    
    static class SecureJarResource extends ContainerHandleURI
    {
        private SecureJar jar;
        
        public SecureJarResource(final SecureJar resource) {
            super(resource.getPrimaryPath().toUri());
            this.jar = resource;
        }
        
        public String getName() {
            return this.jar.name();
        }
        
        public Path getPath() {
            return this.jar.getPrimaryPath();
        }
        
        @Override
        public String toString() {
            return String.format("SecureJarResource(%s)", this.getName());
        }
    }
}
