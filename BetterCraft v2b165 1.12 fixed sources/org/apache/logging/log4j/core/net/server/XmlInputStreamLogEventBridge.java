// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
import java.nio.charset.Charset;

public class XmlInputStreamLogEventBridge extends InputStreamLogEventBridge
{
    private static final String EVENT_END = "</Event>";
    private static final String EVENT_START_NS_N = "<Event>";
    private static final String EVENT_START_NS_Y = "<Event ";
    
    public XmlInputStreamLogEventBridge() {
        this(1024, Charset.defaultCharset());
    }
    
    public XmlInputStreamLogEventBridge(final int bufferSize, final Charset charset) {
        super((ObjectMapper)new Log4jXmlObjectMapper(), bufferSize, charset, "</Event>");
    }
    
    @Override
    protected int[] getEventIndices(final String text, final int beginIndex) {
        int start = text.indexOf("<Event ", beginIndex);
        int startLen = "<Event ".length();
        if (start < 0) {
            start = text.indexOf("<Event>", beginIndex);
            startLen = "<Event>".length();
        }
        final int end = (start < 0) ? -1 : text.indexOf("</Event>", start + startLen);
        return new int[] { start, end };
    }
}
