// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.client;

public class UploadStatus
{
    public volatile Long bytesWritten;
    public volatile Long totalBytes;
    
    public UploadStatus() {
        this.bytesWritten = 0L;
        this.totalBytes = 0L;
    }
}
