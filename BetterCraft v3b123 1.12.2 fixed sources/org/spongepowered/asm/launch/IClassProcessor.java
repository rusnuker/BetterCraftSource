// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.launch;

import org.objectweb.asm.tree.ClassNode;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import java.util.EnumSet;
import org.objectweb.asm.Type;

public interface IClassProcessor
{
    EnumSet<ILaunchPluginService.Phase> handlesClass(final Type p0, final boolean p1, final String p2);
    
    boolean processClass(final ILaunchPluginService.Phase p0, final ClassNode p1, final Type p2, final String p3);
    
    boolean generatesClass(final Type p0);
    
    boolean generateClass(final Type p0, final ClassNode p1);
}
