// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.transformers;

import org.spongepowered.asm.util.asm.ASM;
import org.objectweb.asm.ClassReader;

public class MixinClassReader extends ClassReader
{
    public MixinClassReader(final byte[] classFile, final String name) {
        super(checkClassVersion(classFile, name));
    }
    
    private static byte[] checkClassVersion(final byte[] classFile, final String name) {
        final short majorClassVersion = (short)((classFile[6] & 0xFF) << 8 | (classFile[7] & 0xFF));
        if (majorClassVersion > ASM.getMaxSupportedClassVersionMajor()) {
            throw new IllegalArgumentException(String.format("Class file major version %d is not supported by active ASM (version %d.%d supports class version %d), reading %s", majorClassVersion, ASM.getApiVersionMajor(), ASM.getApiVersionMinor(), ASM.getMaxSupportedClassVersionMajor(), name));
        }
        return classFile;
    }
}
