// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.mixin.throwables.CompanionPluginError;
import java.util.Collections;
import java.util.List;
import org.spongepowered.asm.lib.ClassVisitor;

public class ClassNode extends ClassVisitor
{
    public final int version;
    public final int access;
    public final String name;
    public final String signature;
    public final String superName;
    public final List<String> interfaces;
    public final String sourceFile;
    public final String sourceDebug;
    public final String outerClass;
    public final String outerMethod;
    public final String outerMethodDesc;
    
    public ClassNode(final org.objectweb.asm.tree.ClassNode classNode) {
        this.version = classNode.version;
        this.access = classNode.access;
        this.name = classNode.name;
        this.signature = classNode.signature;
        this.superName = classNode.superName;
        this.interfaces = Collections.unmodifiableList((List<? extends String>)classNode.interfaces);
        this.sourceFile = classNode.sourceFile;
        this.sourceDebug = classNode.sourceDebug;
        this.outerClass = classNode.outerClass;
        this.outerMethod = classNode.outerMethod;
        this.outerMethodDesc = classNode.outerMethodDesc;
    }
    
    public void check(final int api) {
        throw new CompanionPluginError("ClassNode.check");
    }
    
    public void accept(final ClassVisitor cv) {
        throw new CompanionPluginError("ClassNode.accept");
    }
}
