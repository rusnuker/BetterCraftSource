// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.attachment;

import javax.activation.DataHandler;

public abstract class AttachmentUnmarshaller
{
    public abstract DataHandler getAttachmentAsDataHandler(final String p0);
    
    public abstract byte[] getAttachmentAsByteArray(final String p0);
    
    public boolean isXOPPackage() {
        return false;
    }
}
