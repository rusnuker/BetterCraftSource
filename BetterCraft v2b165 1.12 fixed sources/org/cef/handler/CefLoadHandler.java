// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import java.util.HashMap;
import java.util.Map;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefLoadHandler
{
    void onLoadingStateChange(final CefBrowser p0, final boolean p1, final boolean p2, final boolean p3);
    
    void onLoadStart(final CefBrowser p0, final CefFrame p1, final CefRequest.TransitionType p2);
    
    void onLoadEnd(final CefBrowser p0, final CefFrame p1, final int p2);
    
    void onLoadError(final CefBrowser p0, final CefFrame p1, final ErrorCode p2, final String p3, final String p4);
    
    public enum ErrorCode
    {
        ERR_NONE("ERR_NONE", 0, 0), 
        ERR_FAILED("ERR_FAILED", 1, -2), 
        ERR_ABORTED("ERR_ABORTED", 2, -3), 
        ERR_INVALID_ARGUMENT("ERR_INVALID_ARGUMENT", 3, -4), 
        ERR_INVALID_HANDLE("ERR_INVALID_HANDLE", 4, -5), 
        ERR_FILE_NOT_FOUND("ERR_FILE_NOT_FOUND", 5, -6), 
        ERR_TIMED_OUT("ERR_TIMED_OUT", 6, -7), 
        ERR_FILE_TOO_BIG("ERR_FILE_TOO_BIG", 7, -8), 
        ERR_UNEXPECTED("ERR_UNEXPECTED", 8, -9), 
        ERR_ACCESS_DENIED("ERR_ACCESS_DENIED", 9, -10), 
        ERR_NOT_IMPLEMENTED("ERR_NOT_IMPLEMENTED", 10, -11), 
        ERR_CONNECTION_CLOSED("ERR_CONNECTION_CLOSED", 11, -100), 
        ERR_CONNECTION_RESET("ERR_CONNECTION_RESET", 12, -101), 
        ERR_CONNECTION_REFUSED("ERR_CONNECTION_REFUSED", 13, -102), 
        ERR_CONNECTION_ABORTED("ERR_CONNECTION_ABORTED", 14, -103), 
        ERR_CONNECTION_FAILED("ERR_CONNECTION_FAILED", 15, -104), 
        ERR_NAME_NOT_RESOLVED("ERR_NAME_NOT_RESOLVED", 16, -105), 
        ERR_INTERNET_DISCONNECTED("ERR_INTERNET_DISCONNECTED", 17, -106), 
        ERR_SSL_PROTOCOL_ERROR("ERR_SSL_PROTOCOL_ERROR", 18, -107), 
        ERR_ADDRESS_INVALID("ERR_ADDRESS_INVALID", 19, -108), 
        ERR_ADDRESS_UNREACHABLE("ERR_ADDRESS_UNREACHABLE", 20, -109), 
        ERR_SSL_CLIENT_AUTH_CERT_NEEDED("ERR_SSL_CLIENT_AUTH_CERT_NEEDED", 21, -110), 
        ERR_TUNNEL_CONNECTION_FAILED("ERR_TUNNEL_CONNECTION_FAILED", 22, -111), 
        ERR_NO_SSL_VERSIONS_ENABLED("ERR_NO_SSL_VERSIONS_ENABLED", 23, -112), 
        ERR_SSL_VERSION_OR_CIPHER_MISMATCH("ERR_SSL_VERSION_OR_CIPHER_MISMATCH", 24, -113), 
        ERR_SSL_RENEGOTIATION_REQUESTED("ERR_SSL_RENEGOTIATION_REQUESTED", 25, -114), 
        ERR_CERT_COMMON_NAME_INVALID("ERR_CERT_COMMON_NAME_INVALID", 26, -200), 
        ERR_CERT_BEGIN("ERR_CERT_BEGIN", 27, -200), 
        ERR_CERT_DATE_INVALID("ERR_CERT_DATE_INVALID", 28, -201), 
        ERR_CERT_AUTHORITY_INVALID("ERR_CERT_AUTHORITY_INVALID", 29, -202), 
        ERR_CERT_CONTAINS_ERRORS("ERR_CERT_CONTAINS_ERRORS", 30, -203), 
        ERR_CERT_NO_REVOCATION_MECHANISM("ERR_CERT_NO_REVOCATION_MECHANISM", 31, -204), 
        ERR_CERT_UNABLE_TO_CHECK_REVOCATION("ERR_CERT_UNABLE_TO_CHECK_REVOCATION", 32, -205), 
        ERR_CERT_REVOKED("ERR_CERT_REVOKED", 33, -206), 
        ERR_CERT_INVALID("ERR_CERT_INVALID", 34, -207), 
        ERR_CERT_WEAK_SIGNATURE_ALGORITHM("ERR_CERT_WEAK_SIGNATURE_ALGORITHM", 35, -208), 
        ERR_CERT_NON_UNIQUE_NAME("ERR_CERT_NON_UNIQUE_NAME", 36, -210), 
        ERR_CERT_WEAK_KEY("ERR_CERT_WEAK_KEY", 37, -211), 
        ERR_CERT_NAME_CONSTRAINT_VIOLATION("ERR_CERT_NAME_CONSTRAINT_VIOLATION", 38, -212), 
        ERR_CERT_VALIDITY_TOO_LONG("ERR_CERT_VALIDITY_TOO_LONG", 39, -213), 
        ERR_CERT_END("ERR_CERT_END", 40, -213), 
        ERR_INVALID_URL("ERR_INVALID_URL", 41, -300), 
        ERR_DISALLOWED_URL_SCHEME("ERR_DISALLOWED_URL_SCHEME", 42, -301), 
        ERR_UNKNOWN_URL_SCHEME("ERR_UNKNOWN_URL_SCHEME", 43, -302), 
        ERR_TOO_MANY_REDIRECTS("ERR_TOO_MANY_REDIRECTS", 44, -310), 
        ERR_UNSAFE_REDIRECT("ERR_UNSAFE_REDIRECT", 45, -311), 
        ERR_UNSAFE_PORT("ERR_UNSAFE_PORT", 46, -312), 
        ERR_INVALID_RESPONSE("ERR_INVALID_RESPONSE", 47, -320), 
        ERR_INVALID_CHUNKED_ENCODING("ERR_INVALID_CHUNKED_ENCODING", 48, -321), 
        ERR_METHOD_NOT_SUPPORTED("ERR_METHOD_NOT_SUPPORTED", 49, -322), 
        ERR_UNEXPECTED_PROXY_AUTH("ERR_UNEXPECTED_PROXY_AUTH", 50, -323), 
        ERR_EMPTY_RESPONSE("ERR_EMPTY_RESPONSE", 51, -324), 
        ERR_RESPONSE_HEADERS_TOO_BIG("ERR_RESPONSE_HEADERS_TOO_BIG", 52, -325), 
        ERR_CACHE_MISS("ERR_CACHE_MISS", 53, -400), 
        ERR_INSECURE_RESPONSE("ERR_INSECURE_RESPONSE", 54, -501);
        
        private static final Map<Integer, ErrorCode> CODES;
        private final int code;
        
        static {
            CODES = new HashMap<Integer, ErrorCode>();
            ErrorCode[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final ErrorCode ec = values[i];
                if (!ErrorCode.CODES.containsKey(ec.code)) {
                    ErrorCode.CODES.put(ec.code, ec);
                }
            }
        }
        
        private ErrorCode(final String s, final int n, final int code) {
            this.code = code;
        }
        
        public int getCode() {
            return this.code;
        }
        
        public static ErrorCode findByCode(final int code) {
            return ErrorCode.CODES.get(code);
        }
    }
}
