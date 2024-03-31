// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class ServerListEntryNormalVisitor extends ClassEditor
{
    private String drawEntryName;
    private String serverDataName;
    
    public ServerListEntryNormalVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.drawEntryName = LabyModTransformer.getMappingImplementation().getDrawEntryName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("<init>")) {
            this.serverDataName = desc.split(";")[1].substring(1);
        }
        if (name.equals(this.drawEntryName) && (desc.equals("(IIIIIIIZ)V") || desc.equals("(IIIIIIIZF)V"))) {
            return new MethodVisitor(262144, mv) {
                private Label label = new Label();
                private boolean insertedJumpNode;
                private boolean insertedLabel;
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    if (opcode == 181 && !this.insertedJumpNode) {
                        final Label startLabel = new Label();
                        super.visitFieldInsn(opcode, owner, name, desc);
                        this.visitVarInsn(25, 0);
                        this.visitFieldInsn(180, LabyModTransformer.getMappingImplementation().getServerListEntryNormalName(), LabyModTransformer.getMappingImplementation().getServerName(), "L" + ServerListEntryNormalVisitor.this.serverDataName + ";");
                        this.visitFieldInsn(180, ServerListEntryNormalVisitor.this.serverDataName, LabyModTransformer.getMappingImplementation().getServerMotdName(), "Ljava/lang/String;");
                        this.visitJumpInsn(198, startLabel);
                        this.visitMethodInsn(184, "BytecodeMethods", "shouldKeepServerData", "()Z", false);
                        this.visitJumpInsn(153, this.label);
                        this.visitLabel(startLabel);
                        this.insertedJumpNode = true;
                        return;
                    }
                    if (opcode == 178 && this.insertedJumpNode && !this.insertedLabel) {
                        this.visitLabel(this.label);
                        super.visitFieldInsn(opcode, owner, name, desc);
                        this.insertedLabel = true;
                        return;
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            };
        }
        return mv;
    }
}
