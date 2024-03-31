// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.ByteProcessor;
import io.netty.util.AsciiString;

public class HttpResponseStatus implements Comparable<HttpResponseStatus>
{
    public static final HttpResponseStatus CONTINUE;
    public static final HttpResponseStatus SWITCHING_PROTOCOLS;
    public static final HttpResponseStatus PROCESSING;
    public static final HttpResponseStatus OK;
    public static final HttpResponseStatus CREATED;
    public static final HttpResponseStatus ACCEPTED;
    public static final HttpResponseStatus NON_AUTHORITATIVE_INFORMATION;
    public static final HttpResponseStatus NO_CONTENT;
    public static final HttpResponseStatus RESET_CONTENT;
    public static final HttpResponseStatus PARTIAL_CONTENT;
    public static final HttpResponseStatus MULTI_STATUS;
    public static final HttpResponseStatus MULTIPLE_CHOICES;
    public static final HttpResponseStatus MOVED_PERMANENTLY;
    public static final HttpResponseStatus FOUND;
    public static final HttpResponseStatus SEE_OTHER;
    public static final HttpResponseStatus NOT_MODIFIED;
    public static final HttpResponseStatus USE_PROXY;
    public static final HttpResponseStatus TEMPORARY_REDIRECT;
    public static final HttpResponseStatus BAD_REQUEST;
    public static final HttpResponseStatus UNAUTHORIZED;
    public static final HttpResponseStatus PAYMENT_REQUIRED;
    public static final HttpResponseStatus FORBIDDEN;
    public static final HttpResponseStatus NOT_FOUND;
    public static final HttpResponseStatus METHOD_NOT_ALLOWED;
    public static final HttpResponseStatus NOT_ACCEPTABLE;
    public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED;
    public static final HttpResponseStatus REQUEST_TIMEOUT;
    public static final HttpResponseStatus CONFLICT;
    public static final HttpResponseStatus GONE;
    public static final HttpResponseStatus LENGTH_REQUIRED;
    public static final HttpResponseStatus PRECONDITION_FAILED;
    public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE;
    public static final HttpResponseStatus REQUEST_URI_TOO_LONG;
    public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE;
    public static final HttpResponseStatus REQUESTED_RANGE_NOT_SATISFIABLE;
    public static final HttpResponseStatus EXPECTATION_FAILED;
    public static final HttpResponseStatus MISDIRECTED_REQUEST;
    public static final HttpResponseStatus UNPROCESSABLE_ENTITY;
    public static final HttpResponseStatus LOCKED;
    public static final HttpResponseStatus FAILED_DEPENDENCY;
    public static final HttpResponseStatus UNORDERED_COLLECTION;
    public static final HttpResponseStatus UPGRADE_REQUIRED;
    public static final HttpResponseStatus PRECONDITION_REQUIRED;
    public static final HttpResponseStatus TOO_MANY_REQUESTS;
    public static final HttpResponseStatus REQUEST_HEADER_FIELDS_TOO_LARGE;
    public static final HttpResponseStatus INTERNAL_SERVER_ERROR;
    public static final HttpResponseStatus NOT_IMPLEMENTED;
    public static final HttpResponseStatus BAD_GATEWAY;
    public static final HttpResponseStatus SERVICE_UNAVAILABLE;
    public static final HttpResponseStatus GATEWAY_TIMEOUT;
    public static final HttpResponseStatus HTTP_VERSION_NOT_SUPPORTED;
    public static final HttpResponseStatus VARIANT_ALSO_NEGOTIATES;
    public static final HttpResponseStatus INSUFFICIENT_STORAGE;
    public static final HttpResponseStatus NOT_EXTENDED;
    public static final HttpResponseStatus NETWORK_AUTHENTICATION_REQUIRED;
    private final int code;
    private final AsciiString codeAsText;
    private HttpStatusClass codeClass;
    private final String reasonPhrase;
    private final byte[] bytes;
    
    private static HttpResponseStatus newStatus(final int statusCode, final String reasonPhrase) {
        return new HttpResponseStatus(statusCode, reasonPhrase, true);
    }
    
    public static HttpResponseStatus valueOf(final int code) {
        switch (code) {
            case 100: {
                return HttpResponseStatus.CONTINUE;
            }
            case 101: {
                return HttpResponseStatus.SWITCHING_PROTOCOLS;
            }
            case 102: {
                return HttpResponseStatus.PROCESSING;
            }
            case 200: {
                return HttpResponseStatus.OK;
            }
            case 201: {
                return HttpResponseStatus.CREATED;
            }
            case 202: {
                return HttpResponseStatus.ACCEPTED;
            }
            case 203: {
                return HttpResponseStatus.NON_AUTHORITATIVE_INFORMATION;
            }
            case 204: {
                return HttpResponseStatus.NO_CONTENT;
            }
            case 205: {
                return HttpResponseStatus.RESET_CONTENT;
            }
            case 206: {
                return HttpResponseStatus.PARTIAL_CONTENT;
            }
            case 207: {
                return HttpResponseStatus.MULTI_STATUS;
            }
            case 300: {
                return HttpResponseStatus.MULTIPLE_CHOICES;
            }
            case 301: {
                return HttpResponseStatus.MOVED_PERMANENTLY;
            }
            case 302: {
                return HttpResponseStatus.FOUND;
            }
            case 303: {
                return HttpResponseStatus.SEE_OTHER;
            }
            case 304: {
                return HttpResponseStatus.NOT_MODIFIED;
            }
            case 305: {
                return HttpResponseStatus.USE_PROXY;
            }
            case 307: {
                return HttpResponseStatus.TEMPORARY_REDIRECT;
            }
            case 400: {
                return HttpResponseStatus.BAD_REQUEST;
            }
            case 401: {
                return HttpResponseStatus.UNAUTHORIZED;
            }
            case 402: {
                return HttpResponseStatus.PAYMENT_REQUIRED;
            }
            case 403: {
                return HttpResponseStatus.FORBIDDEN;
            }
            case 404: {
                return HttpResponseStatus.NOT_FOUND;
            }
            case 405: {
                return HttpResponseStatus.METHOD_NOT_ALLOWED;
            }
            case 406: {
                return HttpResponseStatus.NOT_ACCEPTABLE;
            }
            case 407: {
                return HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED;
            }
            case 408: {
                return HttpResponseStatus.REQUEST_TIMEOUT;
            }
            case 409: {
                return HttpResponseStatus.CONFLICT;
            }
            case 410: {
                return HttpResponseStatus.GONE;
            }
            case 411: {
                return HttpResponseStatus.LENGTH_REQUIRED;
            }
            case 412: {
                return HttpResponseStatus.PRECONDITION_FAILED;
            }
            case 413: {
                return HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE;
            }
            case 414: {
                return HttpResponseStatus.REQUEST_URI_TOO_LONG;
            }
            case 415: {
                return HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
            }
            case 416: {
                return HttpResponseStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
            }
            case 417: {
                return HttpResponseStatus.EXPECTATION_FAILED;
            }
            case 421: {
                return HttpResponseStatus.MISDIRECTED_REQUEST;
            }
            case 422: {
                return HttpResponseStatus.UNPROCESSABLE_ENTITY;
            }
            case 423: {
                return HttpResponseStatus.LOCKED;
            }
            case 424: {
                return HttpResponseStatus.FAILED_DEPENDENCY;
            }
            case 425: {
                return HttpResponseStatus.UNORDERED_COLLECTION;
            }
            case 426: {
                return HttpResponseStatus.UPGRADE_REQUIRED;
            }
            case 428: {
                return HttpResponseStatus.PRECONDITION_REQUIRED;
            }
            case 429: {
                return HttpResponseStatus.TOO_MANY_REQUESTS;
            }
            case 431: {
                return HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;
            }
            case 500: {
                return HttpResponseStatus.INTERNAL_SERVER_ERROR;
            }
            case 501: {
                return HttpResponseStatus.NOT_IMPLEMENTED;
            }
            case 502: {
                return HttpResponseStatus.BAD_GATEWAY;
            }
            case 503: {
                return HttpResponseStatus.SERVICE_UNAVAILABLE;
            }
            case 504: {
                return HttpResponseStatus.GATEWAY_TIMEOUT;
            }
            case 505: {
                return HttpResponseStatus.HTTP_VERSION_NOT_SUPPORTED;
            }
            case 506: {
                return HttpResponseStatus.VARIANT_ALSO_NEGOTIATES;
            }
            case 507: {
                return HttpResponseStatus.INSUFFICIENT_STORAGE;
            }
            case 510: {
                return HttpResponseStatus.NOT_EXTENDED;
            }
            case 511: {
                return HttpResponseStatus.NETWORK_AUTHENTICATION_REQUIRED;
            }
            default: {
                return new HttpResponseStatus(code);
            }
        }
    }
    
    public static HttpResponseStatus parseLine(final CharSequence line) {
        final String status = line.toString();
        try {
            final int space = status.indexOf(32);
            if (space == -1) {
                return valueOf(Integer.parseInt(status));
            }
            final int code = Integer.parseInt(status.substring(0, space));
            final String reasonPhrase = status.substring(space + 1);
            final HttpResponseStatus responseStatus = valueOf(code);
            if (responseStatus.reasonPhrase().contentEquals(reasonPhrase)) {
                return responseStatus;
            }
            return new HttpResponseStatus(code, reasonPhrase);
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("malformed status line: " + status, e);
        }
    }
    
    public static HttpResponseStatus parseLine(final AsciiString line) {
        try {
            final HttpStatusLineProcessor processor = new HttpStatusLineProcessor(line);
            line.forEachByte(processor);
            final HttpResponseStatus status = processor.status();
            if (status == null) {
                throw new IllegalArgumentException("unable to get status after parsing input");
            }
            return status;
        }
        catch (final Exception e) {
            throw new IllegalArgumentException("malformed status line: " + (Object)line, e);
        }
    }
    
    private HttpResponseStatus(final int code) {
        this(code, (Object)HttpStatusClass.valueOf(code).defaultReasonPhrase() + " (" + code + ')', false);
    }
    
    public HttpResponseStatus(final int code, final String reasonPhrase) {
        this(code, reasonPhrase, false);
    }
    
    private HttpResponseStatus(final int code, final String reasonPhrase, final boolean bytes) {
        if (code < 0) {
            throw new IllegalArgumentException("code: " + code + " (expected: 0+)");
        }
        if (reasonPhrase == null) {
            throw new NullPointerException("reasonPhrase");
        }
        int i = 0;
        while (i < reasonPhrase.length()) {
            final char c = reasonPhrase.charAt(i);
            switch (c) {
                case '\n':
                case '\r': {
                    throw new IllegalArgumentException("reasonPhrase contains one of the following prohibited characters: \\r\\n: " + reasonPhrase);
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        this.code = code;
        this.codeAsText = new AsciiString(Integer.toString(code));
        this.reasonPhrase = reasonPhrase;
        if (bytes) {
            this.bytes = (code + " " + reasonPhrase).getBytes(CharsetUtil.US_ASCII);
        }
        else {
            this.bytes = null;
        }
    }
    
    public int code() {
        return this.code;
    }
    
    public AsciiString codeAsText() {
        return this.codeAsText;
    }
    
    public String reasonPhrase() {
        return this.reasonPhrase;
    }
    
    public HttpStatusClass codeClass() {
        HttpStatusClass type = this.codeClass;
        if (type == null) {
            type = (this.codeClass = HttpStatusClass.valueOf(this.code));
        }
        return type;
    }
    
    @Override
    public int hashCode() {
        return this.code();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof HttpResponseStatus && this.code() == ((HttpResponseStatus)o).code();
    }
    
    @Override
    public int compareTo(final HttpResponseStatus o) {
        return this.code() - o.code();
    }
    
    @Override
    public String toString() {
        return new StringBuilder(this.reasonPhrase.length() + 5).append(this.code).append(' ').append(this.reasonPhrase).toString();
    }
    
    void encode(final ByteBuf buf) {
        if (this.bytes == null) {
            HttpUtil.encodeAscii0(String.valueOf(this.code()), buf);
            buf.writeByte(32);
            HttpUtil.encodeAscii0(String.valueOf(this.reasonPhrase()), buf);
        }
        else {
            buf.writeBytes(this.bytes);
        }
    }
    
    static {
        CONTINUE = newStatus(100, "Continue");
        SWITCHING_PROTOCOLS = newStatus(101, "Switching Protocols");
        PROCESSING = newStatus(102, "Processing");
        OK = newStatus(200, "OK");
        CREATED = newStatus(201, "Created");
        ACCEPTED = newStatus(202, "Accepted");
        NON_AUTHORITATIVE_INFORMATION = newStatus(203, "Non-Authoritative Information");
        NO_CONTENT = newStatus(204, "No Content");
        RESET_CONTENT = newStatus(205, "Reset Content");
        PARTIAL_CONTENT = newStatus(206, "Partial Content");
        MULTI_STATUS = newStatus(207, "Multi-Status");
        MULTIPLE_CHOICES = newStatus(300, "Multiple Choices");
        MOVED_PERMANENTLY = newStatus(301, "Moved Permanently");
        FOUND = newStatus(302, "Found");
        SEE_OTHER = newStatus(303, "See Other");
        NOT_MODIFIED = newStatus(304, "Not Modified");
        USE_PROXY = newStatus(305, "Use Proxy");
        TEMPORARY_REDIRECT = newStatus(307, "Temporary Redirect");
        BAD_REQUEST = newStatus(400, "Bad Request");
        UNAUTHORIZED = newStatus(401, "Unauthorized");
        PAYMENT_REQUIRED = newStatus(402, "Payment Required");
        FORBIDDEN = newStatus(403, "Forbidden");
        NOT_FOUND = newStatus(404, "Not Found");
        METHOD_NOT_ALLOWED = newStatus(405, "Method Not Allowed");
        NOT_ACCEPTABLE = newStatus(406, "Not Acceptable");
        PROXY_AUTHENTICATION_REQUIRED = newStatus(407, "Proxy Authentication Required");
        REQUEST_TIMEOUT = newStatus(408, "Request Timeout");
        CONFLICT = newStatus(409, "Conflict");
        GONE = newStatus(410, "Gone");
        LENGTH_REQUIRED = newStatus(411, "Length Required");
        PRECONDITION_FAILED = newStatus(412, "Precondition Failed");
        REQUEST_ENTITY_TOO_LARGE = newStatus(413, "Request Entity Too Large");
        REQUEST_URI_TOO_LONG = newStatus(414, "Request-URI Too Long");
        UNSUPPORTED_MEDIA_TYPE = newStatus(415, "Unsupported Media Type");
        REQUESTED_RANGE_NOT_SATISFIABLE = newStatus(416, "Requested Range Not Satisfiable");
        EXPECTATION_FAILED = newStatus(417, "Expectation Failed");
        MISDIRECTED_REQUEST = newStatus(421, "Misdirected Request");
        UNPROCESSABLE_ENTITY = newStatus(422, "Unprocessable Entity");
        LOCKED = newStatus(423, "Locked");
        FAILED_DEPENDENCY = newStatus(424, "Failed Dependency");
        UNORDERED_COLLECTION = newStatus(425, "Unordered Collection");
        UPGRADE_REQUIRED = newStatus(426, "Upgrade Required");
        PRECONDITION_REQUIRED = newStatus(428, "Precondition Required");
        TOO_MANY_REQUESTS = newStatus(429, "Too Many Requests");
        REQUEST_HEADER_FIELDS_TOO_LARGE = newStatus(431, "Request Header Fields Too Large");
        INTERNAL_SERVER_ERROR = newStatus(500, "Internal Server Error");
        NOT_IMPLEMENTED = newStatus(501, "Not Implemented");
        BAD_GATEWAY = newStatus(502, "Bad Gateway");
        SERVICE_UNAVAILABLE = newStatus(503, "Service Unavailable");
        GATEWAY_TIMEOUT = newStatus(504, "Gateway Timeout");
        HTTP_VERSION_NOT_SUPPORTED = newStatus(505, "HTTP Version Not Supported");
        VARIANT_ALSO_NEGOTIATES = newStatus(506, "Variant Also Negotiates");
        INSUFFICIENT_STORAGE = newStatus(507, "Insufficient Storage");
        NOT_EXTENDED = newStatus(510, "Not Extended");
        NETWORK_AUTHENTICATION_REQUIRED = newStatus(511, "Network Authentication Required");
    }
    
    private static final class HttpStatusLineProcessor implements ByteProcessor
    {
        private static final byte ASCII_SPACE = 32;
        private final AsciiString string;
        private int i;
        private int state;
        private HttpResponseStatus status;
        
        public HttpStatusLineProcessor(final AsciiString string) {
            this.string = string;
        }
        
        @Override
        public boolean process(final byte value) {
            switch (this.state) {
                case 0: {
                    if (value == 32) {
                        this.state = 1;
                        break;
                    }
                    break;
                }
                case 1: {
                    this.parseStatus(this.i);
                    this.state = 2;
                    return false;
                }
            }
            ++this.i;
            return true;
        }
        
        private void parseStatus(final int codeEnd) {
            final int code = this.string.parseInt(0, codeEnd);
            this.status = HttpResponseStatus.valueOf(code);
            if (codeEnd < this.string.length()) {
                final String actualReason = this.string.toString(codeEnd + 1, this.string.length());
                if (!this.status.reasonPhrase().contentEquals(actualReason)) {
                    this.status = new HttpResponseStatus(code, actualReason);
                }
            }
        }
        
        public HttpResponseStatus status() {
            if (this.state <= 1) {
                this.parseStatus(this.string.length());
                this.state = 3;
            }
            return this.status;
        }
    }
}
