// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Date;

class CefDownloadItem_N extends CefNativeAdapter implements CefDownloadItem
{
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
    
    @Override
    public boolean isValid() {
        try {
            return this.N_IsValid();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isInProgress() {
        try {
            return this.N_IsInProgress();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isComplete() {
        try {
            return this.N_IsComplete();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isCanceled() {
        try {
            return this.N_IsCanceled();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public long getCurrentSpeed() {
        try {
            return this.N_GetCurrentSpeed();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0L;
        }
    }
    
    @Override
    public int getPercentComplete() {
        try {
            return this.N_GetPercentComplete();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public long getTotalBytes() {
        try {
            return this.N_GetTotalBytes();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0L;
        }
    }
    
    @Override
    public long getReceivedBytes() {
        try {
            return this.N_GetReceivedBytes();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0L;
        }
    }
    
    @Override
    public Date getStartTime() {
        try {
            return this.N_GetStartTime();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Date getEndTime() {
        try {
            return this.N_GetEndTime();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFullPath() {
        try {
            return this.N_GetFullPath();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getId() {
        try {
            return this.N_GetId();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public String getURL() {
        try {
            return this.N_GetURL();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getSuggestedFileName() {
        try {
            return this.N_GetSuggestedFileName();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getContentDisposition() {
        try {
            return this.N_GetContentDisposition();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getMimeType() {
        try {
            return this.N_GetMimeType();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    private final native boolean N_IsValid();
    
    private final native boolean N_IsInProgress();
    
    private final native boolean N_IsComplete();
    
    private final native boolean N_IsCanceled();
    
    private final native long N_GetCurrentSpeed();
    
    private final native int N_GetPercentComplete();
    
    private final native long N_GetTotalBytes();
    
    private final native long N_GetReceivedBytes();
    
    private final native Date N_GetStartTime();
    
    private final native Date N_GetEndTime();
    
    private final native String N_GetFullPath();
    
    private final native int N_GetId();
    
    private final native String N_GetURL();
    
    private final native String N_GetSuggestedFileName();
    
    private final native String N_GetContentDisposition();
    
    private final native String N_GetMimeType();
}
