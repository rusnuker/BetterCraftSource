// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_18;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.core.asm.global.ClassEditor;

public class CapeImageBufferVisitor extends ClassEditor
{
    private String abstractClientPlayerName;
    private String resourceLocationName;
    
    public CapeImageBufferVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.abstractClientPlayerName = (LabyModCoreMod.isObfuscated() ? "bet" : "net/minecraft/client/entity/AbstractClientPlayer");
        this.resourceLocationName = LabyModTransformer.getMappingImplementation().getResourceLocationName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return new MethodVisitor(262144, mv) {
            @Override
            public void visitMethodInsn(int opcode, String owner, final String name, String desc, final boolean itf) {
                if (name.equals("parseCape")) {
                    owner = "CapeUtils";
                }
                if (name.equals("setLocationOfCape")) {
                    opcode = 182;
                    owner = CapeImageBufferVisitor.this.abstractClientPlayerName;
                    desc = "(L" + CapeImageBufferVisitor.this.resourceLocationName + ";)V";
                }
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        };
    }
}
