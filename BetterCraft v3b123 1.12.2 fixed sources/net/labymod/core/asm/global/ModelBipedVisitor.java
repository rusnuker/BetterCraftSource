// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;

public class ModelBipedVisitor extends ClassEditor
{
    public ModelBipedVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (desc.startsWith("(FFFFFFL") && desc.endsWith(";)V")) {
            return new MethodVisitor(262144, mv) {
                private boolean preAdded = false;
                
                @Override
                public void visitVarInsn(final int opcode, final int var) {
                    super.visitVarInsn(opcode, var);
                    if (!this.preAdded && opcode == 25 && var == 0) {
                        this.preAdded = true;
                        super.visitVarInsn(25, 7);
                        super.visitMethodInsn(184, "BytecodeMethods", "transformModelPre", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
                        super.visitVarInsn(25, 0);
                    }
                }
                
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        super.visitVarInsn(25, 0);
                        super.visitVarInsn(25, 7);
                        super.visitMethodInsn(184, "BytecodeMethods", "transformModelPost", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
