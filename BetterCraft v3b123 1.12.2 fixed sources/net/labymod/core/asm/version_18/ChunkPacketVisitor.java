// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_18;

import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.core.asm.global.ClassEditor;

public class ChunkPacketVisitor extends ClassEditor
{
    private String readPacketDataName;
    private String packetBufferName;
    
    public ChunkPacketVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        this.readPacketDataName = LabyModTransformer.getMappingImplementation().getReadPacketDataName();
        this.packetBufferName = LabyModTransformer.getMappingImplementation().getPacketBufferName();
    }
    
    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.readPacketDataName) && desc.equals("(L" + this.packetBufferName + ";)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitInsn(final int opcode) {
                    if (opcode == 177) {
                        super.visitIntInsn(25, 1);
                        super.visitIntInsn(25, 0);
                        super.visitMethodInsn(184, "BytecodeMethods", "onReceiveChunkData", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
