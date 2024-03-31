// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.dns;

import java.net.SocketAddress;
import io.netty.channel.AddressedEnvelope;
import io.netty.util.internal.StringUtil;

final class DnsMessageUtil
{
    static StringBuilder appendQuery(final StringBuilder buf, final DnsQuery query) {
        appendQueryHeader(buf, query);
        appendAllRecords(buf, query);
        return buf;
    }
    
    static StringBuilder appendResponse(final StringBuilder buf, final DnsResponse response) {
        appendResponseHeader(buf, response);
        appendAllRecords(buf, response);
        return buf;
    }
    
    static StringBuilder appendRecordClass(final StringBuilder buf, int dnsClass) {
        String name = null;
        switch (dnsClass &= 0xFFFF) {
            case 1: {
                name = "IN";
                break;
            }
            case 2: {
                name = "CSNET";
                break;
            }
            case 3: {
                name = "CHAOS";
                break;
            }
            case 4: {
                name = "HESIOD";
                break;
            }
            case 254: {
                name = "NONE";
                break;
            }
            case 255: {
                name = "ANY";
                break;
            }
            default: {
                name = null;
                break;
            }
        }
        if (name != null) {
            buf.append(name);
        }
        else {
            buf.append("UNKNOWN(").append(dnsClass).append(')');
        }
        return buf;
    }
    
    private static void appendQueryHeader(final StringBuilder buf, final DnsQuery msg) {
        buf.append(StringUtil.simpleClassName(msg)).append('(');
        appendAddresses(buf, msg).append(msg.id()).append(", ").append(msg.opCode());
        if (msg.isRecursionDesired()) {
            buf.append(", RD");
        }
        if (msg.z() != 0) {
            buf.append(", Z: ").append(msg.z());
        }
        buf.append(')');
    }
    
    private static void appendResponseHeader(final StringBuilder buf, final DnsResponse msg) {
        buf.append(StringUtil.simpleClassName(msg)).append('(');
        appendAddresses(buf, msg).append(msg.id()).append(", ").append(msg.opCode()).append(", ").append(msg.code()).append(',');
        boolean hasComma = true;
        if (msg.isRecursionDesired()) {
            hasComma = false;
            buf.append(" RD");
        }
        if (msg.isAuthoritativeAnswer()) {
            hasComma = false;
            buf.append(" AA");
        }
        if (msg.isTruncated()) {
            hasComma = false;
            buf.append(" TC");
        }
        if (msg.isRecursionAvailable()) {
            hasComma = false;
            buf.append(" RA");
        }
        if (msg.z() != 0) {
            if (!hasComma) {
                buf.append(',');
            }
            buf.append(" Z: ").append(msg.z());
        }
        if (hasComma) {
            buf.setCharAt(buf.length() - 1, ')');
        }
        else {
            buf.append(')');
        }
    }
    
    private static StringBuilder appendAddresses(final StringBuilder buf, final DnsMessage msg) {
        if (!(msg instanceof AddressedEnvelope)) {
            return buf;
        }
        final AddressedEnvelope<?, SocketAddress> envelope = (AddressedEnvelope<?, SocketAddress>)msg;
        SocketAddress addr = envelope.sender();
        if (addr != null) {
            buf.append("from: ").append(addr).append(", ");
        }
        addr = envelope.recipient();
        if (addr != null) {
            buf.append("to: ").append(addr).append(", ");
        }
        return buf;
    }
    
    private static void appendAllRecords(final StringBuilder buf, final DnsMessage msg) {
        appendRecords(buf, msg, DnsSection.QUESTION);
        appendRecords(buf, msg, DnsSection.ANSWER);
        appendRecords(buf, msg, DnsSection.AUTHORITY);
        appendRecords(buf, msg, DnsSection.ADDITIONAL);
    }
    
    private static void appendRecords(final StringBuilder buf, final DnsMessage message, final DnsSection section) {
        for (int count = message.count(section), i = 0; i < count; ++i) {
            buf.append(StringUtil.NEWLINE).append('\t').append(message.recordAt(section, i));
        }
    }
    
    private DnsMessageUtil() {
    }
}
