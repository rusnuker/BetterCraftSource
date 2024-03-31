// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import org.apache.logging.log4j.core.LogEvent;
import java.io.IOException;
import org.apache.logging.log4j.core.LogEventListener;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.nio.charset.Charset;
import java.io.InputStream;

public abstract class InputStreamLogEventBridge extends AbstractLogEventBridge<InputStream>
{
    private final int bufferSize;
    private final Charset charset;
    private final String eventEndMarker;
    private final ObjectReader objectReader;
    
    public InputStreamLogEventBridge(final ObjectMapper mapper, final int bufferSize, final Charset charset, final String eventEndMarker) {
        this.bufferSize = bufferSize;
        this.charset = charset;
        this.eventEndMarker = eventEndMarker;
        this.objectReader = mapper.readerFor((Class)Log4jLogEvent.class);
    }
    
    protected abstract int[] getEventIndices(final String p0, final int p1);
    
    @Override
    public void logEvents(final InputStream inputStream, final LogEventListener logEventListener) throws IOException {
        String workingText = "";
        try {
            final byte[] buffer = new byte[this.bufferSize];
            String textRemains;
            workingText = (textRemains = "");
            while (true) {
                final int streamReadLength = inputStream.read(buffer);
                if (streamReadLength == -1) {
                    break;
                }
                final String text;
                workingText = (text = textRemains + new String(buffer, 0, streamReadLength, this.charset));
                int beginIndex = 0;
                while (true) {
                    final int[] pair = this.getEventIndices(text, beginIndex);
                    final int eventStartMarkerIndex = pair[0];
                    if (eventStartMarkerIndex < 0) {
                        textRemains = text.substring(beginIndex);
                        break;
                    }
                    final int eventEndMarkerIndex = pair[1];
                    if (eventEndMarkerIndex <= 0) {
                        textRemains = text.substring(beginIndex);
                        break;
                    }
                    final int eventEndXmlIndex = eventEndMarkerIndex + this.eventEndMarker.length();
                    final String textEvent;
                    workingText = (textEvent = text.substring(eventStartMarkerIndex, eventEndXmlIndex));
                    final LogEvent logEvent = this.unmarshal(textEvent);
                    logEventListener.log(logEvent);
                    beginIndex = eventEndXmlIndex;
                }
            }
        }
        catch (final IOException ex) {
            InputStreamLogEventBridge.logger.error(workingText, ex);
        }
    }
    
    protected Log4jLogEvent unmarshal(final String jsonEvent) throws IOException {
        return (Log4jLogEvent)this.objectReader.readValue(jsonEvent);
    }
}
