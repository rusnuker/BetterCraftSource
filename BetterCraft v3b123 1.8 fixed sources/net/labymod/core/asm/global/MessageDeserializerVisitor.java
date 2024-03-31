// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;

public class MessageDeserializerVisitor extends ClassEditor
{
    public MessageDeserializerVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (desc.endsWith(";Ljava/util/List;)V")) {
            return new MethodVisitor(262144, mv) {
                private boolean add = false;
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 185 && owner.equals("java/util/List") && name.equals("add") && desc.equals("(Ljava/lang/Object;)Z")) {
                        this.add = true;
                    }
                }
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    if (opcode == 178 && this.add) {
                        this.add = false;
                        super.visitIntInsn(25, 6);
                        super.visitMethodInsn(184, "BytecodeMethods", "onIncomingPacket", "(Ljava/lang/Object;)V", false);
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            };
        }
        return mv;
    }
}
