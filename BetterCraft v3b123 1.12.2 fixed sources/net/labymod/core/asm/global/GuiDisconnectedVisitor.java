// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class GuiDisconnectedVisitor extends ClassEditor
{
    private String initGuiName;
    private String messageName;
    private String iChatComponentName;
    private String guiDisconnectedName;
    private String getUnformattedTextForChatName;
    private String parentScreenName;
    private String guiScreenName;
    
    public GuiDisconnectedVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.initGuiName = LabyModTransformer.getMappingImplementation().getInitGuiName();
        this.messageName = LabyModTransformer.getMappingImplementation().getGuiDisconnectedMessageName();
        this.iChatComponentName = LabyModTransformer.getMappingImplementation().getChatComponentClassName();
        this.guiDisconnectedName = LabyModTransformer.getMappingImplementation().getGuiDisconnectedName();
        this.getUnformattedTextForChatName = LabyModTransformer.getMappingImplementation().getGetUnformattedTextForChatName();
        this.parentScreenName = LabyModTransformer.getMappingImplementation().getParentScreenName();
        this.guiScreenName = LabyModTransformer.getMappingImplementation().getGuiScreenName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.initGuiName) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        this.mv.visitVarInsn(25, 0);
                        this.mv.visitFieldInsn(180, GuiDisconnectedVisitor.this.guiDisconnectedName, GuiDisconnectedVisitor.this.messageName, "L" + GuiDisconnectedVisitor.this.iChatComponentName + ";");
                        this.mv.visitMethodInsn(185, GuiDisconnectedVisitor.this.iChatComponentName, GuiDisconnectedVisitor.this.getUnformattedTextForChatName, "()Ljava/lang/String;", true);
                        this.mv.visitVarInsn(25, 0);
                        this.mv.visitFieldInsn(180, GuiDisconnectedVisitor.this.guiDisconnectedName, GuiDisconnectedVisitor.this.parentScreenName, "L" + GuiDisconnectedVisitor.this.guiScreenName + ";");
                        this.mv.visitMethodInsn(184, "net/labymod/gui/GuiWebPanel", "open", "(Ljava/lang/String;L" + GuiDisconnectedVisitor.this.guiScreenName + ";)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
