// 
// Decompiled by Procyon v0.6.0
// 

package oshi.software.os;

public interface OperatingSystem
{
    String getFamily();
    
    String getManufacturer();
    
    OperatingSystemVersion getVersion();
}
