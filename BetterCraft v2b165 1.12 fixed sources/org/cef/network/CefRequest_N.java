// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Map;
import org.cef.callback.CefNative;

class CefRequest_N extends CefRequest implements CefNative
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
    
    CefRequest_N() {
        this.N_CefHandle = 0L;
    }
    
    public static final CefRequest createNative() {
        final CefRequest_N result = new CefRequest_N();
        try {
            result.N_CefRequest_CTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        if (result.N_CefHandle == 0L) {
            return null;
        }
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.N_CefRequest_DTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return;
        }
        finally {
            super.finalize();
        }
        super.finalize();
    }
    
    @Override
    public long getIdentifier() {
        try {
            return this.N_GetIdentifier();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0L;
        }
    }
    
    @Override
    public boolean isReadOnly() {
        try {
            return this.N_IsReadOnly();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
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
    public void setURL(final String url) {
        try {
            this.N_SetURL(url);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getMethod() {
        try {
            return this.N_GetMethod();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setMethod(final String string) {
        try {
            this.N_SetMethod(string);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setReferrer(final String url, final ReferrerPolicy policy) {
        try {
            this.N_SetReferrer(url, policy);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getReferrerURL() {
        try {
            return this.N_GetReferrerURL();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public ReferrerPolicy getReferrerPolicy() {
        try {
            return this.N_GetReferrerPolicy();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefPostData getPostData() {
        try {
            return this.N_GetPostData();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setPostData(final CefPostData postData) {
        try {
            this.N_SetPostData(postData);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void getHeaderMap(final Map<String, String> headerMap) {
        try {
            this.N_GetHeaderMap(headerMap);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setHeaderMap(final Map<String, String> headerMap) {
        try {
            this.N_SetHeaderMap(headerMap);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void set(final String url, final String method, final CefPostData postData, final Map<String, String> headerMap) {
        try {
            this.N_Set(url, method, postData, headerMap);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getFlags() {
        try {
            return this.N_GetFlags();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public void setFlags(final int flags) {
        try {
            this.N_SetFlags(flags);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getFirstPartyForCookies() {
        try {
            return this.N_GetFirstPartyForCookies();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setFirstPartyForCookies(final String url) {
        try {
            this.N_SetFirstPartyForCookies(url);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public ResourceType getResourceType() {
        try {
            return this.N_GetResourceType();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return ResourceType.RT_MAIN_FRAME;
        }
    }
    
    @Override
    public TransitionType getTransitionType() {
        try {
            return this.N_GetTransitionType();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return TransitionType.TT_AUTO_SUBFRAME;
        }
    }
    
    private final native void N_CefRequest_CTOR();
    
    private final native long N_GetIdentifier();
    
    private final native boolean N_IsReadOnly();
    
    private final native String N_GetURL();
    
    private final native void N_SetURL(final String p0);
    
    private final native String N_GetMethod();
    
    private final native void N_SetMethod(final String p0);
    
    private final native void N_SetReferrer(final String p0, final ReferrerPolicy p1);
    
    private final native String N_GetReferrerURL();
    
    private final native ReferrerPolicy N_GetReferrerPolicy();
    
    private final native CefPostData N_GetPostData();
    
    private final native void N_SetPostData(final CefPostData p0);
    
    private final native void N_GetHeaderMap(final Map<String, String> p0);
    
    private final native void N_SetHeaderMap(final Map<String, String> p0);
    
    private final native void N_Set(final String p0, final String p1, final CefPostData p2, final Map<String, String> p3);
    
    private final native int N_GetFlags();
    
    private final native void N_SetFlags(final int p0);
    
    private final native String N_GetFirstPartyForCookies();
    
    private final native void N_SetFirstPartyForCookies(final String p0);
    
    private final native ResourceType N_GetResourceType();
    
    private final native TransitionType N_GetTransitionType();
    
    private final native void N_CefRequest_DTOR();
}
