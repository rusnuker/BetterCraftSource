// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import net.labymod.main.Source;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.core.asm.LabyModTransformer;

public class EntityRendererVisitor extends ClassEditor
{
    private String entityName;
    private String orientCameraName;
    private String renderWorldDirectionsName;
    
    public EntityRendererVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.entityName = LabyModTransformer.getMappingImplementation().getEntityClassName();
        this.orientCameraName = LabyModTransformer.getMappingImplementation().getOrientCameraName();
        this.renderWorldDirectionsName = (LabyModCoreMod.isObfuscated() ? "h" : "renderWorldDirections");
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.orientCameraName) && desc.equals("(F)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        super.visitIntInsn(25, 2);
                        super.visitInsn(3);
                        super.visitMethodInsn(184, "BytecodeMethods", "translateEyeHeight", "(L" + EntityRendererVisitor.this.entityName + ";Z)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        if (!Source.ABOUT_MC_VERSION.startsWith("1.8") && desc.equals("(L" + this.entityName + ";)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        super.visitIntInsn(25, 1);
                        super.visitMethodInsn(184, "BytecodeMethods", "onLoadEntityShader", "(L" + EntityRendererVisitor.this.entityName + ";)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        if (Source.ABOUT_MC_VERSION.startsWith("1.8") && name.equals(this.renderWorldDirectionsName) && desc.equals("(F)V")) {
            return new MethodVisitor(262144, mv) {
                private int called = 0;
                
                @Override
                public void visitTypeInsn(final int opcode, final String type) {
                    if (this.called == 0 && opcode == 187) {
                        super.visitIntInsn(25, 2);
                        super.visitInsn(4);
                        super.visitMethodInsn(184, "BytecodeMethods", "translateEyeHeight", "(L" + EntityRendererVisitor.this.entityName + ";Z)V", false);
                    }
                    super.visitTypeInsn(opcode, type);
                    ++this.called;
                }
            };
        }
        return mv;
    }
}
