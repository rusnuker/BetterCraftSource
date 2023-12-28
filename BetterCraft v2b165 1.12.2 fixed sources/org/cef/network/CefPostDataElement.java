// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

public abstract class CefPostDataElement
{
    CefPostDataElement() {
    }
    
    public static final CefPostDataElement create() {
        return CefPostDataElement_N.createNative();
    }
    
    public abstract boolean isReadOnly();
    
    public abstract void setToEmpty();
    
    public abstract void setToFile(final String p0);
    
    public abstract void setToBytes(final int p0, final byte[] p1);
    
    public abstract Type getType();
    
    public abstract String getFile();
    
    public abstract int getBytesCount();
    
    public abstract int getBytes(final int p0, final byte[] p1);
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    public String toString(final String mimeType) {
        final int bytesCnt = this.getBytesCount();
        byte[] bytes = null;
        if (bytesCnt > 0) {
            bytes = new byte[bytesCnt];
        }
        boolean asText = false;
        if (mimeType != null) {
            if (mimeType.startsWith("text/")) {
                asText = true;
            }
            else if (mimeType.startsWith("application/xml")) {
                asText = true;
            }
            else if (mimeType.startsWith("application/xhtml")) {
                asText = true;
            }
            else if (mimeType.startsWith("application/x-www-form-urlencoded")) {
                asText = true;
            }
        }
        String returnValue = "";
        if (this.getType() == Type.PDE_TYPE_BYTES) {
            final int setBytes = this.getBytes(bytes.length, bytes);
            returnValue = String.valueOf(returnValue) + "    Content-Length: " + bytesCnt + "\n";
            if (asText) {
                returnValue = String.valueOf(returnValue) + "\n    " + new String(bytes);
            }
            else {
                for (int i = 0; i < setBytes; ++i) {
                    if (i % 40 == 0) {
                        returnValue = String.valueOf(returnValue) + "\n    ";
                    }
                    returnValue = String.valueOf(returnValue) + String.format("%02X", bytes[i]) + " ";
                }
            }
            returnValue = String.valueOf(returnValue) + "\n";
        }
        else if (this.getType() == Type.PDE_TYPE_FILE) {
            returnValue = String.valueOf(returnValue) + "\n    Bytes of file: " + this.getFile() + "\n";
        }
        return returnValue;
    }
    
    public enum Type
    {
        PDE_TYPE_EMPTY("PDE_TYPE_EMPTY", 0), 
        PDE_TYPE_BYTES("PDE_TYPE_BYTES", 1), 
        PDE_TYPE_FILE("PDE_TYPE_FILE", 2);
        
        private Type(final String s, final int n) {
        }
    }
}
