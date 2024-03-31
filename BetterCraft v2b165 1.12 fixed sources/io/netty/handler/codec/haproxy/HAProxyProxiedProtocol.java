// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.haproxy;

public enum HAProxyProxiedProtocol
{
    UNKNOWN((byte)0, AddressFamily.AF_UNSPEC, TransportProtocol.UNSPEC), 
    TCP4((byte)17, AddressFamily.AF_IPv4, TransportProtocol.STREAM), 
    TCP6((byte)33, AddressFamily.AF_IPv6, TransportProtocol.STREAM), 
    UDP4((byte)18, AddressFamily.AF_IPv4, TransportProtocol.DGRAM), 
    UDP6((byte)34, AddressFamily.AF_IPv6, TransportProtocol.DGRAM), 
    UNIX_STREAM((byte)49, AddressFamily.AF_UNIX, TransportProtocol.STREAM), 
    UNIX_DGRAM((byte)50, AddressFamily.AF_UNIX, TransportProtocol.DGRAM);
    
    private final byte byteValue;
    private final AddressFamily addressFamily;
    private final TransportProtocol transportProtocol;
    
    private HAProxyProxiedProtocol(final byte byteValue, final AddressFamily addressFamily, final TransportProtocol transportProtocol) {
        this.byteValue = byteValue;
        this.addressFamily = addressFamily;
        this.transportProtocol = transportProtocol;
    }
    
    public static HAProxyProxiedProtocol valueOf(final byte tpafByte) {
        switch (tpafByte) {
            case 17: {
                return HAProxyProxiedProtocol.TCP4;
            }
            case 33: {
                return HAProxyProxiedProtocol.TCP6;
            }
            case 0: {
                return HAProxyProxiedProtocol.UNKNOWN;
            }
            case 18: {
                return HAProxyProxiedProtocol.UDP4;
            }
            case 34: {
                return HAProxyProxiedProtocol.UDP6;
            }
            case 49: {
                return HAProxyProxiedProtocol.UNIX_STREAM;
            }
            case 50: {
                return HAProxyProxiedProtocol.UNIX_DGRAM;
            }
            default: {
                throw new IllegalArgumentException("unknown transport protocol + address family: " + (tpafByte & 0xFF));
            }
        }
    }
    
    public byte byteValue() {
        return this.byteValue;
    }
    
    public AddressFamily addressFamily() {
        return this.addressFamily;
    }
    
    public TransportProtocol transportProtocol() {
        return this.transportProtocol;
    }
    
    public enum AddressFamily
    {
        AF_UNSPEC((byte)0), 
        AF_IPv4((byte)16), 
        AF_IPv6((byte)32), 
        AF_UNIX((byte)48);
        
        private static final byte FAMILY_MASK = -16;
        private final byte byteValue;
        
        private AddressFamily(final byte byteValue) {
            this.byteValue = byteValue;
        }
        
        public static AddressFamily valueOf(final byte tpafByte) {
            final int addressFamily = tpafByte & 0xFFFFFFF0;
            switch ((byte)addressFamily) {
                case 16: {
                    return AddressFamily.AF_IPv4;
                }
                case 32: {
                    return AddressFamily.AF_IPv6;
                }
                case 0: {
                    return AddressFamily.AF_UNSPEC;
                }
                case 48: {
                    return AddressFamily.AF_UNIX;
                }
                default: {
                    throw new IllegalArgumentException("unknown address family: " + addressFamily);
                }
            }
        }
        
        public byte byteValue() {
            return this.byteValue;
        }
    }
    
    public enum TransportProtocol
    {
        UNSPEC((byte)0), 
        STREAM((byte)1), 
        DGRAM((byte)2);
        
        private static final byte TRANSPORT_MASK = 15;
        private final byte transportByte;
        
        private TransportProtocol(final byte transportByte) {
            this.transportByte = transportByte;
        }
        
        public static TransportProtocol valueOf(final byte tpafByte) {
            final int transportProtocol = tpafByte & 0xF;
            switch ((byte)transportProtocol) {
                case 1: {
                    return TransportProtocol.STREAM;
                }
                case 0: {
                    return TransportProtocol.UNSPEC;
                }
                case 2: {
                    return TransportProtocol.DGRAM;
                }
                default: {
                    throw new IllegalArgumentException("unknown transport protocol: " + transportProtocol);
                }
            }
        }
        
        public byte byteValue() {
            return this.transportByte;
        }
    }
}
