// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import net.labymod.core.asm.LabyModCoreMod;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class GuiContainerVisitor extends ClassEditor
{
    private String itemStackName;
    private String mouseReleasedName;
    private String slotName;
    private String getStackName;
    
    public GuiContainerVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.itemStackName = LabyModTransformer.getMappingImplementation().getItemStackName();
        this.mouseReleasedName = LabyModTransformer.getMappingImplementation().getGuiContainerMouseReleasedName();
        this.slotName = LabyModTransformer.getMappingImplementation().getSlotName();
        this.getStackName = LabyModTransformer.getMappingImplementation().getSlotGetStackName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.mouseReleasedName) && desc.equals("(III)V")) {
            return new MethodVisitor(262144, mv) {
                private boolean invokeStaticCalled = false;
                private boolean getFieldCalled = false;
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    super.visitFieldInsn(opcode, owner, name, desc);
                    this.getFieldCalled = (opcode == 180 && desc.equals("L" + GuiContainerVisitor.this.itemStackName + ";"));
                }
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    final boolean invokeStaticCalled = opcode == 184;
                    this.invokeStaticCalled = invokeStaticCalled;
                    if (!invokeStaticCalled) {
                        this.getFieldCalled = false;
                    }
                }
                
                @Override
                public void visitJumpInsn(final int opcode, final Label label) {
                    super.visitJumpInsn(opcode, label);
                    if (opcode != 153) {
                        this.invokeStaticCalled = false;
                        this.getFieldCalled = false;
                    }
                    if (opcode == 153 && this.invokeStaticCalled && this.getFieldCalled) {
                        this.invokeStaticCalled = false;
                        this.getFieldCalled = false;
                        super.visitIntInsn(25, (LabyModCoreMod.isObfuscated() || LabyModCoreMod.isForge()) ? 10 : 9);
                        super.visitMethodInsn(182, GuiContainerVisitor.this.slotName, GuiContainerVisitor.this.getStackName, "()L" + GuiContainerVisitor.this.itemStackName + ";", false);
                        super.visitMethodInsn(184, "BytecodeMethods", "allowedToShiftAllItems", "(L" + GuiContainerVisitor.this.itemStackName + ";)Z", false);
                        super.visitJumpInsn(153, label);
                    }
                }
            };
        }
        return mv;
    }
}
