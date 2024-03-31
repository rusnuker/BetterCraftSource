// 
// Decompiled by Procyon v0.6.0
// 

package oshi.hardware;

public interface HardwareAbstractionLayer
{
    Processor[] getProcessors();
    
    Memory getMemory();
}
