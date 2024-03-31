// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class RenderGlobalVisitor extends ClassEditor
{
    private String renderEntitiesName;
    private String renderGlobalName;
    private String countEntitiesRenderedName;
    private String countEntitiesTotalName;
    private String entityClassName;
    private String iCameraName;
    
    public RenderGlobalVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.renderEntitiesName = LabyModTransformer.getMappingImplementation().getRenderEntitiesName();
        this.renderGlobalName = LabyModTransformer.getMappingImplementation().getRenderGlobalName();
        this.countEntitiesRenderedName = LabyModTransformer.getMappingImplementation().getCountEntitiesRenderedName();
        this.countEntitiesTotalName = LabyModTransformer.getMappingImplementation().getCountEntitiesTotalName();
        this.entityClassName = LabyModTransformer.getMappingImplementation().getEntityClassName();
        this.iCameraName = LabyModTransformer.getMappingImplementation().getICameraClassName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.renderEntitiesName) && desc.equals("(L" + this.entityClassName + ";L" + this.iCameraName + ";F)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        super.visitIntInsn(25, 0);
                        super.visitIntInsn(25, 0);
                        super.visitFieldInsn(180, RenderGlobalVisitor.this.renderGlobalName, RenderGlobalVisitor.this.countEntitiesRenderedName, "I");
                        super.visitIntInsn(25, 0);
                        super.visitFieldInsn(180, RenderGlobalVisitor.this.renderGlobalName, RenderGlobalVisitor.this.countEntitiesTotalName, "I");
                        super.visitMethodInsn(184, "BytecodeMethods", "onUpdateEntityCountInfo", "(II)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
