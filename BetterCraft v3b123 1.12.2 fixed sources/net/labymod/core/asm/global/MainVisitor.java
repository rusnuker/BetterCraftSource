// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import net.labymod.core.asm.LabyModTransformer;
import org.objectweb.asm.MethodVisitor;

public class MainVisitor extends ClassEditor
{
    public MainVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("main")) {
            return new MethodVisitor(262144, mv) {
                private String lastInvokeOwner;
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 182) {
                        this.lastInvokeOwner = owner;
                    }
                }
                
                @Override
                public void visitInsn(final int opcode) {
                    super.visitInsn(opcode);
                    if (opcode == 177 && this.lastInvokeOwner != null) {
                        LabyModTransformer.resolveMinecraftClass(this.lastInvokeOwner);
                    }
                }
            };
        }
        return mv;
    }
}
