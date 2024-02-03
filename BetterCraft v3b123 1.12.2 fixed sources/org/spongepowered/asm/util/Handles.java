// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import org.objectweb.asm.Handle;

public final class Handles
{
    private static final int[] H_OPCODES;
    
    private Handles() {
    }
    
    public static boolean isField(final Handle handle) {
        switch (handle.getTag()) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                return false;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                return true;
            }
            default: {
                throw new IllegalArgumentException("Invalid tag " + handle.getTag() + " for method handle " + handle + ".");
            }
        }
    }
    
    public static int opcodeFromTag(final int tag) {
        return (tag >= 0 && tag < Handles.H_OPCODES.length) ? Handles.H_OPCODES[tag] : 0;
    }
    
    public static int tagFromOpcode(final int opcode) {
        for (int tag = 1; tag < Handles.H_OPCODES.length; ++tag) {
            if (Handles.H_OPCODES[tag] == opcode) {
                return tag;
            }
        }
        return 0;
    }
    
    static {
        H_OPCODES = new int[] { 0, 180, 178, 181, 179, 182, 184, 183, 183, 185 };
    }
}
