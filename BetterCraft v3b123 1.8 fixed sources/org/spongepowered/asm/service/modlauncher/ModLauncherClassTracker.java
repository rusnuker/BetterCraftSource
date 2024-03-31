// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.Phases;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import java.util.EnumSet;
import org.objectweb.asm.Type;
import java.util.HashSet;
import java.util.Set;
import org.spongepowered.asm.service.IClassTracker;
import org.spongepowered.asm.launch.IClassProcessor;

public class ModLauncherClassTracker implements IClassProcessor, IClassTracker
{
    private final Set<String> invalidClasses;
    private final Set<String> loadedClasses;
    
    public ModLauncherClassTracker() {
        this.invalidClasses = new HashSet<String>();
        this.loadedClasses = new HashSet<String>();
    }
    
    @Override
    public void registerInvalidClass(final String className) {
        synchronized (this.invalidClasses) {
            this.invalidClasses.add(className);
        }
    }
    
    @Override
    public boolean isClassLoaded(final String className) {
        synchronized (this.loadedClasses) {
            return this.loadedClasses.contains(className);
        }
    }
    
    @Override
    public String getClassRestrictions(final String className) {
        return "";
    }
    
    @Override
    public EnumSet<ILaunchPluginService.Phase> handlesClass(final Type classType, final boolean isEmpty, final String reason) {
        final String name = classType.getClassName();
        synchronized (this.invalidClasses) {
            if (this.invalidClasses.contains(name)) {
                throw new NoClassDefFoundError(String.format("%s is invalid", name));
            }
        }
        return Phases.AFTER_ONLY;
    }
    
    @Override
    public boolean processClass(final ILaunchPluginService.Phase phase, final ClassNode classNode, final Type classType, final String reason) {
        if ("classloading".equals(reason)) {
            synchronized (this.loadedClasses) {
                this.loadedClasses.add(classType.getClassName());
            }
        }
        return false;
    }
    
    @Override
    public boolean generatesClass(final Type classType) {
        return false;
    }
    
    @Override
    public boolean generateClass(final Type classType, final ClassNode classNode) {
        return false;
    }
}
