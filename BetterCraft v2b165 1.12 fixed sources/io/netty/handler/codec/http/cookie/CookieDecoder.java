// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cookie;

import java.nio.CharBuffer;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.InternalLogger;

public abstract class CookieDecoder
{
    private final InternalLogger logger;
    private final boolean strict;
    
    protected CookieDecoder(final boolean strict) {
        this.logger = InternalLoggerFactory.getInstance(this.getClass());
        this.strict = strict;
    }
    
    protected DefaultCookie initCookie(final String header, final int nameBegin, final int nameEnd, final int valueBegin, final int valueEnd) {
        if (nameBegin == -1 || nameBegin == nameEnd) {
            this.logger.debug("Skipping cookie with null name");
            return null;
        }
        if (valueBegin == -1) {
            this.logger.debug("Skipping cookie with null value");
            return null;
        }
        final CharSequence wrappedValue = CharBuffer.wrap(header, valueBegin, valueEnd);
        final CharSequence unwrappedValue = CookieUtil.unwrapValue(wrappedValue);
        if (unwrappedValue == null) {
            this.logger.debug("Skipping cookie because starting quotes are not properly balanced in '{}'", wrappedValue);
            return null;
        }
        final String name = header.substring(nameBegin, nameEnd);
        int invalidOctetPos;
        if (this.strict && (invalidOctetPos = CookieUtil.firstInvalidCookieNameOctet(name)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because name '{}' contains invalid char '{}'", name, name.charAt(invalidOctetPos));
            }
            return null;
        }
        final boolean wrap = unwrappedValue.length() != valueEnd - valueBegin;
        if (this.strict && (invalidOctetPos = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because value '{}' contains invalid char '{}'", unwrappedValue, unwrappedValue.charAt(invalidOctetPos));
            }
            return null;
        }
        final DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
        cookie.setWrap(wrap);
        return cookie;
    }
}
