// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.util.Enumeration;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import org.apache.logging.log4j.Logger;

public final class NetUtils
{
    private static final Logger LOGGER;
    private static final String UNKNOWN_LOCALHOST = "UNKNOWN_LOCALHOST";
    
    private NetUtils() {
    }
    
    public static String getLocalHostname() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (final UnknownHostException uhe) {
            try {
                final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    final NetworkInterface nic = interfaces.nextElement();
                    final Enumeration<InetAddress> addresses = nic.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        final InetAddress address = addresses.nextElement();
                        if (!address.isLoopbackAddress()) {
                            final String hostname = address.getHostName();
                            if (hostname != null) {
                                return hostname;
                            }
                            continue;
                        }
                    }
                }
            }
            catch (final SocketException se) {
                NetUtils.LOGGER.error("Could not determine local host name", uhe);
                return "UNKNOWN_LOCALHOST";
            }
            NetUtils.LOGGER.error("Could not determine local host name", uhe);
            return "UNKNOWN_LOCALHOST";
        }
    }
    
    public static URI toURI(final String path) {
        try {
            return new URI(path);
        }
        catch (final URISyntaxException e) {
            try {
                final URL url = new URL(path);
                return new URI(url.getProtocol(), url.getHost(), url.getPath(), null);
            }
            catch (final MalformedURLException | URISyntaxException nestedEx) {
                return new File(path).toURI();
            }
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
