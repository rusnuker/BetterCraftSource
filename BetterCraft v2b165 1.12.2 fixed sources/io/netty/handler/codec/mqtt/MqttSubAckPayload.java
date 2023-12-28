// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class MqttSubAckPayload
{
    private final List<Integer> grantedQoSLevels;
    
    public MqttSubAckPayload(final int... grantedQoSLevels) {
        if (grantedQoSLevels == null) {
            throw new NullPointerException("grantedQoSLevels");
        }
        final List<Integer> list = new ArrayList<Integer>(grantedQoSLevels.length);
        for (final int v : grantedQoSLevels) {
            list.add(v);
        }
        this.grantedQoSLevels = Collections.unmodifiableList((List<? extends Integer>)list);
    }
    
    public MqttSubAckPayload(final Iterable<Integer> grantedQoSLevels) {
        if (grantedQoSLevels == null) {
            throw new NullPointerException("grantedQoSLevels");
        }
        final List<Integer> list = new ArrayList<Integer>();
        for (final Integer v : grantedQoSLevels) {
            if (v == null) {
                break;
            }
            list.add(v);
        }
        this.grantedQoSLevels = Collections.unmodifiableList((List<? extends Integer>)list);
    }
    
    public List<Integer> grantedQoSLevels() {
        return this.grantedQoSLevels;
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "grantedQoSLevels=" + this.grantedQoSLevels + ']';
    }
}
