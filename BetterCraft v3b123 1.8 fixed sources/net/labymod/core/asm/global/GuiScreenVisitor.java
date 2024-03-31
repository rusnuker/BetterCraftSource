// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class GuiScreenVisitor extends ClassEditor
{
    private String drawWorldBackgroundName;
    private String drawScreenName;
    
    public GuiScreenVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.drawWorldBackgroundName = LabyModTransformer.getMappingImplementation().getDrawWorldBackgroundName();
        this.drawScreenName = LabyModTransformer.getMappingImplementation().getGuiScreenDrawScreenName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.drawWorldBackgroundName)) {
            return new MethodVisitor(262144, mv) {
                private Label guiBackgroundLabel;
                
                @Override
                public void visitJumpInsn(final int opcode, final Label label) {
                    this.mv.visitJumpInsn(opcode, label);
                    if (opcode == 198) {
                        this.guiBackgroundLabel = new Label();
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "isGuiBackground", "()Z", false);
                        this.mv.visitJumpInsn(153, this.guiBackgroundLabel);
                    }
                }
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 182 && this.guiBackgroundLabel != null) {
                        this.mv.visitLabel(this.guiBackgroundLabel);
                        this.guiBackgroundLabel = null;
                    }
                }
            };
        }
        if (name.equals(this.drawScreenName) && desc.equals("(IIF)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        this.mv.visitIntInsn(21, 1);
                        this.mv.visitIntInsn(21, 2);
                        this.mv.visitIntInsn(23, 3);
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "drawMenuOverlay", "(IIF)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
