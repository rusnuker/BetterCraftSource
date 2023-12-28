// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import org.cef.misc.IntRef;
import net.montoyo.mcef.api.ISchemeResponseData;

public class SchemeResponseData implements ISchemeResponseData
{
    private final byte[] data;
    private final int toRead;
    private final IntRef read;
    
    public SchemeResponseData(final byte[] data, final int toRead, final IntRef read) {
        this.data = data;
        this.toRead = toRead;
        this.read = read;
    }
    
    @Override
    public byte[] getDataArray() {
        return this.data;
    }
    
    @Override
    public int getBytesToRead() {
        return this.toRead;
    }
    
    @Override
    public void setAmountRead(final int rd) {
        this.read.set(rd);
    }
}
