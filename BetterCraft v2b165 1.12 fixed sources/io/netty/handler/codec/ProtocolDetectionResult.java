// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.util.internal.ObjectUtil;

public final class ProtocolDetectionResult<T>
{
    private static final ProtocolDetectionResult NEEDS_MORE_DATE;
    private static final ProtocolDetectionResult INVALID;
    private final ProtocolDetectionState state;
    private final T result;
    
    public static <T> ProtocolDetectionResult<T> needsMoreData() {
        return ProtocolDetectionResult.NEEDS_MORE_DATE;
    }
    
    public static <T> ProtocolDetectionResult<T> invalid() {
        return ProtocolDetectionResult.INVALID;
    }
    
    public static <T> ProtocolDetectionResult<T> detected(final T protocol) {
        return new ProtocolDetectionResult<T>(ProtocolDetectionState.DETECTED, ObjectUtil.checkNotNull(protocol, "protocol"));
    }
    
    private ProtocolDetectionResult(final ProtocolDetectionState state, final T result) {
        this.state = state;
        this.result = result;
    }
    
    public ProtocolDetectionState state() {
        return this.state;
    }
    
    public T detectedProtocol() {
        return this.result;
    }
    
    static {
        NEEDS_MORE_DATE = new ProtocolDetectionResult(ProtocolDetectionState.NEEDS_MORE_DATA, null);
        INVALID = new ProtocolDetectionResult(ProtocolDetectionState.INVALID, null);
    }
}
