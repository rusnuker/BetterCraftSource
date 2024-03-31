// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class SmtpUtils
{
    static List<CharSequence> toUnmodifiableList(final CharSequence... sequences) {
        if (sequences == null || sequences.length == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList((List<? extends CharSequence>)Arrays.asList((T[])sequences));
    }
    
    private SmtpUtils() {
    }
}
