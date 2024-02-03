// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class GuiSlotVisitor extends ClassEditor
{
    private String overlayBackgroundName;
    private String guiName;
    private String optionsBackgroundName;
    private String textureManagerName;
    private String bindTextureName;
    private String resourceLocationName;
    private String tessellatorName;
    private String drawName;
    
    public GuiSlotVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.overlayBackgroundName = LabyModTransformer.getMappingImplementation().getGuiSlotOverlayBackgroundName();
        this.guiName = LabyModTransformer.getMappingImplementation().getGuiName();
        this.optionsBackgroundName = LabyModTransformer.getMappingImplementation().getOptionsBackgroundName();
        this.textureManagerName = LabyModTransformer.getMappingImplementation().getTextureManagerName();
        this.bindTextureName = LabyModTransformer.getMappingImplementation().getBindTextureName();
        this.resourceLocationName = LabyModTransformer.getMappingImplementation().getResourceLocationName();
        this.tessellatorName = LabyModTransformer.getMappingImplementation().getTessellatorName();
        this.drawName = LabyModTransformer.getMappingImplementation().getDrawName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.overlayBackgroundName) && desc.equals("(IIII)V")) {
            return mv;
        }
        return new MethodVisitor(262144, mv) {
            private boolean detectedOptionsBackgroundInstruction;
            private Label label;
            
            @Override
            public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                super.visitFieldInsn(opcode, owner, name, desc);
                if (opcode == 178 && owner.equals(GuiSlotVisitor.this.guiName) && name.equals(GuiSlotVisitor.this.optionsBackgroundName)) {
                    this.detectedOptionsBackgroundInstruction = true;
                }
            }
            
            @Override
            public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                if (this.detectedOptionsBackgroundInstruction && opcode == 182 && owner.equals(GuiSlotVisitor.this.textureManagerName) && name.equals(GuiSlotVisitor.this.bindTextureName) && desc.equals("(L" + GuiSlotVisitor.this.resourceLocationName + ";)V")) {
                    this.detectedOptionsBackgroundInstruction = false;
                    this.mv.visitMethodInsn(184, "BytecodeMethods", "shouldRenderMultiplayerBackground", "()Z", false);
                    this.label = new Label();
                    this.mv.visitJumpInsn(153, this.label);
                }
                if (this.label != null && owner.equals(GuiSlotVisitor.this.tessellatorName) && name.equals(GuiSlotVisitor.this.drawName) && desc.equals("()V")) {
                    this.mv.visitLabel(this.label);
                    this.label = null;
                }
            }
        };
    }
}
