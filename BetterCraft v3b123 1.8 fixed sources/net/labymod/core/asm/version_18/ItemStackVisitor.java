// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_18;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.core.asm.global.ClassEditor;

public class ItemStackVisitor extends ClassEditor
{
    private String itemStackName;
    private String getIsItemStackEqualName;
    
    public ItemStackVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.itemStackName = LabyModTransformer.getMappingImplementation().getItemStackName();
        this.getIsItemStackEqualName = (LabyModCoreMod.isObfuscated() ? "c" : "getIsItemStackEqual");
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.getIsItemStackEqualName) && desc.equals("(L" + this.itemStackName + ";)Z")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 183) {
                        super.visitVarInsn(25, 0);
                        super.visitVarInsn(25, 1);
                        super.visitMethodInsn(184, "BytecodeMethods", "isItemStackEqual", "(ZL" + ItemStackVisitor.this.itemStackName + ";L" + ItemStackVisitor.this.itemStackName + ";)Z", false);
                    }
                }
            };
        }
        return mv;
    }
}
