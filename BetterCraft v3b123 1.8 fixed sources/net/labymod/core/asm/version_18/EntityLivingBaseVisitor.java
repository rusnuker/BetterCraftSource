// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_18;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.core.asm.global.ClassEditor;

public class EntityLivingBaseVisitor extends ClassEditor
{
    private String entityPlayerSPName;
    private String entityName;
    private String getLookInEntityLivingBaseName;
    private String vec3Name;
    private String lastAttackerTimeName;
    
    public EntityLivingBaseVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.entityPlayerSPName = (LabyModCoreMod.isObfuscated() ? "bew" : "net/minecraft/client/entity/EntityPlayerSP");
        this.entityName = (LabyModCoreMod.isObfuscated() ? "pk" : "net/minecraft/entity/Entity");
        this.getLookInEntityLivingBaseName = (LabyModCoreMod.isObfuscated() ? "d" : "getLook");
        this.vec3Name = (LabyModCoreMod.isObfuscated() ? "aui" : "net/minecraft/util/Vec3");
        this.lastAttackerTimeName = LabyModTransformer.getMappingImplementation().getLastAttackerTimeName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.getLookInEntityLivingBaseName) && desc.equals("(F)L" + this.vec3Name + ";")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitCode() {
                    final Label label = new Label();
                    this.mv.visitIntInsn(25, 0);
                    this.mv.visitTypeInsn(193, EntityLivingBaseVisitor.this.entityPlayerSPName);
                    this.mv.visitJumpInsn(153, label);
                    this.mv.visitMethodInsn(184, "BytecodeMethods", "isCrosshairsyncEnabled", "()Z", false);
                    this.mv.visitJumpInsn(153, label);
                    this.mv.visitIntInsn(25, 0);
                    this.mv.visitIntInsn(23, 1);
                    this.mv.visitMethodInsn(183, EntityLivingBaseVisitor.this.entityName, name, desc, false);
                    this.mv.visitInsn(176);
                    this.mv.visitLabel(label);
                    super.visitCode();
                }
            };
        }
        if (!desc.endsWith("V")) {
            return mv;
        }
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
}
