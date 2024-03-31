// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.compress.archivers.dump;

public class UnsupportedCompressionAlgorithmException extends DumpArchiveException
{
    private static final long serialVersionUID = 1L;
    
    public UnsupportedCompressionAlgorithmException() {
        super("this file uses an unsupported compression algorithm.");
    }
    
    public UnsupportedCompressionAlgorithmException(final String alg) {
        super("this file uses an unsupported compression algorithm: " + alg + ".");
    }
}
