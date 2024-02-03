// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.MethodVisitor;

public class MethodVisitorEx extends MethodVisitor
{
    public MethodVisitorEx(final MethodVisitor mv) {
        super(ASM.API_VERSION, mv);
    }
    
    public void visitConstant(final byte constant) {
        if (constant > -2 && constant < 6) {
            this.visitInsn(Bytecode.CONSTANTS_INT[constant + 1]);
            return;
        }
        this.visitIntInsn(16, constant);
    }
}
