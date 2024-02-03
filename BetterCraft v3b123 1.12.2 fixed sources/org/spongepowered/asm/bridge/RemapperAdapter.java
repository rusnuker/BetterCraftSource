// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.bridge;

import org.spongepowered.asm.service.MixinService;
import org.objectweb.asm.commons.Remapper;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.util.ObfuscationUtil;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public abstract class RemapperAdapter implements IRemapper, ObfuscationUtil.IClassRemapper
{
    protected final ILogger logger;
    protected final Remapper remapper;
    
    public RemapperAdapter(final Remapper remapper) {
        this.logger = MixinService.getService().getLogger("mixin");
        this.remapper = remapper;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String mapMethodName(final String owner, final String name, final String desc) {
        this.logger.debug("{} is remapping method {}{} for {}", this, name, desc, owner);
        final String newName = this.remapper.mapMethodName(owner, name, desc);
        if (!newName.equals(name)) {
            return newName;
        }
        final String obfOwner = this.unmap(owner);
        final String obfDesc = this.unmapDesc(desc);
        this.logger.debug("{} is remapping obfuscated method {}{} for {}", this, name, obfDesc, obfOwner);
        return this.remapper.mapMethodName(obfOwner, name, obfDesc);
    }
    
    @Override
    public String mapFieldName(final String owner, final String name, final String desc) {
        this.logger.debug("{} is remapping field {}{} for {}", this, name, desc, owner);
        final String newName = this.remapper.mapFieldName(owner, name, desc);
        if (!newName.equals(name)) {
            return newName;
        }
        final String obfOwner = this.unmap(owner);
        final String obfDesc = this.unmapDesc(desc);
        this.logger.debug("{} is remapping obfuscated field {}{} for {}", this, name, obfDesc, obfOwner);
        return this.remapper.mapFieldName(obfOwner, name, obfDesc);
    }
    
    @Override
    public String map(final String typeName) {
        this.logger.debug("{} is remapping class {}", this, typeName);
        return this.remapper.map(typeName);
    }
    
    @Override
    public String unmap(final String typeName) {
        return typeName;
    }
    
    @Override
    public String mapDesc(final String desc) {
        return this.remapper.mapDesc(desc);
    }
    
    @Override
    public String unmapDesc(final String desc) {
        return ObfuscationUtil.unmapDescriptor(desc, this);
    }
}
