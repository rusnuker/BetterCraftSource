// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import org.spongepowered.asm.service.MixinService;
import java.util.List;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public final class Proxy implements IClassTransformer, ILegacyClassTransformer
{
    private static List<Proxy> proxies;
    private static MixinTransformer transformer;
    private boolean isActive;
    
    public Proxy() {
        this.isActive = true;
        for (final Proxy proxy : Proxy.proxies) {
            proxy.isActive = false;
        }
        Proxy.proxies.add(this);
        MixinService.getService().getLogger("mixin").debug("Adding new mixin transformer proxy #{}", Proxy.proxies.size());
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (this.isActive) {
            return Proxy.transformer.transformClassBytes(name, transformedName, basicClass);
        }
        return basicClass;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        return true;
    }
    
    @Override
    public byte[] transformClassBytes(final String name, final String transformedName, final byte[] basicClass) {
        if (this.isActive) {
            return Proxy.transformer.transformClassBytes(name, transformedName, basicClass);
        }
        return basicClass;
    }
    
    static {
        Proxy.proxies = new ArrayList<Proxy>();
        Proxy.transformer = new MixinTransformer();
    }
}
