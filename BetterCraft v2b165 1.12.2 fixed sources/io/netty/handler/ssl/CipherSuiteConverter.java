// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;

final class CipherSuiteConverter
{
    private static final InternalLogger logger;
    private static final Pattern JAVA_CIPHERSUITE_PATTERN;
    private static final Pattern OPENSSL_CIPHERSUITE_PATTERN;
    private static final Pattern JAVA_AES_CBC_PATTERN;
    private static final Pattern JAVA_AES_PATTERN;
    private static final Pattern OPENSSL_AES_CBC_PATTERN;
    private static final Pattern OPENSSL_AES_PATTERN;
    private static final ConcurrentMap<String, String> j2o;
    private static final ConcurrentMap<String, Map<String, String>> o2j;
    
    static void clearCache() {
        CipherSuiteConverter.j2o.clear();
        CipherSuiteConverter.o2j.clear();
    }
    
    static boolean isJ2OCached(final String key, final String value) {
        return value.equals(CipherSuiteConverter.j2o.get(key));
    }
    
    static boolean isO2JCached(final String key, final String protocol, final String value) {
        final Map<String, String> p2j = CipherSuiteConverter.o2j.get(key);
        return p2j != null && value.equals(p2j.get(protocol));
    }
    
    static String toOpenSsl(final Iterable<String> javaCipherSuites) {
        final StringBuilder buf = new StringBuilder();
        for (String c : javaCipherSuites) {
            if (c == null) {
                break;
            }
            final String converted = toOpenSsl(c);
            if (converted != null) {
                c = converted;
            }
            buf.append(c);
            buf.append(':');
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 1);
            return buf.toString();
        }
        return "";
    }
    
    static String toOpenSsl(final String javaCipherSuite) {
        final String converted = CipherSuiteConverter.j2o.get(javaCipherSuite);
        if (converted != null) {
            return converted;
        }
        return cacheFromJava(javaCipherSuite);
    }
    
    private static String cacheFromJava(final String javaCipherSuite) {
        final String openSslCipherSuite = toOpenSslUncached(javaCipherSuite);
        if (openSslCipherSuite == null) {
            return null;
        }
        CipherSuiteConverter.j2o.putIfAbsent(javaCipherSuite, openSslCipherSuite);
        final String javaCipherSuiteSuffix = javaCipherSuite.substring(4);
        final Map<String, String> p2j = new HashMap<String, String>(4);
        p2j.put("", javaCipherSuiteSuffix);
        p2j.put("SSL", "SSL_" + javaCipherSuiteSuffix);
        p2j.put("TLS", "TLS_" + javaCipherSuiteSuffix);
        CipherSuiteConverter.o2j.put(openSslCipherSuite, p2j);
        CipherSuiteConverter.logger.debug("Cipher suite mapping: {} => {}", javaCipherSuite, openSslCipherSuite);
        return openSslCipherSuite;
    }
    
    static String toOpenSslUncached(final String javaCipherSuite) {
        final Matcher m = CipherSuiteConverter.JAVA_CIPHERSUITE_PATTERN.matcher(javaCipherSuite);
        if (!m.matches()) {
            return null;
        }
        final String handshakeAlgo = toOpenSslHandshakeAlgo(m.group(1));
        final String bulkCipher = toOpenSslBulkCipher(m.group(2));
        final String hmacAlgo = toOpenSslHmacAlgo(m.group(3));
        if (handshakeAlgo.isEmpty()) {
            return bulkCipher + '-' + hmacAlgo;
        }
        return handshakeAlgo + '-' + bulkCipher + '-' + hmacAlgo;
    }
    
    private static String toOpenSslHandshakeAlgo(String handshakeAlgo) {
        final boolean export = handshakeAlgo.endsWith("_EXPORT");
        if (export) {
            handshakeAlgo = handshakeAlgo.substring(0, handshakeAlgo.length() - 7);
        }
        if ("RSA".equals(handshakeAlgo)) {
            handshakeAlgo = "";
        }
        else if (handshakeAlgo.endsWith("_anon")) {
            handshakeAlgo = 'A' + handshakeAlgo.substring(0, handshakeAlgo.length() - 5);
        }
        if (export) {
            if (handshakeAlgo.isEmpty()) {
                handshakeAlgo = "EXP";
            }
            else {
                handshakeAlgo = "EXP-" + handshakeAlgo;
            }
        }
        return handshakeAlgo.replace('_', '-');
    }
    
    private static String toOpenSslBulkCipher(final String bulkCipher) {
        if (bulkCipher.startsWith("AES_")) {
            Matcher m = CipherSuiteConverter.JAVA_AES_CBC_PATTERN.matcher(bulkCipher);
            if (m.matches()) {
                return m.replaceFirst("$1$2");
            }
            m = CipherSuiteConverter.JAVA_AES_PATTERN.matcher(bulkCipher);
            if (m.matches()) {
                return m.replaceFirst("$1$2-$3");
            }
        }
        if ("3DES_EDE_CBC".equals(bulkCipher)) {
            return "DES-CBC3";
        }
        if ("RC4_128".equals(bulkCipher) || "RC4_40".equals(bulkCipher)) {
            return "RC4";
        }
        if ("DES40_CBC".equals(bulkCipher) || "DES_CBC_40".equals(bulkCipher)) {
            return "DES-CBC";
        }
        if ("RC2_CBC_40".equals(bulkCipher)) {
            return "RC2-CBC";
        }
        return bulkCipher.replace('_', '-');
    }
    
    private static String toOpenSslHmacAlgo(final String hmacAlgo) {
        return hmacAlgo;
    }
    
    static String toJava(final String openSslCipherSuite, final String protocol) {
        Map<String, String> p2j = CipherSuiteConverter.o2j.get(openSslCipherSuite);
        if (p2j == null) {
            p2j = cacheFromOpenSsl(openSslCipherSuite);
            if (p2j == null) {
                return null;
            }
        }
        String javaCipherSuite = p2j.get(protocol);
        if (javaCipherSuite == null) {
            javaCipherSuite = protocol + '_' + p2j.get("");
        }
        return javaCipherSuite;
    }
    
    private static Map<String, String> cacheFromOpenSsl(final String openSslCipherSuite) {
        final String javaCipherSuiteSuffix = toJavaUncached(openSslCipherSuite);
        if (javaCipherSuiteSuffix == null) {
            return null;
        }
        final String javaCipherSuiteSsl = "SSL_" + javaCipherSuiteSuffix;
        final String javaCipherSuiteTls = "TLS_" + javaCipherSuiteSuffix;
        final Map<String, String> p2j = new HashMap<String, String>(4);
        p2j.put("", javaCipherSuiteSuffix);
        p2j.put("SSL", javaCipherSuiteSsl);
        p2j.put("TLS", javaCipherSuiteTls);
        CipherSuiteConverter.o2j.putIfAbsent(openSslCipherSuite, p2j);
        CipherSuiteConverter.j2o.putIfAbsent(javaCipherSuiteTls, openSslCipherSuite);
        CipherSuiteConverter.j2o.putIfAbsent(javaCipherSuiteSsl, openSslCipherSuite);
        CipherSuiteConverter.logger.debug("Cipher suite mapping: {} => {}", javaCipherSuiteTls, openSslCipherSuite);
        CipherSuiteConverter.logger.debug("Cipher suite mapping: {} => {}", javaCipherSuiteSsl, openSslCipherSuite);
        return p2j;
    }
    
    static String toJavaUncached(final String openSslCipherSuite) {
        final Matcher m = CipherSuiteConverter.OPENSSL_CIPHERSUITE_PATTERN.matcher(openSslCipherSuite);
        if (!m.matches()) {
            return null;
        }
        String handshakeAlgo = m.group(1);
        boolean export;
        if (handshakeAlgo == null) {
            handshakeAlgo = "";
            export = false;
        }
        else if (handshakeAlgo.startsWith("EXP-")) {
            handshakeAlgo = handshakeAlgo.substring(4);
            export = true;
        }
        else if ("EXP".equals(handshakeAlgo)) {
            handshakeAlgo = "";
            export = true;
        }
        else {
            export = false;
        }
        handshakeAlgo = toJavaHandshakeAlgo(handshakeAlgo, export);
        final String bulkCipher = toJavaBulkCipher(m.group(2), export);
        final String hmacAlgo = toJavaHmacAlgo(m.group(3));
        return handshakeAlgo + "_WITH_" + bulkCipher + '_' + hmacAlgo;
    }
    
    private static String toJavaHandshakeAlgo(String handshakeAlgo, final boolean export) {
        if (handshakeAlgo.isEmpty()) {
            handshakeAlgo = "RSA";
        }
        else if ("ADH".equals(handshakeAlgo)) {
            handshakeAlgo = "DH_anon";
        }
        else if ("AECDH".equals(handshakeAlgo)) {
            handshakeAlgo = "ECDH_anon";
        }
        handshakeAlgo = handshakeAlgo.replace('-', '_');
        if (export) {
            return handshakeAlgo + "_EXPORT";
        }
        return handshakeAlgo;
    }
    
    private static String toJavaBulkCipher(final String bulkCipher, final boolean export) {
        if (bulkCipher.startsWith("AES")) {
            Matcher m = CipherSuiteConverter.OPENSSL_AES_CBC_PATTERN.matcher(bulkCipher);
            if (m.matches()) {
                return m.replaceFirst("$1_$2_CBC");
            }
            m = CipherSuiteConverter.OPENSSL_AES_PATTERN.matcher(bulkCipher);
            if (m.matches()) {
                return m.replaceFirst("$1_$2_$3");
            }
        }
        if ("DES-CBC3".equals(bulkCipher)) {
            return "3DES_EDE_CBC";
        }
        if ("RC4".equals(bulkCipher)) {
            if (export) {
                return "RC4_40";
            }
            return "RC4_128";
        }
        else if ("DES-CBC".equals(bulkCipher)) {
            if (export) {
                return "DES_CBC_40";
            }
            return "DES_CBC";
        }
        else {
            if (!"RC2-CBC".equals(bulkCipher)) {
                return bulkCipher.replace('-', '_');
            }
            if (export) {
                return "RC2_CBC_40";
            }
            return "RC2_CBC";
        }
    }
    
    private static String toJavaHmacAlgo(final String hmacAlgo) {
        return hmacAlgo;
    }
    
    private CipherSuiteConverter() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(CipherSuiteConverter.class);
        JAVA_CIPHERSUITE_PATTERN = Pattern.compile("^(?:TLS|SSL)_((?:(?!_WITH_).)+)_WITH_(.*)_(.*)$");
        OPENSSL_CIPHERSUITE_PATTERN = Pattern.compile("^(?:((?:(?:EXP-)?(?:(?:DHE|EDH|ECDH|ECDHE|SRP)-(?:DSS|RSA|ECDSA)|(?:ADH|AECDH|KRB5|PSK|SRP)))|EXP)-)?(.*)-(.*)$");
        JAVA_AES_CBC_PATTERN = Pattern.compile("^(AES)_([0-9]+)_CBC$");
        JAVA_AES_PATTERN = Pattern.compile("^(AES)_([0-9]+)_(.*)$");
        OPENSSL_AES_CBC_PATTERN = Pattern.compile("^(AES)([0-9]+)$");
        OPENSSL_AES_PATTERN = Pattern.compile("^(AES)([0-9]+)-(.*)$");
        j2o = PlatformDependent.newConcurrentHashMap();
        o2j = PlatformDependent.newConcurrentHashMap();
    }
}
