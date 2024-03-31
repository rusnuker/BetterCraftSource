// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_112;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.core.asm.global.ClassEditor;

public class EntityLivingBaseVisitor extends ClassEditor
{
    private String entityName;
    private String lastAttackerTimeName;
    
    public EntityLivingBaseVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.entityName = (LabyModCoreMod.isObfuscated() ? "vg" : "net/minecraft/entity/Entity");
        this.lastAttackerTimeName = LabyModTransformer.getMappingImplementation().getLastAttackerTimeName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                private boolean canModify = false;
                private boolean modified = false;
                
                @Override
                public void visitLdcInsn(final Object cst) {
                    super.visitLdcInsn(cst);
                    if (!this.modified && this.canModify && cst instanceof Float && (float)cst == 180.0f) {
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "subtractBackwardsWalkingAnimation", "(F)F", false);
                        this.canModify = false;
                        this.modified = true;
                    }
                }
                
                @Override
                public void visitVarInsn(final int opcode, final int var) {
                    super.visitVarInsn(opcode, var);
                    if (!this.modified && opcode == 23) {
                        this.canModify = true;
                    }
                }
            };
        }
        if (desc.endsWith("V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    super.visitFieldInsn(opcode, owner, name, desc);
                    if (opcode == 181 && name.equals(EntityLivingBaseVisitor.this.lastAttackerTimeName)) {
                        this.mv.visitVarInsn(25, 1);
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "onAttack", "(L" + EntityLivingBaseVisitor.this.entityName + ";)V", false);
                    }
                }
            };
        }
        return mv;
    }
}
