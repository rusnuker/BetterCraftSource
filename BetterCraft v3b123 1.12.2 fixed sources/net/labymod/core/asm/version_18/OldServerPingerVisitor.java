// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_18;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.core.asm.global.ClassEditor;

public class OldServerPingerVisitor extends ClassEditor
{
    private String serverDataName;
    
    public OldServerPingerVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.serverDataName = LabyModTransformer.getMappingImplementation().getServerDataName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (access == 1 && desc.equals("(L" + this.serverDataName + ";)V")) {
            return new MethodVisitor(262144, mv) {
                private boolean insertedIfEq;
                private boolean visitedAConstNull;
                private Label label = new Label();
                
                @Override
                public void visitInsn(final int opcode) {
                    super.visitInsn(opcode);
                    if (opcode == 87 && !this.insertedIfEq) {
                        final Label startLabel = new Label();
                        this.visitVarInsn(25, 1);
                        this.visitFieldInsn(180, OldServerPingerVisitor.this.serverDataName, LabyModTransformer.getMappingImplementation().getPingToServerName(), "J");
                        this.visitLdcInsn(new Long(-2L));
                        this.visitInsn(148);
                        this.visitJumpInsn(153, startLabel);
                        this.visitMethodInsn(184, "BytecodeMethods", "shouldKeepServerData", "()Z", false);
                        this.visitJumpInsn(153, this.label);
                        this.visitLabel(startLabel);
                        this.insertedIfEq = true;
                    }
                    if (this.insertedIfEq && opcode == 1) {
                        this.visitedAConstNull = true;
                    }
                }
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    super.visitFieldInsn(opcode, owner, name, desc);
                    if (opcode == 181 && this.visitedAConstNull) {
                        this.visitedAConstNull = false;
                        this.visitLabel(this.label);
                    }
                }
            };
        }
        return mv;
    }
}
