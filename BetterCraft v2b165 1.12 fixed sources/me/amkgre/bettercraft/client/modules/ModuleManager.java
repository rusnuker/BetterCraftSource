// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.modules;

import java.util.Iterator;
import me.amkgre.bettercraft.client.modules.impl.WerbungModule;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager
{
    public List<Module> modules;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.addModule(new WerbungModule());
    }
    
    public void addModule(final Module module) {
        this.modules.add(module);
    }
    
    public List<Module> getModules() {
        return this.modules;
    }
    
    public Module getModuleByName(final String modulename) {
        for (final Module m : this.modules) {
            if (!m.getName().trim().equalsIgnoreCase(modulename) && !m.toString().trim().equalsIgnoreCase(modulename.trim())) {
                continue;
            }
            return m;
        }
        return null;
    }
    
    public Module getModule(final Class<? extends Module> clazz) {
        final Iterator<Module> localIterator = this.modules.iterator();
        if (localIterator.hasNext()) {
            final Module m = localIterator.next();
            return m;
        }
        return null;
    }
}
