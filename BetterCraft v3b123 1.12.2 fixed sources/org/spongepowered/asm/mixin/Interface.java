// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.CLASS)
public @interface Interface {
    Class<?> iface();
    
    String prefix();
    
    boolean unique() default false;
    
    Remap remap() default Remap.ALL;
    
    public enum Remap
    {
        ALL, 
        FORCE(true), 
        ONLY_PREFIXED, 
        NONE;
        
        private final boolean forceRemap;
        
        private Remap() {
            this(false);
        }
        
        private Remap(final boolean forceRemap) {
            this.forceRemap = forceRemap;
        }
        
        public boolean forceRemap() {
            return this.forceRemap;
        }
    }
}
