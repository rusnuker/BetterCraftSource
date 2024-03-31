// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

final class FileUploadUtil
{
    private FileUploadUtil() {
    }
    
    static int hashCode(final FileUpload upload) {
        return upload.getName().hashCode();
    }
    
    static boolean equals(final FileUpload upload1, final FileUpload upload2) {
        return upload1.getName().equalsIgnoreCase(upload2.getName());
    }
    
    static int compareTo(final FileUpload upload1, final FileUpload upload2) {
        return upload1.getName().compareToIgnoreCase(upload2.getName());
    }
}
