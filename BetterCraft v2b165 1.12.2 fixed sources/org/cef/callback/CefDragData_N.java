// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Iterator;
import java.util.Vector;
import java.io.OutputStream;

class CefDragData_N extends CefDragData implements CefNative
{
    private long N_CefHandle;
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefDragData_N() {
        this.N_CefHandle = 0L;
    }
    
    public static CefDragData createNative() {
        try {
            return N_Create();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefDragData clone() {
        try {
            return this.N_Clone(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void dispose() {
        try {
            this.N_Dispose(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean isReadOnly() {
        try {
            return this.N_IsReadOnly(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return true;
        }
    }
    
    @Override
    public boolean isLink() {
        try {
            return this.N_IsLink(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isFragment() {
        try {
            return this.N_IsFragment(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isFile() {
        try {
            return this.N_IsFile(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String getLinkURL() {
        try {
            return this.N_GetLinkURL(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getLinkTitle() {
        try {
            return this.N_GetLinkTitle(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getLinkMetadata() {
        try {
            return this.N_GetLinkMetadata(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFragmentText() {
        try {
            return this.N_GetFragmentText(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFragmentHtml() {
        try {
            return this.N_GetFragmentHtml(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFragmentBaseURL() {
        try {
            return this.N_GetFragmentBaseURL(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getFileContents(final OutputStream writer) {
        try {
            return this.N_GetFileContents(this.N_CefHandle, writer);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public String getFileName() {
        try {
            return this.N_GetFileName(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean getFileNames(final Vector<String> names) {
        try {
            return this.N_GetFileNames(this.N_CefHandle, names);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void setLinkURL(final String url) {
        try {
            this.N_SetLinkURL(this.N_CefHandle, url);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setLinkTitle(final String title) {
        try {
            this.N_SetLinkTitle(this.N_CefHandle, title);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setLinkMetadata(final String data) {
        try {
            this.N_SetLinkMetadata(this.N_CefHandle, data);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setFragmentText(final String text) {
        try {
            this.N_SetFragmentText(this.N_CefHandle, text);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setFragmentHtml(final String html) {
        try {
            this.N_SetFragmentHtml(this.N_CefHandle, html);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setFragmentBaseURL(final String baseUrl) {
        try {
            this.N_SetFragmentBaseURL(this.N_CefHandle, baseUrl);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void resetFileContents() {
        try {
            this.N_ResetFileContents(this.N_CefHandle);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void addFile(final String path, final String displayName) {
        try {
            this.N_AddFile(this.N_CefHandle, path, displayName);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private static final native CefDragData_N N_Create();
    
    private final native CefDragData_N N_Clone(final long p0);
    
    private final native void N_Dispose(final long p0);
    
    private final native boolean N_IsReadOnly(final long p0);
    
    private final native boolean N_IsLink(final long p0);
    
    private final native boolean N_IsFragment(final long p0);
    
    private final native boolean N_IsFile(final long p0);
    
    private final native String N_GetLinkURL(final long p0);
    
    private final native String N_GetLinkTitle(final long p0);
    
    private final native String N_GetLinkMetadata(final long p0);
    
    private final native String N_GetFragmentText(final long p0);
    
    private final native String N_GetFragmentHtml(final long p0);
    
    private final native String N_GetFragmentBaseURL(final long p0);
    
    private final native int N_GetFileContents(final long p0, final OutputStream p1);
    
    private final native String N_GetFileName(final long p0);
    
    private final native boolean N_GetFileNames(final long p0, final Vector<String> p1);
    
    private final native void N_SetLinkURL(final long p0, final String p1);
    
    private final native void N_SetLinkTitle(final long p0, final String p1);
    
    private final native void N_SetLinkMetadata(final long p0, final String p1);
    
    private final native void N_SetFragmentText(final long p0, final String p1);
    
    private final native void N_SetFragmentHtml(final long p0, final String p1);
    
    private final native void N_SetFragmentBaseURL(final long p0, final String p1);
    
    private final native void N_ResetFileContents(final long p0);
    
    private final native void N_AddFile(final long p0, final String p1, final String p2);
    
    @Override
    public String toString() {
        final Vector<String> names = new Vector<String>();
        this.getFileNames(names);
        String fileNamesStr = "{";
        for (final String s : names) {
            fileNamesStr = String.valueOf(fileNamesStr) + s + ",";
        }
        fileNamesStr = String.valueOf(fileNamesStr) + "}";
        return "CefDragData_N [isLink()=" + this.isLink() + ", isFragment()=" + this.isFragment() + ", isFile()=" + this.isFile() + ", getLinkURL()=" + this.getLinkURL() + ", getLinkTitle()=" + this.getLinkTitle() + ", getLinkMetadata()=" + this.getLinkMetadata() + ", getFragmentText()=" + this.getFragmentText() + ", getFragmentHtml()=" + this.getFragmentHtml() + ", getFragmentBaseURL()=" + this.getFragmentBaseURL() + ", getFileName()=" + this.getFileName() + ", getFileNames(vector)=" + fileNamesStr + "]";
    }
}
