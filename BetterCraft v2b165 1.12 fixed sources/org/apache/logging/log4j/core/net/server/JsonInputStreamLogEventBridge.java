// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import java.nio.charset.Charset;

public class JsonInputStreamLogEventBridge extends InputStreamLogEventBridge
{
    private static final int[] END_PAIR;
    private static final char EVENT_END_MARKER = '}';
    private static final char EVENT_START_MARKER = '{';
    private static final char JSON_ESC = '\\';
    private static final char JSON_STR_DELIM = '\"';
    private static final boolean THREAD_CONTEXT_MAP_AS_LIST = false;
    
    public JsonInputStreamLogEventBridge() {
        this(1024, Charset.defaultCharset());
    }
    
    public JsonInputStreamLogEventBridge(final int bufferSize, final Charset charset) {
        super(new Log4jJsonObjectMapper(false, true), bufferSize, charset, String.valueOf('}'));
    }
    
    @Override
    protected int[] getEventIndices(final String text, final int beginIndex) {
        final int start = text.indexOf(123, beginIndex);
        if (start == -1) {
            return JsonInputStreamLogEventBridge.END_PAIR;
        }
        final char[] charArray = text.toCharArray();
        int stack = 0;
        boolean inStr = false;
        boolean inEsc = false;
        for (int i = start; i < charArray.length; ++i) {
            final char c = charArray[i];
            if (inEsc) {
                inEsc = false;
            }
            else {
                switch (c) {
                    case '{': {
                        if (!inStr) {
                            ++stack;
                            break;
                        }
                        break;
                    }
                    case '}': {
                        if (!inStr) {
                            --stack;
                            break;
                        }
                        break;
                    }
                    case '\"': {
                        inStr = !inStr;
                        break;
                    }
                    case '\\': {
                        inEsc = true;
                        break;
                    }
                }
                if (stack == 0) {
                    return new int[] { start, i };
                }
            }
        }
        return JsonInputStreamLogEventBridge.END_PAIR;
    }
    
    static {
        END_PAIR = new int[] { -1, -1 };
    }
}
