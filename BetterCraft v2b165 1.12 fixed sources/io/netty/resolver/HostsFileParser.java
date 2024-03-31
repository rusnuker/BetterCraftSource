// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;
import java.util.Map;
import java.net.InetAddress;
import java.util.Locale;
import io.netty.util.NetUtil;
import java.util.ArrayList;
import java.net.Inet6Address;
import java.net.Inet4Address;
import java.util.HashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import io.netty.util.internal.PlatformDependent;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;
import java.util.regex.Pattern;

public final class HostsFileParser
{
    private static final String WINDOWS_DEFAULT_SYSTEM_ROOT = "C:\\Windows";
    private static final String WINDOWS_HOSTS_FILE_RELATIVE_PATH = "\\system32\\drivers\\etc\\hosts";
    private static final String X_PLATFORMS_HOSTS_FILE_PATH = "/etc/hosts";
    private static final Pattern WHITESPACES;
    private static final InternalLogger logger;
    
    private static File locateHostsFile() {
        File hostsFile;
        if (PlatformDependent.isWindows()) {
            hostsFile = new File(System.getenv("SystemRoot") + "\\system32\\drivers\\etc\\hosts");
            if (!hostsFile.exists()) {
                hostsFile = new File("C:\\Windows\\system32\\drivers\\etc\\hosts");
            }
        }
        else {
            hostsFile = new File("/etc/hosts");
        }
        return hostsFile;
    }
    
    public static HostsFileEntries parseSilently() {
        final File hostsFile = locateHostsFile();
        try {
            return parse(hostsFile);
        }
        catch (final IOException e) {
            HostsFileParser.logger.warn("Failed to load and parse hosts file at " + hostsFile.getPath(), e);
            return HostsFileEntries.EMPTY;
        }
    }
    
    public static HostsFileEntries parse() throws IOException {
        return parse(locateHostsFile());
    }
    
    public static HostsFileEntries parse(final File file) throws IOException {
        ObjectUtil.checkNotNull(file, "file");
        if (file.exists() && file.isFile()) {
            return parse(new BufferedReader(new FileReader(file)));
        }
        return HostsFileEntries.EMPTY;
    }
    
    public static HostsFileEntries parse(final Reader reader) throws IOException {
        ObjectUtil.checkNotNull(reader, "reader");
        final BufferedReader buff = new BufferedReader(reader);
        try {
            final Map<String, Inet4Address> ipv4Entries = new HashMap<String, Inet4Address>();
            final Map<String, Inet6Address> ipv6Entries = new HashMap<String, Inet6Address>();
            String line;
            while ((line = buff.readLine()) != null) {
                final int commentPosition = line.indexOf(35);
                if (commentPosition != -1) {
                    line = line.substring(0, commentPosition);
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                final List<String> lineParts = new ArrayList<String>();
                for (final String s : HostsFileParser.WHITESPACES.split(line)) {
                    if (!s.isEmpty()) {
                        lineParts.add(s);
                    }
                }
                if (lineParts.size() < 2) {
                    continue;
                }
                final byte[] ipBytes = NetUtil.createByteArrayFromIpAddressString(lineParts.get(0));
                if (ipBytes == null) {
                    continue;
                }
                for (int i = 1; i < lineParts.size(); ++i) {
                    final String hostname = lineParts.get(i);
                    final String hostnameLower = hostname.toLowerCase(Locale.ENGLISH);
                    final InetAddress address = InetAddress.getByAddress(hostname, ipBytes);
                    if (address instanceof Inet4Address) {
                        final Inet4Address previous = ipv4Entries.put(hostnameLower, (Inet4Address)address);
                        if (previous != null) {
                            ipv4Entries.put(hostnameLower, previous);
                        }
                    }
                    else {
                        final Inet6Address previous2 = ipv6Entries.put(hostnameLower, (Inet6Address)address);
                        if (previous2 != null) {
                            ipv6Entries.put(hostnameLower, previous2);
                        }
                    }
                }
            }
            return (ipv4Entries.isEmpty() && ipv6Entries.isEmpty()) ? HostsFileEntries.EMPTY : new HostsFileEntries(ipv4Entries, ipv6Entries);
        }
        finally {
            try {
                buff.close();
            }
            catch (final IOException e) {
                HostsFileParser.logger.warn("Failed to close a reader", e);
            }
        }
    }
    
    private HostsFileParser() {
    }
    
    static {
        WHITESPACES = Pattern.compile("[ \t]+");
        logger = InternalLoggerFactory.getInstance(HostsFileParser.class);
    }
}
