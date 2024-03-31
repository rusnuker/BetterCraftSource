// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;

public class ScaledResolutionVisitor extends ClassEditor
{
    public ScaledResolutionVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("<init>")) {
            return new MethodVisitor(262144, mv) {
                boolean flag = true;
                
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 135) {
                        return;
                    }
                    if (opcode == 111) {
                        super.visitInsn(this.flag ? 4 : 3);
                        super.visitMethodInsn(184, "BytecodeMethods", "getCustomScale", "(IIZ)D", false);
                        this.flag = false;
                        return;
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
