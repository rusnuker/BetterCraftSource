// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class GuiMultiplayerVisitor extends ClassEditor
{
    private String drawScreenName;
    
    public GuiMultiplayerVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.drawScreenName = LabyModTransformer.getMappingImplementation().getGuiScreenDrawScreenName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.drawScreenName) && desc.equals("(IIF)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitLdcInsn(Object cst) {
                    if (cst instanceof String && String.valueOf(cst).equals("multiplayer.title")) {
                        cst = "";
                    }
                    super.visitLdcInsn(cst);
                }
            };
        }
        return mv;
    }
}
