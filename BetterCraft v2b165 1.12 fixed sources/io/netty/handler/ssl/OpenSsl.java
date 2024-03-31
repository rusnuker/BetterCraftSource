// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Iterator;
import java.util.Collections;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import java.util.Locale;
import io.netty.internal.tcnative.Library;
import io.netty.util.internal.NativeLibraryLoader;
import java.util.LinkedHashSet;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.internal.tcnative.Buffer;
import io.netty.buffer.ByteBuf;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import java.util.Set;
import io.netty.util.internal.logging.InternalLogger;

public final class OpenSsl
{
    private static final InternalLogger logger;
    private static final String LINUX = "linux";
    private static final String UNKNOWN = "unknown";
    private static final Throwable UNAVAILABILITY_CAUSE;
    static final Set<String> AVAILABLE_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_OPENSSL_CIPHER_SUITES;
    private static final Set<String> AVAILABLE_JAVA_CIPHER_SUITES;
    private static final boolean SUPPORTS_KEYMANAGER_FACTORY;
    private static final boolean SUPPORTS_HOSTNAME_VALIDATION;
    private static final boolean USE_KEYMANAGER_FACTORY;
    static final String PROTOCOL_SSL_V2_HELLO = "SSLv2Hello";
    static final String PROTOCOL_SSL_V2 = "SSLv2";
    static final String PROTOCOL_SSL_V3 = "SSLv3";
    static final String PROTOCOL_TLS_V1 = "TLSv1";
    static final String PROTOCOL_TLS_V1_1 = "TLSv1.1";
    static final String PROTOCOL_TLS_V1_2 = "TLSv1.2";
    static final Set<String> SUPPORTED_PROTOCOLS_SET;
    
    private static boolean doesSupportProtocol(final int protocol) {
        long sslCtx = -1L;
        try {
            sslCtx = SSLContext.make(protocol, 2);
            return true;
        }
        catch (final Exception ignore) {
            return false;
        }
        finally {
            if (sslCtx != -1L) {
                SSLContext.free(sslCtx);
            }
        }
    }
    
    public static boolean isAvailable() {
        return OpenSsl.UNAVAILABILITY_CAUSE == null;
    }
    
    public static boolean isAlpnSupported() {
        return version() >= 268443648L;
    }
    
    public static int version() {
        if (isAvailable()) {
            return SSL.version();
        }
        return -1;
    }
    
    public static String versionString() {
        if (isAvailable()) {
            return SSL.versionString();
        }
        return null;
    }
    
    public static void ensureAvailability() {
        if (OpenSsl.UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(OpenSsl.UNAVAILABILITY_CAUSE);
        }
    }
    
    public static Throwable unavailabilityCause() {
        return OpenSsl.UNAVAILABILITY_CAUSE;
    }
    
    @Deprecated
    public static Set<String> availableCipherSuites() {
        return availableOpenSslCipherSuites();
    }
    
    public static Set<String> availableOpenSslCipherSuites() {
        return OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES;
    }
    
    public static Set<String> availableJavaCipherSuites() {
        return OpenSsl.AVAILABLE_JAVA_CIPHER_SUITES;
    }
    
    public static boolean isCipherSuiteAvailable(String cipherSuite) {
        final String converted = CipherSuiteConverter.toOpenSsl(cipherSuite);
        if (converted != null) {
            cipherSuite = converted;
        }
        return OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES.contains(cipherSuite);
    }
    
    public static boolean supportsKeyManagerFactory() {
        return OpenSsl.SUPPORTS_KEYMANAGER_FACTORY;
    }
    
    public static boolean supportsHostnameValidation() {
        return OpenSsl.SUPPORTS_HOSTNAME_VALIDATION;
    }
    
    static boolean useKeyManagerFactory() {
        return OpenSsl.USE_KEYMANAGER_FACTORY;
    }
    
    static long memoryAddress(final ByteBuf buf) {
        assert buf.isDirect();
        return buf.hasMemoryAddress() ? buf.memoryAddress() : Buffer.address(buf.nioBuffer());
    }
    
    private OpenSsl() {
    }
    
    private static void loadTcNative() throws Exception {
        final String os = normalizeOs(SystemPropertyUtil.get("os.name", ""));
        final String arch = normalizeArch(SystemPropertyUtil.get("os.arch", ""));
        final Set<String> libNames = new LinkedHashSet<String>(3);
        libNames.add("netty-tcnative-" + os + '-' + arch);
        if ("linux".equalsIgnoreCase(os)) {
            libNames.add("netty-tcnative-" + os + '-' + arch + "-fedora");
        }
        libNames.add("netty-tcnative");
        NativeLibraryLoader.loadFirstAvailable(SSL.class.getClassLoader(), (String[])libNames.toArray(new String[libNames.size()]));
    }
    
    private static boolean initializeTcNative() throws Exception {
        return Library.initialize();
    }
    
    private static String normalizeOs(String value) {
        value = normalize(value);
        if (value.startsWith("aix")) {
            return "aix";
        }
        if (value.startsWith("hpux")) {
            return "hpux";
        }
        if (value.startsWith("os400") && (value.length() <= 5 || !Character.isDigit(value.charAt(5)))) {
            return "os400";
        }
        if (value.startsWith("linux")) {
            return "linux";
        }
        if (value.startsWith("macosx") || value.startsWith("osx")) {
            return "osx";
        }
        if (value.startsWith("freebsd")) {
            return "freebsd";
        }
        if (value.startsWith("openbsd")) {
            return "openbsd";
        }
        if (value.startsWith("netbsd")) {
            return "netbsd";
        }
        if (value.startsWith("solaris") || value.startsWith("sunos")) {
            return "sunos";
        }
        if (value.startsWith("windows")) {
            return "windows";
        }
        return "unknown";
    }
    
    private static String normalizeArch(String value) {
        value = normalize(value);
        if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return "x86_64";
        }
        if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return "x86_32";
        }
        if (value.matches("^(ia64|itanium64)$")) {
            return "itanium_64";
        }
        if (value.matches("^(sparc|sparc32)$")) {
            return "sparc_32";
        }
        if (value.matches("^(sparcv9|sparc64)$")) {
            return "sparc_64";
        }
        if (value.matches("^(arm|arm32)$")) {
            return "arm_32";
        }
        if ("aarch64".equals(value)) {
            return "aarch_64";
        }
        if (value.matches("^(ppc|ppc32)$")) {
            return "ppc_32";
        }
        if ("ppc64".equals(value)) {
            return "ppc_64";
        }
        if ("ppc64le".equals(value)) {
            return "ppcle_64";
        }
        if ("s390".equals(value)) {
            return "s390_32";
        }
        if ("s390x".equals(value)) {
            return "s390_64";
        }
        return "unknown";
    }
    
    private static String normalize(final String value) {
        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }
    
    static void releaseIfNeeded(final ReferenceCounted counted) {
        if (counted.refCnt() > 0) {
            ReferenceCountUtil.safeRelease(counted);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OpenSsl.class);
        Throwable cause = null;
        try {
            Class.forName("io.netty.internal.tcnative.SSL", false, OpenSsl.class.getClassLoader());
        }
        catch (final ClassNotFoundException t) {
            cause = t;
            OpenSsl.logger.debug("netty-tcnative not in the classpath; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.");
        }
        if (cause == null) {
            try {
                loadTcNative();
            }
            catch (final Throwable t2) {
                cause = t2;
                OpenSsl.logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable, unless the application has already loaded the symbols by some other means. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t2);
            }
            try {
                initializeTcNative();
                cause = null;
            }
            catch (final Throwable t2) {
                if (cause == null) {
                    cause = t2;
                }
                OpenSsl.logger.debug("Failed to initialize netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable. See http://netty.io/wiki/forked-tomcat-native.html for more information.", t2);
            }
        }
        if ((UNAVAILABILITY_CAUSE = cause) == null) {
            OpenSsl.logger.debug("netty-tcnative using native library: {}", SSL.versionString());
            final Set<String> availableOpenSslCipherSuites = new LinkedHashSet<String>(128);
            boolean supportsKeyManagerFactory = false;
            boolean useKeyManagerFactory = false;
            boolean supportsHostNameValidation = false;
            try {
                final long sslCtx = SSLContext.make(31, 1);
                final long privateKeyBio = 0L;
                long certBio = 0L;
                try {
                    SSLContext.setCipherSuite(sslCtx, "ALL");
                    final long ssl = SSL.newSSL(sslCtx, true);
                    try {
                        for (final String c : SSL.getCiphers(ssl)) {
                            if (c != null && !c.isEmpty()) {
                                if (!availableOpenSslCipherSuites.contains(c)) {
                                    availableOpenSslCipherSuites.add(c);
                                }
                            }
                        }
                        try {
                            SSL.setHostNameValidation(ssl, 0, "netty.io");
                            supportsHostNameValidation = true;
                        }
                        catch (final Throwable ignore) {
                            OpenSsl.logger.debug("Hostname Verification not supported.");
                        }
                        try {
                            final SelfSignedCertificate cert = new SelfSignedCertificate();
                            certBio = ReferenceCountedOpenSslContext.toBIO(cert.cert());
                            SSL.setCertificateChainBio(ssl, certBio, false);
                            supportsKeyManagerFactory = true;
                            try {
                                useKeyManagerFactory = AccessController.doPrivileged((PrivilegedAction<Boolean>)new PrivilegedAction<Boolean>() {
                                    @Override
                                    public Boolean run() {
                                        return SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useKeyManagerFactory", true);
                                    }
                                });
                            }
                            catch (final Throwable ignore2) {
                                OpenSsl.logger.debug("Failed to get useKeyManagerFactory system property.");
                            }
                        }
                        catch (final Throwable ignore) {
                            OpenSsl.logger.debug("KeyManagerFactory not supported.");
                        }
                    }
                    finally {
                        SSL.freeSSL(ssl);
                        if (privateKeyBio != 0L) {
                            SSL.freeBIO(privateKeyBio);
                        }
                        if (certBio != 0L) {
                            SSL.freeBIO(certBio);
                        }
                    }
                }
                finally {
                    SSLContext.free(sslCtx);
                }
            }
            catch (final Exception e) {
                OpenSsl.logger.warn("Failed to get the list of available OpenSSL cipher suites.", e);
            }
            AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.unmodifiableSet((Set<? extends String>)availableOpenSslCipherSuites);
            final Set<String> availableJavaCipherSuites = new LinkedHashSet<String>(OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES.size() * 2);
            for (final String cipher : OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES) {
                availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "TLS"));
                availableJavaCipherSuites.add(CipherSuiteConverter.toJava(cipher, "SSL"));
            }
            AVAILABLE_JAVA_CIPHER_SUITES = Collections.unmodifiableSet((Set<? extends String>)availableJavaCipherSuites);
            final Set<String> availableCipherSuites = new LinkedHashSet<String>(OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES.size() + OpenSsl.AVAILABLE_JAVA_CIPHER_SUITES.size());
            for (final String cipher2 : OpenSsl.AVAILABLE_OPENSSL_CIPHER_SUITES) {
                availableCipherSuites.add(cipher2);
            }
            for (final String cipher2 : OpenSsl.AVAILABLE_JAVA_CIPHER_SUITES) {
                availableCipherSuites.add(cipher2);
            }
            AVAILABLE_CIPHER_SUITES = availableCipherSuites;
            SUPPORTS_KEYMANAGER_FACTORY = supportsKeyManagerFactory;
            SUPPORTS_HOSTNAME_VALIDATION = supportsHostNameValidation;
            USE_KEYMANAGER_FACTORY = useKeyManagerFactory;
            final Set<String> protocols = new LinkedHashSet<String>(6);
            protocols.add("SSLv2Hello");
            if (doesSupportProtocol(1)) {
                protocols.add("SSLv2");
            }
            if (doesSupportProtocol(2)) {
                protocols.add("SSLv3");
            }
            if (doesSupportProtocol(4)) {
                protocols.add("TLSv1");
            }
            if (doesSupportProtocol(8)) {
                protocols.add("TLSv1.1");
            }
            if (doesSupportProtocol(16)) {
                protocols.add("TLSv1.2");
            }
            SUPPORTED_PROTOCOLS_SET = Collections.unmodifiableSet((Set<? extends String>)protocols);
        }
        else {
            AVAILABLE_OPENSSL_CIPHER_SUITES = Collections.emptySet();
            AVAILABLE_JAVA_CIPHER_SUITES = Collections.emptySet();
            AVAILABLE_CIPHER_SUITES = Collections.emptySet();
            SUPPORTS_KEYMANAGER_FACTORY = false;
            SUPPORTS_HOSTNAME_VALIDATION = false;
            USE_KEYMANAGER_FACTORY = false;
            SUPPORTED_PROTOCOLS_SET = Collections.emptySet();
        }
    }
}
