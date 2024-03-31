// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.attachment;

import javax.activation.DataHandler;

public abstract class AttachmentMarshaller
{
    public abstract String addMtomAttachment(final DataHandler p0, final String p1, final String p2);
    
    public abstract String addMtomAttachment(final byte[] p0, final int p1, final int p2, final String p3, final String p4, final String p5);
    
    public boolean isXOPPackage() {
        return false;
    }
    
    public abstract String addSwaRefAttachment(final DataHandler p0);
}
