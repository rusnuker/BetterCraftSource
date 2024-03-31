// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.main.Source;

public class ItemVisitor extends ClassEditor
{
    private static final boolean MC18;
    private String shouldCauseReequipAnimationName;
    
    static {
        MC18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    public ItemVisitor() {
        super(ClassEditorType.CLASS_VISITOR_AND_REMAPPER);
        this.shouldCauseReequipAnimationName = "shouldCauseReequipAnimation";
    }
    
    @Override
    public String visitMapping(final String typeName) {
        if (typeName.equals(LabyModTransformer.getMappingImplementation().getItemBucketName())) {
            return "net/labymod/core_implementation/" + (LabyModTransformer.getVersion().startsWith("1.8") ? "mc18" : "mc112") + "/item/ItemBucketCustom";
        }
        return super.visitMapping(typeName);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (ItemVisitor.MC18 && name.equals(this.shouldCauseReequipAnimationName)) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitMethodInsn(final int opcode, String owner, String name, final String desc, final boolean itf) {
                    if (opcode == 184) {
                        owner = "BytecodeMethods";
                        name = "shouldCancelReequipAnimation";
                    }
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            };
        }
        return mv;
    }
}
