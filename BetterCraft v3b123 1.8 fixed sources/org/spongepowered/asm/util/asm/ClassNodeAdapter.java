// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.ClassNode;
import java.lang.reflect.Field;

public final class ClassNodeAdapter
{
    private static final String NEST_HOST_FIELD = "nestHostClass";
    private static final String NEST_MEMBERS_FIELD = "nestMembers";
    private static final String EXPERIMENTAL_SUFFIX = "Experimental";
    private static final Field fdNestHost;
    private static final Field fdNestMembers;
    private static boolean notSupported;
    
    private ClassNodeAdapter() {
    }
    
    public static String getNestHostClass(final ClassNode classNode) {
        if (ASM.isAtLeastVersion(7)) {
            return classNode.nestHostClass;
        }
        if (ClassNodeAdapter.fdNestHost == null || ClassNodeAdapter.notSupported) {
            return null;
        }
        try {
            return (String)ClassNodeAdapter.fdNestHost.get(classNode);
        }
        catch (final ReflectiveOperationException ex) {
            ClassNodeAdapter.notSupported = true;
            return null;
        }
    }
    
    public static void setNestHostClass(final ClassNode classNode, final String nestHostClass) {
        if (ASM.isAtLeastVersion(7)) {
            classNode.nestHostClass = nestHostClass;
        }
        if (ClassNodeAdapter.fdNestHost == null || ClassNodeAdapter.notSupported) {
            return;
        }
        try {
            ClassNodeAdapter.fdNestHost.set(classNode, nestHostClass);
        }
        catch (final ReflectiveOperationException ex) {
            ClassNodeAdapter.notSupported = true;
        }
    }
    
    public static List<String> getNestMembers(final ClassNode classNode) {
        if (ASM.isAtLeastVersion(7)) {
            return classNode.nestMembers;
        }
        if (ClassNodeAdapter.fdNestMembers == null || ClassNodeAdapter.notSupported) {
            return null;
        }
        try {
            return (List)ClassNodeAdapter.fdNestMembers.get(classNode);
        }
        catch (final ReflectiveOperationException ex) {
            ClassNodeAdapter.notSupported = true;
            return null;
        }
    }
    
    public static List<String> getNestMembersAsList(final ClassNode classNode) {
        List<String> nestMembers = getNestMembers(classNode);
        if (nestMembers == null) {
            nestMembers = new ArrayList<String>();
            setNestMembers(classNode, nestMembers);
        }
        return nestMembers;
    }
    
    public static void setNestMembers(final ClassNode classNode, final List<String> nestMembers) {
        if (ASM.isAtLeastVersion(7)) {
            classNode.nestMembers = nestMembers;
            return;
        }
        if (ClassNodeAdapter.fdNestMembers == null || ClassNodeAdapter.notSupported) {
            return;
        }
        try {
            ClassNodeAdapter.fdNestMembers.set(classNode, nestMembers);
        }
        catch (final ReflectiveOperationException ex) {
            ClassNodeAdapter.notSupported = true;
        }
    }
    
    private static Field getField(final String fieldBaseName) {
        try {
            return ClassNode.class.getDeclaredField(fieldBaseName);
        }
        catch (final NoSuchFieldException ex) {
            try {
                return ClassNode.class.getDeclaredField(fieldBaseName + "Experimental");
            }
            catch (final NoSuchFieldException ex2) {
                ClassNodeAdapter.notSupported = true;
                return null;
            }
        }
    }
    
    static {
        fdNestHost = getField("nestHostClass");
        fdNestMembers = getField("nestMembers");
        ClassNodeAdapter.notSupported = false;
    }
}
