// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public abstract class CefRequest
{
    CefRequest() {
    }
    
    public static final CefRequest create() {
        return CefRequest_N.createNative();
    }
    
    public abstract long getIdentifier();
    
    public abstract boolean isReadOnly();
    
    public abstract String getURL();
    
    public abstract void setURL(final String p0);
    
    public abstract String getMethod();
    
    public abstract void setMethod(final String p0);
    
    public abstract void setReferrer(final String p0, final ReferrerPolicy p1);
    
    public abstract String getReferrerURL();
    
    public abstract ReferrerPolicy getReferrerPolicy();
    
    public abstract CefPostData getPostData();
    
    public abstract void setPostData(final CefPostData p0);
    
    public abstract void getHeaderMap(final Map<String, String> p0);
    
    public abstract void setHeaderMap(final Map<String, String> p0);
    
    public abstract void set(final String p0, final String p1, final CefPostData p2, final Map<String, String> p3);
    
    public abstract int getFlags();
    
    public abstract void setFlags(final int p0);
    
    public abstract String getFirstPartyForCookies();
    
    public abstract void setFirstPartyForCookies(final String p0);
    
    public abstract ResourceType getResourceType();
    
    public abstract TransitionType getTransitionType();
    
    @Override
    public String toString() {
        String returnValue = "\nHTTP-Request";
        returnValue = String.valueOf(returnValue) + "\n  flags: " + this.getFlags();
        returnValue = String.valueOf(returnValue) + "\n  resourceType: " + this.getResourceType();
        returnValue = String.valueOf(returnValue) + "\n  transitionType: " + this.getTransitionType();
        returnValue = String.valueOf(returnValue) + "\n  firstPartyForCookies: " + this.getFirstPartyForCookies();
        returnValue = String.valueOf(returnValue) + "\n  referrerURL: " + this.getReferrerURL();
        returnValue = String.valueOf(returnValue) + "\n  referrerPolicy: " + this.getReferrerPolicy();
        returnValue = String.valueOf(returnValue) + "\n    " + this.getMethod() + " " + this.getURL() + " HTTP/1.1\n";
        final Map<String, String> headerMap = new HashMap<String, String>();
        this.getHeaderMap(headerMap);
        final Set<Map.Entry<String, String>> entrySet = headerMap.entrySet();
        String mimeType = null;
        for (final Map.Entry<String, String> entry : entrySet) {
            final String key = entry.getKey();
            returnValue = String.valueOf(returnValue) + "    " + key + "=" + entry.getValue() + "\n";
            if (key.equals("Content-Type")) {
                mimeType = entry.getValue();
            }
        }
        final CefPostData pd = this.getPostData();
        if (pd != null) {
            returnValue = String.valueOf(returnValue) + pd.toString(mimeType);
        }
        return returnValue;
    }
    
    public enum ReferrerPolicy
    {
        REFERRER_POLICY_DEFAULT("REFERRER_POLICY_DEFAULT", 0), 
        REFERRER_POLICY_CLEAR_REFERRER_ON_TRANSITION_FROM_SECURE_TO_INSECURE("REFERRER_POLICY_CLEAR_REFERRER_ON_TRANSITION_FROM_SECURE_TO_INSECURE", 1), 
        REFERRER_POLICY_REDUCE_REFERRER_GRANULARITY_ON_TRANSITION_CROSS_ORIGIN("REFERRER_POLICY_REDUCE_REFERRER_GRANULARITY_ON_TRANSITION_CROSS_ORIGIN", 2), 
        REFERRER_POLICY_ORIGIN_ONLY_ON_TRANSITION_CROSS_ORIGIN("REFERRER_POLICY_ORIGIN_ONLY_ON_TRANSITION_CROSS_ORIGIN", 3), 
        REFERRER_POLICY_NEVER_CLEAR_REFERRER("REFERRER_POLICY_NEVER_CLEAR_REFERRER", 4), 
        REFERRER_POLICY_ORIGIN("REFERRER_POLICY_ORIGIN", 5), 
        REFERRER_POLICY_CLEAR_REFERRER_ON_TRANSITION_CROSS_ORIGIN("REFERRER_POLICY_CLEAR_REFERRER_ON_TRANSITION_CROSS_ORIGIN", 6), 
        REFERRER_POLICY_ORIGIN_CLEAR_ON_TRANSITION_FROM_SECURE_TO_INSECURE("REFERRER_POLICY_ORIGIN_CLEAR_ON_TRANSITION_FROM_SECURE_TO_INSECURE", 7), 
        REFERRER_POLICY_NO_REFERRER("REFERRER_POLICY_NO_REFERRER", 8), 
        REFERRER_POLICY_LAST_VALUE("REFERRER_POLICY_LAST_VALUE", 9);
        
        private ReferrerPolicy(final String s, final int n) {
        }
    }
    
    public enum ResourceType
    {
        RT_MAIN_FRAME("RT_MAIN_FRAME", 0), 
        RT_SUB_FRAME("RT_SUB_FRAME", 1), 
        RT_STYLESHEET("RT_STYLESHEET", 2), 
        RT_SCRIPT("RT_SCRIPT", 3), 
        RT_IMAGE("RT_IMAGE", 4), 
        RT_FONT_RESOURCE("RT_FONT_RESOURCE", 5), 
        RT_SUB_RESOURCE("RT_SUB_RESOURCE", 6), 
        RT_OBJECT("RT_OBJECT", 7), 
        RT_MEDIA("RT_MEDIA", 8), 
        RT_WORKER("RT_WORKER", 9), 
        RT_SHARED_WORKER("RT_SHARED_WORKER", 10), 
        RT_PREFETCH("RT_PREFETCH", 11), 
        RT_FAVICON("RT_FAVICON", 12), 
        RT_XHR("RT_XHR", 13), 
        RT_PING("RT_PING", 14), 
        RT_SERVICE_WORKER("RT_SERVICE_WORKER", 15);
        
        private ResourceType(final String s, final int n) {
        }
    }
    
    public enum TransitionFlags
    {
        TT_BLOCKED_FLAG("TT_BLOCKED_FLAG", 0, 8388608), 
        TT_FORWARD_BACK_FLAG("TT_FORWARD_BACK_FLAG", 1, 16777216), 
        TT_CHAIN_START_FLAG("TT_CHAIN_START_FLAG", 2, 268435456), 
        TT_CHAIN_END_FLAG("TT_CHAIN_END_FLAG", 3, 536870912), 
        TT_CLIENT_REDIRECT_FLAG("TT_CLIENT_REDIRECT_FLAG", 4, 1073741824), 
        TT_SERVER_REDIRECT_FLAG("TT_SERVER_REDIRECT_FLAG", 5, Integer.MIN_VALUE);
        
        private final int flag;
        
        private TransitionFlags(final String s, final int n, final int flag) {
            this.flag = flag;
        }
        
        public int getValue() {
            return this.flag;
        }
    }
    
    public enum TransitionType
    {
        TT_LINK("TT_LINK", 0, 0), 
        TT_EXPLICIT("TT_EXPLICIT", 1, 1), 
        TT_AUTO_SUBFRAME("TT_AUTO_SUBFRAME", 2, 3), 
        TT_MANUAL_SUBFRAME("TT_MANUAL_SUBFRAME", 3, 4), 
        TT_FORM_SUBMIT("TT_FORM_SUBMIT", 4, 7), 
        TT_RELOAD("TT_RELOAD", 5, 8);
        
        private int value;
        
        private TransitionType(final String s, final int n, final int source) {
            this.value = source;
        }
        
        public int getValue() {
            return this.value;
        }
        
        public int getSource() {
            return this.value & 0xFF;
        }
        
        public void addQualifier(final TransitionFlags flag) {
            this.value |= flag.getValue();
        }
        
        public void addQualifiers(final int flags) {
            this.value |= (flags & 0xFFFFFF00);
        }
        
        public int getQualifiers() {
            return this.value & 0xFFFFFF00;
        }
        
        public void removeQualifier(final TransitionFlags flag) {
            this.value &= ~flag.getValue();
        }
        
        public boolean isSet(final TransitionFlags flag) {
            return (this.value & flag.getValue()) != 0x0;
        }
        
        public boolean isRedirect() {
            return (this.value & 0xC0000000) != 0x0;
        }
    }
    
    public static final class CefUrlRequestFlags
    {
        public static final int UR_FLAG_NONE = 0;
        public static final int UR_FLAG_SKIP_CACHE = 1;
        public static final int UR_FLAG_ALLOW_CACHED_CREDENTIALS = 2;
        public static final int UR_FLAG_REPORT_UPLOAD_PROGRESS = 8;
        public static final int UR_FLAG_REPORT_RAW_HEADERS = 32;
        public static final int UR_FLAG_NO_DOWNLOAD_DATA = 64;
        public static final int UR_FLAG_NO_RETRY_ON_5XX = 128;
    }
}
