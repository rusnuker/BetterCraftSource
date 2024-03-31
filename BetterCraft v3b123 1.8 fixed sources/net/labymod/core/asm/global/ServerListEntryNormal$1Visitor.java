// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class ServerListEntryNormal$1Visitor extends ClassEditor
{
    private String serverDataName;
    private String serverListEntryNormalName;
    private String thisClassName;
    
    public ServerListEntryNormal$1Visitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.serverDataName = LabyModTransformer.getMappingImplementation().getServerDataName();
        this.serverListEntryNormalName = LabyModTransformer.getMappingImplementation().getServerListEntryNormalName();
        this.thisClassName = String.valueOf(this.serverListEntryNormalName) + "$1";
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("run")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    super.visitFieldInsn(opcode, owner, name, desc);
                    if (owner.equals(ServerListEntryNormal$1Visitor.this.serverDataName) && name.equals(LabyModTransformer.getMappingImplementation().getServerMotdName())) {
                        this.visitVarInsn(25, 0);
                        this.visitFieldInsn(180, ServerListEntryNormal$1Visitor.this.thisClassName, "this$0", "L" + ServerListEntryNormal$1Visitor.this.serverListEntryNormalName + ";");
                        this.visitMethodInsn(184, ServerListEntryNormal$1Visitor.this.serverListEntryNormalName, "access$000", "(L" + ServerListEntryNormal$1Visitor.this.serverListEntryNormalName + ";)L" + ServerListEntryNormal$1Visitor.this.serverDataName + ";", false);
                        this.visitLdcInsn("");
                        this.visitFieldInsn(181, ServerListEntryNormal$1Visitor.this.serverDataName, LabyModTransformer.getMappingImplementation().getPopulationInfoName(), "Ljava/lang/String;");
                    }
                }
            };
        }
        return mv;
    }
}
