// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import java.util.HashMap;
import org.spongepowered.asm.service.IPropertyKey;
import java.util.Map;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.service.IGlobalPropertyService;

public final class GlobalProperties
{
    private static IGlobalPropertyService service;
    
    private GlobalProperties() {
    }
    
    private static IGlobalPropertyService getService() {
        if (GlobalProperties.service == null) {
            GlobalProperties.service = MixinService.getGlobalPropertyService();
        }
        return GlobalProperties.service;
    }
    
    public static <T> T get(final Keys key) {
        final IGlobalPropertyService service = getService();
        return service.getProperty(key.resolve(service));
    }
    
    public static void put(final Keys key, final Object value) {
        final IGlobalPropertyService service = getService();
        service.setProperty(key.resolve(service), value);
    }
    
    public static <T> T get(final Keys key, final T defaultValue) {
        final IGlobalPropertyService service = getService();
        return service.getProperty(key.resolve(service), defaultValue);
    }
    
    public static String getString(final Keys key, final String defaultValue) {
        final IGlobalPropertyService service = getService();
        return service.getPropertyString(key.resolve(service), defaultValue);
    }
    
    public static final class Keys
    {
        public static final Keys INIT;
        public static final Keys AGENTS;
        public static final Keys CONFIGS;
        public static final Keys PLATFORM_MANAGER;
        public static final Keys FML_LOAD_CORE_MOD;
        public static final Keys FML_GET_REPARSEABLE_COREMODS;
        public static final Keys FML_CORE_MOD_MANAGER;
        public static final Keys FML_GET_IGNORED_MODS;
        private static Map<String, Keys> keys;
        private final String name;
        private IPropertyKey key;
        
        private Keys(final String name) {
            this.name = name;
        }
        
        IPropertyKey resolve(final IGlobalPropertyService service) {
            if (this.key != null) {
                return this.key;
            }
            if (service == null) {
                return null;
            }
            return this.key = service.resolveKey(this.name);
        }
        
        public static Keys of(final String name) {
            if (Keys.keys == null) {
                Keys.keys = new HashMap<String, Keys>();
            }
            Keys key = Keys.keys.get(name);
            if (key == null) {
                key = new Keys(name);
                Keys.keys.put(name, key);
            }
            return key;
        }
        
        static {
            INIT = of("mixin.initialised");
            AGENTS = of("mixin.agents");
            CONFIGS = of("mixin.configs");
            PLATFORM_MANAGER = of("mixin.platform");
            FML_LOAD_CORE_MOD = of("mixin.launch.fml.loadcoremodmethod");
            FML_GET_REPARSEABLE_COREMODS = of("mixin.launch.fml.reparseablecoremodsmethod");
            FML_CORE_MOD_MANAGER = of("mixin.launch.fml.coremodmanagerclass");
            FML_GET_IGNORED_MODS = of("mixin.launch.fml.ignoredmodsmethod");
        }
    }
}
