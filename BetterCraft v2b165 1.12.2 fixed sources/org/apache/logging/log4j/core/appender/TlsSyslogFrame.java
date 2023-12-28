// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender;

import java.nio.charset.StandardCharsets;

public class TlsSyslogFrame
{
    private final String message;
    private final int byteLength;
    
    public TlsSyslogFrame(final String message) {
        this.message = message;
        final byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        this.byteLength = messageBytes.length;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public String toString() {
        return Integer.toString(this.byteLength) + ' ' + this.message;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.message == null) ? 0 : this.message.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TlsSyslogFrame)) {
            return false;
        }
        final TlsSyslogFrame other = (TlsSyslogFrame)obj;
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        }
        else if (!this.message.equals(other.message)) {
            return false;
        }
        return true;
    }
}
