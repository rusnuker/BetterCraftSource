// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.List;
import java.security.AccessController;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.security.PrivilegedAction;
import java.net.SocketException;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import io.netty.util.internal.logging.InternalLogger;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.Inet6Address;
import java.net.Inet4Address;

public final class NetUtil
{
    public static final Inet4Address LOCALHOST4;
    public static final Inet6Address LOCALHOST6;
    public static final InetAddress LOCALHOST;
    public static final NetworkInterface LOOPBACK_IF;
    public static final int SOMAXCONN;
    private static final int IPV6_WORD_COUNT = 8;
    private static final int IPV6_MAX_CHAR_COUNT = 39;
    private static final int IPV6_BYTE_COUNT = 16;
    private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;
    private static final int IPV6_MIN_SEPARATORS = 2;
    private static final int IPV6_MAX_SEPARATORS = 8;
    private static final int IPV4_BYTE_COUNT = 4;
    private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;
    private static final int IPV4_SEPARATORS = 3;
    private static final boolean IPV4_PREFERRED;
    private static final boolean IPV6_ADDRESSES_PREFERRED;
    private static final InternalLogger logger;
    
    public static boolean isIpV4StackPreferred() {
        return NetUtil.IPV4_PREFERRED;
    }
    
    public static boolean isIpV6AddressesPreferred() {
        return NetUtil.IPV6_ADDRESSES_PREFERRED;
    }
    
    public static byte[] createByteArrayFromIpAddressString(String ipAddressString) {
        if (isValidIpV4Address(ipAddressString)) {
            final StringTokenizer tokenizer = new StringTokenizer(ipAddressString, ".");
            final byte[] byteAddress = new byte[4];
            for (int i = 0; i < 4; ++i) {
                final String token = tokenizer.nextToken();
                final int tempInt = Integer.parseInt(token);
                byteAddress[i] = (byte)tempInt;
            }
            return byteAddress;
        }
        if (isValidIpV6Address(ipAddressString)) {
            if (ipAddressString.charAt(0) == '[') {
                ipAddressString = ipAddressString.substring(1, ipAddressString.length() - 1);
            }
            final int percentPos = ipAddressString.indexOf(37);
            if (percentPos >= 0) {
                ipAddressString = ipAddressString.substring(0, percentPos);
            }
            final StringTokenizer tokenizer2 = new StringTokenizer(ipAddressString, ":.", true);
            final ArrayList<String> hexStrings = new ArrayList<String>();
            final ArrayList<String> decStrings = new ArrayList<String>();
            String token2 = "";
            String prevToken = "";
            int doubleColonIndex = -1;
            while (tokenizer2.hasMoreTokens()) {
                prevToken = token2;
                token2 = tokenizer2.nextToken();
                if (":".equals(token2)) {
                    if (":".equals(prevToken)) {
                        doubleColonIndex = hexStrings.size();
                    }
                    else {
                        if (prevToken.isEmpty()) {
                            continue;
                        }
                        hexStrings.add(prevToken);
                    }
                }
                else {
                    if (!".".equals(token2)) {
                        continue;
                    }
                    decStrings.add(prevToken);
                }
            }
            if (":".equals(prevToken)) {
                if (":".equals(token2)) {
                    doubleColonIndex = hexStrings.size();
                }
                else {
                    hexStrings.add(token2);
                }
            }
            else if (".".equals(prevToken)) {
                decStrings.add(token2);
            }
            int hexStringsLength = 8;
            if (!decStrings.isEmpty()) {
                hexStringsLength -= 2;
            }
            if (doubleColonIndex != -1) {
                for (int numberToInsert = hexStringsLength - hexStrings.size(), j = 0; j < numberToInsert; ++j) {
                    hexStrings.add(doubleColonIndex, "0");
                }
            }
            final byte[] ipByteArray = new byte[16];
            for (int j = 0; j < hexStrings.size(); ++j) {
                convertToBytes(hexStrings.get(j), ipByteArray, j << 1);
            }
            for (int j = 0; j < decStrings.size(); ++j) {
                ipByteArray[j + 12] = (byte)(Integer.parseInt(decStrings.get(j)) & 0xFF);
            }
            return ipByteArray;
        }
        return null;
    }
    
    private static void convertToBytes(final String hexWord, final byte[] ipByteArray, final int byteIndex) {
        final int hexWordLength = hexWord.length();
        int hexWordIndex = 0;
        ipByteArray[byteIndex + 1] = (ipByteArray[byteIndex] = 0);
        if (hexWordLength > 3) {
            final int charValue = getIntValue(hexWord.charAt(hexWordIndex++));
            ipByteArray[byteIndex] |= (byte)(charValue << 4);
        }
        if (hexWordLength > 2) {
            final int charValue = getIntValue(hexWord.charAt(hexWordIndex++));
            ipByteArray[byteIndex] |= (byte)charValue;
        }
        if (hexWordLength > 1) {
            final int charValue = getIntValue(hexWord.charAt(hexWordIndex++));
            final int n = byteIndex + 1;
            ipByteArray[n] |= (byte)(charValue << 4);
        }
        final int charValue = getIntValue(hexWord.charAt(hexWordIndex));
        final int n2 = byteIndex + 1;
        ipByteArray[n2] |= (byte)(charValue & 0xF);
    }
    
    private static int getIntValue(char c) {
        switch (c) {
            case '0': {
                return 0;
            }
            case '1': {
                return 1;
            }
            case '2': {
                return 2;
            }
            case '3': {
                return 3;
            }
            case '4': {
                return 4;
            }
            case '5': {
                return 5;
            }
            case '6': {
                return 6;
            }
            case '7': {
                return 7;
            }
            case '8': {
                return 8;
            }
            case '9': {
                return 9;
            }
            default: {
                c = Character.toLowerCase(c);
                switch (c) {
                    case 'a': {
                        return 10;
                    }
                    case 'b': {
                        return 11;
                    }
                    case 'c': {
                        return 12;
                    }
                    case 'd': {
                        return 13;
                    }
                    case 'e': {
                        return 14;
                    }
                    case 'f': {
                        return 15;
                    }
                    default: {
                        return 0;
                    }
                }
                break;
            }
        }
    }
    
    public static String intToIpAddress(final int i) {
        final StringBuilder buf = new StringBuilder(15);
        buf.append(i >> 24 & 0xFF);
        buf.append('.');
        buf.append(i >> 16 & 0xFF);
        buf.append('.');
        buf.append(i >> 8 & 0xFF);
        buf.append('.');
        buf.append(i & 0xFF);
        return buf.toString();
    }
    
    public static String bytesToIpAddress(final byte[] bytes) {
        return bytesToIpAddress(bytes, 0, bytes.length);
    }
    
    public static String bytesToIpAddress(final byte[] bytes, final int offset, final int length) {
        switch (length) {
            case 4: {
                return new StringBuilder(15).append(bytes[offset] & 0xFF).append('.').append(bytes[offset + 1] & 0xFF).append('.').append(bytes[offset + 2] & 0xFF).append('.').append(bytes[offset + 3] & 0xFF).toString();
            }
            case 16: {
                return toAddressString(bytes, offset, false);
            }
            default: {
                throw new IllegalArgumentException("length: " + length + " (expected: 4 or 16)");
            }
        }
    }
    
    public static boolean isValidIpV6Address(final String ipAddress) {
        final int length = ipAddress.length();
        boolean doubleColon = false;
        int numberOfColons = 0;
        int numberOfPeriods = 0;
        final StringBuilder word = new StringBuilder();
        char c = '\0';
        int startOffset = 0;
        int endOffset = ipAddress.length();
        if (endOffset < 2) {
            return false;
        }
        if (ipAddress.charAt(0) == '[') {
            if (ipAddress.charAt(endOffset - 1) != ']') {
                return false;
            }
            startOffset = 1;
            --endOffset;
        }
        final int percentIdx = ipAddress.indexOf(37, startOffset);
        if (percentIdx >= 0) {
            endOffset = percentIdx;
        }
        for (int i = startOffset; i < endOffset; ++i) {
            final char prevChar = c;
            c = ipAddress.charAt(i);
            switch (c) {
                case '.': {
                    if (++numberOfPeriods > 3) {
                        return false;
                    }
                    if (!isValidIp4Word(word.toString())) {
                        return false;
                    }
                    if (numberOfColons != 6 && !doubleColon) {
                        return false;
                    }
                    if (numberOfColons == 7 && ipAddress.charAt(startOffset) != ':' && ipAddress.charAt(1 + startOffset) != ':') {
                        return false;
                    }
                    word.delete(0, word.length());
                    break;
                }
                case ':': {
                    if (i == startOffset && (ipAddress.length() <= i || ipAddress.charAt(i + 1) != ':')) {
                        return false;
                    }
                    if (++numberOfColons > 7) {
                        return false;
                    }
                    if (numberOfPeriods > 0) {
                        return false;
                    }
                    if (prevChar == ':') {
                        if (doubleColon) {
                            return false;
                        }
                        doubleColon = true;
                    }
                    word.delete(0, word.length());
                    break;
                }
                default: {
                    if (word != null && word.length() > 3) {
                        return false;
                    }
                    if (!isValidHexChar(c)) {
                        return false;
                    }
                    word.append(c);
                    break;
                }
            }
        }
        if (numberOfPeriods > 0) {
            if (numberOfPeriods != 3 || !isValidIp4Word(word.toString()) || numberOfColons >= 7) {
                return false;
            }
        }
        else {
            if (numberOfColons != 7 && !doubleColon) {
                return false;
            }
            if (word.length() == 0 && ipAddress.charAt(length - 1 - startOffset) == ':' && ipAddress.charAt(length - 2 - startOffset) != ':') {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isValidIp4Word(final String word) {
        if (word.length() < 1 || word.length() > 3) {
            return false;
        }
        for (int i = 0; i < word.length(); ++i) {
            final char c = word.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return Integer.parseInt(word) <= 255;
    }
    
    private static boolean isValidHexChar(final char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }
    
    private static boolean isValidNumericChar(final char c) {
        return c >= '0' && c <= '9';
    }
    
    public static boolean isValidIpV4Address(final String value) {
        int periods = 0;
        final int length = value.length();
        if (length > 15) {
            return false;
        }
        final StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            final char c = value.charAt(i);
            if (c == '.') {
                if (++periods > 3) {
                    return false;
                }
                if (word.length() == 0) {
                    return false;
                }
                if (Integer.parseInt(word.toString()) > 255) {
                    return false;
                }
                word.delete(0, word.length());
            }
            else {
                if (!Character.isDigit(c)) {
                    return false;
                }
                if (word.length() > 2) {
                    return false;
                }
                word.append(c);
            }
        }
        return word.length() != 0 && Integer.parseInt(word.toString()) <= 255 && periods == 3;
    }
    
    public static Inet6Address getByName(final CharSequence ip) {
        return getByName(ip, true);
    }
    
    public static Inet6Address getByName(final CharSequence ip, final boolean ipv4Mapped) {
        final byte[] bytes = new byte[16];
        final int ipLength = ip.length();
        int compressBegin = 0;
        int compressLength = 0;
        int currentIndex = 0;
        int value = 0;
        int begin = -1;
        int i = 0;
        int ipv6Seperators = 0;
        int ipv4Seperators = 0;
        boolean needsShift = false;
        while (i < ipLength) {
            final char c = ip.charAt(i);
            switch (c) {
                case ':': {
                    ++ipv6Seperators;
                    if (i - begin > 4 || ipv4Seperators > 0 || ipv6Seperators > 8 || currentIndex + 1 >= bytes.length) {
                        return null;
                    }
                    value <<= 4 - (i - begin) << 2;
                    if (compressLength > 0) {
                        compressLength -= 2;
                    }
                    bytes[currentIndex++] = (byte)((value & 0xF) << 4 | (value >> 4 & 0xF));
                    bytes[currentIndex++] = (byte)((value >> 8 & 0xF) << 4 | (value >> 12 & 0xF));
                    int tmp = i + 1;
                    if (tmp < ipLength && ip.charAt(tmp) == ':') {
                        ++tmp;
                        if (compressBegin != 0 || (tmp < ipLength && ip.charAt(tmp) == ':')) {
                            return null;
                        }
                        needsShift = (++ipv6Seperators == 2 && value == 0);
                        compressBegin = currentIndex;
                        compressLength = bytes.length - compressBegin - 2;
                        ++i;
                    }
                    value = 0;
                    begin = -1;
                    break;
                }
                case '.': {
                    ++ipv4Seperators;
                    if (i - begin > 3 || ipv4Seperators > 3 || (ipv6Seperators > 0 && currentIndex + compressLength < 12) || i + 1 >= ipLength || currentIndex >= bytes.length || begin < 0 || (begin == 0 && ((i == 3 && (!isValidNumericChar(ip.charAt(2)) || !isValidNumericChar(ip.charAt(1)) || !isValidNumericChar(ip.charAt(0)))) || (i == 2 && (!isValidNumericChar(ip.charAt(1)) || !isValidNumericChar(ip.charAt(0)))) || (i == 1 && !isValidNumericChar(ip.charAt(0)))))) {
                        return null;
                    }
                    value <<= 3 - (i - begin) << 2;
                    begin = (value & 0xF) * 100 + (value >> 4 & 0xF) * 10 + (value >> 8 & 0xF);
                    if (begin < 0 || begin > 255) {
                        return null;
                    }
                    bytes[currentIndex++] = (byte)begin;
                    value = 0;
                    begin = -1;
                    break;
                }
                default: {
                    if (!isValidHexChar(c) || (ipv4Seperators > 0 && !isValidNumericChar(c))) {
                        return null;
                    }
                    if (begin < 0) {
                        begin = i;
                    }
                    else if (i - begin > 4) {
                        return null;
                    }
                    value += getIntValue(c) << (i - begin << 2);
                    break;
                }
            }
            ++i;
        }
        final boolean isCompressed = compressBegin > 0;
        if (ipv4Seperators > 0) {
            if ((begin > 0 && i - begin > 3) || ipv4Seperators != 3 || currentIndex >= bytes.length) {
                return null;
            }
            if (ipv6Seperators == 0) {
                compressLength = 12;
            }
            else {
                if (ipv6Seperators < 2 || ip.charAt(ipLength - 1) == ':' || ((isCompressed || ipv6Seperators != 6 || ip.charAt(0) == ':') && (!isCompressed || ipv6Seperators + 1 >= 8 || (ip.charAt(0) == ':' && compressBegin > 2)))) {
                    return null;
                }
                compressLength -= 2;
            }
            value <<= 3 - (i - begin) << 2;
            begin = (value & 0xF) * 100 + (value >> 4 & 0xF) * 10 + (value >> 8 & 0xF);
            if (begin < 0 || begin > 255) {
                return null;
            }
            bytes[currentIndex++] = (byte)begin;
        }
        else {
            final int tmp = ipLength - 1;
            if ((begin > 0 && i - begin > 4) || ipv6Seperators < 2 || (!isCompressed && (ipv6Seperators + 1 != 8 || ip.charAt(0) == ':' || ip.charAt(tmp) == ':')) || (isCompressed && (ipv6Seperators > 8 || (ipv6Seperators == 8 && ((compressBegin <= 2 && ip.charAt(0) != ':') || (compressBegin >= 14 && ip.charAt(tmp) != ':'))))) || currentIndex + 1 >= bytes.length) {
                return null;
            }
            if (begin >= 0 && i - begin <= 4) {
                value <<= 4 - (i - begin) << 2;
            }
            bytes[currentIndex++] = (byte)((value & 0xF) << 4 | (value >> 4 & 0xF));
            bytes[currentIndex++] = (byte)((value >> 8 & 0xF) << 4 | (value >> 12 & 0xF));
        }
        i = currentIndex + compressLength;
        if (needsShift || i >= bytes.length) {
            if (i >= bytes.length) {
                ++compressBegin;
            }
            for (i = currentIndex; i < bytes.length; ++i) {
                for (begin = bytes.length - 1; begin >= compressBegin; --begin) {
                    bytes[begin] = bytes[begin - 1];
                }
                bytes[begin] = 0;
                ++compressBegin;
            }
        }
        else {
            for (i = 0; i < compressLength; ++i) {
                begin = i + compressBegin;
                currentIndex = begin + compressLength;
                if (currentIndex >= bytes.length) {
                    break;
                }
                bytes[currentIndex] = bytes[begin];
                bytes[begin] = 0;
            }
        }
        if (ipv4Mapped && ipv4Seperators > 0 && bytes[0] == 0 && bytes[1] == 0 && bytes[2] == 0 && bytes[3] == 0 && bytes[4] == 0 && bytes[5] == 0 && bytes[6] == 0 && bytes[7] == 0 && bytes[8] == 0 && bytes[9] == 0) {
            bytes[10] = (bytes[11] = -1);
        }
        try {
            return Inet6Address.getByAddress(null, bytes, -1);
        }
        catch (final UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String toSocketAddressString(final InetSocketAddress addr) {
        final String port = String.valueOf(addr.getPort());
        StringBuilder sb;
        if (addr.isUnresolved()) {
            final String hostString = (PlatformDependent.javaVersion() >= 7) ? addr.getHostString() : addr.getHostName();
            sb = newSocketAddressStringBuilder(hostString, port, !isValidIpV6Address(hostString));
        }
        else {
            final InetAddress address = addr.getAddress();
            final String hostString2 = toAddressString(address);
            sb = newSocketAddressStringBuilder(hostString2, port, address instanceof Inet4Address);
        }
        return sb.append(':').append(port).toString();
    }
    
    public static String toSocketAddressString(final String host, final int port) {
        final String portStr = String.valueOf(port);
        return newSocketAddressStringBuilder(host, portStr, !isValidIpV6Address(host)).append(':').append(portStr).toString();
    }
    
    private static StringBuilder newSocketAddressStringBuilder(final String host, final String port, final boolean ipv4) {
        final int hostLen = host.length();
        if (ipv4) {
            return new StringBuilder(hostLen + 1 + port.length()).append(host);
        }
        final StringBuilder stringBuilder = new StringBuilder(hostLen + 3 + port.length());
        if (hostLen > 1 && host.charAt(0) == '[' && host.charAt(hostLen - 1) == ']') {
            return stringBuilder.append(host);
        }
        return stringBuilder.append('[').append(host).append(']');
    }
    
    public static String toAddressString(final InetAddress ip) {
        return toAddressString(ip, false);
    }
    
    public static String toAddressString(final InetAddress ip, final boolean ipv4Mapped) {
        if (ip instanceof Inet4Address) {
            return ip.getHostAddress();
        }
        if (!(ip instanceof Inet6Address)) {
            throw new IllegalArgumentException("Unhandled type: " + ip);
        }
        return toAddressString(ip.getAddress(), 0, ipv4Mapped);
    }
    
    private static String toAddressString(final byte[] bytes, final int offset, final boolean ipv4Mapped) {
        final int[] words = new int[8];
        for (int end = offset + words.length, i = offset; i < end; ++i) {
            words[i] = ((bytes[i << 1] & 0xFF) << 8 | (bytes[(i << 1) + 1] & 0xFF));
        }
        int currentStart = -1;
        int currentLength = 0;
        int shortestStart = -1;
        int shortestLength = 0;
        int i;
        for (i = 0; i < words.length; ++i) {
            if (words[i] == 0) {
                if (currentStart < 0) {
                    currentStart = i;
                }
            }
            else if (currentStart >= 0) {
                currentLength = i - currentStart;
                if (currentLength > shortestLength) {
                    shortestStart = currentStart;
                    shortestLength = currentLength;
                }
                currentStart = -1;
            }
        }
        if (currentStart >= 0) {
            currentLength = i - currentStart;
            if (currentLength > shortestLength) {
                shortestStart = currentStart;
                shortestLength = currentLength;
            }
        }
        if (shortestLength == 1) {
            shortestLength = 0;
            shortestStart = -1;
        }
        final int shortestEnd = shortestStart + shortestLength;
        final StringBuilder b = new StringBuilder(39);
        if (shortestEnd < 0) {
            b.append(Integer.toHexString(words[0]));
            for (i = 1; i < words.length; ++i) {
                b.append(':');
                b.append(Integer.toHexString(words[i]));
            }
        }
        else {
            boolean isIpv4Mapped;
            if (inRangeEndExclusive(0, shortestStart, shortestEnd)) {
                b.append("::");
                isIpv4Mapped = (ipv4Mapped && shortestEnd == 5 && words[5] == 65535);
            }
            else {
                b.append(Integer.toHexString(words[0]));
                isIpv4Mapped = false;
            }
            for (i = 1; i < words.length; ++i) {
                if (!inRangeEndExclusive(i, shortestStart, shortestEnd)) {
                    if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd)) {
                        if (!isIpv4Mapped || i == 6) {
                            b.append(':');
                        }
                        else {
                            b.append('.');
                        }
                    }
                    if (isIpv4Mapped && i > 5) {
                        b.append(words[i] >> 8);
                        b.append('.');
                        b.append(words[i] & 0xFF);
                    }
                    else {
                        b.append(Integer.toHexString(words[i]));
                    }
                }
                else if (!inRangeEndExclusive(i - 1, shortestStart, shortestEnd)) {
                    b.append("::");
                }
            }
        }
        return b.toString();
    }
    
    private static boolean inRangeEndExclusive(final int value, final int start, final int end) {
        return value >= start && value < end;
    }
    
    private NetUtil() {
    }
    
    static {
        IPV4_PREFERRED = Boolean.getBoolean("java.net.preferIPv4Stack");
        IPV6_ADDRESSES_PREFERRED = Boolean.getBoolean("java.net.preferIPv6Addresses");
        (logger = InternalLoggerFactory.getInstance(NetUtil.class)).debug("-Djava.net.preferIPv4Stack: {}", (Object)NetUtil.IPV4_PREFERRED);
        NetUtil.logger.debug("-Djava.net.preferIPv6Addresses: {}", (Object)NetUtil.IPV6_ADDRESSES_PREFERRED);
        final byte[] LOCALHOST4_BYTES = { 127, 0, 0, 1 };
        final byte[] LOCALHOST6_BYTES = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
        Inet4Address localhost4 = null;
        try {
            localhost4 = (Inet4Address)InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);
        }
        catch (final Exception e) {
            PlatformDependent.throwException(e);
        }
        LOCALHOST4 = localhost4;
        Inet6Address localhost5 = null;
        try {
            localhost5 = (Inet6Address)InetAddress.getByAddress("localhost", LOCALHOST6_BYTES);
        }
        catch (final Exception e2) {
            PlatformDependent.throwException(e2);
        }
        LOCALHOST6 = localhost5;
        final List<NetworkInterface> ifaces = new ArrayList<NetworkInterface>();
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    final NetworkInterface iface = interfaces.nextElement();
                    if (SocketUtils.addressesFromNetworkInterface(iface).hasMoreElements()) {
                        ifaces.add(iface);
                    }
                }
            }
        }
        catch (final SocketException e3) {
            NetUtil.logger.warn("Failed to retrieve the list of available network interfaces", e3);
        }
        NetworkInterface loopbackIface = null;
        InetAddress loopbackAddr = null;
    Label_0388:
        for (final NetworkInterface iface2 : ifaces) {
            final Enumeration<InetAddress> i = SocketUtils.addressesFromNetworkInterface(iface2);
            while (i.hasMoreElements()) {
                final InetAddress addr = i.nextElement();
                if (addr.isLoopbackAddress()) {
                    loopbackIface = iface2;
                    loopbackAddr = addr;
                    break Label_0388;
                }
            }
        }
        if (loopbackIface == null) {
            try {
                for (final NetworkInterface iface2 : ifaces) {
                    if (iface2.isLoopback()) {
                        final Enumeration<InetAddress> i = SocketUtils.addressesFromNetworkInterface(iface2);
                        if (i.hasMoreElements()) {
                            loopbackIface = iface2;
                            loopbackAddr = i.nextElement();
                            break;
                        }
                        continue;
                    }
                }
                if (loopbackIface == null) {
                    NetUtil.logger.warn("Failed to find the loopback interface");
                }
            }
            catch (final SocketException e4) {
                NetUtil.logger.warn("Failed to find the loopback interface", e4);
            }
        }
        if (loopbackIface != null) {
            NetUtil.logger.debug("Loopback interface: {} ({}, {})", loopbackIface.getName(), loopbackIface.getDisplayName(), loopbackAddr.getHostAddress());
        }
        else if (loopbackAddr == null) {
            try {
                if (NetworkInterface.getByInetAddress(NetUtil.LOCALHOST6) != null) {
                    NetUtil.logger.debug("Using hard-coded IPv6 localhost address: {}", localhost5);
                    loopbackAddr = localhost5;
                }
            }
            catch (final Exception ex) {}
            finally {
                if (loopbackAddr == null) {
                    NetUtil.logger.debug("Using hard-coded IPv4 localhost address: {}", localhost4);
                    loopbackAddr = localhost4;
                }
            }
        }
        LOOPBACK_IF = loopbackIface;
        LOCALHOST = loopbackAddr;
        SOMAXCONN = AccessController.doPrivileged((PrivilegedAction<Integer>)new PrivilegedAction<Integer>() {
            @Override
            public Integer run() {
                int somaxconn = PlatformDependent.isWindows() ? 200 : 128;
                final File file = new File("/proc/sys/net/core/somaxconn");
                BufferedReader in = null;
                try {
                    if (file.exists()) {
                        in = new BufferedReader(new FileReader(file));
                        somaxconn = Integer.parseInt(in.readLine());
                        if (NetUtil.logger.isDebugEnabled()) {
                            NetUtil.logger.debug("{}: {}", file, somaxconn);
                        }
                    }
                    else if (NetUtil.logger.isDebugEnabled()) {
                        NetUtil.logger.debug("{}: {} (non-existent)", file, somaxconn);
                    }
                }
                catch (final Exception e) {
                    NetUtil.logger.debug("Failed to get SOMAXCONN from: {}", file, e);
                }
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        }
                        catch (final Exception ex) {}
                    }
                }
                return somaxconn;
            }
        });
    }
}
