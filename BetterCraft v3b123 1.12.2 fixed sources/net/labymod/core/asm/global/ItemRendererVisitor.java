// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.main.Source;

public class ItemRendererVisitor extends ClassEditor
{
    private static final boolean MC18;
    private String transformFirstPersonItemName;
    private String renderItemInFirstPersonName;
    private String itemStackName;
    private String itemRendererName;
    private String itemToRenderName;
    private String pushMatrixName;
    
    static {
        MC18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    public ItemRendererVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.transformFirstPersonItemName = LabyModTransformer.getMappingImplementation().getTransformFirstPersonItemName();
        this.renderItemInFirstPersonName = LabyModTransformer.getMappingImplementation().getRenderItemInFirstPersonName();
        this.itemStackName = LabyModTransformer.getMappingImplementation().getItemStackName();
        this.itemRendererName = LabyModTransformer.getMappingImplementation().getItemRendererName();
        this.itemToRenderName = LabyModTransformer.getMappingImplementation().getItemToRenderName();
        this.pushMatrixName = LabyModTransformer.getMappingImplementation().getPushMatrixName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        Label_0076: {
            if (name.equals(this.transformFirstPersonItemName)) {
                if (ItemRendererVisitor.MC18) {
                    if (!desc.equals("(FF)V")) {
                        break Label_0076;
                    }
                }
                else {
                    if (!desc.startsWith("(L")) {
                        break Label_0076;
                    }
                    if (!desc.endsWith(";F)V")) {
                        break Label_0076;
                    }
                }
                return new MethodVisitor(262144, mv) {
                    @Override
                    public void visitCode() {
                        this.mv.visitVarInsn(25, 0);
                        this.mv.visitFieldInsn(180, ItemRendererVisitor.this.itemRendererName, ItemRendererVisitor.this.itemToRenderName, "L" + ItemRendererVisitor.this.itemStackName + ";");
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "transformFirstPersonItem", "(L" + ItemRendererVisitor.this.itemStackName + ";)V", false);
                        super.visitCode();
                    }
                };
            }
        }
        if (name.equals(this.renderItemInFirstPersonName)) {
            if (ItemRendererVisitor.MC18) {
                if (!desc.equals("(F)V")) {
                    return mv;
                }
            }
            else if (!desc.endsWith("(;F)V") || !desc.contains(";FFL")) {
                return mv;
            }
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 184 && name.equals(ItemRendererVisitor.this.pushMatrixName)) {
                        this.mv.visitVarInsn(25, 0);
                        this.mv.visitFieldInsn(180, ItemRendererVisitor.this.itemRendererName, ItemRendererVisitor.this.itemToRenderName, "L" + ItemRendererVisitor.this.itemStackName + ";");
                        this.mv.visitIntInsn(23, 4);
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "renderItemInFirstPerson", "(L" + ItemRendererVisitor.this.itemStackName + ";F)F", false);
                        this.mv.visitIntInsn(56, 6);
                    }
                }
                
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 11 && ItemRendererVisitor.MC18) {
                        this.mv.visitIntInsn(23, 6);
                    }
                    else {
                        super.visitInsn(opcode);
                    }
                }
            };
        }
        return mv;
    }
}
