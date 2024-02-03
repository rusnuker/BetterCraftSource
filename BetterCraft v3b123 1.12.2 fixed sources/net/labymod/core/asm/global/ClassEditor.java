// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassVisitor;

public abstract class ClassEditor extends ClassVisitor
{
    private ClassEditorType type;
    
    public ClassEditor(final ClassEditorType type) {
        super(262144);
        this.type = type;
    }
    
    public void accept(final String name, final ClassNode node) {
    }
    
    public void accept(final String name, final ClassVisitor visitor) {
        this.cv = visitor;
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
    
    public String visitMapping(final String typeName) {
        return typeName;
    }
    
    public ClassEditorType getType() {
        return this.type;
    }
    
    public enum ClassEditorType
    {
        CLASS_VISITOR("CLASS_VISITOR", 0), 
        CLASS_NODE("CLASS_NODE", 1), 
        CLASS_VISITOR_AND_REMAPPER("CLASS_VISITOR_AND_REMAPPER", 2);
        
        private ClassEditorType(final String s, final int n) {
        }
    }
}
